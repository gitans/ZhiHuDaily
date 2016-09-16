package com.marktony.zhihudaily.home;

import android.content.Context;
import android.content.Intent;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.bean.GuokrHandpickPost;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.ui.activity.GuokrReadActivity;
import com.marktony.zhihudaily.util.Api;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public class GuokrPresenter implements GuokrContract.Presenter, OnStringListener {

    private GuokrContract.View view;
    private Context context;
    private StringModelImpl model;

    private ArrayList<GuokrHandpickPost.result> list = new ArrayList<GuokrHandpickPost.result>();

    public GuokrPresenter(Context context, GuokrContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        model = new StringModelImpl(context);
    }

    @Override
    public void startReading(int position) {
        GuokrHandpickPost.result item = list.get(position);
        context.startActivity(new Intent(context, GuokrReadActivity.class)
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
        GuokrHandpickPost question = gson.fromJson(result, GuokrHandpickPost.class);
        for (GuokrHandpickPost.result re : question.getResult()){
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
