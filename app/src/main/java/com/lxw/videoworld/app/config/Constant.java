package com.lxw.videoworld.app.config;

import android.os.Environment;

import com.lxw.videoworld.app.model.ConfigModel;
import com.lxw.videoworld.app.model.SourceDetailModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class Constant {

    public static ConfigModel configModel;
    public static List<SourceDetailModel> detailModels = new ArrayList<>();

    /**
     * 网络请求
     */
    public static String SOURCE_TYPE;
    public static final String KEY_SOURCE_TYPE = "SOURCE_TYPE";
    public static final String SOURCE_TYPE_1 = "1";// 飘花电影
    public static final String SOURCE_TYPE_2 = "2";// 猫扑电影
    public static final String SOURCE_TYPE_3 = "3";// 阳光电影
    public static final String SOURCE_TYPE_4 = "4";// 最新资源
    public static final String SOURCE_TYPE_5 = "5";// 看看屋

    public static String SEARCH_TYPE;
    public static final String SEARCH_TYPE_1 = "1";// 种子搜索
    public static final String SEARCH_TYPE_2 = "2";// 屌丝搜索
    public static final String KEY_SEARCH_TYPE = "SEARCH_TYPE";
    public static final String KEY_SEARCH_HOTWORDS = "KEY_SEARCH_HOTWORDS";

    public static final String KEY_NOTICE = "KEY_NOTICE";

    public static final String KEY_USER_SIGN_ADMIRE = "KEY_USER_SIGN_ADMIRE";
    public static final String USER_SIGN_ADMIRE_TIPS = "(签到 3 天即可获得 1 天畅玩特权，赞赏亦可获赠畅玩特权)";

    public static String THEME_TYPE;
    public static final String THEME_TYPE_1 = "1";// glod
    public static final String THEME_TYPE_2 = "2";// red
    public static final String THEME_TYPE_3 = "3";// blue
    public static final String KEY_THEME_TYPE = "THEME_TYPE";

    public static final String DOWNLOAD_TYPE = "DOWNLOAD_TYPE";
    public static final String DOWNLOAD_TYPE_ALL = "DOWNLOAD_TYPE_ALL";
    public static final String DOWNLOAD_TYPE_DOWNLOADING = "DOWNLOAD_TYPE_DOWNLOADING";
    public static final String DOWNLOAD_TYPE_COMPLETE = "DOWNLOAD_TYPE_COMPLETE";

    public static final String API_VERSION = "v1/";
    // TODO !!!测试地址!!!
    public static final String BASE_URL = "http://localhost:8080/videoworld/" + Constant.API_VERSION;

    /** 是否debug模式 **/
    public static boolean isDebug = true;
    /** 是否旋转屏幕**/
    public static boolean isAllowScreenRoate = false;
    /** 下载种子任务选择的index集合**/
    public static String KEY_DOWNLOAD_XLTASKINFOS = "KEY_DOWNLOAD_XLTASKINFOS";
    /** 是否允许切换 4g 网络时下载 **/
    public static String KEY_IS_ALLOW_4G = "KEY_IS_ALLOW_4G";

    /** 内置SD卡路径**/
    public static String PATH_SD_CARD = Environment.getExternalStorageDirectory().toString() + "/";
    /** APP文件夹路径**/
    public static String PATH_APP = PATH_SD_CARD + "VideoWorld/";
    /** 保存图片文件夹路径**/
    public static String PATH_SAVE_PICTURE = PATH_APP + "pictures/";
    /** 离线图片文件夹路径**/
    public static String PATH_OFFLINE_DOWNLOAD = PATH_APP + "download/";
    /** 启动页图片文件夹路径**/
    public static String PATH_SPLASH_PICTURE = PATH_APP + "splash/";
    /** 启动页图片路径**/
    public static String PATH_SPLASH_PICTURE_PNG = "splash_picture.png";

    /** 通用状态值**/
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";
    public static final String STATUS_2 = "2";
    public static final String STATUS_3 = "3";
    public static final String STATUS_4 = "4";

    /** GridLayoutManager 每行显示列数**/
    public static int GRIDLAYOUTMANAGER_SPANCOUNT = 3;
    public static final String KEY_GRIDLAYOUTMANAGER_SPANCOUNT = "GRIDLAYOUTMANAGER_SPANCOUNT";
    public static final int DEFAULT_GRIDLAYOUTMANAGER_SPANCOUNT = 3;
    /** Banner 条数和列表条数**/
    public static final int BANNER_LIMIT = 5;
    public static final int LIST_LIMIT = 18;

    public static final String[] videos = new String[]{"mp4", "mkv", "avi", "wmv", "flv", "f4v", "xv", "3gp", "webm", "vdat", "ts",
            "rmvb", "m3u8","rm", "mpg", "mpeg", "mov","asf", "vob", "swf"};
    public static final String[] musics = new String[]{"mp3", "ra", "wma", "ogg", "m4a", "acc", "m4r", "flac", "ac3", "mmf", "amr",
            "wv", "mod", "apg", "dsd", "mp2", "vqf", "cd", "ape", "md", "mid", "mp3pro"};
    public static final String[] images = new String[]{"bmp", "jpg", "png", "tiff", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd",
            "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "wmf", "jpeg"};
    public static final String[] documents = new String[]{"txt", "pdf", "doc", "docx", "docm", "rtf", "xls", "xlsx", "pptx", "pptm",
            "xlsm", "ppt", "html", "htm", "wpd", "torrent", "chm", "mht"};
    public static final String[] packages = new String[]{"exe", "iso", "apk", "rar", "zip", "7z", "pkg", "dmg"};
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

    public static final String CATEGORY_14 = "dy";// 电影
    public static final String CATEGORY_15 = "ds";// 电视剧
    public static final String CATEGORY_16 = "dm";// 动漫
    public static final String CATEGORY_23 = "zy";// 综艺
    public static final String CATEGORY_24 = "fl";// 福利


    public static final String CATEGORY_17 = "gndy";// 电影
    public static final String CATEGORY_18 = "tv";// 电视剧
    public static final String CATEGORY_19 = "zongyi2013";// 综艺
    public static final String CATEGORY_20 = "dongman";// 动漫
    public static final String CATEGORY_21 = "game";// 游戏
    public static final String CATEGORY_22 = "2009zongyi";// 旧版综艺

    public static final String CATEGORY_25 = "电影";
    public static final String CATEGORY_26 = "电视剧";
    public static final String CATEGORY_27 = "动漫";
    public static final String CATEGORY_28 = "综艺";
    public static final String CATEGORY_29 = "微电影";

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
    // 最新资源
    public static final String TYPE_12 = "动作片";
    public static final String TYPE_13 = "喜剧片";
    public static final String TYPE_14 = "剧情片";
    public static final String TYPE_15 = "爱情片";
    public static final String TYPE_16 = "科幻片";
    public static final String TYPE_17 = "战争片";
    public static final String TYPE_18 = "恐怖片";
    public static final String TYPE_19 = "记录片";
    public static final String TYPE_20 = "伦理片";
    public static final String TYPE_28 = "福利";

    public static final String TYPE_21 = "国产剧";
    public static final String TYPE_22 = "港台剧";
    public static final String TYPE_23 = "欧美剧";
    public static final String TYPE_24 = "日剧";
    public static final String TYPE_25 = "韩剧";
    public static final String TYPE_26 = "泰剧";

    public static final String TYPE_29 = "国产剧";
    public static final String TYPE_30 = "香港剧";
    public static final String TYPE_31 = "台湾剧";
    public static final String TYPE_32 = "韩国剧";
    public static final String TYPE_33 = "日本剧";
    public static final String TYPE_34 = "欧美剧";
    public static final String TYPE_35 = "海外剧";

    //    搜索
    public static final String BASE_ZHONGZI_SEARCH_1 = "http://www.zhongziso.com/list_ctime/keyword/page";// 时间排序
    public static final String BASE_ZHONGZI_SEARCH_2 = "http://www.zhongziso.com/list_click/keyword/page";// 点击
    public static final String BASE_ZHONGZI_SEARCH_3 = "http://www.zhongziso.com/list_length/keyword/page";// 大小
    public static final String BASE_DIAOSI_SEARCH = "http://www.diaosisou.org/";// 屌丝搜索首页
    public static final String BASE_DIAOSI_SEARCH_1 = "http://www.diaosisou.org/list/keyword/page/time_d";// 时间排序
    public static final String BASE_DIAOSI_SEARCH_2 = "http://www.diaosisou.org/list/keyword/page/size_d";// 大小
    public static final String BASE_DIAOSI_SEARCH_3 = "http://www.diaosisou.org/list/keyword/page/rala_d";// 相关度

    public static final String BASE_MAOYAN_MOVIE = "https://piaofang.maoyan.com/second-box";
}
