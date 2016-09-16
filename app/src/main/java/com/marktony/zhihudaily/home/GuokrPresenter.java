package com.marktony.zhihudaily.home;

import android.content.Context;
import android.content.Intent;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.bean.GuokrHandpickNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.detail.GuokrDetailActivity;
import com.marktony.zhihudaily.util.Api;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public class GuokrPresenter implements GuokrContract.Presenter, OnStringListener {

    private GuokrContract.View view;
    private Context context;
    private StringModelImpl model;

    private ArrayList<GuokrHandpickNews.result> list = new ArrayList<GuokrHandpickNews.result>();

    public GuokrPresenter(Context context, GuokrContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        model = new StringModelImpl(context);
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
        }
        view.showResults(list);
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showError();
    }

}
