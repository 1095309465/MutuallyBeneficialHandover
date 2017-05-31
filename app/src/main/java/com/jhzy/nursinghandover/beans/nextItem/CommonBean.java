package com.jhzy.nursinghandover.beans.nextItem;

/**
 * Created by nakisaRen
 * on 17/3/27.
 */

public class CommonBean {
    /**
     * ID : 42
     * CareItemTitle : 换纸尿裤
     * CareItemUrl : http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_hnb.jpg
     * Subproject : [{"Number":"1","SubprojectIco":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_db.jpg","SubprojectName":"大便"},{"Number":"2","SubprojectIco":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/subIcon/icon_xb.jpg","SubprojectName":"小便"}]
     */

    private int ID;
    private String CareItemTitle;
    private String CareItemUrl;
    private String Subproject;


    public int getID() { return ID;}


    public void setID(int ID) { this.ID = ID;}


    public String getCareItemTitle() { return CareItemTitle;}


    public void setCareItemTitle(String CareItemTitle) { this.CareItemTitle = CareItemTitle;}


    public String getCareItemUrl() { return CareItemUrl;}


    public void setCareItemUrl(String CareItemUrl) { this.CareItemUrl = CareItemUrl;}


    public String getSubproject() { return Subproject;}


    public void setSubproject(String Subproject) { this.Subproject = Subproject;}
}
