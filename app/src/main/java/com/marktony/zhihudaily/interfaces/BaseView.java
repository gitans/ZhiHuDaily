package com.marktony.zhihudaily.interfaces;

import android.view.View;

/**
 * Created by Lizhaotailang on 2016/9/3.
 */

public interface BaseView<T> {

    /**
     * set the presenter of mvp
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * init the views of fragment
     * @param view
     */
    void initViews(View view);

}
