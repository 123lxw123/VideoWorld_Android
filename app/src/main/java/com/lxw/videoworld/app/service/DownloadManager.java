package com.lxw.videoworld.app.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lxw.videoworld.R;
import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.model.SourceDetailModel;
import com.lxw.videoworld.app.model.SourceHistoryModel;
import com.lxw.videoworld.app.ui.PlayVideoActivity;
import com.lxw.videoworld.app.util.RealmUtil;
import com.lxw.videoworld.app.util.XLTaskInfoComparator;
import com.lxw.videoworld.app.widget.DownloadTorrentDialog;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.FileUtil;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.lxw.videoworld.framework.util.NetUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.util.ValueUtil;
import com.xunlei.downloadlib.XLDownloadManager;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;


/**
 * 任务状态 0 连接中 1 下载中 2 已完成 3 错误 4 暂停中
 * Created by Zion on 2017/8/23.
 */

public class DownloadManager {
    public static Vector<XLTaskInfo> xLTaskInfos = new Vector<>();
    public static XLTaskInfoComparator comparator = new XLTaskInfoComparator();
    public static final String TAG = "DownloadManager";
    public static final CompositeDisposable disposables = new CompositeDisposable();
    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo, final boolean isInitDownload) {
        return addNormalTask(context, link, isPlayVideo, isInitDownload, new ArrayList<String>(), false, null, null);
    }

    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo, final boolean isInitDownload, final ArrayList<String> links) {
        return addNormalTask(context, link, isPlayVideo, isInitDownload, links, false, null, null);
    }

    /**
     * 所有的下载都走这个方法，有它判断调用迅雷对应的Api
     *
     * @param context
     * @param link
     * @param isPlayVideo
     * @return
     */
    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo,
                                     final boolean isInitDownload, final ArrayList<String> links, final boolean isStartApp, Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId;
        try {
            if (link == null) return -1;
            if (!isInitDownload && isInDownloadQueue(link, XLTaskHelper.instance().getFileName(link))) {
                ToastUtil.showMessage("资源已在下载队列中");
                String localUrl = XLTaskHelper.instance().getLoclUrl(PATH_OFFLINE_DOWNLOAD +
                        XLTaskHelper.instance().getFileName(link));
                if (isPlayVideo && !(link.startsWith("magnet:?") || XLTaskHelper.instance().getFileName(link).endsWith("torrent"))) {
                    ArrayList<String> newLinks = new ArrayList<>();
                    for (int i = 0; i < links.size(); i++) {
                        if (links.get(i).equals(link)) newLinks.add(localUrl);
                        else newLinks.add(links.get(i));
                    }
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("url", localUrl);
                    intent.putStringArrayListExtra("urlList", newLinks);
                    context.startActivity(intent);
                }
                return -1;
            }
            if (link.startsWith("magnet:?") || XLTaskHelper.instance().getFileName(link).endsWith("torrent")) {
                if (link.startsWith("magnet:?")) {
                    taskId = addMagnetTask(link, PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, isInitDownload, isStartApp, nextCall, errorCall);
                } else {
                    taskId = addMagnetTask(getRealUrl(link), PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, isInitDownload, isStartApp, nextCall, errorCall);
                }
                if (NetUtil.getNetWorkState(context) == NetUtil.NETWORK_MOBILE){
                    ToastUtil.showMessage("资源开始下载\n当前为移动网络，请注意您的流量");
                }else if (!isStartApp){
                    ToastUtil.showMessage("资源开始下载");
                }
            } else {
                taskId = addThunderTask(link, PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, isStartApp, links, nextCall, errorCall);
            }
            XLTaskInfo xLTaskInfo = XLTaskHelper.instance().getTaskInfo(taskId);
            xLTaskInfo.sourceUrl = link;
            xLTaskInfo.mFileName = XLTaskHelper.instance().getFileName(link);
            if (!isInitDownload) {
                addXLTaskInfo(xLTaskInfo);
            } else {
                updateXLTaskInfo(xLTaskInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return taskId;
    }

    public static long addTorrentTask(final String sourceUrl, final String torrentPath, final String savePath, List<Integer> indexs, final boolean isInitDownload) {
        return addTorrentTask(sourceUrl, torrentPath, savePath, indexs, isInitDownload, false, null, null);
    }

    /**
     * 添加种子下载任务,如果是磁力链需要先通过addMagnetTask将种子下载下来
     *
     * @param torrentPath 种子地址
     * @param savePath    保存路径
     * @param indexs      需要下载的文件索引
     * @return
     * @throws Exception
     */
    public static long addTorrentTask(final String sourceUrl, final String torrentPath, final String savePath, List<Integer> indexs, final boolean isInitDownload, final boolean isStartApp,
                                      final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        if (TextUtils.isEmpty(torrentPath) || indexs == null) return -1;
        long taskId = -1;
        try {
            final TorrentInfo torrentInfo = XLTaskHelper.instance().getTorrentInfo(torrentPath);
            final List<Integer> newIndexs = new ArrayList<>();
            if (!isInitDownload) {
                for (int i = 0; i < indexs.size(); i++) {
                    if (isInDownloadQueue(sourceUrl, torrentInfo.mSubFileInfo[indexs.get(i)].mFileName, indexs.get(i))) {
                        ToastUtil.showMessage("资源已在下载队列中");
                    } else newIndexs.add(indexs.get(i));
                }
            }
            taskId = XLTaskHelper.instance().addTorrentTask(torrentPath, savePath, indexs);
            if (NetUtil.getNetWorkState(BaseApplication.appContext) == NetUtil.NETWORK_MOBILE){
                ToastUtil.showMessage("资源开始下载\n当前为移动网络，请注意您的流量");
            }else if (!isStartApp) {
                ToastUtil.showMessage("资源开始下载");
            }
            for (int i = 0; i < indexs.size(); i++) {
                XLTaskInfo xLTaskInfo = XLTaskHelper.instance().getBtSubTaskInfo(taskId, indexs.get(i)).mTaskInfo;
                xLTaskInfo.mFileName = torrentInfo.mSubFileInfo[indexs.get(i)].mFileName;
                xLTaskInfo.torrentPath = torrentPath;
                xLTaskInfo.sourceUrl = sourceUrl;
                xLTaskInfo.index = indexs.get(i);
                if (!isInitDownload) {
                    addXLTaskInfo(xLTaskInfo);
                } else {
                    updateXLTaskInfo(xLTaskInfo);
                }
            }
            getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>() {
                Disposable mD = null;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mD = d;
                    disposables.add(d);
                }

                @Override
                public void onNext(XLTaskInfo xlTaskInfo) {
                    LoggerHelper.info(TAG, xlTaskInfo.toString());
                    switch (String.valueOf(xlTaskInfo.mTaskStatus)) {
                        case "0":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_0");
                            if (xlTaskInfo.mFileSize > 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize) {
                                mD.dispose();
                                xlTaskInfo.timestamp = System.currentTimeMillis();
                                xlTaskInfo.mTaskStatus = 2;
                            }else {
                                xlTaskInfo.mDownloadSpeed = 0;
                            }
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_1");
                            if (isStartApp) stopTask(xlTaskInfo.mTaskId);
                            break;
                        case "2":
                            mD.dispose();
                            LoggerHelper.info(TAG, "TORRENT_STATUS_2");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            break;
                        case "3":
                            mD.dispose();
                            LoggerHelper.info(TAG, "TORRENT_STATUS_3");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            ToastUtil.showMessage("资源下载失败");
                            break;
                        case "4":
                            mD.dispose();
                            LoggerHelper.info(TAG, "TORRENT_STATUS_4");
                            xlTaskInfo.mDownloadSpeed = 0;
                            break;
                        default:
                            mD.dispose();
                            LoggerHelper.info(TAG, "TORRENT_STATUS_DEFAULT");
                            break;
                    }
                    for (int i = 0; i < newIndexs.size(); i++) {
                        updateXLTaskInfo(XLTaskHelper.instance().getBtSubTaskInfo(xlTaskInfo.mTaskId, newIndexs.get(i)).mTaskInfo);
                    }
                    try {
                        if (nextCall != null) {
                            nextCall.accept(xlTaskInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    try {
                        if (errorCall != null) {
                            errorCall.accept(t);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskId;
    }

    /**
     * 添加磁力链任务
     *
     * @param url      磁力链接 magnet:? 开头
     * @param savePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static long addMagnetTask(final String url, final String savePath, String fileName,
                                     final Context context, final boolean isPlayVideo, final boolean isInitDownload, final boolean isStartApp,
                                     final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try {
            taskId = XLTaskHelper.instance().addMagnetTask(url, savePath, fileName);
            getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>() {
                Disposable mD = null;
                MaterialDialog loadingDialog;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mD = d;
                    disposables.add(d);
                    loadingDialog = new MaterialDialog.Builder(context)
                            .title("Loading")
                            .cancelable(true)
                            .progress(true, 70)
                            .titleColor(ValueUtil.getCustomColor(context, R.styleable.BaseColor_com_font_A))
                            .backgroundColor(context.getResources().getColor(R.color.color_FFFFFF))
                            .widgetColor(ValueUtil.getCustomColor(context, R.styleable.BaseColor_com_assist_A))
                            .build();
                }

                @Override
                public void onNext(XLTaskInfo xlTaskInfo) {
                    LoggerHelper.info(TAG, xlTaskInfo.toString());
                    switch (String.valueOf(xlTaskInfo.mTaskStatus)) {
                        case "0":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_0");
                            if (xlTaskInfo.mFileSize > 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize) {
                                mD.dispose();
                                xlTaskInfo.timestamp = System.currentTimeMillis();
                                if (!isInitDownload && loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                } else if (!loadingDialog.isShowing()) {
                                    loadingDialog.show();
                                }
                            }else xlTaskInfo.mDownloadSpeed = 0;
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_1");
                            if (isStartApp) stopTask(xlTaskInfo.mTaskId);
                            if (!isInitDownload && !loadingDialog.isShowing()) {
                                loadingDialog.show();
                            }
                            break;
                        case "2":
                            mD.dispose();
                            LoggerHelper.info(TAG, "MAGNET_STATUS_2");
                            if (!isInitDownload && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            xlTaskInfo.mTaskStatus = 2;
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            String torrentPath = PATH_OFFLINE_DOWNLOAD + XLTaskHelper.instance().getFileName(url);
                            TorrentInfo torrentInfo = XLTaskHelper.instance().getTorrentInfo(torrentPath);
                            torrentInfo.torrentPath = torrentPath;
                            if (!(context instanceof Application)) {
                                DownloadTorrentDialog dialog = new DownloadTorrentDialog(context, url, torrentInfo, isPlayVideo);
                                dialog.show();
                            }
                            break;
                        case "3":
                            mD.dispose();
                            LoggerHelper.info(TAG, "MAGNET_STATUS_3");
                            if (!isInitDownload && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            ToastUtil.showMessage("资源下载失败");
                            break;
                        case "4":
                            mD.dispose();
                            LoggerHelper.info(TAG, "TORRENT_STATUS_4");
                            xlTaskInfo.mDownloadSpeed = 0;
                        default:
                            mD.dispose();
                            LoggerHelper.info(TAG, "MAGNET_STATUS_DEFAULT");
                            if (!isInitDownload && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            break;
                    }
                    updateXLTaskInfo(xlTaskInfo);
                    try {
                        if (nextCall != null) {
                            nextCall.accept(xlTaskInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if (!isInitDownload && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    try {
                        if (errorCall != null) {
                            errorCall.accept(t);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {
                    if (!isInitDownload && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskId;
    }

    /**
     * 添加迅雷链接任务 支持thunder:// ftp:// ed2k:// http:// https:// 协议
     *
     * @param url
     * @param savePath 下载文件保存路径
     * @param fileName 下载文件名 可以通过 getFileName(url) 获取到,为空默认为getFileName(url)的值
     * @return
     */
    public static long addThunderTask(String url, String savePath, @Nullable String fileName,
                                      final Context context, final boolean isPlayVideo, final boolean isStartApp, final ArrayList<String> links, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try {
            taskId = XLTaskHelper.instance().addThunderTask(url, savePath, fileName);
            String localUrl = XLTaskHelper.instance().getLoclUrl(PATH_OFFLINE_DOWNLOAD +
                    XLTaskHelper.instance().getFileName(url));
            if (isPlayVideo) {
                SourceHistoryModel oldSourceHistoryModel = RealmUtil.queryHistoryModelByLink(url);
                SourceHistoryModel sourceHistoryModel = new SourceHistoryModel();
                sourceHistoryModel.setLink(localUrl);
                sourceHistoryModel.setStatus(Constant.STATUS_1);
                if (oldSourceHistoryModel != null) {
                    oldSourceHistoryModel.setStatus(Constant.STATUS_0);
                    sourceHistoryModel.setSourceDetailModel(oldSourceHistoryModel.getSourceDetailModel());
                }
                else {
                    SourceDetailModel sourceDetailModel = new SourceDetailModel();
                    sourceDetailModel.setTitle(url);
                    sourceHistoryModel.setSourceDetailModel(sourceDetailModel);
                }
                RealmUtil.copyOrUpdateHistoryModel(oldSourceHistoryModel, false);
                RealmUtil.copyOrUpdateHistoryModel(sourceHistoryModel, false);

                ArrayList<String> newLinks = new ArrayList<>();
                for (int i = 0; i < links.size(); i++) {
                    if (links.get(i).equals(url)) newLinks.add(localUrl);
                    else newLinks.add(links.get(i));
                }
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("url", localUrl);
                intent.putStringArrayListExtra("urlList", newLinks);
                context.startActivity(intent);
            }
            getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>() {
                Disposable mD = null;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mD = d;
                    disposables.add(d);
                }

                @Override
                public void onNext(XLTaskInfo xlTaskInfo) {
                    LoggerHelper.info(TAG, xlTaskInfo.toString());
                    switch (String.valueOf(xlTaskInfo.mTaskStatus)) {
                        case "0":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_0");
                            if (xlTaskInfo.mFileSize > 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize) {
                                mD.dispose();
                                xlTaskInfo.timestamp = System.currentTimeMillis();
                                xlTaskInfo.mTaskStatus = 2;
                            }else xlTaskInfo.mDownloadSpeed = 0;
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_1");
                            if (isStartApp) stopTask(xlTaskInfo.mTaskId);
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_2");
                            mD.dispose();
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_3");
                            mD.dispose();
                            ToastUtil.showMessage("资源下载失败");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            break;
                        case "4":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_4");
                            mD.dispose();
                            xlTaskInfo.mDownloadSpeed = 0;
                        default:
                            LoggerHelper.info(TAG, "THUNDER_STATUS_DEFAULT");
                            mD.dispose();
                            break;
                    }
                    updateXLTaskInfo(xlTaskInfo);
                    try {
                        if (nextCall != null) {
                            nextCall.accept(xlTaskInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    try {
                        if (errorCall != null) {
                            errorCall.accept(t);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskId;
    }

    /**
     * 获取某个文件的本地proxy url,如果是音视频文件可以实现变下边播
     *
     * @param filePath
     * @return
     */
    public static String getLoclUrl(String filePath) {
        return XLTaskHelper.instance().getLoclUrl(filePath);
    }

    /**
     * 通过链接获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        return XLTaskHelper.instance().getFileName(url);
    }

    /**
     * 删除一个任务，会把文件也删掉
     *
     * @param taskId
     * @param savePath
     */
    public static void deleteTask(long taskId, final String savePath) {
        XLTaskHelper.instance().deleteTask(taskId, savePath);
    }

    /**
     * 停止任务 文件保留
     *
     * @param taskId
     */
    public static void stopTask(long taskId) {
        XLTaskHelper.instance().stopTask(taskId);
    }

    /**
     * 删除任务 文件保留
     *
     * @param taskId
     */
    public static void removeTask(long taskId) {
        XLTaskHelper.instance().removeTask(taskId);
    }

    /**
     * 开始任务
     *
     * @param context
     * @param xlTaskInfo
     */
    public static void startTask(Context context, XLTaskInfo xlTaskInfo) {
        XLDownloadManager.getInstance().releaseTask(xlTaskInfo.mTaskId);
        if (xlTaskInfo.index >= 0) {
            addTorrentTask(xlTaskInfo.sourceUrl, xlTaskInfo.torrentPath,
                    PATH_OFFLINE_DOWNLOAD, Collections.singletonList(xlTaskInfo.index), true);
        } else {
            addNormalTask(context, xlTaskInfo.sourceUrl, false, true);
        }
    }

    /**
     * 获取下载任务的被观察者
     *
     * @param taskId
     * @return
     */
    public static Observable getDownloadObservable(final long taskId) {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, XLTaskInfo>() {

                    @Override
                    public XLTaskInfo apply(@NonNull Long aLong) throws Exception {
                        return XLTaskHelper.instance().getTaskInfo(taskId);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 迅雷thunder://地址与普通url地址转换
     * 其实迅雷的thunder://地址就是将普通url地址加前缀‘AA’、后缀‘ZZ’，再base64编码后得到的字符串
     *
     * @param url
     * @return
     */
    public static String getRealUrl(String url) {
        if (url.startsWith("thunder://")) url = XLDownloadManager.getInstance().parserThunderUrl(url);
        return url;
    }

    public static void addXLTaskInfo(XLTaskInfo xLTaskInfo) {
        if (xLTaskInfo == null) return;
        if (xLTaskInfos == null) xLTaskInfos = new Vector<>();
        xLTaskInfos.add(xLTaskInfo);
        Collections.sort(DownloadManager.xLTaskInfos, comparator);
        LoggerHelper.info(TAG, GsonUtil.bean2json(xLTaskInfos));
        SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_XLTASKINFOS,
                GsonUtil.bean2json(xLTaskInfos));
    }

    public static void removeXLTaskInfo(XLTaskInfo xLTaskInfo) {
        if (xLTaskInfos == null || xLTaskInfo == null) return;
        for (int i = 0; i < xLTaskInfos.size(); i++) {
            if (xLTaskInfo.mFileName.equals(xLTaskInfos.get(i).mFileName)) {
                xLTaskInfos.remove(xLTaskInfo);
                break;
            }
        }
        Collections.sort(DownloadManager.xLTaskInfos, comparator);
        SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_XLTASKINFOS,
                GsonUtil.bean2json(xLTaskInfos));
    }

    private static void updateXLTaskInfo(XLTaskInfo xLTaskInfo) {
        if (xLTaskInfo == null || xLTaskInfos == null) return;
        for (int i = 0; i < xLTaskInfos.size(); i++) {
            if ((!TextUtils.isEmpty(xLTaskInfo.mFileName) && !TextUtils.isEmpty(xLTaskInfos.get(i).mFileName)
                    && xLTaskInfos.get(i).mFileName.equals(xLTaskInfo.mFileName))
                    || (xLTaskInfo.mTaskId == xLTaskInfos.get(i).mTaskId && (TextUtils.isEmpty(xLTaskInfo.mFileName) && !TextUtils.isEmpty(xLTaskInfos.get(i).mFileName)
                    || !TextUtils.isEmpty(xLTaskInfo.mFileName) && TextUtils.isEmpty(xLTaskInfos.get(i).mFileName)))) {
                xLTaskInfos.get(i).mAddedHighSourceState = xLTaskInfo.mAddedHighSourceState;
                xLTaskInfos.get(i).mAdditionalResCount = xLTaskInfo.mAdditionalResCount;
                xLTaskInfos.get(i).mAdditionalResDCDNBytes = xLTaskInfo.mAdditionalResDCDNBytes;
                xLTaskInfos.get(i).mAdditionalResDCDNSpeed = xLTaskInfo.mAdditionalResDCDNSpeed;
                xLTaskInfos.get(i).mAdditionalResPeerBytes = xLTaskInfo.mAdditionalResPeerBytes;
                xLTaskInfos.get(i).mAdditionalResPeerSpeed = xLTaskInfo.mAdditionalResPeerSpeed;
                xLTaskInfos.get(i).mAdditionalResType = xLTaskInfo.mAdditionalResType;
                xLTaskInfos.get(i).mAdditionalResVipRecvBytes = xLTaskInfo.mAdditionalResVipRecvBytes;
                xLTaskInfos.get(i).mAdditionalResVipSpeed = xLTaskInfo.mAdditionalResVipSpeed;
                xLTaskInfos.get(i).mCid = xLTaskInfo.mCid;
                if (xLTaskInfo.mDownloadSize > 0)
                    xLTaskInfos.get(i).mDownloadSize = xLTaskInfo.mDownloadSize;
                xLTaskInfos.get(i).mDownloadSpeed = xLTaskInfo.mDownloadSpeed;
                xLTaskInfos.get(i).mErrorCode = xLTaskInfo.mErrorCode;
                if (!TextUtils.isEmpty(xLTaskInfo.mFileName))
                    xLTaskInfos.get(i).mFileName = xLTaskInfo.mFileName;
                if (xLTaskInfo.mFileSize > 0)
                    xLTaskInfos.get(i).mFileSize = xLTaskInfo.mFileSize;
                xLTaskInfos.get(i).mGcid = xLTaskInfo.mGcid;
                xLTaskInfos.get(i).mInfoLen = xLTaskInfo.mInfoLen;
                xLTaskInfos.get(i).mOriginRecvBytes = xLTaskInfo.mOriginRecvBytes;
                xLTaskInfos.get(i).mOriginSpeed = xLTaskInfo.mOriginSpeed;
                xLTaskInfos.get(i).mP2PRecvBytes = xLTaskInfo.mP2PRecvBytes;
                xLTaskInfos.get(i).mP2PSpeed = xLTaskInfo.mP2PSpeed;
                xLTaskInfos.get(i).mP2SRecvBytes = xLTaskInfo.mP2SRecvBytes;
                xLTaskInfos.get(i).mP2SSpeed = xLTaskInfo.mP2SSpeed;
                xLTaskInfos.get(i).mQueryIndexStatus = xLTaskInfo.mQueryIndexStatus;
                xLTaskInfos.get(i).mTaskId = xLTaskInfo.mTaskId;
                xLTaskInfos.get(i).mTaskStatus = xLTaskInfo.mTaskStatus;
                xLTaskInfos.get(i).timestamp = xLTaskInfo.timestamp;
                break;
            }
        }
        Collections.sort(DownloadManager.xLTaskInfos, comparator);
        SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_XLTASKINFOS,
                GsonUtil.bean2json(xLTaskInfos));
    }

    public static void initDownload() {
        if (xLTaskInfos != null && xLTaskInfos.size() > 0) {
            for (int i = 0; i < xLTaskInfos.size(); i++) {
                XLTaskInfo xlTaskInfo = xLTaskInfos.get(i);
                if (xlTaskInfo.mTaskStatus != 2 && !(xlTaskInfo.mFileSize > 0 && xlTaskInfo.mFileSize == xlTaskInfo.mDownloadSize)) {
                    if (xlTaskInfo.index >= 0) {
                        addTorrentTask(xlTaskInfo.sourceUrl, xlTaskInfo.torrentPath,
                                PATH_OFFLINE_DOWNLOAD, Collections.singletonList(xlTaskInfo.index), true, true, null, null);
                    } else {
                        addNormalTask(BaseApplication.appContext, xlTaskInfo.sourceUrl, false, true, new ArrayList<String>(), true, null, null);
                    }
                }
            }
            Collections.sort(DownloadManager.xLTaskInfos, comparator);
        }
    }

    public static boolean isInDownloadQueue(String sourceUrl, String fileName) {
        return isInDownloadQueue(sourceUrl, fileName, -1);
    }

    public static boolean isInDownloadQueue(String sourceUrl, String fileName, int index) {
        if (xLTaskInfos == null || xLTaskInfos.size() == 0 || TextUtils.isEmpty(sourceUrl))
            return false;
        for (int i = 0; i < xLTaskInfos.size(); i++) {
            if (xLTaskInfos.get(i).sourceUrl.equals(sourceUrl) && xLTaskInfos.get(i).index == index)
                return true;
            if (FileUtil.isFileExists(PATH_OFFLINE_DOWNLOAD + fileName)) return true;
        }
        return false;
    }

    public static void removeAllXLTaskInfo(){
        disposables.clear();
        if (xLTaskInfos == null) return;
        for (int i = 0; i < xLTaskInfos.size(); i++) {
            XLTaskInfo xlTaskInfo = xLTaskInfos.get(i);
            DownloadManager.deleteTask(xlTaskInfo.mTaskId, PATH_OFFLINE_DOWNLOAD + xlTaskInfo.mFileName);
        }
        xLTaskInfos.clear();
        SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_XLTASKINFOS, "[]");
    }

    public static void stopAllTask(){
        if (xLTaskInfos != null && !xLTaskInfos.isEmpty()){
            for (int i = 0; i < xLTaskInfos.size(); i++) {
                DownloadManager.stopTask(xLTaskInfos.get(i).mTaskId);
            }
        }
    }

    public static void startAllTask(Context context){
        if (xLTaskInfos != null && !xLTaskInfos.isEmpty()){
            for (int i = 0; i < xLTaskInfos.size(); i++) {
                DownloadManager.startTask(context, xLTaskInfos.get(i));
            }
        }
    }

}