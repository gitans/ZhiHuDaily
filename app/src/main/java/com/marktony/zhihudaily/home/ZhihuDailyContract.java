package com.marktony.zhihudaily.home;

import com.marktony.zhihudaily.interfaces.BasePresenter;
import com.marktony.zhihudaily.interfaces.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/16.
 */

public interface ZhihuDailyContract {

    interface View extends BaseView<Presenter> {

        void showError();

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter {

        void setUrl(String url);

        void refresh();

        void startReading(int position);

    }

}
