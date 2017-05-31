package com.jhzy.nursinghandover.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @Description: 描述
 * @AUTHOR 刘楠  Create By 2016/10/27 0027 15:56
 */
public class FileUtils {


    public static File createFile(Context context) {


        File file = null;
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {

            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.apk");
        } else {
            file = new File(context.getCacheDir().getAbsolutePath() + "/test.apk");
        }
        Log.e("123", "file=" + file.getAbsolutePath());

        return file;

    }


    public static void writeFile2Disk(Response<ResponseBody> response, File file, HttpCallBack httpCallBack) {


        long currentLength = 0;
        OutputStream os = null;
        ResponseBody body = response.body();
        if (body == null) {
            httpCallBack.onFaile();
            return;
        }

        InputStream is = body.byteStream();
        long totalLength = body.contentLength();

        try {
            os = new FileOutputStream(file);


            int len;

            byte[] buff = new byte[1024];
            int preProgress = 0;

            while ((len = is.read(buff)) != -1 && CONTACT.isCanDownLoad) {

                os.write(buff, 0, len);
                currentLength += len;
                Log.e("123", "当前进度：" + currentLength);

                httpCallBack.onLoading(currentLength, totalLength);
            }
            // httpCallBack.onLoading(currentLength,totalLength,true);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


}
