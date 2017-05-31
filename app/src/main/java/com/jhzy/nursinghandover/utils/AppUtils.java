package com.jhzy.nursinghandover.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/3/22.
 */

public class AppUtils {
    /**
     * 判断NFc是否有 16位
     *
     * @param NFCStr
     * @return
     */
    public static String checkTo16Length(String NFCStr) {
        int length = NFCStr.length();
        if (length < 16) {
            StringBuilder sb = new StringBuilder();
            int lessLength = 16 - length;
            for (int i = 0; i < lessLength; i++) {
                sb.append("0");
            }
            return sb.toString() + NFCStr;
        }
        return NFCStr;
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
