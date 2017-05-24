package com.lxw.videoworld.framework.config;

import android.os.Environment;

import com.lxw.dailynews.app.bean.RealmLatestNewsBean;
import com.lxw.dailynews.app.bean.RealmNewsThemeBean;

import java.util.List;

/**
 * Created by lxw9047 on 2016/10/12.
 */

public class Constant {
    /** 是否debug模式 **/
    public static boolean isDebug = true;
    /** 是否旋转屏幕**/
    public static boolean isAllowScreenRoate = false;
//    /** 启动页图片信息URL**/
//    public static String URL_SPLASH_PICTURE_INFO = "http://news-at.zhihu.com/api/4/start-image/720*1280";
//    /** 最新消息URL**/
//    public static String URL_LATEST_NEWS = "http://news-at.zhihu.com/api/4/news/latest";


    /** 内置SD卡路径**/
    public static String PATH__SD_CARD = Environment.getExternalStorageDirectory().toString() + "/";
    /** APP文件夹路径**/
    public static String PATH_APP = PATH__SD_CARD + "DailyNews/";
    /** 保存图片文件夹路径**/
    public static String PATH_SAVE_PICTURE = PATH_APP + "pictures/";
    /** 离线图片文件夹路径**/
    public static String PATH_OFFLINE_PICTURE = PATH_APP + "download/";
    /** 启动页图片文件夹路径**/
    public static String PATH_SPLASH_PICTURE = PATH_APP + "splash/";
    /** 启动页图片路径**/
    public static String PATH_SPLASH_PICTURE_PNG = "splash_picture.png";

    /** 新闻评论的类型：长评论| 短评论**/
    public static String COMMENTS_TYPE_LONG = "long-comments";
    public static String COMMENTS_TYPE_SHORT = "short-comments";
    public static String COMMENTS_TYPE_EMPTY = "empty-comments";

    /** 通用状态值**/
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";
    public static final String STATUS_2 = "2";
    public static final String STATUS_3 = "3";

    /** 离线下载传递数据**/
    public static RealmNewsThemeBean realmNewsThemeBeen;
    public static List<RealmLatestNewsBean> realmLatestNewsBeens;

    /** 离线下载热闻与新闻的文件夹**/
    public static String STORIES_TYPE_COMMON = "stories/";
    public static String STORIES_TYPE_TOP = "stories_top/";

    /** 保存离线下载图片的文件夹的个数，用于删除旧的文件**/
    public static int OFFLINE_DOWNLOAD_SIZE = 5;

    /** 获取离线下载图片的地址**/
    public static String getDownloadFilePath(String date, String storiesType, int newsId, String fileName){
        return "file://" + PATH_OFFLINE_PICTURE + date + "/" + storiesType + newsId + "/" + fileName;
    }
}
