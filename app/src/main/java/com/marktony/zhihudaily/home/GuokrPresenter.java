package com.marktony.zhihudaily.home;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.interfaces.OnStringListener;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public class GuokrPresenter implements GuokrContract.Presenter, OnStringListener {

    private GuokrContract.View view;
    private Context context;
    private StringModelImpl model;

    public GuokrPresenter(Context context, GuokrContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void startReading(int position) {

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
    public void onSuccess(String result) {
        view.stopLoading();
        view.showSuccess();
        Log.d("result", result);
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
        Log.d("error", error.toString());
    }

}
