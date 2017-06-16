package com.lxw.videoworld.framework.util;

import android.content.Context;
import android.text.TextUtils;

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
}
