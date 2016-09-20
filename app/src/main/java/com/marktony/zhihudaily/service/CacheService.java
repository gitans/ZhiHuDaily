package com.marktony.zhihudaily.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.marktony.zhihudaily.app.VolleySingleton;
import com.marktony.zhihudaily.bean.ZhihuDailyStory;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.util.Api;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/18.
 */

public class CacheService extends Service {

    private ArrayList<Integer> zhihuIds = new ArrayList<Integer>();
    private ArrayList<Integer> guokrIds = new ArrayList<Integer>();
    private ArrayList<Integer> doubanIds = new ArrayList<Integer>();
    private Gson gson = new Gson();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private static final String TAG = CacheService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(this,"History.db",null,4);
        db = dbHelper.getWritableDatabase();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void setZhihuIds(ArrayList<Integer> zhihuIds) {
        this.zhihuIds = zhihuIds;
    }

    public void startCache() {
        Log.d("story", "onStartCache" + "size" + zhihuIds.size());
        for (int i = 0; i < zhihuIds.size(); i++) {
            final int finalI = i;
            final int finalI1 = i;
            StringRequest request = new StringRequest(Request.Method.GET, Api.ZHIHU_NEWS + zhihuIds.get(i), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    ZhihuDailyStory story = gson.fromJson(s, ZhihuDailyStory.class);
                    /*Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex("zhihu_id")) == (zhihuIds.get(finalI1))) {
                                ContentValues values = new ContentValues();
                                values.put("zhihu_content", s);
                                db.update("Zhihu", values, "zhihu_id = ?", new String[] {String.valueOf(story.getId())});
                                values.clear();
                                Log.d("content", "" + cursor.getString(cursor.getColumnIndex("zhihu_news")));
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();*/
                    if (finalI == zhihuIds.size() - 1) {
                        stopSelf();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (finalI == zhihuIds.size() - 1) {
                        stopSelf();
                    }
                }
            });
            VolleySingleton.getVolleySingleton(this).getRequestQueue().add(request);

        }
    }

    public class MyBinder extends Binder {
        public CacheService getService() {
            return CacheService.this;
        }
    }

}
