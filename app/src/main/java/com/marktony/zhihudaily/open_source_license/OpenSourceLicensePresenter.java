package com.marktony.zhihudaily.open_source_license;

import android.webkit.WebView;

/**
 * Created by Lizhaotailang on 2016/9/3.
 */

public class OpenSourceLicensePresenter implements OpenSourceListenConstract.Presenter {

    private OpenSourceListenConstract.View view;
    private String filePath;

    public OpenSourceLicensePresenter(OpenSourceListenConstract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void showLicense(WebView webView) {
        webView.loadUrl(filePath);
    }

    @Override
    public void start() {
        filePath = "file:///android_asset/open_source_license.html";
    }

}
