package com.lxw.videoworld.framework.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleableRes;
import android.text.TextUtils;

import com.lxw.videoworld.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxw9047 on 2016/11/25.
 */

public class ValueUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 数组 string 转成 List
     * @param string 数组 string
     * @return
     */
    public static List<String> string2list(String string){
        if(!TextUtils.isEmpty(string) && string.length() > 2){
            String[] array = string.substring(1, string.length() - 1).split(",");
            if(array != null && array.length > 0){
                List<String> list = new ArrayList<>();
                for(int i = 0; i < array.length; i++){
                    if(!TextUtils.isEmpty(array[i])){
                        list.add(array[i].trim());
                    }
                }
                return list;
            }else{
                return null;
            }
        }
        return null;
    }

    public static String formatFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f M" : "%.1f M", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f K" : "%.1f K", f);
        } else
            return String.format("%d B", size);
    }

    public static String formatTime(long timeSecond) {
        if (timeSecond < 0) return "";
        DecimalFormat decimalFormat = new DecimalFormat("00");
        long second = timeSecond % 60;
        long minute = (timeSecond / 60) % 60;
        long hour = timeSecond / 60 / 60;
        if (hour > 99) return "99:59:59";
        else return decimalFormat.format(hour) + ":" + decimalFormat.format(minute) + ":" + decimalFormat.format(second);
    }

    /**
     * 获取主题某颜色的值
     * @param index 如 R.styleable.BaseColor_com_wm_bg
     * @return
     */
    public static int getCustomColor(Context context, @StyleableRes int index){
        return getCustomColor(context, index, 0xFFFFFF);
    }

    /**
     * 获取主题某颜色的值
     * @param index 如 R.styleable.BaseColor_com_wm_bg
     * @param defValue 默认颜色值
     * @return
     */
    public static int getCustomColor(Context context, @StyleableRes int index, int defValue){
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.BaseColor, 0, 0);
        int color = a.getColor(index, defValue);
        a.recycle();
        return color;
    }

}
