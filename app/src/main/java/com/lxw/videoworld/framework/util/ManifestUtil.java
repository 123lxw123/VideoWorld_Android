package com.lxw.videoworld.framework.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;

public class ManifestUtil {
	
	public static String marketCode = null;

	public static String getApkVersionName(Context context) {
		try {
			String version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
			return version;
		} catch (Exception e) {
			//
		}
		return null;
	}

	public static String getApkVersionCode(Context context) {
		try {
			int version = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
			return String.valueOf(version);
		} catch (Exception e) {
			//
		}
		return null;
	}

	public static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			//
		}
		return null;
	}
	
	public static void initMarketCode(Context context) {

		if (marketCode != null) {
			return;
		}

		marketCode = getMarketCode(context);
	}

	public static String getMarketCode() {
		return marketCode;
	}

	public static String getMarketCode(Context context) {
		String code = getMetaData(context, "APPLICATION_CHANNEL");
		if (code != null) {
			return code;
		}
		return "A1";
	}

	public static int getScreenWidth(Activity activity){
		WindowManager wm = activity.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}

	public static int getScreenHeight(Activity activity){
		WindowManager wm = activity.getWindowManager();
		int height = wm.getDefaultDisplay().getHeight();
		return height;
	}

	public static String getDeviceUid(Application application) {
		String serial = Build.SERIAL;
		String androidId =  Settings.System.getString(application.getContentResolver(), Settings.System.ANDROID_ID);
		String deviceId =  ((TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		StringBuilder stringBuilder = new StringBuilder();
		if (!TextUtils.isEmpty(serial)) {
			stringBuilder.append(serial);
		}
		if (!TextUtils.isEmpty(deviceId)) {
			stringBuilder.append(deviceId);
		}
		if (TextUtils.isEmpty(serial) && TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(androidId)) {
			stringBuilder.append(androidId);
		}
		return stringBuilder.toString();
	}
}
