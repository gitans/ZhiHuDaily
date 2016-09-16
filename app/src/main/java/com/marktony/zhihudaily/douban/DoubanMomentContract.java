package com.marktony.zhihudaily.douban;

import com.marktony.zhihudaily.bean.DoubanMomentPost;
import com.marktony.zhihudaily.interfaces.BasePresenter;
import com.marktony.zhihudaily.interfaces.BaseView;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/10.
 */

public interface DoubanMomentContract {

    interface View extends BaseView<Presenter> {

        void startLoading();

        void stopLoading();

        void showLoadError();

        void showResults(ArrayList<DoubanMomentPost.posts> list);

    }

    interface Presenter extends BasePresenter {

        void startReading(int position);

        void loadPosts(long date);

        void setDate(int year, int month, int day);

        void refresh();

    }

}
