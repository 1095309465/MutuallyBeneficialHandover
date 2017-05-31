package com.jhzy.nursinghandover.model.lock;

/**
 * Created by Administrator on 2017/2/27.
 */

public class LockCode {


    /**
     * code : A00000
     * data : {"elders":[{"ElderID":3,"ElderName":"吕钲","PhotoUrl":"https://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/Org/1/Elder/20170314162943603.jpg","BedTitle":"A012","StaffName":null}],"staffs":""}
     * msg :
     */

    private String code;
    /**
     * elders : [{"ElderID":3,"ElderName":"吕钲","PhotoUrl":"https://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/Org/1/Elder/20170314162943603.jpg","BedTitle":"A012","StaffName":null}]
     * staffs :
     */

    private Lock data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Lock getData() {
        return data;
    }

    public void setData(Lock data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
