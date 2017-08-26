package com.lxw.videoworld.app.service;

import android.content.Context;
import android.content.Intent;

import com.lxw.videoworld.app.ui.PlayVideoActivity;
import com.lxw.videoworld.framework.util.ToastUtil;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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
                getDownloadObservable(taskId).subscribe(new Observer<XLTaskInfo>(){
                    Disposable mD = null;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mD = d;
                    }

                    @Override
                    public void onNext(XLTaskInfo xlTaskInfo) {
                        switch (String.valueOf(xlTaskInfo.mTaskId)) {
                            case "0":
                                ToastUtil.showMessage("资源已在下载队列中");
                                mD.dispose();
                                break;
                            case "1":
                                break;
                            case "2":

                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

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

    public static Observable getDownloadObservable(final long taskId){
        return Observable.interval(0, 5, TimeUnit.SECONDS)
                .map(new Function<Long, XLTaskInfo>() {

                    @Override
                    public XLTaskInfo apply(@NonNull Long aLong) throws Exception {
                        return XLTaskHelper.instance().getTaskInfo(taskId) ;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}