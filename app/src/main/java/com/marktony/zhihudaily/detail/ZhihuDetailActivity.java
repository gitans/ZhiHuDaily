package com.marktony.zhihudaily.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.innerbrowser.InnerBrowserActivity;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.NetworkState;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.marktony.zhihudaily.util.Theme;

import org.json.JSONException;
import org.json.JSONObject;


public class ZhihuDetailActivity extends AppCompatActivity {

    private WebView webViewRead;
    private FloatingActionButton fab;
    private ImageView ivFirstImg;
    private TextView tvCopyRight;
    private CollapsingToolbarLayout toolbarLayout;

    private RequestQueue queue;

    private AlertDialog dialog;

    private String shareUrl = null;
    private int id;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.universal_read_layout);

        initViews();

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);

        dialog = new AlertDialog.Builder(ZhihuDetailActivity.this).create();
        dialog.setView(getLayoutInflater().inflate(R.layout.loading_layout,null));
        dialog.show();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        final String title = intent.getStringExtra("title");
        final String image = intent.getStringExtra("image");

        setCollapsingToolbarLayoutTitle(title);

        queue = Volley.newRequestQueue(getApplicationContext());

        //能够和js交互
        webViewRead.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        webViewRead.getSettings().setBuiltInZoomControls(false);
        //缓存
        webViewRead.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启DOM storage API功能
        webViewRead.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        webViewRead.getSettings().setAppCacheEnabled(false);

        if (sp.getBoolean("in_app_browser",true)){

            //不调用第三方浏览器即可进行页面反应
            webViewRead.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    startActivity(new Intent(ZhihuDetailActivity.this, InnerBrowserActivity.class).putExtra("url", url));
                    return true;
                }

            });

            // 设置在本WebView内可以通过按下返回上一个html页面
            webViewRead.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN){
                        if (keyCode == KeyEvent.KEYCODE_BACK && webViewRead.canGoBack()){
                            webViewRead.goBack();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }

        // 设置是否加载图片，true不加载，false加载图片
        webViewRead.getSettings().setBlockNetworkImage(sp.getBoolean("no_picture_mode",false));

        // 如果当前没有网络连接，则加载缓存中的内容
        if ( !NetworkState.networkConnected(ZhihuDetailActivity.this)){

            ivFirstImg.setImageResource(R.drawable.no_img);
            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            String content = loadContentFromDB("" + id).replace("<div class=\"img-place-holder\">", "");
            content = content.replace("<div class=\"headline\">", "");

            String theme = "<body className=\"\" onload=\"onLoaded()\">";
            if (App.getThemeValue() == Theme.NIGHT_THEME){
                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
            }

            if (loadContentFromDB("" + id) == null || loadContentFromDB("" + id).isEmpty()){
                Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
            } else {
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

                String html = "<!DOCTYPE html>\n"
                        + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                        + "<head>\n"
                        + "\t<meta charset=\"utf-8\" />"
                        + css
                        + "\n</head>\n"
                        + theme
                        + content
                        + "</body></html>";

                webViewRead.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);
            }

            if (dialog.isShowing()){
                dialog.dismiss();
            }

        } else {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.ZHIHU_NEWS + id, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {

                        // 需要注意的是这里有可能没有body。。。 好多坑。。。
                        // attention here, it may not contain 'body'
                        // 如果没有body，则加载share_url中内容
                        // if 'body' is null, load the content of share_url
                        if (jsonObject.isNull("body")){

                            webViewRead.loadUrl(jsonObject.getString("share_url"));
                            ivFirstImg.setImageResource(R.drawable.no_img);
                            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            // 在body为null的情况下，share_url的值是依然存在的
                            // though 'body' is null, share_url exists.
                            shareUrl = jsonObject.getString("share_url");

                        } else {  // body不为null  body is not null

                            shareUrl = jsonObject.getString("share_url");

                            if ( !jsonObject.isNull("image")){

                                Glide.with(ZhihuDetailActivity.this).load(jsonObject.getString("image")).centerCrop().into(ivFirstImg);

                                tvCopyRight.setText(jsonObject.getString("image_source"));

                            } else if (image == null){

                                ivFirstImg.setImageResource(R.drawable.no_img);
                                ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            } else {

                                Glide.with(ZhihuDetailActivity.this).load(image).centerCrop().into(ivFirstImg);

                            }

                            // 在api中，css的地址是以一个数组的形式给出，这里需要设置
                            // in fact,in api,css addresses are given as an array
                            // api中还有js的部分，这里不再解析js
                            // javascript is included,but here I don't use it
                            // 不再选择加载网络css，而是加载本地assets文件夹中的css
                            // use the css file from local assets folder,not from network
                            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

                            // body中替换掉img-place-holder div
                            // replace the img-place-holder with an empty value in body
                            // 可以去除网页中div所占的区域
                            // to avoid the div occupy the area
                            // 如果没有去除这个div，那么整个网页的头部将会出现一部分的空白区域
                            // if we don't delete the div, the web page will have a blank area

                            String content = jsonObject.getString("body").replace("<div class=\"img-place-holder\">", "");
                            // div headline占据了一段高度，需要手动去除
                            // delete the headline div
                            content = content.replace("<div class=\"headline\">", "");

                            // 根据主题的不同确定不同的加载内容
                            // load content judging by different theme
                            String theme = "<body className=\"\" onload=\"onLoaded()\">";
                            if (App.getThemeValue() == Theme.NIGHT_THEME){
                                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
                            }

                            String html = "<!DOCTYPE html>\n"
                                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                                    + "<head>\n"
                                    + "\t<meta charset=\"utf-8\" />"
                                    + css
                                    + "\n</head>\n"
                                    + theme
                                    + content
                                    + "</body></html>";

                            webViewRead.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();

                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            });

            queue.add(request);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                    String shareText = title + " " +  shareUrl + getString(R.string.share_extra);
                    shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                    startActivity(Intent.createChooser(shareIntent,getString(R.string.share_to)));
                } catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 通过监听toolbar中MenuItem的id值来确定是否为返回箭头被点击
        // 如果是，则直接调用onBackPressed方法
        // 废除 返回在Manifest文件中声明的ParentActivity
        // 这样写的好处是不用在返回MainActivity之后重新new MainActivity
        // 重新做网络请求之类的
        if (id == android.R.id.home){
            onBackPressed();
        }

        if (id == R.id.action_open_in_browser){
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Api.ZHIHU_DAILY_BASE_URL + this.id)));
        }

        return super.onOptionsItemSelected(item);
    }

    // 初始化view
    private void initViews() {

        webViewRead = (WebView) findViewById(R.id.web_view);
        webViewRead.setScrollbarFadingEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivFirstImg = (ImageView) findViewById(R.id.image_view);
        tvCopyRight = (TextView) findViewById(R.id.text_view);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

    }

    /**
     * 离线时从数据库中获取body内容块
     * @param id 所要获取的字符串块对应的id
     * @return 内容
     */
    private String loadContentFromDB(String id){

        String content = null;
        DatabaseHelper dbHelper = new DatabaseHelper(ZhihuDetailActivity.this,"History.db",null,3);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Contents",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (cursor.getString(cursor.getColumnIndex("id")).equals(id)){
                    content = cursor.getString(cursor.getColumnIndex("content"));
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        return content;
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