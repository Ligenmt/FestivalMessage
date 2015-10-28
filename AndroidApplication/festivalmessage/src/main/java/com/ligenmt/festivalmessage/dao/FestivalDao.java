package com.ligenmt.festivalmessage.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleAdapter;

import com.ligenmt.festivalmessage.bean.Festival;
import com.ligenmt.festivalmessage.bean.Message;
import com.ligenmt.festivalmessage.bean.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenov0 on 2015/10/6.
 */
public class FestivalDao {

    private static FestivalDaoHelper helper;
    public FestivalDao(Context context) {
        this.helper = new FestivalDaoHelper(context);
    }

    public void addFestival(String name, String date) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into festival values(null, ?, ?)", new String[]{name, date});
        db.close();
    }

    public Festival findFestivalById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Festival festival = null;
        Cursor cursor = db.rawQuery("select * from festival where _id = ?", new String[]{String.valueOf(id)});
        if(cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            festival = new Festival(id, name, date);
        }
        cursor.close();
        db.close();
        return festival;
    }

    public void deleteFestivalById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from festival where _id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Festival> enumFestival() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from festival", null);
        List<Festival> festivals = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Festival festival = new Festival(id, name, date);
            festivals.add(festival);
        }
        cursor.close();
        db.close();
        return festivals;
    }

    public void deleteMessageById(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from message where _id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void addMessage(int fid, String content) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into message values(null, ?, ?)", new String[]{String.valueOf(fid), content});
        db.close();
    }

    public List<Message> enumMessage() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from message", null);
        List<Message> messages = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int fid = cursor.getInt(cursor.getColumnIndex("fid"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            Message message = new Message(id, fid, content);
            messages.add(message);
        }
        cursor.close();
        db.close();
        return messages;
    }

    public List<Message> findMessageByFid(int fid) {
        List<Message> allmessages = enumMessage();
        List<Message> foundMessages = new ArrayList<>();
        for(Message message : allmessages) {
            if(message.getFestivalId() == fid) {
                foundMessages.add(message);
            }
        }
        return foundMessages;
    }

    public void addRecord(String content, String date) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("insert into record values(null,?,?)", new String[]{content, date});
        db.close();
    }

    public List<Record> enumRecord() {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from record", null);
        List<Record> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Record record = new Record(id, content, date);
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }
}
