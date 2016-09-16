package com.marktony.zhihudaily.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.bean.ZhihuDailyNews;
import com.marktony.zhihudaily.detail.ZhihuDetailActivity;
import com.marktony.zhihudaily.interfaces.OnStringListener;
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

    public ZhihuDailyPresenter(Context context, ZhihuDailyContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
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
                .putExtra("title",list.get(position).getTitle())
        );
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
        }
        view.showResults(list);
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
    }

}
