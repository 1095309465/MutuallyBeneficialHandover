package com.jhzy.nursinghandover.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by sxmd on 2016/9/19.
 */
public class ScreenBritnessUtil {


    //屏幕亮度:调暗
    private static void setScreenBack(Window window) {
        if (window == null) return;
        //设置当前activity的屏幕亮度
        WindowManager.LayoutParams lp = window.getAttributes();
        //0到1,调整亮度暗到全亮
        lp.screenBrightness = 0f;
        window.setAttributes(lp);
        backgroundAlpha(window, 0f);
        Log.e("321", "使屏幕变暗");

    }

    public static void removeTimerTask() {
        if (handler == null) return;
        handler.removeMessages(0);
        Log.e("321", "清除亮度延时变暗功能");
    }


    /**
     * 设置背景透明度半透明
     */
    public static void backgroundAlpha(Window window, float alpha) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha; // 0.0-1.0
        window.setAttributes(lp);

    }


    //屏幕亮度:调亮
    public static void setScreenBritness(Window window) {
        Log.e("321", "使屏幕变亮");
        if (window == null) return;
        //设置当前activity的屏幕亮度
        WindowManager.LayoutParams lp = window.getAttributes();
        //0到1,调整亮度暗到全亮
        lp.screenBrightness = 0.6f;
        window.setAttributes(lp);
        backgroundAlpha(window, 1f);
        if (handler.hasMessages(0)) {
            handler.removeMessages(0);
            Log.e("321", "有旧的Handler数据,执行清除");
        }
        Message msg = handler.obtainMessage(0, window);
        handler.sendMessageDelayed(msg, 1 * 60 * 1000);
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Window window = (Window) msg.obj;
                setScreenBack(window);
            }
        }
    };


}
