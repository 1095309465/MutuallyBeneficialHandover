package com.jhzy.nursinghandover.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jhzy.nursinghandover.beans.NFCBean;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.NFCUtils;
import com.xishua.integration.XsReaderBroadcast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by sxmd on 2017/2/27.
 */

public class TagStatusReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (XsReaderBroadcast.ACTION_NFC_TAG_DISCOVERED.equals(action)) {
            Log.e("123", "捕捉到读卡动作");

            if (intent.hasExtra("cardUID")) {
                // JiesuoUtil.wakeAndUnlock(true, getApplicationContext());
                // JiesuoUtil.wakeUnlock(getApplicationContext());
                // 通过包名获取要跳转的app，创建intent对象

                Log.e("123", "开屏和解锁");
                String uid = intent.getStringExtra("cardUID");
                Log.e("123", "uid=" + uid);//9EAFFD4B
                try {
                    byte[] bytes = uid.getBytes();


                    Long b = getDec2(bytes);
                    //判断nfc有没有 16位 ，如果没有则前面补0
                    String NFCStr = b.toString();
                    //取整形数据
                    NFCStr = NFCUtils.toStr2(NFCStr);
                    if (CONTACT.type == CONTACT.NFCTAG.TAG_MAINACTIVITY) {
                        EventBus.getDefault().post(new NFCBean(NFCUtils.toStr2(NFCStr), "NFC"));
                        Log.e("123", "NFC");
                    } else if (CONTACT.type == CONTACT.NFCTAG.TAG_MORENURSE) {
                        EventBus.getDefault().post(new NFCBean(NFCUtils.toStr2(NFCStr), "MORE_NFC"));
                        Log.e("123", "MORE_NFC");
                    }
                    Log.e("123", "ת1成功" + b);
                } catch (Exception e) {
                    Log.e("123", "ת失败" + uid);
                }
//                try {
//                    Long b = Long.parseLong(uid, 16);
//                    if (CONTACT.type == CONTACT.NFCTAG.TAG_MAINACTIVITY) {
//                        EventBus.getDefault().post(new NFCBean("" + b, "NFC"));
//                        Log.e("123", "NFC");
//                    } else if (CONTACT.type == CONTACT.NFCTAG.TAG_MORENURSE) {
//                        EventBus.getDefault().post(new NFCBean("" + b, "MORE_NFC"));
//                        Log.e("123", "MORE_NFC");
//                    }
//
//                    Log.e("123", "ת1成功" + b);
//                } catch (Exception e) {
//                    Log.e("123", "ת失败" + uid);
//                }

            }

        } else if (XsReaderBroadcast.ACTION_NFC_TAG_LEFT_FIELD.equals(action)) {
        } else
            throw new IllegalArgumentException("Unexpected action " + action);
    }


    private long getDec2(byte[] bytes) {
        long result = 0;
        long factor = 1;
        byte[] bytes4 = new byte[4];
        for (int ii = 0; ii < bytes.length; ii += 2) {
            if ((bytes[ii] >= 0x30) && (bytes[ii] <= 0x39)) //高4位是0-9数字
                bytes4[ii / 2] = (byte) ((bytes[ii] - 0x30) << 4);
            else if ((bytes[ii] >= 0x41) && (bytes[ii] <= 0x46))//高4位是A-F字母
                bytes4[ii / 2] = (byte) ((bytes[ii] - 0x37) << 4);
            else if ((bytes[ii] >= 0x61) && (bytes[ii] <= 0x66))//高4位是a-f字母
                bytes4[ii / 2] = (byte) ((bytes[ii] - 0x57) << 4);
            if ((bytes[ii + 1] >= 0x30) && (bytes[ii + 1] <= 0x39))  //低4位是0-9数字
                bytes4[ii / 2] += (byte) ((bytes[ii + 1] - 0x30));
            else if ((bytes[ii + 1] >= 0x41) && (bytes[ii + 1] <= 0x46))//低4位是A-F字母
                bytes4[ii / 2] += (byte) ((bytes[ii + 1] - 0x37));
            else if ((bytes[ii + 1] >= 0x61) && (bytes[ii + 1] <= 0x66))//低4位是0-9数字
                bytes4[ii / 2] += (byte) ((bytes[ii + 1] - 0x57));
        }

        for (int i = 0; i < bytes4.length; ++i) {
            long value = bytes4[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
}
