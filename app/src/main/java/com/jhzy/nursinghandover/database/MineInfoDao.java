package com.jhzy.nursinghandover.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by  bigyu  on  2016/12/26 .
 * 个人信息缓冲
 */

public class MineInfoDao {


    private static DBHelper dbHelper = null;
    private Context mContext;
    private static SQLiteDatabase mDB;
    private static MineInfoDao mineInfoDao;


    public MineInfoDao() {
    }


    public static MineInfoDao getInstance(Context context) {
        if (null == mineInfoDao) {
            mineInfoDao = new MineInfoDao();
            dbHelper = new DBHelper(context);
            mDB = dbHelper.getDB();
        }
        return mineInfoDao;
    }

    /**
     * 当Activity中调用此构造方法，传入一个版本号时，系统会在下一次调用数据库时调用Helper中的onUpgrade()方法进行更新
     *
     * @param cxt
     * @param version
     */
    private MineInfoDao(Context cxt, int version) {
        dbHelper = new DBHelper(cxt, version);
        mDB = dbHelper.getDB();
    }

    // 插入操作  id  ,json,createTime
    public synchronized void insertData(String... context) {
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from mineInfo where id=?", new String[]{context[0]});
        int count = cursor.getCount();
        if (count == 0) { // 没有数据则添加数据
            String sql = "insert into mineInfo(id ,json,createTime)values(?,?,?)";
            writableDatabase.execSQL(sql, context);
        } else if (count == 1) {// 有数据则更新
            String sql = "update mineInfo set json=?,createTime=? where id=?";
            writableDatabase.execSQL(sql, new Object[]{context[1], context[2], context[0]});
        }
        cursor.close();
        //writableDatabase.close();
        //readableDatabase.close();
    }


    public synchronized  String selectMineInfo(String id) {
        String json = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select json from mineInfo where id=?", new String[]{id});
        while (cursor.moveToNext()) {
            json = cursor.getString(cursor.getColumnIndex("json"));
        }
        cursor.close();
        //db.close();
        return json;
    }

    /**
     * 增加轨迹点
     * @param entity
     */
    /*public synchronized void insertLatLng(TrackEntity entity) {
        mDB.execSQL(
            "Insert into track (orderid, guideid, touristid, lat, lng, "
                + "time)"
                + " Values (?, ?, ?, ?, ?, ?)",
            new String[] {
                entity.getOrderid(), entity.getGuideid(),
                entity.getTouristid(), entity.getLat(),
                entity.getLng(), entity.getTime()
            });
    }*/


    /**
     * 增加正在服务的订单信息
     * @param info
     */
    /*public synchronized void insertOrderInfo(OrderInfo info) {
        mDB.execSQL("Insert into orderinfo (orderid, guideid, touristid,state,startTime)" +
                "Values (?,?,?,?,?)",
            new Object[] {info.getOrderid(), info.getGuideid()
                , info.getTouristid(),"on",info.getStartTime()});
    }*/

    /**
     * 关掉结束服务的订单信息
     */
    public synchronized boolean deleteOrderInfoById(String orderid) {
        try {
            String sqlDelete = "update orderinfo set state = ? where orderid = ?";
            //String sqlDelete = "delete from order where sub_orderid = ?";
            mDB.execSQL(sqlDelete,new String[]{"off",orderid});
            return true;
        } catch (Exception ex) {

        }
        return false;
    }

}
