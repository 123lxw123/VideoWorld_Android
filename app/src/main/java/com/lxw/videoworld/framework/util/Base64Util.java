package com.lxw.videoworld.framework.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Zion on 2017/8/27.
 */

public class Base64Util {

    // 加密
    public static String encodeBase64(String str) {
        String result = "";
        if(!TextUtils.isEmpty(str)) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.DEFAULT), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // 解密
    public static String decodeBase64(String str) {
        String result = "";
        if (!TextUtils.isEmpty(str)) {
            try {
                result = new String(Base64.decode(str, Base64.DEFAULT), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
