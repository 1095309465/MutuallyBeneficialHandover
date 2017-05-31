package com.jhzy.nursinghandover.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by ZhouChengHua on 2016-11-05.
 */

public class GetSign {
    static String mySign = "";
    public static final String APPKEY = "zjkfjadskljfklsdajklfjkl";

    private static final String PAYAPPKEY = "&key=Shanghaijingfannetwork9600296002";

    public static String GetSign(SortedMap<String, String> parameters, String token) {
        String characterEncoding = "UTF-8";
        mySign = createSign(characterEncoding, parameters, token);
        Log.e("rxff","sign====" + mySign);
        return mySign;
    }


    private static String createSign(String characterEncoding, SortedMap<String, String> parameters, String token) {
        StringBuffer sb = new StringBuffer();
        if(null == parameters){
            sb.append("").append(token == null ? "" : token).append(APPKEY);
            return MD5Utils.MD5Encode(sb.toString(), characterEncoding);
        }
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            sb.append(k).append("=").append(v).append("&");
        }
        if(sb.length() == 0){
            sb.append(token == null ? "" : token).append(APPKEY);
        }else{
            sb.deleteCharAt(sb.length() - 1).append(token == null ? "" : token).append(APPKEY);
        }
        String sign = MD5Utils.MD5Encode(sb.toString(), characterEncoding);
        return sign;
    }

    public static String GetSignUper(SortedMap<String, String> parameters, String token) {
        String characterEncoding = "UTF-8";
        mySign = createSignUper(characterEncoding, parameters, token);
        Log.e("rxff","sign====" + mySign);
        return mySign;
    }

    private static String createSignUper(String characterEncoding, SortedMap<String, String> parameters, String token) {
        StringBuffer sb = new StringBuffer();
        if(null == parameters){
            sb.append("").append(token == null ? "" : token).append(APPKEY);
            return MD5Utils.MD5Encode(sb.toString(), characterEncoding);
        }
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            sb.append(k).append("=").append(v).append("&");
        }
        if(sb.length() == 0){
            sb.append(token == null ? "" : token).append(APPKEY);
        }else{
            sb.deleteCharAt(sb.length() - 1).append(token == null ? "" : token).append(PAYAPPKEY);
        }
        Log.e("rxf","微信支付的加密串==" + sb.toString());
        String sign = MD5Utils.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }
}
