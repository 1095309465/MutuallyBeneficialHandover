package com.jhzy.nursinghandover.ui;

/**
 * Created by Administrator on 2017/3/27.
 */

public class Test {

    private  String title;
    private  boolean isSelector;

    public Test(String title, boolean isSelector) {
        this.title = title;
        this.isSelector = isSelector;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }
}
