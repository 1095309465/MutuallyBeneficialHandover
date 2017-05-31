package com.jhzy.nursinghandover.beans;

/**
 * Created by sxmd on 2017/3/3.
 */

public class JiaoBean {

    @Override
    public String toString() {
        return "JiaoBean{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", msg=" + msg +
                '}';
    }

    /**
     * code : A00000
     * data : 接班成功
     * msg : null
     */

    private String code;
    private String data;
    private Object msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
