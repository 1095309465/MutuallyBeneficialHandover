package com.jhzy.nursinghandover.beans.nextItem;

/**
 * Created by nakisaRen
 * on 17/3/1.
 */

public class EldersBean {
    /**
     * TaskId : 2
     * ElderID : 1
     * ElderName : 张三
     * PhotoUrl : http://v1.qzone.cc/avatar/201508/05/11/05/55c17d7dce37c723.jpg%21200x200.jpg
     * BedTitle : 01房001床
     */

    private int TaskId;
    private int ElderID;
    private String ElderName;
    private String PhotoUrl;
    private String BedTitle;
    private boolean isChecked = false;
    private String subItem;
    private int IsCompleted;//0:未完成  1：已完成
    private int Cid;
    private String CName;


    public int getTaskId() { return TaskId;}


    public void setTaskId(int TaskId) { this.TaskId = TaskId;}


    public int getElderID() { return ElderID;}


    public void setElderID(int ElderID) { this.ElderID = ElderID;}


    public String getElderName() { return ElderName;}


    public void setElderName(String ElderName) { this.ElderName = ElderName;}


    public String getPhotoUrl() { return PhotoUrl;}


    public void setPhotoUrl(String PhotoUrl) { this.PhotoUrl = PhotoUrl;}


    public String getBedTitle() { return BedTitle;}


    public void setBedTitle(String BedTitle) { this.BedTitle = BedTitle;}

    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getSubItem() {
        return subItem;
    }


    public void setSubItem(String subItem) {
        this.subItem = subItem;
    }


    public int getIsCompleted() {
        return IsCompleted;
    }


    public void setIsCompleted(int isCompleted) {
        IsCompleted = isCompleted;
    }


    public int getCid() {
        return Cid;
    }


    public void setCid(int cid) {
        Cid = cid;
    }


    public String getCName() {
        return CName;
    }


    public void setCName(String CName) {
        this.CName = CName;
    }
}

