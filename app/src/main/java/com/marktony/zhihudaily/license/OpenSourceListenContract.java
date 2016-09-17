package com.marktony.zhihudaily.license;

import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/3.
 * This specifies the contract between the view and the presenter.
 */

public interface OpenSourceListenContract {

    interface View extends BaseView<Presenter> {

        void loadLicense(String path);

    }

    interface Presenter extends BasePresenter {

        void showLicense();

    }

}
