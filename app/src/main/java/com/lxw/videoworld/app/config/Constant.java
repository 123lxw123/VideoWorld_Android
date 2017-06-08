package com.lxw.videoworld.app.config;

import android.os.Environment;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class Constant {
    /**
     * 网络请求
     */
    public static String SOURCE_TYPE;
    public static final String API_VERSION = "v1/";
    public static final String KEY_SOURCE_TYPE = "SOURCE_TYPE";
    public static final String SOURCE_TYPE_1 = "1";
    public static final String SOURCE_TYPE_2 = "2";
    public static final String SOURCE_TYPE_3 = "3";
    public static final String BASE_URL = "http://172840g32p.iok.la:80/videoworld/" + Constant.API_VERSION;

    /** 是否debug模式 **/
    public static boolean isDebug = true;
    /** 是否旋转屏幕**/
    public static boolean isAllowScreenRoate = false;

    /** 内置SD卡路径**/
    public static String PATH__SD_CARD = Environment.getExternalStorageDirectory().toString() + "/";
    /** APP文件夹路径**/
    public static String PATH_APP = PATH__SD_CARD + "VideoWorld/";
    /** 保存图片文件夹路径**/
    public static String PATH_SAVE_PICTURE = PATH_APP + "pictures/";
    /** 离线图片文件夹路径**/
    public static String PATH_OFFLINE_PICTURE = PATH_APP + "download/";
    /** 启动页图片文件夹路径**/
    public static String PATH_SPLASH_PICTURE = PATH_APP + "splash/";
    /** 启动页图片路径**/
    public static String PATH_SPLASH_PICTURE_PNG = "splash_picture.png";

    /** 通用状态值**/
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";
    public static final String STATUS_2 = "2";
    public static final String STATUS_3 = "3";

    /**
     * 请求网络成功
     */
    public static final int CODE_SUCCESS = 1000;

    /**
     * 请求网络失败原因
     */
    /**
     * 解析数据失败
     */
    public static final String PARSE_ERROR = "数据解析失败";
    public static final int PARSE_ERROR_CODE = 2001;
    /**
     * 连接错误
     */
    public static final String CONNECT_ERROR = "网络连接失败";
    public static final int CONNECT_ERROR_CODE = 2002;
    /**
     * 连接超时
     */
    public static final String CONNECT_TIMEOUT = "网络连接超时";
    public static final int CONNECT_TIMEOUT_CODE = 2003;
    /**
     * 未知错误
     */
    public static final String UNKNOWN_ERROR = "未知错误";
    public static final int UNKNOWN_ERROR_CODE = 2004;
    /**
     * 未知错误
     */
    public static final String SERVICE_ERROR = "服务器异常";
    public static final int SERVICE_ERROR_CODE = 2005;
}
