package com.jhzy.nursinghandover.beans;

/**
 * Created by nakisaRen
 * on 17/3/23.
 */

public class WorkerDataBean {
    private int StaffID;
    private String StaffName;
    private String PhotoUrl;
    private String workerNo;


    public String getWorkerNo() {
        return workerNo;
    }


    public void setWorkerNo(String workerNo) {
        this.workerNo = workerNo;
    }


    public int getStaffID() {
        return StaffID;
    }


    public void setStaffID(int StaffID) {
        this.StaffID = StaffID;
    }


    public String getStaffName() {
        return StaffName;
    }


    public void setStaffName(String StaffName) {
        this.StaffName = StaffName;
    }


    public String getPhotoUrl() {
        return PhotoUrl;
    }


    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }


    public WorkerDataBean() {
    }


    public WorkerDataBean(int staffID, String staffName, String photoUrl) {
        StaffID = staffID;
        StaffName = staffName;
        PhotoUrl = photoUrl;
    }
}
