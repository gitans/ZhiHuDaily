package com.marktony.zhihudaily.home;

import com.marktony.zhihudaily.interfaces.BasePresenter;
import com.marktony.zhihudaily.interfaces.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public interface GuokrContract {

    interface View extends BaseView<Presenter> {

        void showError();

        void showSuccess();

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter{

        void setUrl(String url);

        void startReading(int position);

    }

}
