package com.marktony.zhihudaily.detail;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.bean.DoubanMomentStory;
import com.marktony.zhihudaily.bean.DoubanMomentNews;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.customtabs.CustomFallback;
import com.marktony.zhihudaily.customtabs.CustomTabActivityHelper;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.NetworkState;
import com.marktony.zhihudaily.util.Theme;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class DoubanDetailPresenter
        implements DoubanDetailContract.Presenter, OnStringListener {

    private DoubanDetailContract.View view;
    private AppCompatActivity activity;
    private StringModelImpl model;

    private int id;

    private SharedPreferences sp;

    private DoubanMomentStory post;

    public DoubanDetailPresenter(AppCompatActivity activity, DoubanDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
        sp = activity.getSharedPreferences("user_settings",MODE_PRIVATE);
        model = new StringModelImpl(activity);
    }

    @Override
    public void shareTo() {
        if (post.getShort_url() != null){
            try {
                Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                String shareText = post.getTitle()
                        + " "
                        + post.getShort_url()
                        + "\t\t\t"
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
    public void reLoad() {
        loadResult(id);
    }

    @Override
    public void copyText() {
        if (post == null) {
            view.showCopyTextError();
            return;
        }
        ClipboardManager manager = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = null;
        if (Build.VERSION.SDK_INT >= 24) {
            clipData = ClipData.newPlainText("text", Html.fromHtml(post.getContent(), Html.FROM_HTML_MODE_LEGACY).toString());
        } else {
            clipData = ClipData.newPlainText("text", Html.fromHtml(post.getContent()).toString());
        }
        manager.setPrimaryClip(clipData);
        view.showTextCopied();
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void loadResult(int id) {
        view.showLoading();
        if (NetworkState.networkConnected(activity)) {
            model.load(Api.DOUBAN_ARTICLE_DETAIL + id, this);
        } else {
            Cursor cursor = new DatabaseHelper(activity, "History.db", null, 4).getReadableDatabase()
                    .query("Douban", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(cursor.getColumnIndex("douban_id")) == id) {
                        Gson gson = new Gson();
                        post = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_content")), DoubanMomentStory.class);
                        view.showResult(convertContent());
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            view.setMainImageResurce();
            view.setTitle(post.getTitle());
            view.stopLoading();
        }
    }

    @Override
    public void start() {
        loadResult(id);
    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        post = gson.fromJson(result, DoubanMomentStory.class);
        view.showResult(convertContent());
        if (post.getPhotos().size() != 0) {
            view.showMainImage(post.getPhotos().get(0).getMedium().getUrl());
        } else {
            view.setMainImageResurce();
        }
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
