package com.marktony.zhihudaily.detail;

import android.webkit.WebView;

import com.marktony.zhihudaily.BasePresenter;
import com.marktony.zhihudaily.BaseView;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public interface GuokrDetailContract {

    interface View extends BaseView<Presenter> {

        void showLoading();

        void stopLoading();

        void showLoadError();

        void showShareError();

        void showResult(String result);

        void  setTitle(String title);

        void showMainImage(String imageUrl);

        void setUsingLocalImage();

        void setWebViewImageMode(boolean showImage);

        void showBrowserNotFoundError();

        void showTextCopied();

        void showCopyTextError();

    }

    interface Presenter extends BasePresenter {

        void loadData(int id);

        void setId(int id);

        void setTitle(String title);

        void setImageUrl(String url);

        void share();

        void openInBrowser();

        void openUrl(WebView webView, String url);

        void reload();

        void copyText();

    }

}
