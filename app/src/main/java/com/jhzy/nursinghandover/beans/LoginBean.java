package com.jhzy.nursinghandover.beans;

/**
 * Created by nakisaRen
 * on 17/3/2.
 */

public class LoginBean {

    /**
     * code : A00000
     * data : {"auth_token":"rgA6+f2Kt3Rzy0+GYZNqtOu4rkWbpQV8tjqGYj9rH6J9h10zWVbA6Rrb4l9pDsJsI61Y5NhSTNxkrdPsW8h4UQ=="}
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


    public static class DataBean {
        /**
         * auth_token : rgA6+f2Kt3Rzy0+GYZNqtOu4rkWbpQV8tjqGYj9rH6J9h10zWVbA6Rrb4l9pDsJsI61Y5NhSTNxkrdPsW8h4UQ==
         */

        private String auth_token;


        public String getAuth_token() { return auth_token;}


        public void setAuth_token(String auth_token) { this.auth_token = auth_token;}
    }
}
