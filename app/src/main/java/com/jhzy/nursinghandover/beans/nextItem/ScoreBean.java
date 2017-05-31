package com.jhzy.nursinghandover.beans.nextItem;

/**
 * Created by nakisaRen
 * on 17/3/3.
 */

public class ScoreBean {

    /**
     * code : A00000
     * data : {"Point":10}
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
         *  DayPoint = 10,
         MonthPoint = 10
         */

        private int DayPoint;
        private int MonthPoint;


        public int getDayPoint() {
            return DayPoint;
        }


        public void setDayPoint(int dayPoint) {
            DayPoint = dayPoint;
        }


        public int getMonthPoint() {
            return MonthPoint;
        }


        public void setMonthPoint(int monthPoint) {
            MonthPoint = monthPoint;
        }
    }
}
