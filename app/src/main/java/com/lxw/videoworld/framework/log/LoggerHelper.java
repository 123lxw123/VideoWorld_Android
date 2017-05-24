package com.lxw.videoworld.framework.log;

import com.lxw.dailynews.framework.config.Constant;
import com.orhanobut.logger.Logger;

/**
 * Created by lxw9047 on 2016/8/9.
 */
public class LoggerHelper {
    public static boolean isDebug = Constant.isDebug;

    public static void init(String tag) {
        if(isDebug) {
            Logger.init(tag);
        }
    }

    public static void json(String jsonData) {
        if(isDebug) {
        Logger.json(jsonData);
        }
    }

    public static void debug(String tag, String message) {
        if(isDebug) {
        Logger.t(tag).d(message);
        }
    }

    public static void info(String tag, String message) {
        if(isDebug) {
        Logger.t(tag).i(message);
        }
    }

    public static void error(String tag, String message) {
        if(isDebug) {
        Logger.t(tag).e(message);
        }
    }

    public static void debug(String message) {
        if(isDebug) {
        Logger.d(message);
        }
    }

    public static void info(String message) {
        if(isDebug) {
        Logger.i(message);
        }
    }

    public static void error(String message) {
        if(isDebug) {
        Logger.e(message);
        }
    }
}
