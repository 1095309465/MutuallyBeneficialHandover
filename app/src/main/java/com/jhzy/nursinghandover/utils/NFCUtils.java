package com.jhzy.nursinghandover.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by sxmd on 2016/10/13.
 */
public class NFCUtils {

    @Deprecated
    public static String toStr(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        if (str.length() != 10)
            return "";
        Long num = Long.parseLong(str);
        if (String.valueOf(num).length() != 10) {
            return String.valueOf(num);
        }
        str = Long.toHexString(num);

        Log.e("----------------", "toStr: " + str); //4119367731  --->f5889033  ---->8949811
        //        1111 0101 1000 1000 1001 0000 0011 0011
        //        1111 0101 1000 1000 1001 0000 0011 0011
        //        F      5   8    8    9   0     3   3
        //                  100010001001000000110011
        //        0000 1010 0110 0000 1100 1011 1000 1011
        if (str.length() == 8) {
            str = str.substring(2);
            str = Integer.parseInt(str, 16) + "";
            Log.e("------8", "toStr: " + str);
        }
        Log.e("------", "toStr: " + str);
        return str;
    }

    /**
     * 长整形转为整形
     *
     * @param str
     * @return
     */
    public static String toStr2(String str) {
        //判断字符串非空
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        //判断是否数字字符
        if (!TextUtils.isDigitsOnly(str)) {
            return str;
        }
        //转为长整形
        Long num = Long.parseLong(str);
        //转为二进制字符串
        String s = Long.toBinaryString(num);
        //如果二进制字符串小于等于24位，则返回本身
        if (s.length() <= 24) {
            return String.valueOf(num);
        }
        //截取底24位的二进制字符串
        String substring = s.substring(s.length() - 24, s.length());
        //转整形
        str = Integer.parseInt(substring, 2) + "";
        return str;
    }
}
