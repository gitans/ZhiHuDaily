package com.marktony.zhihudaily.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.utils.UtilFunctions;

public class FanfouPostDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String avatar;
    private String content;
    private String time;
    private String imgUrl;
    private String author;

    private TextView tvContent;
    private TextView tvTime;
    private ImageView ivAvatar;
    private ImageView ivMain;
    private TextView tvAuthor;

    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UtilFunctions.setTheme(FanfouPostDetailActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanfou_post_detail);

        initViews();

        dialog = new MaterialDialog.Builder(FanfouPostDetailActivity.this)
                .content(getString(R.string.loading))
                .progress(true,0)
                .build();

        Intent i = getIntent();
        avatar = i.getStringExtra("avatar");
        content = i.getStringExtra("content");
        time = i.getStringExtra("time");
        imgUrl = i.getStringExtra("imgUrl");
        author = i.getStringExtra("author");

        if (imgUrl.equals("") || imgUrl == null){

            ivMain.setVisibility(View.GONE);

        } else {

            dialog.show();
            Glide.with(FanfouPostDetailActivity.this)
                    .load(imgUrl)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            dialog.dismiss();

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            dialog.dismiss();

                            return false;
                        }
                    })
                    .into(ivMain);
        }

        Glide.with(FanfouPostDetailActivity.this).load(avatar).into(ivAvatar);
        tvAuthor.setText(author);
        tvContent.setText(content);
        tvTime.setText(time);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAuthor = (TextView) findViewById(R.id.tv_author);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvTime = (TextView) findViewById(R.id.tv_time);
        ivAvatar = (ImageView) findViewById(R.id.avatar);
        ivMain = (ImageView) findViewById(R.id.fanfou_iv_main);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
