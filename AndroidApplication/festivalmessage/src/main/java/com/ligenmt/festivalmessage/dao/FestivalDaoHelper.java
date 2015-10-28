package com.ligenmt.festivalmessage.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lenov0 on 2015/10/6.
 */
public class FestivalDaoHelper extends SQLiteOpenHelper {


    public FestivalDaoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public FestivalDaoHelper(Context context) {
        super(context, "FestivalMessage.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table festival(_id integer primary key autoincrement, name text, date text)");
        db.execSQL("create table message(_id integer primary key autoincrement, fid integer, content text)");
        db.execSQL("create table record(_id integer primary key autoincrement, content text, date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
