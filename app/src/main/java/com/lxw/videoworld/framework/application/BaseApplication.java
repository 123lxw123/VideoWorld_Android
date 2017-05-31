package com.lxw.videoworld.framework.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.lxw.videoworld.app.ui.MainActivity;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.ManifestUtil;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import cn.jpush.android.api.JPushInterface;
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
    private String versionName;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            this.versionName = ManifestUtil.getApkVersionName(getApplicationContext());
//            Log.i("<<",versionName);
        } catch (Exception e) {
            this.versionName = "1.0";
        }
        // Jpush 初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        /**
         * Hotfix2.0初始化
         */
        SophixManager.getInstance().setContext(this)//<必选> Application上下文context
                .setAppVersion(versionName)//<必选>应用的版本名称
                .setAesKey(null)//<可选>用户自定义aes秘钥, 会对补丁包采用对称加密。这个参数值必须是16位数字或字母的组合，是和补丁工具设置里面AES Key保持完全一致, 补丁才能正确被解密进而加载
                .setEnableDebug(true)//<可选> 默认为false, 是否调试模式, 调试模式下会输出日志以及不进行补丁签名校验. 线下调试此参数可以设置为true, 查看日志过滤TAG:Sophix, 同时强制不对补丁进行签名校验, 所有就算补丁未签名或者签名失败也发现可以加载成功. 但是正式发布该参数必须为false, false会对补丁做签名校验, 否则就可能存在安全漏洞风险
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {//<可选> 设置patch加载状态监听器, 该方法参数需要实现
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
//                            SophixManager.getInstance().cleanPatches();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
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
