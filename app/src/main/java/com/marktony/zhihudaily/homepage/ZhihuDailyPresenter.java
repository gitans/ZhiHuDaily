package com.marktony.zhihudaily.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.bean.ZhihuDailyNews;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.detail.ZhihuDetailActivity;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.service.CacheService;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.DateFormatter;
import com.marktony.zhihudaily.util.NetworkState;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lizhaotailang on 2016/9/16.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter, OnStringListener {

    private ZhihuDailyContract.View view;
    private Context context;
    private StringModelImpl model;

    private DateFormatter formatter = new DateFormatter();

    private ArrayList<ZhihuDailyNews.Question> list = new ArrayList<ZhihuDailyNews.Question>();

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ZhihuDailyPresenter(Context context, ZhihuDailyContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
        dbHelper = new DatabaseHelper(context, "History.db", null, 4);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void loadPosts(long date, boolean clearing) {
        view.showLoading();
        if (clearing) {
            list.clear();
        }
        if (NetworkState.networkConnected(context)) {
            model.load(Api.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), this);
        } else {
            Gson gson = new Gson();
            Cursor cursor = db.query("Zhihu", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    ZhihuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), ZhihuDailyNews.Question.class);
                    list.add(question);
                } while (cursor.moveToNext());
            }
            cursor.close();
            view.stopLoading();
            view.showResults(list);
        }
    }

    @Override
    public void refresh() {
        list.clear();
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        if (NetworkState.networkConnected(context)) {
            model.load(Api.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), this);
        } else {
            view.showNetworkError();
        }
    }

    @Override
    public void startReading(int position) {
        context.startActivity(new Intent(context, ZhihuDetailActivity.class)
                .putExtra("id",list.get(position).getId())
        );
    }

    @Override
    public void goToSettings() {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        ZhihuDailyNews post = gson.fromJson(result, ZhihuDailyNews.class);

        ContentValues values = new ContentValues();

        for (ZhihuDailyNews.Question item : post.getStories()) {
            list.add(item);
            if ( !queryIfIDExists(item.getId())) {
                db.beginTransaction();
                try {
                    DateFormat format = new SimpleDateFormat("yyyyMMdd");
                    Date date = format.parse(post.getDate());
                    values.put("zhihu_id", item.getId());
                    values.put("zhihu_news", gson.toJson(item));
                    values.put("zhihu_content", "");
                    values.put("zhihu_time", date.getTime() / 1000);
                    db.insert("Zhihu", null, values);
                    values.clear();
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }

            }
            Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
            intent.putExtra("type", CacheService.TYPE_ZHIHU);
            intent.putExtra("id", item.getId());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }

        view.showResults(list);
        view.stopLoading();

    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
    }

    private boolean queryIfIDExists(int id){
        Cursor cursor = db.query("Zhihu",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("zhihu_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

}
