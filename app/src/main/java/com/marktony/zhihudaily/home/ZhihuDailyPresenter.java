package com.marktony.zhihudaily.home;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.interfaces.OnStringListener;

/**
 * Created by Lizhaotailang on 2016/9/16.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter, OnStringListener {

    private ZhihuDailyContract.View view;
    private Context context;
    private StringModelImpl model;

    public ZhihuDailyPresenter(Context context, ZhihuDailyContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void setUrl(String url) {
        view.showLoading();
        model.load(url, this);
    }

    @Override
    public void refresh() {

    }

    @Override
    public void startReading(int position) {

    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        view.stopLoading();
        Log.d("zhihuresult", result);
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        Log.d("zhihuerror", error.toString());
    }

}
