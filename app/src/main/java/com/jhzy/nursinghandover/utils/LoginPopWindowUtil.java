package com.jhzy.nursinghandover.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.beans.LoginBean;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sxmd on 2017/3/6.
 */

public class LoginPopWindowUtil {

    public static void showLoginPopWindow(final Context mContext, View view, final Window window, final LoginPopWindowListener loginPopWindowListener) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.logincheck_layout, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final TextView btn_login = (TextView) contentView.findViewById(R.id.btn_login);
        final EditText ed_id = (EditText) contentView.findViewById(R.id.ed_id);
        final EditText ed_password = (EditText) contentView.findViewById(R.id.ed_password);
        ImageView iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zh = ed_id.getText().toString();
                String password = ed_password.getText().toString();
                login(ed_id, mContext, zh, password, popupWindow, loginPopWindowListener);

            }
        });

        popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha = 1f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
                window.setAttributes(wl);
            }
        });

    }

    private static void login(final EditText ed_id, final Context mContext, String account, String password, final PopupWindow popupWindow, final LoginPopWindowListener loginPopWindowListener) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "请填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Account", account);
        map.put("PassWord", password);
        map.put("sign", GetSign.GetSign(map, null));
        HttpUtils.getInstance().getRetrofitApi().login(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {


                if (response.body() != null && CONTACT.DataSuccess.equals(response.body().getCode())) {
                    popupWindow.dismiss();
                    if (loginPopWindowListener != null) {
                        String id = ed_id.getText().toString();
                        loginPopWindowListener.loginSucceed(id);
                        SPUtils.updateOrSave(CONTACT.TOKEN,response.body().getData().getAuth_token());
                    }
                }else if (response.body() != null){
                    if (loginPopWindowListener != null) {
                        Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        loginPopWindowListener.loginFailed();
                    }
                }else {
                    if (loginPopWindowListener != null) {
                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                        loginPopWindowListener.loginFailed();
                    }
                }
            }


            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                if (!AppUtils.isNetworkConnected(mContext)){
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                }
                if (loginPopWindowListener != null) {
                    loginPopWindowListener.loginFailed();
                }
            }
        });
    }

    public interface LoginPopWindowListener {

        void loginSucceed(String id);

        void loginFailed();

    }
}
