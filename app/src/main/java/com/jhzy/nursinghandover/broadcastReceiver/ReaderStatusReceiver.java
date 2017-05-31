package com.jhzy.nursinghandover.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xishua.integration.XsReaderBroadcast;

/**
 * Created by sxmd on 2017/2/27.
 */

public class ReaderStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (XsReaderBroadcast.ACTION_NFC_READER_OPEN.equals(action)) {
            Log.e("123", "读卡器开启成功");
        } else if (XsReaderBroadcast.ACTION_NFC_READER_CLOSED.equals(action)) {
            Log.e("123", "读卡器关闭");
        } else if (XsReaderBroadcast.ACTION_NFC_READER_GET_POWER.equals(action)) {
        } else
            throw new IllegalArgumentException("Unexpected action " + action);

    }
}
