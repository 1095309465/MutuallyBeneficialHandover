package com.jhzy.nursinghandover.beans;

/**
 * Created by nakisaRen
 * on 17/3/28.
 */

public class BedHomepageInfo {
    /**
     * code : A00000
     * data : {"BedID":1,"BedTitle":"A201-001","ElderID":73,"ElderName":"张振修","BirthDate":"1917-07-22 00:00:00","CheckInDate":"2017-03-15 00:00:00","PhotoUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/a4dd310d-daaa-4812-ad22-bb0ba45c65e4/card/张振修_副本.jpg","CareLevelID":2,"CareLevelTitle":"二级护理A","SicknessCategory":"高血压，高血糖","Attention":"null","CareNote":"null","MedicineNote":"null","SicknessNote":"null","Character":"null","Age":100}
     * msg : null
     */

    private String code;
    private BedHomepageInfoDataBean data;
    private String msg;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public BedHomepageInfoDataBean getData() { return data;}


    public void setData(BedHomepageInfoDataBean data) { this.data = data;}


    public String getMsg() { return msg;}


    public void setMsg(String msg) { this.msg = msg;}

}
