package com.marktony.zhihudaily.ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.utils.Api;
import com.marktony.zhihudaily.utils.NetworkState;
import com.marktony.zhihudaily.utils.UtilFunctions;
import com.marktony.zhihudaily.db.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;



public class ZhihuReadActivity extends AppCompatActivity {

    private WebView webViewRead;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ImageView ivFirstImg;
    private TextView tvCopyRight;
    private CollapsingToolbarLayout toolbarLayout;

    private RequestQueue queue;

    private int likes = 0;
    private int comments = 0;

    private MaterialDialog dialog;

    private String shareUrl = null;
    private String id;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        if (UtilFunctions.getThemeState(ZhihuReadActivity.this) == 0){
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_read);

        initViews();

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);

        dialog = new MaterialDialog.Builder(ZhihuReadActivity.this)
                .content(getString(R.string.loading))
                .progress(true,0)
                .build();

        dialog.show();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
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
        //不调用第三方浏览器即可进行页面反应
        webViewRead.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webViewRead.loadUrl(url);
                return true;
            }
        });
        // 设置是否加载图片，true不加载，false加载图片
        webViewRead.getSettings().setBlockNetworkImage(sp.getBoolean("no_picture_mode",false));

        // 如果当前没有网络连接，则加载缓存中的内容
        if ( !NetworkState.networkConneted(ZhihuReadActivity.this)){

            ivFirstImg.setImageResource(R.drawable.no_img);
            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            String parseByTheme = null;
            if (UtilFunctions.getThemeState(ZhihuReadActivity.this) == 0){
                parseByTheme = "<body>\n";
            } else {
                parseByTheme = "<body style=\"background-color:#212b30\">\n";
            }
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/master.css\" type=\"text/css\">";
            String html = "<!DOCTYPE html>\n"
                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "<head>\n"
                    + "\t<meta charset=\"utf-8\" />\n</head>\n"
                    + parseByTheme
                    + css
                    + loadContentFromDB(id).replace("<div class=\"img-place-holder\">", "")
                    + "\n<body>";

            webViewRead.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);

            dialog.dismiss();
        } else {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.NEWS + id, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {

                        //需要注意的是这里有可能没有body。。。 好多坑。。。
                        // 如果没有body，则加载share_url中内容
                        if (jsonObject.isNull("body")){

                            webViewRead.loadUrl(jsonObject.getString("share_url"));
                            ivFirstImg.setImageResource(R.drawable.no_img);
                            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            // 在body为null的情况下，share_url的值是依然存在的
                            shareUrl = jsonObject.getString("share_url");

                        } else {  // body不为null

                            shareUrl = jsonObject.getString("share_url");

                            if ( !jsonObject.isNull("image")){

                                Glide.with(ZhihuReadActivity.this).load(jsonObject.getString("image")).centerCrop().into(ivFirstImg);

                                tvCopyRight.setText(jsonObject.getString("image_source"));

                            } else if (image == null){

                                ivFirstImg.setImageResource(R.drawable.no_img);
                                ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            } else {

                                Glide.with(ZhihuReadActivity.this).load(image).centerCrop().into(ivFirstImg);

                            }

                            //在api中，css的地址是以一个数组的形式给出，这里需要设置
                            //api中还有js的部分，这里不再解析js
                            // 不再选择加载网络css，而是加载本地assets文件夹中的css
                            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/master.css\" type=\"text/css\">";

                            // body中替换掉img-place-holder div
                            // 可以去除网页中div所占的区域
                            // 如果没有去除这个div，那么整个网页的头部将会出现一部分的空白区域

                            String content = jsonObject.getString("body").replace("<div class=\"img-place-holder\">", "");
                            // div headline占据了一段高度，需要手动去除
                            content = content.replace("<div class=\"headline\">", "");

                            // 根据主题的不同确定不同的加载内容
                            String parseByTheme = null;
                            if (UtilFunctions.getThemeState(ZhihuReadActivity.this) == 0){
                                parseByTheme = "<body>\n";
                            } else {
                                parseByTheme = "<body style=\"background-color:#212b30\">\n";
                            }

                            String html = "<!DOCTYPE html>\n"
                                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                                    + "<head>\n"
                                    + "\t<meta charset=\"utf-8\" />\n</head>\n"
                                    + parseByTheme
                                    + css
                                    + content
                                    + "\n<body>";
                            webViewRead.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);

                            dialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    dialog.dismiss();
                    Snackbar.make(fab, R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
                }
            });

            queue.add(request);

            // 请求评论和赞的数量
            JsonObjectRequest re = new JsonObjectRequest(Request.Method.GET, Api.STORY_EXTRA + id, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    if (!jsonObject.isNull("comments")){
                        try {
                            likes = jsonObject.getInt("popularity");
                            comments = jsonObject.getInt("comments");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Snackbar.make(fab,R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Snackbar.make(fab,R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
                }
            });

            queue.add(re);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                String shareText = title + " " +  shareUrl + getString(R.string.share_extra);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                startActivity(Intent.createChooser(shareIntent,getString(R.string.share_to)));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zhihu_read,menu);
        return true;
    }

    // 在onPrepareOptionsMenu方法中更新数据
    // 如果是在create方法中更新，那么只会创建一次，可能获取不到最新的数据
    // 而在这个方法中去更新，可以保证每次都是最新的
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        String temp = getResources().getString(R.string.likes) + ":" + likes;
        menu.findItem(R.id.action_likes).setTitle(temp);
        temp = getResources().getString(R.string.comments) + ":" + comments;
        menu.findItem(R.id.action_comments).setTitle(temp);

        return super.onPrepareOptionsMenu(menu);
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

        if (id == R.id.action_comments){
            startActivity(new Intent(ZhihuReadActivity.this,CommentsActivity.class).putExtra("id",this.id));
        }
        return super.onOptionsItemSelected(item);
    }

    // 初始化view
    private void initViews() {

        webViewRead = (WebView) findViewById(R.id.wb_read);
        webViewRead.setScrollbarFadingEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivFirstImg = (ImageView) findViewById(R.id.head_img);
        tvCopyRight = (TextView) findViewById(R.id.tv_copyright);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

    }

    /**
     * 离线时从数据库中获取body内容块
     * @param id 所要获取的字符串块对应的id
     * @return 内容
     */
    private String loadContentFromDB(String id){

        String content = null;
        DatabaseHelper dbHelper = new DatabaseHelper(ZhihuReadActivity.this,"History.db",null,1);
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