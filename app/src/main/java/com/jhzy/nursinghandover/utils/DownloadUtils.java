package com.jhzy.nursinghandover.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by Administrator on 2017/3/15.
 */
@Deprecated
public class DownloadUtils {

    public void download(Context context) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(""));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "update.apk");
        // 设置一些基本显示信息
        request.setTitle("app");
        request.setDescription("hahhaa");
        request.setMimeType("application/vnd.android.package-archive");


        //long downloadId = dm.enqueue(req);
        //Log.d("DownloadManager", downloadId + "");
        //dm.openDownloadedFile()
    }
}
