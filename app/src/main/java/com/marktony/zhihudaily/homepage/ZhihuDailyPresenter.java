package com.marktony.zhihudaily.homepage;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lizhaotailang on 2016/9/16.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter, OnStringListener {

    private ZhihuDailyContract.View view;
    private Context context;
    private StringModelImpl model;

    private DateFormatter formatter = new DateFormatter();

    private ArrayList<ZhihuDailyNews.Question> list = new ArrayList<ZhihuDailyNews.Question>();
    private ArrayList<Integer> zhihuIds = new ArrayList<Integer>();

    private ServiceConnection conn;
    private CacheService service;

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
        model.load(Api.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), this);
    }

    @Override
    public void refresh() {
        list.clear();
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        model.load(Api.ZHIHU_HISTORY + formatter.ZhihuDailyDateFormat(date), this);
    }

    @Override
    public void startReading(int position) {
        context.startActivity(new Intent(context, ZhihuDetailActivity.class)
                .putExtra("id",list.get(position).getId())
        );
    }

    @Override
    public void bindService() {
        conn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                CacheService.MyBinder binder = (CacheService.MyBinder) iBinder;
                service = binder.getService();
                binder.getService().setZhihuIds(zhihuIds);
                bindService();
                service.startZhihuCache();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        context.bindService(new Intent(context, CacheService.class), conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void unBindService() {
        if (service != null) {
            service.unbindService(conn);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        view.stopLoading();
        Gson gson = new Gson();
        ZhihuDailyNews post = gson.fromJson(result, ZhihuDailyNews.class);
        for (ZhihuDailyNews.Question item : post.getStories()) {
            list.add(item);
            zhihuIds.add(item.getId());
            ContentValues values = new ContentValues();
            values.put("zhihu_id", item.getId());
            values.put("zhihu_news", gson.toJson(item));
            values.put("zhihu_content", "");
            db.insert("Zhihu", null, values);
            values.clear();
        }

        view.showResults(list);

        for (int i = 0; i < zhihuIds.size(); i++) {

        }

    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
    }

}
