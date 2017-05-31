package com.jhzy.nursinghandover.model.loginnfc;

/**
 * Created by Administrator on 2017/2/28.
 */

public class StaffLogin {
    private String Position;
    private int StaffID;
    private String StaffCode;
    private int StaffLevel;

    @Override
    public String toString() {
        return "StaffLogin{" +
                "Position='" + Position + '\'' +
                ", StaffID=" + StaffID +
                ", StaffCode='" + StaffCode + '\'' +
                '}';
    }

    public String getStaffCode() {
        return StaffCode;
    }

    public void setStaffCode(String staffCode) {
        StaffCode = staffCode;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public int getStaffID() {
        return StaffID;
    }

    public void setStaffID(int StaffID) {
        this.StaffID = StaffID;
    }


    public int getStaffLevel() {
        return StaffLevel;
    }


    public void setStaffLevel(int staffLevel) {
        StaffLevel = staffLevel;
    }
}
