package com.marktony.zhihudaily.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.customtabs.CustomFallback;
import com.marktony.zhihudaily.customtabs.CustomTabActivityHelper;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.Theme;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class GuokrDetailPresenter implements GuokrDetailContract.Presenter, OnStringListener {

    private GuokrDetailContract.View view;
    private AppCompatActivity activity;
    private StringModelImpl model;

    private int id;
    private String title;
    private String imageUrl;

    private SharedPreferences sp;

    public GuokrDetailPresenter(AppCompatActivity activity, GuokrDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(activity);
        sp = activity.getSharedPreferences("user_settings",MODE_PRIVATE);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadDataFromNet(int id) {
        view.showLoading();
        model.load(Api.GUOKR_ARTICLE_LINK_V2 + id, this);
    }

    @Override
    public void loadDataFromDB(int id) {

    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setImageUrl(String url) {
        this.imageUrl = url;
    }

    @Override
    public void share() {
        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = title + " " +  Api.GUOKR_ARTICLE_LINK_V1 + id + activity.getString(R.string.share_extra);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            activity.startActivity(Intent.createChooser(shareIntent,activity.getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex){
            view.showShareError();
        }
    }

    @Override
    public void openInBrowser() {
        if (id == 0) {
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Api.GUOKR_ARTICLE_LINK_V2 + id)));
            } catch (android.content.ActivityNotFoundException ex){
                view.showLoadError();
            }
        } else {
            view.showLoadError();
        }
    }

    @Override
    public void openUrl(WebView webView, String url) {
        CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(activity.getResources().getColor(R.color.colorPrimaryDark))
                .setShowTitle(true);
        CustomTabActivityHelper.openCustomTab(
                activity,
                customTabsIntent.build(),
                Uri.parse(url),
                new CustomFallback() {
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        super.openUri(activity, uri);
                    }
                }
        );
    }

    @Override
    public void onSuccess(String result) {
        if (App.getThemeValue() == Theme.NIGHT_THEME){
            result = result.replace("<div class=\"article \" id=\"contentMain\">", "<div class=\"article \" id=\"contentMain\" style=\"background-color:#212b30; color:#878787\">");
            result = result.replace(" <div class=\"content clearfix\" id=\"articleContent\">", " <div class=\"content clearfix\" id=\"articleContent\"> style=\"background-color:#212b30\"");
        }
        view.setUseInnerBrowser(sp.getBoolean("in_app_browser",true));
        view.setWebViewImageMode(sp.getBoolean("no_picture_mode",false));
        view.showResult(result);
        view.showMainImage(imageUrl);
        view.setTitle(title);
        view.stopLoading();
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showLoadError();
    }

}
