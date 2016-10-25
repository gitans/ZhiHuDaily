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
import com.marktony.zhihudaily.bean.DoubanMomentNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.interfaze.OnStringListener;
import com.marktony.zhihudaily.detail.DoubanDetailActivity;
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
 * Created by Lizhaotailang on 2016/9/10.
 */

public class DoubanMomentPresenter implements DoubanMomentContract.Presenter {

    private DoubanMomentContract.View view;
    private Context context;
    private StringModelImpl model;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private Gson gson = new Gson();

    private ArrayList<DoubanMomentNews.posts> list = new ArrayList<>();

    public DoubanMomentPresenter(Context context, DoubanMomentContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
        dbHelper = new DatabaseHelper(context, "History.db", null, 4);
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public void startReading(int position) {
        DoubanMomentNews.posts item = list.get(position);
        Intent intent = new Intent(context, DoubanDetailActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("title", item.getTitle());
        if (item.getThumbs().size() == 0){
            intent.putExtra("image", "");
        } else {
            intent.putExtra("image", item.getThumbs().get(0).getMedium().getUrl());
        }
        context.startActivity(intent);
    }

    @Override
    public void loadPosts(long date, final boolean clearing) {

        if (clearing) {
            view.startLoading();
        }

        if (NetworkState.networkConnected(context)) {

            model.load(Api.DOUBAN_MOMENT + new DateFormatter().DoubanDateFormat(date), new OnStringListener() {
                @Override
                public void onSuccess(String result) {

                    DoubanMomentNews post = gson.fromJson(result, DoubanMomentNews.class);
                    ContentValues values = new ContentValues();

                    if (clearing) {
                        list.clear();
                    }

                    for (DoubanMomentNews.posts item : post.getPosts()) {

                        list.add(item);

                        if ( !queryIfIDExists(item.getId())) {
                            db.beginTransaction();
                            try {
                                values.put("douban_id", item.getId());
                                values.put("douban_news", gson.toJson(item));
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = format.parse(item.getPublished_time());
                                values.put("douban_time", date.getTime() / 1000);
                                values.put("douban_content", "");
                                db.insert("Douban", null, values);
                                values.clear();
                                db.setTransactionSuccessful();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                db.endTransaction();
                            }
                        }
                        Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
                        intent.putExtra("type", CacheService.TYPE_DOUBAN);
                        intent.putExtra("id", item.getId());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                    view.showResults(list);
                    view.stopLoading();

                }

                @Override
                public void onError(VolleyError error) {
                    view.stopLoading();
                    view.showLoadError();
                }
            });
        } else {

            if (clearing) {

                list.clear();

                Cursor cursor = db.query("Douban", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        DoubanMomentNews.posts post = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_news")), DoubanMomentNews.posts.class);
                        list.add(post);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                view.stopLoading();
                view.showResults(list);
            } else {
                view.showNetworkError();
            }
        }
    }

    @Override
    public void refresh() {
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        loadPosts(date, false);
    }

    @Override
    public void goToSettings() {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Override
    public void start() {
        refresh();
    }

    private boolean queryIfIDExists(int id){
        Cursor cursor = db.query("Douban",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("douban_id"))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

}
