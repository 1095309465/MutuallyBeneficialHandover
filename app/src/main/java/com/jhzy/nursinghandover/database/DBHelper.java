package com.jhzy.nursinghandover.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.jhzy.nursinghandover.utils.CONTACT.DB_NAME;
import static com.jhzy.nursinghandover.utils.CONTACT.VERSION;

/**
 * Created by  bigyu  on  2016/12/26 .
 */

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;


    //自带的构造方法
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //为了每次构造时不用传入dbName和版本号，自己得新定义一个构造方法
    public DBHelper(Context cxt) {
        this(cxt, DB_NAME, null, VERSION);//调用上面的构造方法
        getReadDatabase();
    }

    //版本变更时
    public DBHelper(Context cxt, int version) {
        this(cxt, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建个人中心的表
        try {
            db.execSQL(createTrackTable);
            db.execSQL(createOrderTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.db = db;
        //getReadDatabase();

    }

    private static final String createTrackTable = "create table track (" +
        "_id integer primary key autoincrement," +
        "orderid text, " +
        "guideid text," +
        "touristid text," +
        "lat text," +
        "lng text," +
        "time date" +
        ")";
    private static final String createOrderTable = "create table orderinfo (" +
        "orderid text, " +
        "guideid text," +
        "touristid text," +
        "state text," +
        "startTime text" +
        ")";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase getDB(){
        return db;
    }

    private void getReadDatabase() {
        db = getReadableDatabase();
    }
}
