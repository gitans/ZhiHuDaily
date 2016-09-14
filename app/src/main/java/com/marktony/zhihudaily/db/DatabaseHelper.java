package com.marktony.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lizhaotailang on 2016/5/8.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists LatestPosts("
                + "id integer primary key,"
                + "title text not null,"
                + "type integer not null,"
                + "img_url text not null,"
                + "date integer not null)");
        db.execSQL("create table if not exists Contents("
                + "id integer primary key,"
                + "date integer not null,"
                + "content text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:

            case 2:
                db.execSQL("create table if not exists Contents(id integer primary key,date integer not null,content text not null)");
            // case 3:
                // delete table if exists
                // db.execSQL("drop table if exists LatestPosts");
                // db.execSQL("drop table if exists Contents");
                // create a new table of zhihu daily
                /*db.execSQL("create table if not exists Zhihu("
                        + "id integer primary key autoincrement,"
                        + "zhihu_id integer not null,"
                        + "type integer not null,"
                        + "title text not null,"
                        + "image text,"
                        + "image_source text,"
                        + "date text,"
                        + "share_url text,"
                        + "body text)");*/


                // more
                //db.execSQL("create table if not exists Guokr(id integer primary key autoincrement)");
                // more
                //db.execSQL("create table if not exists Douban(id integer primary key autoincrement)");

        }
    }
}
