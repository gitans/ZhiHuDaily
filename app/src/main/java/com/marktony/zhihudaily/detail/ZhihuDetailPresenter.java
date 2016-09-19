package com.marktony.zhihudaily.detail;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.bean.ZhihuDailyStory;
import com.marktony.zhihudaily.customtabs.CustomFallback;
import com.marktony.zhihudaily.customtabs.CustomTabActivityHelper;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.Theme;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class ZhihuDetailPresenter implements ZhihuDetailContract.Presenter, OnStringListener {

    private ZhihuDetailContract.View view;
    private AppCompatActivity activity;
    private StringModelImpl model;
    private ZhihuDailyStory story;
    private int id;

    private SharedPreferences sp;

    public ZhihuDetailPresenter(AppCompatActivity activity, ZhihuDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
        this.model = new StringModelImpl(activity);
        sp = activity.getSharedPreferences("user_settings",MODE_PRIVATE);
    }

    @Override
    public void start() {

    }

    @Override
    public void openInBrowser() {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(story.getShare_url())));
        } catch (android.content.ActivityNotFoundException ex){
            view.showLoadError();
        }
    }

    @Override
    public void share() {
        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = story.getTitle() + " " +  story.getShare_url() + "\t\t\t" + activity.getString(R.string.share_extra);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex){
            view.showShareError();
        }
    }

    @Override
    public void requestData() {
        model.load(Api.ZHIHU_NEWS + id, this);
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void openUrl(WebView webView, String url) {
        CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(activity.getResources().getColor(R.color.colorAccent))
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
        Gson gson = new Gson();
        story = gson.fromJson(result, ZhihuDailyStory.class);
        if (story.getBody() == null) {
            view.showResultWithoutBody(story.getShare_url());
            view.setMainImageRes();
        } else {
            view.showResult(convertResult(story.getBody()));
            view.showMainImage(story.getImage());
            view.setMainImageSource(story.getImage_source());
            view.useInnerBrowser(sp.getBoolean("in_app_browser",true));
            view.setImageMode(sp.getBoolean("no_picture_mode",false));
            view.setTitle(story.getTitle());
        }
        view.stopLoading();
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showLoadError();
    }

    private String convertResult(String preReuslt) {

        preReuslt = preReuslt.replace("<div class=\"img-place-holder\">", "");
        preReuslt = preReuslt.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // in fact,in api,css addresses are given as an array
        // api中还有js的部分，这里不再解析js
        // javascript is included,but here I don't use it
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        // use the css file from local assets folder,not from network
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        // 根据主题的不同确定不同的加载内容
        // load content judging by different theme
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if (App.getThemeValue() == Theme.NIGHT_THEME){
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }

        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preReuslt)
                .append("</body></html>").toString();

    }

}
