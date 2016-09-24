package com.marktony.zhihudaily.homepage;

import com.marktony.zhihudaily.bean.DoubanMomentNews;
import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/10.
 */

public interface DoubanMomentContract {

    interface View extends BaseView<Presenter> {

        void startLoading();

        void stopLoading();

        void showLoadError();

        void showResults(ArrayList<DoubanMomentNews.posts> list);

        void showNetworkError();

    }

    interface Presenter extends BasePresenter {

        void startReading(int position);

        void loadPosts(long date, boolean clearing);

        void refresh();

        void loadMore(long date);

        void goToSettings();

    }

}
