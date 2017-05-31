package com.jhzy.nursinghandover.model.loginnfc;

/**
 * Created by Administrator on 2017/2/28.
 */

public class StaffLoginCode {

    @Override
    public String toString() {
        return "StaffLoginCode{" +
            "code='" + code + '\'' +
            ", data=" + data +
            ", msg='" + msg + '\'' +
            '}';
    }


    /**
     * code : A00000
     * data : {"Position":"护工","StaffID":1}
     * msg : null
     */

    private String code;
    /**
     * Position : 护工
     * StaffID : 1
     */

    private StaffLogin data;
    private String msg;


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public StaffLogin getData() {
        return data;
    }


    public void setData(StaffLogin data) {
        this.data = data;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }
}
