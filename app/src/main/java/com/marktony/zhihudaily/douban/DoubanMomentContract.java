package com.marktony.zhihudaily.douban;

import com.marktony.zhihudaily.interfaces.BasePresenter;
import com.marktony.zhihudaily.interfaces.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/10.
 */

public interface DoubanMomentContract {

    interface View extends BaseView<Presenter> {

        void startLoading();

        void stopLoading();

        void showLoadError();

    }

    interface Presenter extends BasePresenter {

        void showArticle(int id);

        void loadPosts(long date);

        void setDate(int year, int month, int day);

    }

}
