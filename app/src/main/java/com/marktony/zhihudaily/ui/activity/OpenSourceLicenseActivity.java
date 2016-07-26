package com.marktony.zhihudaily.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.util.UtilFunctions;

public class OpenSourceLicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UtilFunctions.setTheme(OpenSourceLicenseActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_license);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/license.html");

    }
}
