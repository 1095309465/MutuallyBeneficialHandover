package com.jhzy.nursinghandover.beans.nextItem;

/**
 * Created by nakisaRen
 * on 17/3/1.
 */

public class StaffBean {
    /**
     * StaffID : 95
     * StaffCode : 1001
     * Position : 护工
     * StaffName : 张三
     * PhotoUrl : http://qszy.oss-cn-shanghai.aliyuncs.com/default/avatar.png
     * DayPoint : 0
     * MonthPoint : 0
     */

    private int StaffID;
    private String StaffCode;
    private String Position;
    private String StaffName;
    private String PhotoUrl;
    private int DayPoint;
    private int MonthPoint;


    public int getStaffID() { return StaffID;}


    public void setStaffID(int StaffID) { this.StaffID = StaffID;}


    public String getStaffCode() { return StaffCode;}


    public void setStaffCode(String StaffCode) { this.StaffCode = StaffCode;}


    public String getPosition() { return Position;}


    public void setPosition(String Position) { this.Position = Position;}


    public String getStaffName() { return StaffName;}


    public void setStaffName(String StaffName) { this.StaffName = StaffName;}


    public String getPhotoUrl() { return PhotoUrl;}


    public void setPhotoUrl(String PhotoUrl) { this.PhotoUrl = PhotoUrl;}


    public int getDayPoint() { return DayPoint;}


    public void setDayPoint(int DayPoint) { this.DayPoint = DayPoint;}


    public int getMonthPoint() { return MonthPoint;}


    public void setMonthPoint(int MonthPoint) { this.MonthPoint = MonthPoint;}
}
