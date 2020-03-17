package com.example.wangzhi6.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class MySQLite extends SQLiteOpenHelper {

    private static String name = "diary.db";
    private static int version = 1 ;

    public MySQLite(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table user(" +
                "id integer primary key autoincrement, " +
                "title varchar(64)," +
                "remark varchar(144)," +
                "url_icon varchar(144), "+
                "url varchar(144))";
        db.execSQL(sql);
    }
//    userDo.addSql(title,remark,url_icon,url);

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
