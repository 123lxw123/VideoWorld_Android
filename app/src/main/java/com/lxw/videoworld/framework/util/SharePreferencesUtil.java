package com.lxw.videoworld.framework.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class SharePreferencesUtil {

    public static void setStringSharePreferences(Context context, String key, String value) {
        //实例化SharedPreferences对象
        SharedPreferences sharedPreferences = (SharedPreferences) PreferenceManager
                .getDefaultSharedPreferences(context);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString(key, value);
        //提交当前数据
        editor.commit();
    }

    public static String getStringSharePreferences(Context context, String key, String defaultValue) {
        //实例化SharedPreferences对象
        SharedPreferences sharedPreferences = (SharedPreferences) PreferenceManager
                .getDefaultSharedPreferences(context);
        //获取key相应value 否则返回默认值
       String value = sharedPreferences.getString(key, defaultValue);
        return value;
    }

    public static void setIntSharePreferences(Context context, String key, int value) {
        //实例化SharedPreferences对象
        SharedPreferences sharedPreferences = (SharedPreferences) PreferenceManager
                .getDefaultSharedPreferences(context);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }

    public static int getIntSharePreferences(Context context, String key, int defaultValue) {
        //实例化SharedPreferences对象
        SharedPreferences sharedPreferences = (SharedPreferences) PreferenceManager
                .getDefaultSharedPreferences(context);
        //获取key相应value 否则返回默认值
        int value = sharedPreferences.getInt(key, defaultValue);
        return value;
    }
}
