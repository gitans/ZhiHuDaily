package com.marktony.zhihudaily.douban;

import android.content.Context;
import android.content.Intent;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.bean.DoubanMomentNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.detail.DoubanDetailActivity;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.DateFormatter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lizhaotailang on 2016/9/10.
 */

public class DoubanMomentPresenter implements DoubanMomentContract.Presenter, OnStringListener {

    private DoubanMomentContract.View view;
    private Context context;
    private StringModelImpl model;

    private ArrayList<DoubanMomentNews.posts> list = new ArrayList<>();

    public DoubanMomentPresenter(Context context, DoubanMomentContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
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
    public void loadPosts(long date, boolean clearing) {
        view.startLoading();
        if (clearing) {
            list.clear();
        }
        model.load(Api.DOUBAN_MOMENT + new DateFormatter().DoubanDateFormat(date), this);
    }

    @Override
    public void refresh() {
        list.clear();
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        model.load(Api.DOUBAN_MOMENT + new DateFormatter().DoubanDateFormat(date), this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        view.stopLoading();
        Gson gson = new Gson();
        DoubanMomentNews post = gson.fromJson(result, DoubanMomentNews.class);
        for (DoubanMomentNews.posts item : post.getPosts()) {
            list.add(item);
        }
        view.showResults(list);
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showLoadError();
    }

}
