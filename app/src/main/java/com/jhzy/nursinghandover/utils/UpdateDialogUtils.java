package com.jhzy.nursinghandover.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.UpdateBean;

import java.io.File;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sxmd on 2017/3/13.
 */

public class UpdateDialogUtils implements View.OnClickListener {

    private Dialog dialog;
    private Context mContext;
    private TextView tvTitle, btnOk, btnCancle;
    private ProgressBar pb;
    private View line;


    public UpdateDialogUtils(Context mContext) {
        this.mContext = mContext;
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_updat_layout, null);
        initView(contentView);
        dialog = new Dialog(mContext, R.style.mydialog);
        dialog.setContentView(contentView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
        CONTACT.isCanDownLoad = true;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
        CONTACT.isCanDownLoad = false;
    }

    private void initView(View contentView) {
        pb = (ProgressBar) contentView.findViewById(R.id.pb);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_info);
        btnOk = (TextView) contentView.findViewById(R.id.btn_ok);
        btnCancle = (TextView) contentView.findViewById(R.id.btn_cancle);
        line = contentView.findViewById(R.id.line);
        btnOk.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://确认按钮
                netWork();
                line.setVisibility(View.GONE);
                btnOk.setVisibility(View.GONE);
                break;
            case R.id.btn_cancle://取消按钮
                dismiss();
                break;
        }


    }

    private void netWork() {

        Message msg = mHandler.obtainMessage(1);
        msg.obj = "正在检查新版本...";
        mHandler.sendMessage(msg);

        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("Platform", "android");
        treeMap.put("Version", getAppVersionName(mContext));
        //treeMap.put("Version", "0.0");
        treeMap.put("App", "2");
        treeMap.put("sign", GetSign.GetSign(treeMap, null));
        HttpUtils.getInstance().getRetrofitApi().updateApp(treeMap).enqueue(new Callback<UpdateBean>() {
            @Override
            public void onResponse(Call<UpdateBean> call, Response<UpdateBean> response) {
                UpdateBean body = response.body();
                String msg = body.getMsg() == null ? "" : body.getMsg().toString();
                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    String url = body.getData().getURL();
                    startDownLoad(url);
                } else {
                    Message msg1 = mHandler.obtainMessage(0);
                    msg1.obj = msg;
                    mHandler.sendMessageDelayed(msg1, 3000);
                }
            }

            @Override
            public void onFailure(Call<UpdateBean> call, Throwable t) {
                Message msg1 = mHandler.obtainMessage(0);
                msg1.obj = "     更新失败，\r\n请确认网络是否连接";
                mHandler.sendMessageDelayed(msg1, 3000);
            }
        });
        Log.e("123", "netWork");

    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VersionInfo", "Exception", e);
        }
        Log.e("123", "versionName=" + versionName);
        return versionName;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {//不显示进度圈
                pb.setVisibility(View.GONE);
            } else if (msg.what == 1) {//显示进度圈
                pb.setVisibility(View.VISIBLE);
            }else if (msg.what == 2){
                pb.setVisibility(View.GONE);
            }

            String info = (String) msg.obj;
            tvTitle.setText(info);
        }
    };
    int progress = 0;

    public void setProgress(int num) {
        if (progress == num) return;
        Message msg1 = mHandler.obtainMessage(0);
        msg1.obj = "更新中...(" + num + "%)";
        mHandler.sendMessage(msg1);
        progress = num;

    }


    private void startDownLoad(String url) {

        HttpUtils.getInstance().getRetrofitApi().downloadFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.apk");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        //保存到本地
                        FileUtils.writeFile2Disk(response, file, new HttpCallBack() {
                            @Override
                            public void onLoading(final long current, final long total) {
                                int now = (int) (current * 100 / total);
                                Log.e("123", "now==" + now);

                                setProgress(now);

                                if (now == 100) {
                                    Log.e("123", "开始安装");
                                    install(file.getAbsolutePath());
                                }
                            }

                            @Override
                            public void onFaile() {
                                Log.e("---------", "onFaile: " );
                                Message msg1 = mHandler.obtainMessage(0);
                                msg1.what = 2;
                                msg1.obj = "更新失败，服务器地址异常!";
                                mHandler.sendMessage(msg1);
                            }
                        });


                    }
                }.start();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Message msg1 = mHandler.obtainMessage(0);
                msg1.obj = "      更新失败\r\n请确认网络是否正常";
                mHandler.sendMessage(msg1);

            }
        });

    }

   /* private MyThread myThread;

    private class MyThread extends Thread {


        @Override
        public void run() {
            super.run();
        }
    }*/


    public void install(String fileName) {
        //安装文件apk路径
        //创建URI
        Uri uri = Uri.fromFile(new File(fileName));
        //创建Intent意图
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启动新的activity
        //设置Uri和类型
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //执行安装
        mContext.startActivity(intent);
        dismiss();

    }

}
