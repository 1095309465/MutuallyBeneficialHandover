package com.jhzy.nursinghandover.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/4.
 */
public class TimeParseUtils {

    /**
     * 把string类型的时间转换为毫秒    1900-01-01 00:00:00
     *
     * @param date
     * @return
     */
    public static long getTimeMillion(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        long millionSeconds = -1;// 毫秒
        try {
            millionSeconds = sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }

    public static long getTimeMillion2(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long millionSeconds = -1;// 毫秒
        try {
            millionSeconds = sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds;
    }

    public static String getDateTime() {
        long timeMillis = System.currentTimeMillis();
        Date d = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
    public static String getDateTime(long timeMillis) {
        Date d = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
    public static String getCurrentDate() {
        long timeMillis = System.currentTimeMillis();
        Date d = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return sdf.format(d);
    }
    public static String getStorageStr() {
        long timeMillis = System.currentTimeMillis();
        Date d = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(d);
    }

    public static String getDateDate() {
        long timeMillis = System.currentTimeMillis();
        Date d = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    public static String getTimeHM(String date) {
        long timeMillion = getTimeMillion(date);
        Date d = new Date(timeMillion);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(d);
    }

    public static String getDateCN(String date) {
        long timeMillion = getTimeMillion(date);
        Date d = new Date(timeMillion);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(d);
    }

    public static String getDateDate1(String date) {
        long timeMillion = getTimeMillion(date);
        Date d = new Date(timeMillion);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(d);
    }


}
