package com.jhzy.nursinghandover.model.lock;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/27.
 */

public class Elders implements Serializable {

    private int ElderID;
    private String ElderName;
    private String PhotoUrl;
    private String BedTitle;
    private String StaffName;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getElderID() {
        return ElderID;
    }

    public void setElderID(int ElderID) {
        this.ElderID = ElderID;
    }

    public String getElderName() {
        return ElderName;
    }

    public void setElderName(String ElderName) {
        this.ElderName = ElderName;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public String getBedTitle() {
        return BedTitle;
    }

    public void setBedTitle(String BedTitle) {
        this.BedTitle = BedTitle;
    }

    public String getStaffName() {
        return StaffName;
    }

    public void setStaffName(String StaffName) {
        this.StaffName = StaffName;
    }
}
