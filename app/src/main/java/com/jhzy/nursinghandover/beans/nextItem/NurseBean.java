package com.jhzy.nursinghandover.beans.nextItem;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/3/1.
 */

public class NurseBean {
    /**
     * CareItemID : 0
     * Subproject : [{"Number":"1","SubprojectIco":"http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png","SubprojectName":"按摩"}]
     * CareItemTitle : 护理项目标题1
     * CareItemUrl : http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png
     * StartTime : 02:00:00
     * EndTime : 07:00:00
     * AllCompleted
     * Elders : [{"TaskId":2,"ElderID":1,"ElderName":"张三","PhotoUrl":"http://v1.qzone.cc/avatar/201508/05/11/05/55c17d7dce37c723.jpg%21200x200.jpg","BedTitle":"01房001床"}]
     */

    private int CareItemID;
    private String Subproject;
    private String CareItemTitle;
    private String CareItemUrl;
    private String StartTime;
    private String EndTime;
    private List<EldersBean> Elders;
    private boolean isChecked = false;
    private boolean AllCompleted;

    public int getCareItemID() { return CareItemID;}


    public void setCareItemID(int CareItemID) { this.CareItemID = CareItemID;}


    public String getSubproject() { return Subproject;}


    public void setSubproject(String Subproject) { this.Subproject = Subproject;}


    public String getCareItemTitle() { return CareItemTitle;}


    public void setCareItemTitle(String CareItemTitle) {
        this.CareItemTitle = CareItemTitle;
    }


    public String getCareItemUrl() { return CareItemUrl;}


    public void setCareItemUrl(String CareItemUrl) { this.CareItemUrl = CareItemUrl;}


    public String getStartTime() { return StartTime;}


    public void setStartTime(String StartTime) { this.StartTime = StartTime;}


    public String getEndTime() { return EndTime;}


    public void setEndTime(String EndTime) { this.EndTime = EndTime;}


    public List<EldersBean> getElders() { return Elders;}


    public void setElders(List<EldersBean> Elders) { this.Elders = Elders;}


    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public boolean isAllCompleted() {
        return AllCompleted;
    }


    public void setAllCompleted(boolean allCompleted) {
        AllCompleted = allCompleted;
    }
}