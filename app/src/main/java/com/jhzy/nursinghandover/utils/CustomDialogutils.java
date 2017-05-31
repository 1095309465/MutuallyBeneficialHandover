package com.jhzy.nursinghandover.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.StringRes;
import android.util.Log;


import com.jhzy.nursinghandover.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/6/29.
 */
public class CustomDialogutils {

    private Context mContext;
    private Activity mActivity;
    // 网络数据请求数据等待刷新对话框
    private SweetAlertDialog networkRefreshDialog;

    public CustomDialogutils() {
    }


    public CustomDialogutils(Activity activity) {
        this.mContext = activity;
        this.mActivity = activity;
    }

    public CustomDialogutils(Context context) {
        this.mContext = context;
    }

    /**
     * 正在加载的对话框
     *
     * @param context
     * @param info
     * @return
     */
    public static SweetAlertDialog setLoadingDialog(Context context, String info) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(info);
        pDialog.setCancelable(false);

        return pDialog;

    }

    /**
     * 消息提示的对话框
     */
    public static SweetAlertDialog setMessageDialog(Context context, String info) {
        SweetAlertDialog sd = new SweetAlertDialog(context);
        sd.setCancelable(true);
        sd.setCanceledOnTouchOutside(true);
        sd.setTitleText(info);
        return sd;

    }

    public void setResfreshDialog(String startTxt, long timeOut) {
        //
        networkRefreshDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(startTxt);
        networkRefreshDialog.setCancelable(false);
        networkRefreshDialog.getProgressHelper().setBarColor(
                mContext.getResources().getColor(R.color.blue_btn_bg_color));
        networkRefreshDialog.show();
        new CountDownTimer(timeOut, 500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dismissDialog();

            }
        }.start();

    }

    public void setResfreshDialog(String startTxt) {
        networkRefreshDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(startTxt);
        networkRefreshDialog.setCancelable(false);
        networkRefreshDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
        networkRefreshDialog.show();
        new CountDownTimer(1500, 500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dismissDialog();

            }
        }.start();

    }

    /**
     * 开始----等待网络数据回调等待对话框
     *
     * @param startTxt 等待显示语
     */
    public void networkRefreshDiallog(String startTxt) {
        if (networkRefreshDialog == null) {
            networkRefreshDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(startTxt);
            networkRefreshDialog.setCancelable(true);
            networkRefreshDialog.getProgressHelper().setBarColor(mContext.getResources().getColor(R.color.blue_btn_bg_color));
            networkRefreshDialog.show();
          /*  if (!networkRefreshDialog.isShowing() && !mActivity.isFinishing()) {

                networkRefreshDialog.show();
                new CountDownTimer(5000, 2000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        dismissDialog();

                    }
                }.start();
            } else {
                MessageUtils.showLoge("窗口存在了");
            }*/
        }
    }

    /**
     * 开始----等待网络数据回调等待对话框
     */
    public void networkRefreshDiallog(@StringRes int resID) {
        networkRefreshDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(mContext.getResources().getText(resID).toString());
        networkRefreshDialog.setCancelable(false);
        networkRefreshDialog.getProgressHelper().setBarColor(
                mContext.getResources().getColor(R.color.blue_btn_bg_color));
        networkRefreshDialog.show();
    }

    /**
     * 关闭对话框
     */
    public void dismissDialog() {
        if (networkRefreshDialog != null && networkRefreshDialog.isShowing()) {
            networkRefreshDialog.dismissWithAnimation();
            networkRefreshDialog = null;
        }
    }

    public void dismissDialogWithoutAnim() {
        if (networkRefreshDialog != null && networkRefreshDialog.isShowing()) {
            networkRefreshDialog.dismiss();
            networkRefreshDialog = null;
        }
    }

    /**
     * 取消----等待网络数据回调等待对话框，加完成的状态
     *
     * @param endTxt        完成显示语0
     * @param errorListener 数据请求是否成功
     */
    public void cancelNetworkDialog(final String endTxt, final SweetAlertDialog.ErrorListener errorListener) {
        if (errorListener == null) return;
        Activity activity = (Activity) mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                networkRefreshDialog.setTitleText(endTxt);
                networkRefreshDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                networkRefreshDialog.setErrorListener(errorListener);
                networkRefreshDialog.setConfirmButton(false);
            }
        });

    }

    /**
     * 取消----等待网络数据回调等待对话框，加完成的状态
     *
     * @param endTxt  完成显示语
     * @param success 数据请求是否成功
     */
    public static void cancelNetworkDialog(Context context, final SweetAlertDialog sweetAlertDialog, final String endTxt, final boolean success) {
        if (sweetAlertDialog == null)
            return;

        Activity activity = (Activity) context;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int time;//完成动画是持续的时间
                sweetAlertDialog.setTitleText(endTxt);
                if (success) {
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    time = 1200;
                } else {
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    time = 1200;
                }
                sweetAlertDialog.setConfirmButton(false);
                sweetAlertDialog.showCancelButton(false);
                //time时间后执行onFinish()方法
                new CountDownTimer(time, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (sweetAlertDialog != null) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }
                }.start();

            }
        });

    }

    public void cancelNetworkDialog(final String endTxt, final boolean success) {
        if (networkRefreshDialog == null)
            return;

        Activity activity = (Activity) mContext;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int time;//完成动画是持续的时间
                networkRefreshDialog.setTitleText(endTxt);
                Log.e("123", "cancelNetworkDialog: 1");
                if (success) {
                    networkRefreshDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    time = 1200;
                } else {
                    networkRefreshDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    time = 1200;
                }
                networkRefreshDialog.setConfirmButton(false);
                //time时间后执行onFinish()方法
                new CountDownTimer(time, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        if (networkRefreshDialog != null) {
                            networkRefreshDialog.dismissWithAnimation();
                            networkRefreshDialog = null;
                        }
                    }
                }.start();

            }
        });

    }

    /**
     * 删除是的警告对话框warning_cancel_test
     */
    public static SweetAlertDialog setDeleteWarningDialog(Context context, final SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        SweetAlertDialog sd = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确定删除吗?")

                .setCancelText("取消")
                .setConfirmText("删除")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("已取消")

                                .setConfirmText("确认")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("已删除")

                                .setConfirmText("确认")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(onSweetClickListener)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                });
        return sd;
    }

    public boolean isShowing() {
        return networkRefreshDialog != null && networkRefreshDialog.isShowing();
    }

    public interface OnDeleteListener {
        void succeed();

        void fail();
    }

    public OnDeleteListener onDeleteListener;


    public static void showDeleteDialog(final Context context, SweetAlertDialog dialogDele, final OnDeleteListener onDeleteListener) {
        if (null == onDeleteListener) return;
        if (dialogDele != null && dialogDele.isShowing()) dialogDele.dismiss();
        dialogDele = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialogDele.setTitleText("确定删除吗?")
                .setCancelText("取消")
                .setConfirmText("删除")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        onDeleteListener.fail();
                        sDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        onDeleteListener.succeed();
                        CustomDialogutils.cancelNetworkDialog(context, sDialog, "已删除", true);
                    }
                }).show();

    }
}
