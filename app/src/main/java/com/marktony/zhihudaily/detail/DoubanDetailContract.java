package com.marktony.zhihudaily.detail;

import android.webkit.WebView;

import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public interface DoubanDetailContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void stopLoading();

        void setTitle(String title);

        void showResult(String result);

        void showLoadError();

        void showShareError();

        void showMainImage(String imageUrl);

        void setMainImageResurce();

        void setWebViewImageMode(boolean showImage);

        void setUseInnerBrowser(boolean use);

    }

    interface Presenter extends BasePresenter {

        void loadResult(int id);

        void setArticleId(int id);

        void shareTo();

        void openInBrowser();

        void openUrl(WebView webView, String url);

    }

}
