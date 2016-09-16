package com.marktony.zhihudaily.douban;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.bean.DoubanMomentPost;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.ui.activity.DoubanReadActivity;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.DateFormatter;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/10.
 */

public class DoubanMomentPresenter implements DoubanMomentContract.Presenter, OnStringListener {

    private DoubanMomentContract.View view;
    private Context context;
    private StringModelImpl model;

    private ArrayList<DoubanMomentPost.posts> list = new ArrayList<>();

    private int year, month, day;

    public DoubanMomentPresenter(Context context, DoubanMomentContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void startReading(int position) {
        DoubanMomentPost.posts item = list.get(position);
        Intent intent = new Intent(context, DoubanReadActivity.class);
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
    public void loadPosts(long date) {
        view.startLoading();
        model.load(Api.DOUBAN_MOMENT + new DateFormatter().DoubanDateFormat(date), this);
    }

    @Override
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        view.stopLoading();
        Gson gson = new Gson();
        DoubanMomentPost post = gson.fromJson(result, DoubanMomentPost.class);
        for (DoubanMomentPost.posts item : post.getPosts()) {
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
