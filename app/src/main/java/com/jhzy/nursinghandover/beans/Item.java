package com.jhzy.nursinghandover.beans;

/**
 * Created by nakisaRen
 * on 17/2/23.
 */

public class Item {
    private String startTime;
    private String endTime;
    private String itemName;
    private boolean isChecked = false;


    public String getStartTime() {
        return startTime;
    }


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public String getEndTime() {
        return endTime;
    }


    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getItemName() {
        return itemName;
    }


    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
