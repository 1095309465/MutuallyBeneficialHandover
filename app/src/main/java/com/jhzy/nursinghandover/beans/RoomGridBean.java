package com.jhzy.nursinghandover.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class RoomGridBean {

    /**
     * code : A00000
     * data : {"Count":25,"Tasks":[{"BedID":1,"ElderID":73,"ElderName":"张振修","BedTitle":"A201-001","PhotoUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/a4dd310d-daaa-4812-ad22-bb0ba45c65e4/card/张振修_副本.jpg","Count":22,"Staff":null},{"BedID":2,"ElderID":53,"ElderName":"刘树华","BedTitle":"A201-002","PhotoUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/4f21e79c-1b28-4bb6-9916-5f4c05d7ddfe/card/刘树华_副本.jpg","Count":3,"Staff":null}]}
     * msg : null
     */

    private String code;
    private DataBean data;
    private Object msg;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public DataBean getData() { return data;}


    public void setData(DataBean data) { this.data = data;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public static class DataBean {
        /**
         * Count : 25
         * Tasks : [{"BedID":1,"ElderID":73,"ElderName":"张振修","BedTitle":"A201-001","PhotoUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/a4dd310d-daaa-4812-ad22-bb0ba45c65e4/card/张振修_副本.jpg","Count":22,"Staff":null},{"BedID":2,"ElderID":53,"ElderName":"刘树华","BedTitle":"A201-002","PhotoUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/4f21e79c-1b28-4bb6-9916-5f4c05d7ddfe/card/刘树华_副本.jpg","Count":3,"Staff":null}]
         */

        private int Count;
        private List<TasksBean> Tasks;


        public int getCount() { return Count;}


        public void setCount(int Count) { this.Count = Count;}


        public List<TasksBean> getTasks() { return Tasks;}


        public void setTasks(List<TasksBean> Tasks) { this.Tasks = Tasks;}


        public static class TasksBean {
            /**
             * BedID : 1
             * ElderID : 73
             * ElderName : 张振修
             * BedTitle : A201-001
             * PhotoUrl : http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/data/a4dd310d-daaa-4812-ad22-bb0ba45c65e4/card/张振修_副本.jpg
             * Count : 22
             * Staff : null
             */

            private int BedID;
            private int ElderID;
            private String ElderName;
            private String BedTitle;
            private String PhotoUrl;
            private int Count;
            private String Staff;


            public int getBedID() { return BedID;}


            public void setBedID(int BedID) { this.BedID = BedID;}


            public int getElderID() { return ElderID;}


            public void setElderID(int ElderID) { this.ElderID = ElderID;}


            public String getElderName() { return ElderName;}


            public void setElderName(String ElderName) { this.ElderName = ElderName;}


            public String getBedTitle() { return BedTitle;}


            public void setBedTitle(String BedTitle) { this.BedTitle = BedTitle;}


            public String getPhotoUrl() { return PhotoUrl;}


            public void setPhotoUrl(String PhotoUrl) { this.PhotoUrl = PhotoUrl;}


            public int getCount() { return Count;}


            public void setCount(int Count) { this.Count = Count;}


            public String getStaff() { return Staff;}


            public void setStaff(String Staff) { this.Staff = Staff;}
        }
    }
}
