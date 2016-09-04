package com.marktony.zhihudaily.open_source_license;


/**
 * Created by Lizhaotailang on 2016/9/3.
 */

public class OpenSourceLicensePresenter implements OpenSourceListenContract.Presenter {

    private OpenSourceListenContract.View view;
    private String filePath;

    public OpenSourceLicensePresenter(OpenSourceListenContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void showLicense() {
        view.loadLicense(filePath);
    }

    @Override
    public void start() {
        filePath = "file:///android_asset/license.html";
        showLicense();
    }

}
