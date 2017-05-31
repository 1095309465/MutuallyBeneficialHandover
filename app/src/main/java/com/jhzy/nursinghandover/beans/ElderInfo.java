package com.jhzy.nursinghandover.beans;

/**
 * Created by nakisaRen
 * on 17/2/23.
 */

public class ElderInfo {
    private String name;
    private String bed;
    private String head;
    private boolean isChecked;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getBed() {
        return bed;
    }


    public void setBed(String bed) {
        this.bed = bed;
    }


    public String getHead() {
        return head;
    }


    public void setHead(String head) {
        this.head = head;
    }


    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
