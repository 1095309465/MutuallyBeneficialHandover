package com.jhzy.nursinghandover.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jhzy.nursinghandover.ui.LoginActivity;


/**
 * Created by Administrator on 2016/5/12.
 */
public class BootStartReceiver extends BroadcastReceiver {//开机广播接收器
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Intent ootStartIntent = new Intent(context, LoginActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }

    }
}
