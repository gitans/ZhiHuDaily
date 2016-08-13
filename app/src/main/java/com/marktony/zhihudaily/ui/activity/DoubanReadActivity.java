package com.marktony.zhihudaily.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.VolleySingleton;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.ThemeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lizhaotailang on 2016/8/12.
 */

public class DoubanReadActivity extends AppCompatActivity {

    private WebView webView;
    private FloatingActionButton fab;
    private ImageView imageView;
    private CollapsingToolbarLayout toolbarLayout;

    private AlertDialog dialog;

    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (ThemeHelper.getThemeState(DoubanReadActivity.this) == 0){
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal_read_layout);

        initViews();

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);

        dialog = new AlertDialog.Builder(DoubanReadActivity.this).create();
        dialog.setView(getLayoutInflater().inflate(R.layout.loading_layout,null));
        dialog.show();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        setCollapsingToolbarLayoutTitle(intent.getStringExtra("title"));
        if (intent.getStringExtra("image") != null){
            Glide.with(DoubanReadActivity.this)
                    .load(intent.getStringExtra("image"))
                    .asBitmap()
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.no_img);
        }


        //能够和js交互
        webView.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        webView.getSettings().setBuiltInZoomControls(false);
        //缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        webView.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        webView.getSettings().setAppCacheEnabled(false);
        //不调用第三方浏览器即可进行页面反应
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return sp.getBoolean("in_app_browser",false);
            }

        });

        // 设置是否加载图片，true不加载，false加载图片sp.getBoolean("no_picture_mode",false)
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.DOUBAN_ARTICLE_DETAIL + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if ( !jsonObject.isNull("content")){
                    String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_moment_init.css\" type=\"text/css\">"
                            + "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_moment.css\" type=\"text/css\">";
                    String parseTheme = null;
                    if (ThemeHelper.getThemeState(DoubanReadActivity.this) == 0){
                        parseTheme = "<body>\n";
                    } else {
                        parseTheme = "<body style=\"background-color:#212b30;color:#696e74\">\n";
                    }

                    try {

                        String content = jsonObject.getString("content");

                        JSONArray images = jsonObject.getJSONArray("photos");

                        // 这是谁设计的api，你站出来，我保证不打死你。。。
                        for (int i = 0; i < images.length(); i++){
                            JSONObject o = images.getJSONObject(i);
                            String tag = o.getString("tag_name");
                            String old = "<img id=\"" + tag + "\" />";
                            String newStr = "<img id=\"" + tag + "\" " + "src=\"" + o.getJSONObject("medium").getString("url") + "\"/>";
                            content = content.replace(old, newStr);
                        }

                        String html = "<!DOCTYPE html>\n"
                                + "<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                                + "<head>\n"
                                + "\t<meta charset=\"utf-8\" />"
                                + css
                                + "\n</head>\n"
                                + parseTheme
                                + "<div class=\"container bs-docs-container\">\n" +
                                "            <div class=\"post-container\">\n" +
                                content +
                                "            </div>\n" +
                                "        </div>"
                                + "</body></html>";

                        webView.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                if (dialog.isShowing()){
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(fab, R.string.wrong_process,Snackbar.LENGTH_SHORT).show();

                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

        VolleySingleton.getVolleySingleton(DoubanReadActivity.this).addToRequestQueue(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.web_view);
        webView.setScrollbarFadingEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.image_view);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

    }

    // to change the title's font size of toolbar layout
    private void setCollapsingToolbarLayoutTitle(String title) {
        toolbarLayout.setTitle(title);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        toolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        toolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }

}
