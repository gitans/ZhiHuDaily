package com.marktony.zhihudaily.detail;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class ZhihuDetailPresenter implements ZhihuDetailContract.Presenter {

    private ZhihuDetailContract.View view;
    private AppCompatActivity activity;

    public ZhihuDetailPresenter(AppCompatActivity activity, ZhihuDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
