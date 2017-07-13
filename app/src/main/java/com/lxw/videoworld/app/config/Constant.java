package com.lxw.videoworld.app.config;

import android.os.Environment;

import com.lxw.videoworld.app.model.ConfigModel;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class Constant {

    public static ConfigModel configModel;
    /**
     * 网络请求
     */
    public static String SOURCE_TYPE;
    public static final String KEY_SOURCE_TYPE = "SOURCE_TYPE";
    public static final String SOURCE_TYPE_1 = "1";// 飘花电影
    public static final String SOURCE_TYPE_2 = "2";// 猫扑电影
    public static final String SOURCE_TYPE_3 = "3";// 阳光电影

    public static String SEARCH_TYPE;
    public static final String SEARCH_TYPE_1 = "1";// 种子搜索
    public static final String SEARCH_TYPE_2 = "2";// 屌丝搜索
    public static final String KEY_SEARCH_TYPE = "SEARCH_TYPE";
    public static final String KEY_SEARCH_HOTWORDS = "KEY_SEARCH_HOTWORDS";

    public static final String KEY_NOTICE = "KEY_NOTICE";

    public static String THEME_TYPE;
    public static final String THEME_TYPE_1 = "1";// glod
    public static final String THEME_TYPE_2 = "2";// red
    public static final String THEME_TYPE_3 = "3";// blue
    public static final String KEY_THEME_TYPE = "THEME_TYPE";

    public static final String API_VERSION = "v1/";
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

    /** GridLayoutManager 每行显示列数**/
    public static int GRIDLAYOUTMANAGER_SPANCOUNT = 3;
    public static final String KEY_GRIDLAYOUTMANAGER_SPANCOUNT = "GRIDLAYOUTMANAGER_SPANCOUNT";
    public static final int DEFAULT_GRIDLAYOUTMANAGER_SPANCOUNT = 3;
    /** Banner 条数和列表条数**/
    public static final int BANNER_LIMIT = 5;
    public static final int LIST_LIMIT = 18;
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

    public static final String TAB_1 = "dianying";
    public static final String TAB_2 = "dianshi";
    public static final String TAB_3 = "zongyi";
    public static final String TAB_4 = "dongman";
    public static final String TAB_5 = "youxi";

    public static final String CATEGORY_1 = "dongzuo";
    public static final String CATEGORY_2 = "xiju";
    public static final String CATEGORY_3 = "aiqing";
    public static final String CATEGORY_4 = "kehuan";
    public static final String CATEGORY_5 = "juqing";
    public static final String CATEGORY_6 = "xuannian";
    public static final String CATEGORY_7 = "wenyi";
    public static final String CATEGORY_8 = "zhanzheng";
    public static final String CATEGORY_9 = "kongbu";
    public static final String CATEGORY_10 = "zainan";
    public static final String CATEGORY_11 = "lianxuju";
    public static final String CATEGORY_12 = "dongman";
    public static final String CATEGORY_13 = "zongyijiemu";

    public static final String CATEGORY_14 = "dy";
    public static final String CATEGORY_15 = "ds";
    public static final String CATEGORY_16 = "dm";


    public static final String CATEGORY_17 = "gndy";// 电影
    public static final String CATEGORY_18 = "tv";// 电视剧
    public static final String CATEGORY_19 = "zongyi2013";// 综艺
    public static final String CATEGORY_20 = "dongman";// 动漫
    public static final String CATEGORY_21 = "game";// 游戏
    public static final String CATEGORY_22 = "2009zongyi";// 旧版综艺

    public static final String TYPE_0 = "all";
    public static final String TYPE_1 = "jddy";// 经典电影
    public static final String TYPE_2 = "dyzz";// 最新电影
    public static final String TYPE_3 = "hytv";// 华语电视剧
    public static final String TYPE_4 = "rihantv";// 日韩电视剧
    public static final String TYPE_5 = "oumeitv";// 欧美电视剧
    public static final String TYPE_6 = "daluzongyi";// 大陆综艺
    public static final String TYPE_7 = "taiwanzongyi";// 台湾综艺
    public static final String TYPE_8 = "qitazongyi";// 其他综艺
    public static final String TYPE_9 = "jingdianyouxifabu";// 经典游戏
    public static final String TYPE_10 = "remenyouxi";// 热门游戏
    public static final String TYPE_11 = "zuixinyouxifabu";// 最新游戏

    //    搜索
    public static final String BASE_ZHONGZI_SEARCH_1 = "http://www.zhongziso.com/list_ctime/keyword/page";// 时间排序
    public static final String BASE_ZHONGZI_SEARCH_2 = "http://www.zhongziso.com/list_click/keyword/page";// 点击
    public static final String BASE_ZHONGZI_SEARCH_3 = "http://www.zhongziso.com/list_length/keyword/page";// 大小
    public static final String BASE_DIAOSI_SEARCH = "http://www.diaosisou.net/";// 屌丝搜索首页
    public static final String BASE_DIAOSI_SEARCH_1 = "http://www.diaosisou.net/list/keyword/page/time_d";// 时间排序
    public static final String BASE_DIAOSI_SEARCH_2 = "http://www.diaosisou.net/list/keyword/page/size_d";// 大小
    public static final String BASE_DIAOSI_SEARCH_3 = "http://www.diaosisou.net/list/keyword/page/rala_d";// 相关度
}
