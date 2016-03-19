package com.marktony.zhihudaily.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;

public class ReadActivity extends AppCompatActivity {

    private WebView webViewRead;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ImageView ivFirstImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        initViews();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String firstImg = intent.getStringExtra("firstImg");

        if (firstImg != null){
            Glide.with(ReadActivity.this).load(firstImg).centerCrop().into(ivFirstImg);
        }

        if (id != null){
            webViewRead.loadUrl(Api.NEWS + id);
        }
    }

    private void initViews() {

        webViewRead = (WebView) findViewById(R.id.wb_read);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivFirstImg = (ImageView) findViewById(R.id.head_img);

        webViewRead.getSettings().setJavaScriptEnabled(true);
        webViewRead.getSettings().setBuiltInZoomControls(true);

    }
}
