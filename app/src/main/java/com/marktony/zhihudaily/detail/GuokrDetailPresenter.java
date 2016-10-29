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
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.customtabs.CustomFallback;
import com.marktony.zhihudaily.customtabs.CustomTabActivityHelper;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.interfaze.OnStringListener;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.NetworkState;
import com.marktony.zhihudaily.util.Theme;

import static android.content.Context.CLIPBOARD_SERVICE;
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

    private String post;

    public GuokrDetailPresenter(AppCompatActivity activity, GuokrDetailContract.View view) {
        this.activity = activity;
        this.view = view;
        this.view.setPresenter(this);
        model = new StringModelImpl(activity);
        sp = activity.getSharedPreferences("user_settings",MODE_PRIVATE);
    }

    @Override
    public void start() {
        loadData(id);
    }

    @Override
    public void loadData(int id) {
        view.showLoading();
        if (NetworkState.networkConnected(activity)) {
            model.load(Api.GUOKR_ARTICLE_LINK_V2 + id, this);
        } else {
            Cursor cursor = new DatabaseHelper(activity, "History.db", null, 4).getReadableDatabase()
                    .query("Guokr", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(cursor.getColumnIndex("guokr_id")) == id) {
                        post = cursor.getString(cursor.getColumnIndex("guokr_content"));
                        onSuccess(post);
                        break;
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
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
            String shareText = title + " " +  Api.GUOKR_ARTICLE_LINK_V1 + id + "\t\t\t" + activity.getString(R.string.share_extra);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            activity.startActivity(Intent.createChooser(shareIntent,activity.getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex){
            view.showShareError();
        }
    }

    @Override
    public void openInBrowser() {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Api.GUOKR_ARTICLE_LINK_V2 + id)));
        } catch (android.content.ActivityNotFoundException ex){
            view.showLoadError();
        }
    }

    @Override
    public void openUrl(WebView webView, String url) {

        if (sp.getBoolean("in_app_browser",true)) {

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
        } else {

            try{
                activity.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            } catch (android.content.ActivityNotFoundException ex){
                view.showBrowserNotFoundError();
            }

        }

    }

    @Override
    public void reload() {
        loadData(id);
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
            clipData = ClipData.newPlainText("text", Html.fromHtml(post, Html.FROM_HTML_MODE_LEGACY).toString());
        } else {
            clipData = ClipData.newPlainText("text", Html.fromHtml(post).toString());
        }
        manager.setPrimaryClip(clipData);
        view.showTextCopied();
    }

    @Override
    public void onSuccess(String result) {
        this.post = result;
        if (App.getThemeValue() == Theme.NIGHT_THEME){
            post = post.replace("<div class=\"article \" id=\"contentMain\">", "<div class=\"article \" id=\"contentMain\" style=\"background-color:#212b30; color:#878787\">");
            post = post.replace("<div class=\"content clearfix\" id=\"articleContent\">", " <div class=\"content clearfix\" id=\"articleContent\" style=\"background-color:#212b30\">");
        }
        view.setWebViewImageMode(sp.getBoolean("no_picture_mode",false));
        view.showResult(post);
        view.showMainImage(imageUrl);
        view.setTitle(title);
        view.stopLoading();
    }

    @Override
    public void onError(VolleyError error) {
        view.setUsingLocalImage();
        view.stopLoading();
        view.showLoadError();
    }

}
