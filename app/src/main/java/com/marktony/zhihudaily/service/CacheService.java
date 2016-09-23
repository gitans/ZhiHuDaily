package com.marktony.zhihudaily.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
 * // TODO: 2016/9/24 too much memory used
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

    public void startZhihuCache() {
        for (int i = 0; i < zhihuIds.size(); i++) {
            final int finali = i;
            StringRequest request = new StringRequest(Request.Method.GET, Api.ZHIHU_NEWS + zhihuIds.get(i), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    ZhihuDailyStory story = gson.fromJson(s, ZhihuDailyStory.class);
                    Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if ((cursor.getInt(cursor.getColumnIndex("zhihu_id")) == (zhihuIds.get(finali)))
                                    && (cursor.getString(cursor.getColumnIndex("zhihu_content")).equals(""))) {
                                /*db.beginTransaction();
                                try {

                                    db.setTransactionSuccessful();
                                } catch (Exception e){
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }*/
                                ContentValues values = new ContentValues();
                                values.put("zhihu_content", s);
                                db.update("Zhihu", values, "zhihu_id = ?", new String[] {String.valueOf(story.getId())});
                                values.clear();
                            }
                            break;
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            request.setTag(TAG);
            VolleySingleton.getVolleySingleton(this).getRequestQueue().add(request);
        }
    }

    public void setGuokrIds(ArrayList<Integer> guokrIds) {
        this.guokrIds = guokrIds;
    }

    public void startGuokrCache() {
        for (int i = 0; i < guokrIds.size(); i++) {
            final int finalI = i;
            StringRequest request = new StringRequest(Api.GUOKR_ARTICLE_LINK_V2 + guokrIds.get(i), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Cursor cursor = db.query("Guokr", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex("guokr_id")) == (guokrIds.get(finalI))
                                    && (cursor.getString(cursor.getColumnIndex("guokr_content")).equals(""))) {
                                /*db.beginTransaction();
                                try {

                                    db.setTransactionSuccessful();
                                } catch (Exception e){
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }*/
                                ContentValues values = new ContentValues();
                                values.put("guokr_content", s);
                                db.update("Guokr", values, "guokr_id = ?", new String[] {String.valueOf(guokrIds.get(finalI))});
                                values.clear();
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            request.setTag(TAG);
            VolleySingleton.getVolleySingleton(this).getRequestQueue().add(request);
        }
    }

    public void setDoubanIds(ArrayList<Integer> doubanIds) {
        this.doubanIds = doubanIds;
    }

    public void startDoubanCache() {
        for (int i = 0; i < guokrIds.size(); i++) {
            final int finalI = i;
            StringRequest request = new StringRequest(Api.DOUBAN_ARTICLE_DETAIL + guokrIds.get(i), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Cursor cursor = db.query("Douban", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex("douban_id")) == (doubanIds.get(finalI))
                                    && (cursor.getString(cursor.getColumnIndex("douban_content")).equals(""))) {
                                /*db.beginTransaction();
                                try {

                                    db.setTransactionSuccessful();
                                } catch (Exception e){
                                    e.printStackTrace();
                                } finally {
                                    db.endTransaction();
                                }*/
                                ContentValues values = new ContentValues();
                                values.put("douban_content", s);
                                db.update("Douban", values, "douban_id = ?", new String[] {String.valueOf(doubanIds.get(finalI))});
                                values.clear();
                                break;
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            request.setTag(TAG);
            VolleySingleton.getVolleySingleton(this).getRequestQueue().add(request);
        }
    }

    public class MyBinder extends Binder {
        public CacheService getService() {
            return CacheService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleySingleton.getVolleySingleton(this).getRequestQueue().cancelAll(TAG);
    }
}
