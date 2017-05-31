package com.jhzy.nursinghandover.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.jhzy.nursinghandover.utils.ScreenBritnessUtil;


/**
 * bigyu  2017/2/15  14:56
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(getContentView());
        mContext = this;
        init();
    }



    public abstract int getContentView();

    public void init() {
    }
}
