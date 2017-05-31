package com.jhzy.nursinghandover.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.PowerManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by sxmd on 2016/10/20.
 */
public class ScreenListener {


    private Context mContext;

    private ScreenBroadcastReceiver mScreenReceiver;

    private ScreenStateListener mScreenStateListener;


    public ScreenListener(Context context) {

        mContext = context;

        mScreenReceiver = new ScreenBroadcastReceiver();

    }


    /**
     * screen状态广播接收者
     */

    private class ScreenBroadcastReceiver extends BroadcastReceiver {

        private String action = null;


        @Override

        public void onReceive(Context context, Intent intent) {

            action = intent.getAction();

            if (Intent.ACTION_SCREEN_ON.equals(action)) { // 开屏

                mScreenStateListener.onScreenOn();

            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { // 锁屏

                mScreenStateListener.onScreenOff();

            } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁

                mScreenStateListener.onUserPresent();

            }

        }

    }

    /**
     * 开始监听screen状态
     *
     * @param listener
     */

    public void begin(ScreenStateListener listener) {

        mScreenStateListener = listener;

        registerListener();

        getScreenState();

    }


    /**
     * 获取screen状态
     */

    private void getScreenState() {

        PowerManager manager = (PowerManager) mContext

                .getSystemService(Context.POWER_SERVICE);

        if (manager.isScreenOn()) {

            if (mScreenStateListener != null) {

                mScreenStateListener.onScreenOn();

            }

        } else {

            if (mScreenStateListener != null) {

                mScreenStateListener.onScreenOff();

            }

        }

    }


    /**
     * 停止screen状态监听
     */

    public void unregisterListener() {

        mContext.unregisterReceiver(mScreenReceiver);

    }


    /**
     * 启动screen状态广播接收器
     */

    private void registerListener() {

        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_SCREEN_ON);

        filter.addAction(Intent.ACTION_SCREEN_OFF);

        filter.addAction(Intent.ACTION_USER_PRESENT);

        mContext.registerReceiver(mScreenReceiver, filter);

    }


    public interface ScreenStateListener {// 返回给调用者屏幕状态信息

        public void onScreenOn();


        public void onScreenOff();


        public void onUserPresent();

    }


    public static boolean openPackage(Context context, String packageName) {
        Context pkgContext = getPackageContext(context, packageName);
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (pkgContext != null && intent != null) {
            pkgContext.startActivity(intent);
            return true;
        }
        return false;
    }

    public static boolean openPackageInCurrentPackage(Context context, String packageName) {
        Intent intent = getAppOpenIntentByPackageName(context, packageName);
        if (context != null && intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    public static Context getPackageContext(Context context, String packageName) {
        Context pkgContext = null;
        if (context.getPackageName().equals(packageName)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境
            try {
                pkgContext = context.createPackageContext(packageName,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgContext;
    }

    public static Intent getAppOpenIntentByPackageName(Context context, String packageName) {
        // MainActivity完整名
        String mainAct = null;
        // 根据包名寻找MainActivity
        PackageManager pkgMag = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> list = pkgMag.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(packageName)) {
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            return null;
        }
        intent.setComponent(new ComponentName(packageName, mainAct));
        return intent;
    }

}