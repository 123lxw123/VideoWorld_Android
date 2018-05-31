package com.lxw.videoworld.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lxw.videoworld.app.config.Constant;
import com.lxw.videoworld.framework.log.LoggerHelper;
import com.lxw.videoworld.framework.util.SharePreferencesUtil;

/**
 * Created by Zion on 2018/1/7.
 */

public class AppInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getData().getSchemeSpecificPart();
        LoggerHelper.info("----", packageName);
        LoggerHelper.info("----", context.getPackageName());
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            if (packageName.equals(context.getPackageName())){
                Constant.SOURCE_TYPE =  Constant.SOURCE_TYPE_4;
                SharePreferencesUtil.setStringSharePreferences(context,
                        Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_4);
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {

        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            if (packageName.equals(context.getPackageName())){
                Constant.SOURCE_TYPE =  Constant.SOURCE_TYPE_4;
                SharePreferencesUtil.setStringSharePreferences(context,
                        Constant.KEY_SOURCE_TYPE, Constant.SOURCE_TYPE_4);
            }
        }
    }

}
