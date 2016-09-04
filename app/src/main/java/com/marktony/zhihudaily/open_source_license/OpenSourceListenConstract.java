package com.marktony.zhihudaily.open_source_license;

import android.webkit.WebView;

import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/3.
 */

public interface OpenSourceListenConstract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void showLicense(WebView webView);
    }

}
