package com.lxw.videoworld.framework.util.floatutil;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Screen {
    public static final int width = 0;
    public static final int height = 1;

    @IntDef({width, height})
    @Retention(RetentionPolicy.SOURCE)
    @interface screenType {
    }
}
