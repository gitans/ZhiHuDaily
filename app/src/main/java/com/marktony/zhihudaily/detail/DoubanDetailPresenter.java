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
import com.marktony.zhihudaily.bean.DoubanMomentArticle;
import com.marktony.zhihudaily.bean.DoubanMomentNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.customtabs.CustomFallback;
import com.marktony.zhihudaily.customtabs.CustomTabActivityHelper;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.Theme;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class DoubanDetailPresenter
        implements DoubanDetailContract.Presenter, OnStringListener {

    private DoubanDetailContract.View view;
    private AppCompatActivity activity;
    private StringModelImpl model;

    private SharedPreferences sp;

    private int id;
    private DoubanMomentArticle post;

    public DoubanDetailPresenter(AppCompatActivity activity, DoubanDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
        sp = activity.getSharedPreferences("user_settings",MODE_PRIVATE);
        model = new StringModelImpl(activity);
    }

    @Override
    public void setArticleId(int id) {
        this.id = id;
    }

    @Override
    public void shareTo() {
        if (post.getShort_url() != null){
            try {
                Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                String shareText = post.getTitle()
                        + " "
                        + post.getShort_url()
                        + activity.getString(R.string.share_extra);

                shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                activity.startActivity(Intent.createChooser(shareIntent,activity.getString(R.string.share_to)));
            } catch (android.content.ActivityNotFoundException ex){
                view.showLoadError();
            }
        } else {
            view.showShareError();
        }
    }

    @Override
    public void openInBrowser() {
        if (post.getShort_url() != null){
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(post.getShort_url())));
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
    public void loadResult(int id) {
        view.showLoading();
        model.load(Api.DOUBAN_ARTICLE_DETAIL + id, this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        post = gson.fromJson(result, DoubanMomentArticle.class);
        view.showResult(convertContent());
        String imageUrl = null;
        if (post.getPhotos().size() != 0) {
            imageUrl = post.getThumbs().get(0).getMedium().getUrl();
        }
        view.showMainImage(imageUrl);
        view.setTitle(post.getTitle());
        view.setWebViewImageMode(sp.getBoolean("no_picture_mode",false));
        view.setUseInnerBrowser(sp.getBoolean("in_app_browser",true));
        view.stopLoading();
    }

    @Override
    public void onError(VolleyError error) {
        view.stopLoading();
        view.showLoadError();
    }

    private String convertContent() {
        if (post.getContent() == null) {
            return null;
        }
        String css;
        if (App.getThemeValue() == Theme.DAY_THEME){
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_light.css\" type=\"text/css\">";
        } else {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_dark.css\" type=\"text/css\">";
        }
        String content = post.getContent();
        ArrayList<DoubanMomentNews.posts.thumbs> imageList = post.getPhotos();
        for (int i = 0; i < imageList.size(); i++) {
            String old = "<img id=\"" + imageList.get(i).getTag_name() + "\" />";
            String newStr = "<img id=\"" + imageList.get(i).getTag_name() + "\" "
                    + "src=\"" + imageList.get(i).getMedium().getUrl() + "\"/>";
            content = content.replace(old, newStr);
        }
        StringBuilder builder = new StringBuilder();
        builder.append( "<!DOCTYPE html>\n");
        builder.append("<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        builder.append("<head>\n<meta charset=\"utf-8\" />\n");
        builder.append(css);
        builder.append("\n</head>\n<body>\n");
        builder.append("<div class=\"container bs-docs-container\">\n");
        builder.append("<div class=\"post-container\">\n");
        builder.append(content);
        builder.append("</div>\n</div>\n</body>\n</html>");

        return builder.toString();
    }

}
