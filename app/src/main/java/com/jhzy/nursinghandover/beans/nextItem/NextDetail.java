package com.jhzy.nursinghandover.beans.nextItem;

/**
 * Created by nakisaRen
 * on 17/2/28.
 */

public class NextDetail {

    /**
     * code : A00000
     * data : {"Room":{"ID":5,"RoomTitle":"A301室"},"Staff":{"StaffID":95,"StaffCode":"1001","Position":"护工","StaffName":"张三","PhotoUrl":"http://qszy.oss-cn-shanghai.aliyuncs.com/default/avatar.png","DayPoint":0,"MonthPoint":0},"Nurse":[{"CareItemID":0,"Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png\",\"SubprojectName\":\"按摩\"}]","CareItemTitle":"护理项目标题1","CareItemUrl":"http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png","StartTime":"02:00:00","EndTime":"07:00:00","Elders":[{"TaskId":2,"ElderID":1,"ElderName":"张三","PhotoUrl":"http://v1.qzone.cc/avatar/201508/05/11/05/55c17d7dce37c723.jpg%21200x200.jpg","BedTitle":"01房001床"}]},{"CareItemID":1,"Subproject":"[{\"Number\":\"1\",\"SubprojectIco\":\"http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png\",\"SubprojectName\":\"按摩\"}]","CareItemTitle":"护理项目标题2","CareItemUrl":"http://192.168.0.38/OrgWeb/UploadFile/Staff/weifan.png","StartTime":"03:00:00","EndTime":"09:00:00","Elders":[{"TaskId":1,"ElderID":1,"ElderName":"张三","PhotoUrl":"http://v1.qzone.cc/avatar/201508/05/11/05/55c17d7dce37c723.jpg%21200x200.jpg","BedTitle":"01房001床"}]}]}
     * msg : null
     */

    private String code;
    private DataBean data;
    private String msg;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public DataBean getData() { return data;}


    public void setData(DataBean data) { this.data = data;}


    public String getMsg() { return msg;}


    public void setMsg(String msg) { this.msg = msg;}
}
