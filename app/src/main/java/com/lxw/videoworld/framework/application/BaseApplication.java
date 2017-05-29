package com.lxw.videoworld.framework.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;


import com.lxw.videoworld.MainActivity;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class BaseApplication extends Application implements
        Thread.UncaughtExceptionHandler {


    public static Context appContext;// 全局Context
    public static int appStartCount;// app启动次数
    private final String APP_START_COUNT = "APP_START_COUNT";


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化 realm 数据库
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(configuration);

        appContext = getApplicationContext();
        appStartCount = SharePreferencesUtil.getIntSharePreferences(appContext, APP_START_COUNT, 0);
        appStartCount = appStartCount + 1;
        SharePreferencesUtil.setIntSharePreferences(appContext, APP_START_COUNT, appStartCount);
    }

    public static Context getappContext() {
        return appContext;
    }

    public static int getAppStartCount() {
        return appStartCount;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LoggerHelper.info("UncaughtException", "UncaughtException");
        System.exit(0);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
