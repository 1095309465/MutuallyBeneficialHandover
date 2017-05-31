package com.jhzy.nursinghandover.utils;

/**
 * Created by nakisaRen
 * on 17/2/20.
 */

public class CONTACT {
    //是否debug模式
    public  static boolean Debug = false;
    public final static String DB_NAME = "jhzy.db"; // 轻松智游的数据库
    public final static int VERSION = 1;//轻松智游的数据库版本号

    public  static  final  String VERSION_INTERFACE = "v1_1";

    public final static String DataSuccess = "A00000";
    public final static String TOKEN = "token";

    public static int type = 0;//(0:MainActivity;1:多人界面)

    public static class NFCTAG {//(0:MainActivity;1:多人界面)
        public final static int TAG_MAINACTIVITY = 0;
        public final static int TAG_MORENURSE = 1;
    }

    public static final String IMEI = "imei";


    public static boolean isCanDownLoad = false;

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";


}
