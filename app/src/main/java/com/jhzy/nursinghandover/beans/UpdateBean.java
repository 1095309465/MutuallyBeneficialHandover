package com.jhzy.nursinghandover.beans;

/**
 * Created by sxmd on 2017/3/15.
 */

public class UpdateBean {


    /**
     * code : A00000
     * data : {"Version":"1.2","URL":"http://apk.r1.market.hiapk.com/data/upload/apkres/2016/12_5/16/com.yanxin.eloanan_044312.apk"}
     * msg : null
     */

    private String code;
    /**
     * Version : 1.2
     * URL : http://apk.r1.market.hiapk.com/data/upload/apkres/2016/12_5/16/com.yanxin.eloanan_044312.apk
     */

    private DataBean data;
    private Object msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String Version;
        private String URL;

        public String getVersion() {
            return Version;
        }

        public void setVersion(String Version) {
            this.Version = Version;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
