package com.marktony.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lizhaotailang on 2016/5/8.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String CREATE_TABLE_HISTORY = "create table History(\" "
            +  " id integer primary key,"
            +  "  \")";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
