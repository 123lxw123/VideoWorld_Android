package com.lxw.videoworld.framework.util;

import android.app.Activity;
import android.widget.Toast;

import com.lxw.videoworld.framework.application.BaseApplication;

public class ToastUtil {

	public static void showMessage(final String msg) {
		Toast.makeText(BaseApplication.getappContext(), msg, Toast.LENGTH_SHORT).show();
	}

	public static void showMessage(Activity activity, final String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

}
