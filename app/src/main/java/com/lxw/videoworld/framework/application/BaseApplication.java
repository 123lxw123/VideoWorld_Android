package com.lxw.videoworld.framework.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.app.service.DownloadManager;
import com.lxw.videoworld.app.ui.MainActivity;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.GsonUtil;
import com.lxw.videoworld.framework.util.ManifestUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class BaseApplication extends Application implements Thread.UncaughtExceptionHandler {

    public static String uid;// 手机唯一标识
    public static Context appContext;// 全局Context
    public static int appStartCount;// app启动次数
    public static boolean isFirstHomePage = true;
    public static boolean isFirstSearchPage = true;
    private final String APP_START_COUNT = "APP_START_COUNT";
    private String versionName;
    public static String appId = "a21eba392a5c6257";
    public static String appSecret = "e5bf305a1d2881fa";

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            this.versionName = ManifestUtil.getApkVersionName(getApplicationContext());
//            Log.i("<<",versionName);
        } catch (Exception e) {
            this.versionName = "1.0";
        }

        SophixManager.getInstance().setContext(this).setAppVersion(versionName).setAesKey(null)
                .setEnableDebug(true).setPatchLoadStatusStub(new PatchLoadStatusListener() {
            @Override
            public void onLoad(final int mode, final int code, final String info, final int
                    handlePatchVersion) {
                // 补丁加载回调通知
                if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                    // 表明补丁加载成功
                } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                    // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                    // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                } else {
                    // 其它错误信息, 查看PatchStatus类说明
                }
            }
        }).initialize();
// queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();

        // realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);

        uid = android.os.Build.SERIAL;
        appContext = getApplicationContext();
        // app启动次数
        appStartCount = SharePreferencesUtil.getIntSharePreferences(appContext, APP_START_COUNT, 0);
        appStartCount = appStartCount + 1;
        SharePreferencesUtil.setIntSharePreferences(appContext, APP_START_COUNT, appStartCount);

        // 皮肤
        Constant.THEME_TYPE = SharePreferencesUtil.getStringSharePreferences(appContext, Constant
                .KEY_THEME_TYPE, Constant.THEME_TYPE_1);
        // 切换影视来源
        Constant.SOURCE_TYPE = SharePreferencesUtil.getStringSharePreferences(appContext,
                Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_5);
        // GridLayoutManager 每行显示列数
        Constant.GRIDLAYOUTMANAGER_SPANCOUNT = SharePreferencesUtil.getIntSharePreferences
                (appContext, Constant.KEY_GRIDLAYOUTMANAGER_SPANCOUNT, Constant
                        .DEFAULT_GRIDLAYOUTMANAGER_SPANCOUNT);
        // 切换搜索引擎
        Constant.SEARCH_TYPE = SharePreferencesUtil.getStringSharePreferences(appContext,
                Constant.KEY_SEARCH_TYPE, Constant.SEARCH_TYPE_2);
        // 初始化迅雷下载
        XLTaskHelper.init(getApplicationContext());
        // 初始化下载种子任务信息集合
        String taskInfoJsonString = SharePreferencesUtil.getStringSharePreferences(appContext,
                Constant.KEY_DOWNLOAD_XLTASKINFOS, "");
        if(!TextUtils.isEmpty(taskInfoJsonString)){
            DownloadManager.xLTaskInfos = GsonUtil.json2Vector(taskInfoJsonString, XLTaskInfo[].class);
            DownloadManager.initDownload();
        }
        // Jpush 初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Context getAppContext() {
        return appContext;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LoggerHelper.info("UncaughtException", "UncaughtException");
        DownloadManager.stopAllTask();
        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        System.exit(0);
                        Intent intent = new Intent(appContext, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
