package com.lxw.videoworld.framework.util;

import android.widget.Toast;

import com.lxw.videoworld.framework.application.BaseApplication;

public class ToastUtil {

	public static void showMessage(final String msg) {
		Toast.makeText(BaseApplication.getappContext(), msg, Toast.LENGTH_SHORT).show();
	}

}
