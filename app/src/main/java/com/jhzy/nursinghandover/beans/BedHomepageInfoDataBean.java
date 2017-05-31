package com.jhzy.nursinghandover.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/29.
 */

public class BedHomepageInfoDataBean implements Parcelable {
    /**
     * BedID : 1
     * BedTitle : A201-001
     * ElderID : 73
     * ElderName : 张振修
     * BirthDate : 1917-07-22 00:00:00
     * CheckInDate : 2017-03-15 00:00:00
     * PhotoUrl : http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/a4dd310d-daaa-4812-ad22-bb0ba45c65e4/card/张振修_副本.jpg
     * CareLevelID : 2
     * CareLevelTitle : 二级护理A
     * SicknessCategory : 高血压，高血糖
     * Attention : null
     * CareNote : null
     * MedicineNote : null
     * SicknessNote : null
     * Character : null
     * Age : 100
     */

    private int BedID;
    private String BedTitle;
    private int ElderID;
    private String ElderName;
    private String BirthDate;
    private String CheckInDate;
    private String PhotoUrl;
    private int CareLevelID;
    private String CareLevelTitle;
    private String SicknessCategory;
    private String Attention;
    private String CareNote;
    private String MedicineNote;
    private String SicknessNote;
    private String Character;
    private int Age;

    private String FoodCategoryies;

    public String getFoodCategoryies() {
        return FoodCategoryies;
    }

    public void setFoodCategoryies(String foodCategoryies) {
        FoodCategoryies = foodCategoryies;
    }

    public static Creator<BedHomepageInfoDataBean> getCREATOR() {
        return CREATOR;
    }

    protected BedHomepageInfoDataBean(Parcel in) {
        BedID = in.readInt();
        BedTitle = in.readString();
        ElderID = in.readInt();
        ElderName = in.readString();
        BirthDate = in.readString();
        CheckInDate = in.readString();
        PhotoUrl = in.readString();
        CareLevelID = in.readInt();
        CareLevelTitle = in.readString();
        SicknessCategory = in.readString();
        Attention = in.readString();
        CareNote = in.readString();
        MedicineNote = in.readString();
        SicknessNote = in.readString();
        Character = in.readString();
        Age = in.readInt();
    }

    public static final Creator<BedHomepageInfoDataBean> CREATOR = new Creator<BedHomepageInfoDataBean>() {
        @Override
        public BedHomepageInfoDataBean createFromParcel(Parcel in) {
            return new BedHomepageInfoDataBean(in);
        }

        @Override
        public BedHomepageInfoDataBean[] newArray(int size) {
            return new BedHomepageInfoDataBean[size];
        }
    };

    public int getBedID() {
        return BedID;
    }


    public void setBedID(int BedID) {
        this.BedID = BedID;
    }


    public String getBedTitle() {
        return BedTitle;
    }


    public void setBedTitle(String BedTitle) {
        this.BedTitle = BedTitle;
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


    public String getBirthDate() {
        return BirthDate;
    }


    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }


    public String getCheckInDate() {
        return CheckInDate;
    }


    public void setCheckInDate(String CheckInDate) {
        this.CheckInDate = CheckInDate;
    }


    public String getPhotoUrl() {
        return PhotoUrl;
    }


    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }


    public int getCareLevelID() {
        return CareLevelID;
    }


    public void setCareLevelID(int CareLevelID) {
        this.CareLevelID = CareLevelID;
    }


    public String getCareLevelTitle() {
        return CareLevelTitle;
    }


    public void setCareLevelTitle(String CareLevelTitle) {
        this.CareLevelTitle = CareLevelTitle;
    }


    public String getSicknessCategory() {
        return SicknessCategory;
    }


    public void setSicknessCategory(String SicknessCategory) {
        this.SicknessCategory = SicknessCategory;
    }


    public String getAttention() {
        return Attention;
    }


    public void setAttention(String Attention) {
        this.Attention = Attention;
    }


    public String getCareNote() {
        return CareNote;
    }


    public void setCareNote(String CareNote) {
        this.CareNote = CareNote;
    }


    public String getMedicineNote() {
        return MedicineNote;
    }


    public void setMedicineNote(String MedicineNote) {
        this.MedicineNote = MedicineNote;
    }


    public String getSicknessNote() {
        return SicknessNote;
    }


    public void setSicknessNote(String SicknessNote) {
        this.SicknessNote = SicknessNote;
    }


    public String getCharacter() {
        return Character;
    }


    public void setCharacter(String Character) {
        this.Character = Character;
    }


    public int getAge() {
        return Age;
    }


    public void setAge(int Age) {
        this.Age = Age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(BedID);
        parcel.writeString(BedTitle);
        parcel.writeInt(ElderID);
        parcel.writeString(ElderName);
        parcel.writeString(BirthDate);
        parcel.writeString(CheckInDate);
        parcel.writeString(PhotoUrl);
        parcel.writeInt(CareLevelID);
        parcel.writeString(CareLevelTitle);
        parcel.writeString(SicknessCategory);
        parcel.writeString(Attention);
        parcel.writeString(CareNote);
        parcel.writeString(MedicineNote);
        parcel.writeString(SicknessNote);
        parcel.writeString(Character);
        parcel.writeInt(Age);
    }
}