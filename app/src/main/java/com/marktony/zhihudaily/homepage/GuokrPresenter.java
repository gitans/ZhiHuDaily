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
import com.marktony.zhihudaily.bean.GuokrHandpickNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.detail.GuokrDetailActivity;
import com.marktony.zhihudaily.service.CacheService;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.NetworkState;

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

    private boolean isNetworkConnected = true;

    private ArrayList<GuokrHandpickNews.result> list = new ArrayList<GuokrHandpickNews.result>();

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
    public void start() {

    }

    @Override
    public void loadPosts() {
        view.showLoading();
        if (NetworkState.networkConnected(context)) {
            model.load(Api.GUOKR_ARTICLES, this);
        } else {
            Gson gson = new Gson();
            Cursor cursor = db.query("Guokr", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    GuokrHandpickNews.result result = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")), GuokrHandpickNews.result.class);
                    list.add(result);
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
        loadPosts();
    }

    @Override
    public void onSuccess(String result) {
        view.stopLoading();
        Gson gson = new Gson();
        GuokrHandpickNews question = gson.fromJson(result, GuokrHandpickNews.class);
        for (GuokrHandpickNews.result re : question.getResult()){
            list.add(re);
            if(!queryIfIDExists(re.getId())) {
                try {
                    db.beginTransaction();
                    ContentValues values = new ContentValues();
                    values.put("guokr_id", re.getId());
                    values.put("guokr_news", gson.toJson(re));
                    values.put("guokr_content", "");
                    values.put("guokr_time", (long)re.getDate_picked());
                    db.insert("Guokr", null, values);
                    values.clear();
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }

            }
            Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
            intent.putExtra("type", CacheService.TYPE_GUOKR);
            intent.putExtra("id", re.getId());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }
        view.showResults(list);

    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
    }

    private boolean queryIfIDExists(int id){
        Cursor cursor = db.query("Guokr",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("guokr_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

}
