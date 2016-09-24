package com.marktony.zhihudaily.about;

import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/4.
 * This specifies the contract between the view and the presenter.
 */

public interface AboutContract {

    interface View extends BaseView<Presenter>{

        void showRateError();

        void showFeedbackError();

        void showBrowserNotFoundError();

    }

    interface Presenter extends BasePresenter {

        void rate();

        void openLicense();

        void followOnGithub();

        void followOnZhihu();

        void feedback();

        void donate();

        void showEasterEgg();

    }

}
