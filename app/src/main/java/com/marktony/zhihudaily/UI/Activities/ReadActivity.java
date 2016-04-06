package com.marktony.zhihudaily.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;

import org.json.JSONException;
import org.json.JSONObject;

public class ReadActivity extends AppCompatActivity {

    private WebView webViewRead;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ImageView ivFirstImg;

    private RequestQueue queue;

    private int likes = 0;
    private int comments = 0;

    private MaterialDialog dialog;

    private String shareUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        initViews();

        dialog = new MaterialDialog.Builder(ReadActivity.this)
                .content(getString(R.string.loading))
                .progress(true,0)
                .build();

        dialog.show();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        final String title = intent.getStringExtra("title");
        final String image = intent.getStringExtra("image");
        getSupportActionBar().setTitle(title);

        queue = Volley.newRequestQueue(getApplicationContext());

        //能够和js交互
        webViewRead.getSettings().setJavaScriptEnabled(true);
        //缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        webViewRead.getSettings().setBuiltInZoomControls(false);
        //缓存
        webViewRead.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //开启DOM storage API功能
        webViewRead.getSettings().setDomStorageEnabled(true);
        //开启application Cache功能
        webViewRead.getSettings().setAppCacheEnabled(true);
        //不调用第三方浏览器即可进行页面反应
        webViewRead.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webViewRead.loadUrl(url);
                return true;
            }
        });

        // 设置是否加载图片，true不加载，false加载图片
        webViewRead.getSettings().setBlockNetworkImage(true);

        Log.d("url",Api.NEWS + id);
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

                        // body不为null
                    } else {
                        if ( !jsonObject.isNull("image")){

                            Glide.with(ReadActivity.this).load(jsonObject.getString("image")).centerCrop().into(ivFirstImg);
                            shareUrl = jsonObject.getString("share_url");

                        } else if (image == null){

                            ivFirstImg.setImageResource(R.drawable.no_img);
                            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        } else {

                            Glide.with(ReadActivity.this).load(image).centerCrop().into(ivFirstImg);
                        }

                        //在api中，css的地址是以一个数组的形式给出，这里需要设置
                        //api中还有js的部分，这里不再解析js
                        String css = null;
                        if (jsonObject.getJSONArray("css").length() != 0){
                            for (int i = 0;i < jsonObject.getJSONArray("css").length();i++){
                                css = "<link type=\"text/css\" href=\"" +
                                        jsonObject.getJSONArray("css").getString(i) +
                                        "\" " +
                                        "rel=\"stylesheet\" />\n";
                            }
                        }
                        /**
                         * body中替换掉img-place-holder div
                         * 可以取出网页中div所占的区域
                         * 如果没有去除这个div，那么整个网页的头部将会出现一部分的空白区域
                         */
                        String html = "<!DOCTYPE html>\n" +
                                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                                "<head>\n" +
                                "\t<meta charset=\"utf-8\" />\n</head>\n" +
                                "<body>\n"  + css +
                                jsonObject.getString("body").replace("<div class=\"img-place-holder\">", "") + "\n<body>";
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                String shareText = title + shareUrl + getString(R.string.share_extra);
                shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
                startActivity(Intent.createChooser(shareIntent,getString(R.string.share_to)));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read,menu);
        return true;
    }

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

        if (id == R.id.action_comments){

        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        webViewRead = (WebView) findViewById(R.id.wb_read);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivFirstImg = (ImageView) findViewById(R.id.head_img);

    }
}
