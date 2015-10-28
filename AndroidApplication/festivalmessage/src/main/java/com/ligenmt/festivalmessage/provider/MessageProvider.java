package com.ligenmt.festivalmessage.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.ligenmt.festivalmessage.dao.FestivalDaoHelper;

import java.util.IllegalFormatException;

/**
 * Created by lenov0 on 2015/10/13.
 */
public class MessageProvider extends ContentProvider {

    private static final String AUTHORITY = "com.ligenmt.sms.provider.MessageProvider";
    public static final Uri URI_SMS_ALL = Uri.parse(AUTHORITY);

    private static UriMatcher matcher;
    private static int SMS_ALL = 0;
    private static int SMS_ONE = 1;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "sms", SMS_ALL);
        matcher.addURI(AUTHORITY, "sms/#", SMS_ONE);

    }

    private FestivalDaoHelper helper;
    private SQLiteDatabase db;


    @Override
    public boolean onCreate() {
        helper = new FestivalDaoHelper(getContext());
//        getContext().getContentResolver().insert();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match = matcher.match(uri);
        if(match != SMS_ALL) {
            throw new IllegalArgumentException("Wrong uri:" + uri);
        }
        db = helper.getWritableDatabase();
        db.close();
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
