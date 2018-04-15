package com.lxw.videoworld.app.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;

import com.lxw.videoworld.R;

/**
 * Created by Zion on 2018/4/15.
 */

public class ColorUtil {

    /**
     * 获取主题某颜色的值
     * @param index 如 R.styleable.BaseColor_com_wm_bg
     * @return
     */
    public static int getCustomColor(Context context, @StyleableRes int index){
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.BaseColor, 0, 0);
        int color = a.getColor(index, 0xFFFFFF);
        a.recycle();
        return color;
    }
}
