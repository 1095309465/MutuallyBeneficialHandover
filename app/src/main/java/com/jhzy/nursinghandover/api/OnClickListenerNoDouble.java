package com.jhzy.nursinghandover.api;


import android.view.View;

/**
 * Created by bigyu on 2017/2/16.
 * 防止二次点击
 */
public abstract class OnClickListenerNoDouble implements View.OnClickListener {
    private final long CLICK_TIME = 250;
    private long lastTime = 0;

    @Override
    public void onClick(View view) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime >= CLICK_TIME) {
            lastTime = currentTime;
            myOnClick(view);
        }
    }
    public abstract void myOnClick(View view);
}
