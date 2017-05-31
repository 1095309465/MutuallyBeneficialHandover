package com.jhzy.nursinghandover.beans.nextItem;

import android.graphics.drawable.Drawable;

/**
 * Created by nakisaRen
 * on 17/3/1.
 */

public class SubItem {

    /**
     * Number : 1
     * SubprojectIco : http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png
     * SubprojectName : 按摩
     */

    private String Number;
    private String SubprojectIco;
    private String SubprojectIcoClk;
    private String SubprojectName;
    private Integer SubprojectIcoInt;
    private boolean selected;
    private boolean isTask;


    public SubItem(String number, Integer SubprojectIcoInt, String subprojectName,boolean isTask) {
        Number = number;
        this.SubprojectIcoInt = SubprojectIcoInt;
        SubprojectName = subprojectName;
    }

    public SubItem(String number, Drawable SubprojectIcoInt, String subprojectName,boolean isTask) {
        Number = number;
        //this.SubprojectIcoInt = SubprojectIcoInt;
        SubprojectName = subprojectName;
        //this.isTask = isTask;
    }

    public String getNumber() {
        return Number;
    }


    public void setNumber(String Number) {
        this.Number = Number;
    }


    public String getSubprojectIco() {
        return SubprojectIco;
    }


    public void setSubprojectIco(String SubprojectIco) {
        this.SubprojectIco = SubprojectIco;
    }


    public String getSubprojectName() {
        return SubprojectName;
    }


    public void setSubprojectName(String SubprojectName) {
        this.SubprojectName = SubprojectName;
    }


    public Integer getSubprojectIcoInt() {
        return SubprojectIcoInt;
    }


    public void setSubprojectIcoInt(Integer subprojectIcoInt) {
        SubprojectIcoInt = subprojectIcoInt;
    }


    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public boolean isTask() {
        return isTask;
    }


    public void setTask(boolean task) {
        isTask = task;
    }


    public String getSubprojectIcoClk() {
        return SubprojectIcoClk;
    }


    public void setSubprojectIcoClk(String subprojectIcoClk) {
        SubprojectIcoClk = subprojectIcoClk;
    }
}
