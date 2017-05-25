package com.lxw.videoworld.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lxw9047 on 2016/11/2.
 */

public class TimeUtil {

    //获取当前日期 如 20161102
    public static String getDateNumber() {
        String mYear;
        String mMonth;
        String mDay;
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(calendar.get(Calendar.YEAR));// 获取当前年份
        mMonth = String.format("%02d", calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + mMonth + mDay;
    }

    /**
     * 获取某一天是星期几
     *
     * @param dateNumber 日期 如 20161102
     * @return
     */
    public static String getWeek(String dateNumber) {
        int mYear = Integer.valueOf(dateNumber.substring(0, 4));
        int mMonth = Integer.valueOf(dateNumber.substring(4, 6));
        int mDay = Integer.valueOf(dateNumber.substring(6, 8));
        String week = "";//星期几
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);//指定年份
        calendar.set(Calendar.MONTH, mMonth - 1);//指定月份 Java月份从0开始算

        //获取指定年份月份中指定某天是星期几
        calendar.set(Calendar.DAY_OF_MONTH, mDay);  //指定日
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 获取某一天的前N天的日期
     *
     * @param dateNumber 日期 如 20161102
     * @param count      前N天
     * @return
     */
    public static String getBeforeDate(String dateNumber, int count) {
        int mYear = Integer.valueOf(dateNumber.substring(0, 4));
        int mMonth = Integer.valueOf(dateNumber.substring(4, 6));
        int mDay = Integer.valueOf(dateNumber.substring(6, 8));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);//指定年份
        calendar.set(Calendar.MONTH, mMonth - 1);//指定月份 Java月份从0开始算
        calendar.set(Calendar.DAY_OF_MONTH, mDay - count);
        String Year = String.valueOf(calendar.get(Calendar.YEAR));// 获取当前年份
        String Month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);// 获取当前月份
        String Day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return Year + Month + Day;
    }

    /**
     * 时间戳转换成日期、时间
     *
     * @param timestamp 时间戳
     * @return 格式化日期、时间
     */
    public static String getFormatDate(long timestamp) {
        String date = new SimpleDateFormat("MM-dd HH:mm").format(new Date(timestamp * 1000));
        return date;
    }
}
