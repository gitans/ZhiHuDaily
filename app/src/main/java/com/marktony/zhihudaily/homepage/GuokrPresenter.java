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
import com.marktony.zhihudaily.bean.GuokrHandpickNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.detail.GuokrDetailActivity;
import com.marktony.zhihudaily.service.CacheService;
import com.marktony.zhihudaily.util.Api;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public class GuokrPresenter implements GuokrContract.Presenter, OnStringListener {

    private GuokrContract.View view;
    private Context context;
    private StringModelImpl model;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private ServiceConnection conn;
    private CacheService service;

    private ArrayList<GuokrHandpickNews.result> list = new ArrayList<GuokrHandpickNews.result>();
    private ArrayList<Integer> guokrIds = new ArrayList<Integer>();

    public GuokrPresenter(Context context, GuokrContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        model = new StringModelImpl(context);
        dbHelper = new DatabaseHelper(context, "History.db", null, 4);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void startReading(int position) {
        GuokrHandpickNews.result item = list.get(position);
        context.startActivity(new Intent(context, GuokrDetailActivity.class)
                .putExtra("id", item.getId())
                .putExtra("headlineImageUrl", item.getHeadline_img())
                .putExtra("title", item.getTitle())
        );
    }

    @Override
    public void bindService() {
        conn = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                CacheService.MyBinder binder = (CacheService.MyBinder) iBinder;
                service = binder.getService();
                binder.getService().setGuokrIds(guokrIds);
                bindService();
                service.startGuokrCache();
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
    public void setUrl(String url) {
        view.showLoading();
        model.load(url, this);
    }

    @Override
    public void refresh() {
        list.clear();
        setUrl(Api.GUOKR_ARTICLES);
    }

    @Override
    public void onSuccess(String result) {
        view.stopLoading();
        Gson gson = new Gson();
        GuokrHandpickNews question = gson.fromJson(result, GuokrHandpickNews.class);
        for (GuokrHandpickNews.result re : question.getResult()){
            list.add(re);
            guokrIds.add(re.getId());

            ContentValues values = new ContentValues();
            values.put("guokr_id", re.getId());
            values.put("guokr_news", gson.toJson(re));
            values.put("guokr_content", "");
            db.insert("Guokr", null, values);

        }
        view.showResults(list);

    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
    }

}
