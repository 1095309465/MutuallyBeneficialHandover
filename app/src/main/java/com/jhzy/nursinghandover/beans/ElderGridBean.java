package com.jhzy.nursinghandover.beans;

import java.util.List;

/**
 * Created by Administrator on 2017/3/14.
 */

public class ElderGridBean {

    /**
     * code : A00000
     * data : [{"CareItemName":"夜间护理","CareItemUrl":"http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_yjhl.jpg","StartTime":"07:00:00","EndTime":"07:30:00"}]
     * msg : null
     */

    private String code;
    private Object msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean {
        /**
         * CareItemName : 夜间护理
         * CareItemUrl : http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/NurseIcon/HLJJAPP/taskIcon/icon_yjhl.jpg
         * StartTime : 07:00:00
         * EndTime : 07:30:00
         */

        private String CareItemName;
        private String CareItemUrl;
        private String StartTime;
        private String EndTime;

        public String getCareItemName() {
            return CareItemName;
        }

        public void setCareItemName(String CareItemName) {
            this.CareItemName = CareItemName;
        }

        public String getCareItemUrl() {
            return CareItemUrl;
        }

        public void setCareItemUrl(String CareItemUrl) {
            this.CareItemUrl = CareItemUrl;
        }

        public String getStartTime() {
            return StartTime.substring(0,5);
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime.substring(0,5);
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "CareItemName='" + CareItemName + '\'' +
                    ", CareItemUrl='" + CareItemUrl + '\'' +
                    ", StartTime='" + StartTime + '\'' +
                    ", EndTime='" + EndTime + '\'' +
                    '}';
        }
    }
}
