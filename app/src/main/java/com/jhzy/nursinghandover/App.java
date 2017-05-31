package com.jhzy.nursinghandover;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jhzy.nursinghandover.utils.LogUtils;
import com.jhzy.nursinghandover.utils.SPUtils;
import com.jhzy.nursinghandover.utils.ScreenListener;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by nakisaRen
 * on 17/2/20.
 */

public class App extends Application {

    private ScreenListener screenListener;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init(this);
        Fresco.initialize(this);
        SPUtils.init(getSharedPreferences("sxmd", MODE_PRIVATE));
        CrashReport.initCrashReport(getApplicationContext(), "e45666c141", false);
        initScreenListener();
    }

    public void initScreenListener() {
        screenListener = new ScreenListener(getApplicationContext());
        screenListener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.e("123", "屏幕打开");

            }

            @Override
            public void onScreenOff() {
                Log.e("123", "屏幕关闭");

            }

            @Override
            public void onUserPresent() {
                Log.e("123", "屏幕解锁,正在自动启动程序");
                ScreenListener.openPackage(getApplicationContext(), "com.jhzy.nursinghandover");
                Log.e("123", "跳转");


            }
        });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号获取失败";
        }
    }

}
