package com.jhzy.nursinghandover.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by nakisaRen
 * on 17/2/23.
 */

public class ImageLoaderUtils {

    public static void load(SimpleDraweeView imageview, String url) {
        if (!TextUtils.isEmpty(url)) {
            url = url + "?x-oss-process=image/resize,m_lfit,h_300,w_300";
            Uri uri = Uri.parse(url);
            imageview.setImageURI(uri);
        }
    }
}
