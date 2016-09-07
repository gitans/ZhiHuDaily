package com.marktony.zhihudaily.settings;

import android.support.v7.preference.Preference;

import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/5.
 */

public interface SettingsContract {

    interface View extends BaseView<Presenter> {

        void showCleanGlideCacheDone();

    }

    interface Presenter extends BasePresenter {

        void setNoPictureMode(Preference preference);

        void setInAppBrowser(Preference preference);

        void cleanGlideCache();

        void setNavigationBarTint(Preference preference);

    }

}
