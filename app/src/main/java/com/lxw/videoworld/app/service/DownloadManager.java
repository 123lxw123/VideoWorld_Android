package com.lxw.videoworld.app.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.ui.PlayVideoActivity;
import com.lxw.videoworld.app.util.XLTaskInfoComparator;
import com.lxw.videoworld.app.widget.DownloadTorrentDialog;
import com.lxw.videoworld.framework.application.BaseApplication;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.Base64Util;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.widget.LoadingDialog;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;


/**
 * Created by Zion on 2017/8/23.
 */

public class DownloadManager {
    public static List<String> downloadUrls = new ArrayList<>();
    public static Vector<XLTaskInfo> xLTaskInfos = new Vector<>();
    public static XLTaskInfoComparator comparator = new XLTaskInfoComparator();
    public static final String TAG = "DownloadManager";

    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo) {
        return addNormalTask(context, link, isPlayVideo, null, null);
    }

    /**
     * 所有的下载都走这个方法，有它判断调用迅雷对应的Api
     *
     * @param context
     * @param link
     * @param isPlayVideo
     * @return
     */
    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        if (link == null) return -1;
        if (downloadUrls == null) downloadUrls = new ArrayList<>();
        if (downloadUrls.contains(link)) {
            ToastUtil.showMessage("资源已在下载队列中");
            return -1;
        }
        long taskId = -1;
        try {
            if (link.startsWith("magnet:?") || XLTaskHelper.instance().getFileName(link).endsWith("torrent")) {
                if (link.startsWith("magnet:?")) {
                    taskId = addMagnetTask(link, PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, nextCall, errorCall);
                } else {
                    taskId = addMagnetTask(getRealUrl(link), PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, nextCall, errorCall);
                }
            } else {
                taskId = addThunderTask(link, PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, nextCall, errorCall);
            }
            addDownloadUrl(link);
            XLTaskInfo xLTaskInfo = XLTaskHelper.instance().getTaskInfo(taskId);
            xLTaskInfo.sourceUrl = link;
            xLTaskInfo.mFileName = XLTaskHelper.instance().getFileName(link);
            addXLTaskInfo(xLTaskInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return taskId;
    }

    public static long addTorrentTask(final String torrentUrl, final String torrentPath, final String savePath, int[] indexs) {
        return addTorrentTask(torrentUrl, torrentPath, savePath, indexs, null, null);
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
    public static long addTorrentTask(final String torrentUrl, final String torrentPath, final String savePath, final int[] indexs,
                                      final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        if (torrentPath == null || indexs == null) return -1;
        if (downloadUrls == null) downloadUrls = new ArrayList<>();
        if (downloadUrls.contains(torrentPath)) {
            ToastUtil.showMessage("资源已在下载队列中");
            return -1;
        }
        long taskId = -1;
        try {
            taskId = XLTaskHelper.instance().addTorrentTask(torrentPath, savePath, indexs);
            addDownloadUrl(torrentPath);
            final TorrentInfo torrentInfo = XLTaskHelper.instance().getTorrentInfo(torrentPath);
            for (int i = 0; i < indexs.length; i++) {
                XLTaskInfo xLTaskInfo = XLTaskHelper.instance().getBtSubTaskInfo(taskId, indexs[i]).mTaskInfo;
                xLTaskInfo.mFileName = torrentInfo.mSubFileInfo[indexs[i]].mFileName;
                xLTaskInfo.sourceUrl = torrentPath;
                xLTaskInfo.torrentUrl = torrentUrl;
                xLTaskInfo.index = indexs[i];
                addXLTaskInfo(xLTaskInfo);
            }
            getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>() {
                Disposable mD = null;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mD = d;
                }

                @Override
                public void onNext(XLTaskInfo xlTaskInfo) {
                    LoggerHelper.info(TAG, xlTaskInfo.toString());
                    switch (String.valueOf(xlTaskInfo.mTaskStatus)) {
                        case "0":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_0");
                            if(xlTaskInfo.mFileSize > 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize){
                                xlTaskInfo.timestamp = System.currentTimeMillis();
                                mD.dispose();
                            }
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_1");
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_2");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            mD.dispose();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_3");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            ToastUtil.showMessage("资源下载失败");
                            mD.dispose();
                            break;
                        default:
                            LoggerHelper.info(TAG, "TORRENT_STATUS_DEFAULT");
                            mD.dispose();
                            break;
                    }
                    for (int i = 0; i < indexs.length; i++) {
                        updateXLTaskInfo(XLTaskHelper.instance().getBtSubTaskInfo(xlTaskInfo.mTaskId, indexs[i]).mTaskInfo);
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
                                     final Context context, final boolean isPlayVideo, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try {
            taskId = XLTaskHelper.instance().addMagnetTask(url, savePath, fileName);
            getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>() {
                LoadingDialog loadingDialog = new LoadingDialog(context);
                Disposable mD = null;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mD = d;
                }

                @Override
                public void onNext(XLTaskInfo xlTaskInfo) {
                    LoggerHelper.info(TAG, xlTaskInfo.toString());
                    switch (String.valueOf(xlTaskInfo.mTaskStatus)) {
                        case "0":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_0");
                            if(xlTaskInfo.mFileSize > 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize){
                                xlTaskInfo.timestamp = System.currentTimeMillis();
                                mD.dispose();
                                if (loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                }
                            }else if (!loadingDialog.isShowing()) {
                                loadingDialog.show();
                            }
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_1");
                            if (!loadingDialog.isShowing()) {
                                loadingDialog.show();
                            }
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_2");
                            mD.dispose();
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            String torrentPath = PATH_OFFLINE_DOWNLOAD + XLTaskHelper.instance().getFileName(url);
                            TorrentInfo torrentInfo = XLTaskHelper.instance().getTorrentInfo(torrentPath);
                            torrentInfo.torrentPath = torrentPath;
                            DownloadTorrentDialog dialog = new DownloadTorrentDialog(context, url, torrentInfo, isPlayVideo);
                            dialog.show();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_3");
                            mD.dispose();
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            ToastUtil.showMessage("资源下载失败");
                            break;
                        default:
                            LoggerHelper.info(TAG, "MAGNET_STATUS_DEFAULT");
                            mD.dispose();
                            if (loadingDialog.isShowing()) {
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
                    if (loadingDialog.isShowing()) {
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
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskId;
    }

    public static long addThunderTask(String url, String savePath, @Nullable String fileName, Consumer<? super XLTaskInfo> nextCall, Consumer<? super Throwable> errorCall) {
        return addThunderTask(url, savePath, fileName, null, false, nextCall, errorCall);
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
                                      final Context context, final boolean isPlayVideo, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try {
            taskId = XLTaskHelper.instance().addThunderTask(url, savePath, fileName);
            String localUrl = XLTaskHelper.instance().getLoclUrl(PATH_OFFLINE_DOWNLOAD +
                    XLTaskHelper.instance().getFileName(url));
            if (isPlayVideo) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("url", localUrl);
                context.startActivity(intent);
            }
            getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>() {
                Disposable mD = null;

                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    mD = d;
                }

                @Override
                public void onNext(XLTaskInfo xlTaskInfo) {
                    LoggerHelper.info(TAG, xlTaskInfo.toString());
                    switch (String.valueOf(xlTaskInfo.mTaskStatus)) {
                        case "0":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_0");
                            if(xlTaskInfo.mFileSize > 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize){
                                xlTaskInfo.timestamp = System.currentTimeMillis();
                                mD.dispose();
                            }
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_1");
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_2");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            mD.dispose();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_3");
                            ToastUtil.showMessage("资源下载失败");
                            xlTaskInfo.timestamp = System.currentTimeMillis();
                            mD.dispose();
                            break;
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
     * @param taskId
     */
    public static void startTask(long taskId) {
        XLTaskHelper.instance().startTask(taskId);
    }

    /**
     * 获取下载任务的被观察者
     *
     * @param taskId
     * @return
     */
    public static Observable getDownloadObservable(final long taskId) {
        return Observable.interval(0, 5, TimeUnit.SECONDS)
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
        String realUrl = "";
        if (!TextUtils.isEmpty(url)) {
            //去掉迅雷地址前缀
            url = url.substring(10, url.length());
            //解密
            realUrl = Base64Util.decodeBase64(url);
            //去掉头AA，尾ZZ
            realUrl = realUrl.substring(2, realUrl.length() - 2);
        }
        return realUrl;
    }

    public static void addDownloadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (downloadUrls == null) downloadUrls = new ArrayList<>();
            if (!downloadUrls.contains(url)) {
                downloadUrls.add(url);
                SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_URLS, downloadUrls.toString());
            }
        }
    }

    public static void removeDownloadUrl(String url) {
        if (!TextUtils.isEmpty(url) && downloadUrls != null) {
            if (downloadUrls.contains(url)) {
                downloadUrls.remove(url);
                SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_URLS, downloadUrls.toString());
            }
        }
    }

    public static void addXLTaskInfo(XLTaskInfo xLTaskInfo){
        if(xLTaskInfo == null) return;
        if (xLTaskInfos == null) xLTaskInfos = new Vector<>();
        xLTaskInfos.add(xLTaskInfo);
        Collections.sort(DownloadManager.xLTaskInfos, comparator);
        LoggerHelper.info(TAG, GsonUtil.bean2json(xLTaskInfos));
        SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_XLTASKINFOS,
                GsonUtil.bean2json(xLTaskInfos));
    }

    public static void removeXLTaskInfo(XLTaskInfo xLTaskInfo){
        if(xLTaskInfos == null || xLTaskInfo == null) return;
        for (int i = 0; i <)
        Collections.sort(DownloadManager.xLTaskInfos, comparator);
        SharePreferencesUtil.setStringSharePreferences(BaseApplication.appContext, Constant.KEY_DOWNLOAD_XLTASKINFOS,
                GsonUtil.bean2json(xLTaskInfos));
    }

    private static void updateXLTaskInfo(XLTaskInfo xLTaskInfo) {
        if (xLTaskInfo == null || xLTaskInfos == null) return;
        for (int i = 0; i < xLTaskInfos.size(); i++) {
            if (xLTaskInfo.mTaskId == xLTaskInfos.get(i).mTaskId) {
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
                xLTaskInfos.get(i).mDownloadSize = xLTaskInfo.mDownloadSize;
                xLTaskInfos.get(i).mDownloadSpeed = xLTaskInfo.mDownloadSpeed;
                xLTaskInfos.get(i).mErrorCode = xLTaskInfo.mErrorCode;
                if (!TextUtils.isEmpty(xLTaskInfo.mFileName))
                    xLTaskInfos.get(i).mFileName = xLTaskInfo.mFileName;
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

    public static void initXLTaskInfos(){
        if(xLTaskInfos != null && xLTaskInfos.size() > 0){
            for (int i = 0; i < xLTaskInfos.size(); i++){
                if(xLTaskInfos.get(i).mTaskStatus == 1) {
                    xLTaskInfos.get(i).mDownloadSpeed = 0;
                    xLTaskInfos.get(i).mTaskStatus = 0;
                }
            }
            Collections.sort(DownloadManager.xLTaskInfos, comparator);
        }
    }

}