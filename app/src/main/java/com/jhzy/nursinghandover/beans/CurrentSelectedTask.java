package com.jhzy.nursinghandover.beans;

import android.view.View;

/**
 * Created by Administrator on 2017/3/28.
 */

public class CurrentSelectedTask {


    private String url;
    private View view;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }


    public CurrentSelectedTask(String url, View view, int width, int height) {
        this.url = url;
        this.view = view;
        this.width = width;
        this.height = height;
    }
}
