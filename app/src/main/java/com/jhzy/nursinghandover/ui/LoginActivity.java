package com.jhzy.nursinghandover.ui;

import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.nursinghandover.R;
import com.jhzy.nursinghandover.api.OnClickListenerNoDouble;
import com.jhzy.nursinghandover.beans.LoginBean;
import com.jhzy.nursinghandover.utils.AppUtils;
import com.jhzy.nursinghandover.utils.CONTACT;
import com.jhzy.nursinghandover.utils.CustomDialogutils;
import com.jhzy.nursinghandover.utils.GetSign;
import com.jhzy.nursinghandover.utils.HttpUtils;
import com.jhzy.nursinghandover.utils.SPUtils;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登陆
 */
public class LoginActivity extends BaseActivity {

    private EditText accountView;
    private EditText passwordView;
    private TextView loginView;
    private CustomDialogutils customDialogutils;
    private ImageView icon;


    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }


    @Override
    public void init() {
        initUtils();
        initView();
        initListener();
        String account = SPUtils.find(CONTACT.ACCOUNT);
        String password = SPUtils.find(CONTACT.PASSWORD);
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            accountView.setText(account);
            passwordView.setText(password);
            login();
        }
    }


    private void initUtils() {
        customDialogutils = new CustomDialogutils(this);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        SPUtils.updateOrSave(CONTACT.IMEI, tm.getDeviceId());
    }


    private void initListener() {
        loginView.setOnClickListener(listenerNoDouble);
        icon.setOnClickListener(listenerNoDouble);
    }


    private void initView() {
        icon = (ImageView) findViewById(R.id.icon);
        accountView = ((EditText) findViewById(R.id.login_account));
        passwordView = ((EditText) findViewById(R.id.login_password));
        loginView = ((TextView) findViewById(R.id.login_login));
        //accountView.setText("sxmd");
        //passwordView.setText("123456");
        //ImageMaskView maskView = (ImageMaskView) findViewById(R.id.img);
    }


    OnClickListenerNoDouble listenerNoDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            switch (view.getId()) {
                case R.id.login_login:
                    login();
                    break;
                case R.id.icon:
                    break;
            }
        }
    };


    private void login() {
        final String account = accountView.getText().toString().trim();
        final String password = passwordView.getText().toString().trim();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            Toast.makeText(mContext, "账号或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        customDialogutils.setResfreshDialog("登录中...", 20*1000);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Account", account);
        map.put("PassWord", password);
        map.put("Imei" ,SPUtils.find(CONTACT.IMEI) );
        map.put("sign", GetSign.GetSign(map, null));
        HttpUtils.getInstance().getRetrofitApi().login(map).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                LoginBean body = response.body();
                if (body != null &&
                        CONTACT.DataSuccess.equals(body.getCode())) {
                    SPUtils.updateOrSave(CONTACT.TOKEN, body.getData().getAuth_token());
                    SPUtils.updateOrSave(CONTACT.ACCOUNT, account);
                    SPUtils.updateOrSave(CONTACT.PASSWORD, password);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            customDialogutils.cancelNetworkDialog("登录成功", true);
                            finish();
                        }
                    }, 1000);
                } else if (body!= null){
                    customDialogutils.cancelNetworkDialog(body.getMsg(), false);
                }else {
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }
            }


            @Override
            public void onFailure(Call<LoginBean> call, Throwable t) {
                t.printStackTrace();
                if (AppUtils.isNetworkConnected(mContext)) {
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                } else {
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }
            }
        });
    }

}
