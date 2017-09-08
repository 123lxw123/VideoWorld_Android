package com.lxw.videoworld.app.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.lxw.videoworld.app.ui.PlayVideoActivity;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.Base64Util;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.lxw.videoworld.framework.widget.DownloadTorrentDialog;
import com.lxw.videoworld.framework.widget.LoadingDialog;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.TorrentInfo;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 *
 * Created by Zion on 2017/8/23.
 */

public class DownloadManager {
    public static List<String> downloadUrls = new ArrayList<>();
    public static Map<String, XLTaskInfo> xLTaskInfos = new HashMap<>();
    public static final String TAG = "DownloadManager";

    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo) {
        return addNormalTask(context, link, isPlayVideo, null, null);
    }

    /**
     * 所有的下载都走这个方法，有它判断调用迅雷对应的Api
     * @param context
     * @param link
     * @param isPlayVideo
     * @return
     */
    public static long addNormalTask(final Context context, final String link, final boolean isPlayVideo, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        if(link == null) return -1;
        if(downloadUrls.contains(link)) {
            ToastUtil.showMessage("资源已在下载队列中");
            return -1;
        }
        long taskId = -1;
        try {
            if (link.startsWith("magnet:?") || XLTaskHelper.instance().getFileName(link).endsWith("torrent")) {
                if(link.startsWith("magnet:?")){
                    taskId = addMagnetTask(link, PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, nextCall, errorCall);
                }else {
                    taskId = addMagnetTask(getRealUrl(link), PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, nextCall, errorCall);
                }
            } else {
                taskId = addThunderTask(link, PATH_OFFLINE_DOWNLOAD, null, context, isPlayVideo, nextCall, errorCall);
            }
            downloadUrls.add(link);
            xLTaskInfos.put(link, XLTaskHelper.instance().getTaskInfo(taskId));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return taskId;
    }

    public static long addTorrentTask(String torrentPath, String savePath, int[] indexs, Consumer<? super XLTaskInfo> nextCall, Consumer<? super Throwable> errorCall) {
        return addTorrentTask(torrentPath, savePath, indexs, -1, nextCall, errorCall);
    }

    /**
     * 添加种子下载任务,如果是磁力链需要先通过addMagnetTask将种子下载下来
     * @param torrentPath 种子地址
     * @param savePath 保存路径
     * @param indexs 需要下载的文件索引
     * @return
     * @throws Exception
     */
    public static long addTorrentTask(String torrentPath, String savePath, int[] indexs, int index,
                                      final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try {
            taskId = XLTaskHelper.instance().addTorrentTask(torrentPath, savePath, indexs);
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
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_1");
                            if (xlTaskInfo.mFileSize != 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize)
                                mD.dispose();
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_2");
                            mD.dispose();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "TORRENT_STATUS_3");
                            ToastUtil.showMessage("资源下载失败");
                            mD.dispose();
                            break;
                        default:
                            LoggerHelper.info(TAG, "TORRENT_STATUS_DEFAULT");
                            break;
                    }
                    try{
                        if (nextCall != null){
                            nextCall.accept(xlTaskInfo);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    try{
                        if (errorCall != null){
                            errorCall.accept(t);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {

                }
            });
            for(int i = 0; i < indexs.length; i++){
                XLTaskHelper.instance().startDcdn(taskId, indexs[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskId;
    }

    /**
     * 添加磁力链任务
     * @param url 磁力链接 magnet:? 开头
     * @param savePath
     * @param fileName
     * @return
     * @throws Exception
     */
    public static long addMagnetTask(final String url, final String savePath, String fileName,
                                     final Context context, final boolean isPlayVideo, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try{
            taskId = XLTaskHelper.instance().addMagnetTask(url, savePath, fileName);
            downloadUrls.add(url);
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
                            if(!loadingDialog.isShowing()){
                                loadingDialog.show();
                            }
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_1");
                            if(!loadingDialog.isShowing()){
                                loadingDialog.show();
                            }
                            if (xlTaskInfo.mFileSize != 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize)
                                mD.dispose();
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_2");
                            mD.dispose();
                            if(loadingDialog.isShowing()){
                                loadingDialog.dismiss();
                            }
                            String torrentPath = PATH_OFFLINE_DOWNLOAD +XLTaskHelper.instance().getFileName(url);
                            TorrentInfo torrentInfo = XLTaskHelper.instance().getTorrentInfo(torrentPath);
                            torrentInfo.torrentPath = torrentPath;
                            DownloadTorrentDialog dialog = new DownloadTorrentDialog(context, torrentInfo, isPlayVideo);
                            dialog.show();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "MAGNET_STATUS_3");
                            mD.dispose();
                            if(loadingDialog.isShowing()){
                                loadingDialog.dismiss();
                            }
                            ToastUtil.showMessage("资源下载失败");
                            break;
                        default:
                            LoggerHelper.info(TAG, "MAGNET_STATUS_DEFAULT");
                            mD.dispose();
                            if(loadingDialog.isShowing()){
                                loadingDialog.dismiss();
                            }
                            break;
                    }
                    try{
                        if (nextCall != null){
                            nextCall.accept(xlTaskInfo);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }
                    try{
                        if (errorCall != null){
                            errorCall.accept(t);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {
                    if(loadingDialog.isShowing()){
                        loadingDialog.dismiss();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return taskId;
    }

    public static long addThunderTask(String url, String savePath, @Nullable String fileName, Consumer<? super XLTaskInfo> nextCall, Consumer<? super Throwable> errorCall) {
        return addThunderTask(url, savePath, fileName, null, false, nextCall, errorCall);
    }

    /**
     * 添加迅雷链接任务 支持thunder:// ftp:// ed2k:// http:// https:// 协议
     * @param url
     * @param savePath 下载文件保存路径
     * @param fileName 下载文件名 可以通过 getFileName(url) 获取到,为空默认为getFileName(url)的值
     * @return
     */
    public static long addThunderTask(String url, String savePath, @Nullable String fileName,
                                      final Context context, final boolean isPlayVideo, final Consumer<? super XLTaskInfo> nextCall, final Consumer<? super Throwable> errorCall) {
        long taskId = -1;
        try{
            taskId = XLTaskHelper.instance().addThunderTask(url, savePath, fileName);
            downloadUrls.add(url);
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
                            break;
                        case "1":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_1");
                            if (xlTaskInfo.mFileSize != 0 && xlTaskInfo.mDownloadSize == xlTaskInfo.mFileSize)
                                mD.dispose();
                            break;
                        case "2":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_2");
                            mD.dispose();
                            break;
                        case "3":
                            LoggerHelper.info(TAG, "THUNDER_STATUS_3");
                            ToastUtil.showMessage("资源下载失败");
                            mD.dispose();
                            break;
                        default:
                            LoggerHelper.info(TAG, "THUNDER_STATUS_DEFAULT");
                            break;
                    }
                    try{
                        if (nextCall != null){
                            nextCall.accept(xlTaskInfo);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    try{
                        if (errorCall != null){
                            errorCall.accept(t);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return taskId;
    }

    /**
     * 获取下载任务的被观察者
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
     * @param url
     * @return
     */
    public static String getRealUrl(String url){
        String realUrl="";
        if(!TextUtils.isEmpty(url)){
            //去掉迅雷地址前缀
            url = url.substring(10, url.length());
            //解密
            realUrl = Base64Util.decodeBase64(url);
            //去掉头AA，尾ZZ
            realUrl = realUrl.substring(2, realUrl.length()-2);
        }
        return realUrl;
    }
}