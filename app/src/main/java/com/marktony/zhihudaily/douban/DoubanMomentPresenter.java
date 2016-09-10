package com.marktony.zhihudaily.douban;

import android.support.v7.app.AppCompatActivity;

import com.marktony.zhihudaily.bean.DoubanMomentPost;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/10.
 */

public class DoubanMomentPresenter implements DoubanMomentContract.Presenter {

    private DoubanMomentContract.View view;
    private AppCompatActivity activity;

    private ArrayList<DoubanMomentPost> list = new ArrayList<>();

    private int year, month, day;

    public DoubanMomentPresenter(AppCompatActivity activity, DoubanMomentContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void showArticle(int id) {

    }

    @Override
    public void loadPosts(long date) {

    }

    @Override
    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public void start() {

    }

}
