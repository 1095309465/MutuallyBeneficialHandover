package com.jhzy.nursinghandover.beans;

/**
 * Created by sxmd on 2017/2/23.
 */

public class ShiftBean {

    private String photo;//头像
    private String id;//长者Id
    private String name;//长者姓名
    private String bedCode;//床位号
    private String nurseName;//责任护工
    private boolean isSelected;//是否选中

    @Override
    public String toString() {
        return "ShiftBean{" +
                "photo='" + photo + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bedCode='" + bedCode + '\'' +
                ", nurseName='" + nurseName + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBedCode() {
        return bedCode;
    }

    public void setBedCode(String bedCode) {
        this.bedCode = bedCode;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }
}
