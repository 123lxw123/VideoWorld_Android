package com.lxw.videoworld.app.service;

import android.content.Context;
import android.content.Intent;

import com.lxw.videoworld.app.ui.PlayVideoActivity;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.TorrentInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.lxw.videoworld.app.config.Constant.PATH_OFFLINE_DOWNLOAD;


/**
 * Created by Zion on 2017/8/23.
 */

public class DownloadManager {
    public static List<Long> taskIds;

    public static long download(Context context, final String link, boolean isPlayVideo){
        long taskId = -1;
        try {
            if (link.startsWith("magnet:?")) {
                taskId = XLTaskHelper.instance().addMagentTask(link, PATH_OFFLINE_DOWNLOAD, null);
                Observable.just(taskId).delay(10, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        TorrentInfo info = XLTaskHelper.instance().getTorrentInfo(PATH_OFFLINE_DOWNLOAD + XLTaskHelper.instance().getFileName(link));
                        LoggerHelper.info("DownloadManager", GsonUtil.bean2json(info));
                    }
                });
            }else {
                taskId = XLTaskHelper.instance().addThunderTask(link, PATH_OFFLINE_DOWNLOAD, null);
                if(isPlayVideo){
                    String url = XLTaskHelper.instance().getLoclUrl(PATH_OFFLINE_DOWNLOAD +
                            XLTaskHelper.instance().getFileName(link));
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra("url",  url);
                    context.startActivity(intent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return taskId;
        }
        if(taskId != -1){
            if(taskIds == null) taskIds = new ArrayList<>();
            taskIds.add(taskId);
        }
        return taskId;
    }
}
