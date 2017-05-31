package com.jhzy.nursinghandover.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xishua.integration.XsReaderBroadcast;

/**
 * Created by sxmd on 2017/2/27.
 */

public class ServiceStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (XsReaderBroadcast.ACTION_SERVICE_STARTED.equals(action)) {
            Log.e("123", "Xishua NFC 服务开启");
        } else if (XsReaderBroadcast.ACTION_SERVICE_STOPPED.equals(action)) {
            Log.e("123", "Xishua NFC 服务关闭");
        } else
            throw new IllegalArgumentException("Unexpected action " + action);
    }
}
