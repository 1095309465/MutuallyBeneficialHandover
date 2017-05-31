package com.jhzy.nursinghandover.beans;

/**
 * Created by nakisaRen
 * on 17/3/28.
 */

public class BedOrDoor {

    /**
     * code : A00000
     * data : {"BedCount":0}
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
         * BedCount : 0
         */

        private int BedCount;


        public int getBedCount() { return BedCount;}


        public void setBedCount(int BedCount) { this.BedCount = BedCount;}
    }
}
