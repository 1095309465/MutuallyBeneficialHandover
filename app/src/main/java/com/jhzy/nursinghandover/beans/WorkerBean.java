package com.jhzy.nursinghandover.beans;

/**
 * Created by sxmd on 2017/3/3.
 */

public class WorkerBean {


    /**
     * code : A00000
     * data : {"StaffID":1,"StaffName":"111","PhotoUrl":"头像地址"}
     * msg : null
     */

    private String code;
    /**
     * StaffID : 1
     * StaffName : 111
     * PhotoUrl : 头像地址
     */

    private WorkerDataBean data;
    private Object msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WorkerDataBean getData() {
        return data;
    }

    public void setData(WorkerDataBean data) {
        this.data = data;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }


}
