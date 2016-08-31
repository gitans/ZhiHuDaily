package com.marktony.zhihudaily.inner_browser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marktony.zhihudaily.R;

/**
 * Created by Lizhaotailang on 2016/8/30.
 */

public class InnerBrowserFragment extends Fragment {

    private ProgressBar progressBar;
    private WebView webView;
    private Toolbar toolbar;
    private TextView textView;
    private ImageView imageView;
    private String url;

    public InnerBrowserFragment() {

    }

    public static InnerBrowserFragment getInstance() {
        return new InnerBrowserFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getActivity().getIntent().getStringExtra("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inner_browser, container, false);

        initViews(view);
        initWebViewSettings(webView);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setMax(100);
                progressBar.setProgress(newProgress);
                toolbar.setTitle(webView.getTitle());
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.setVisibility(View.GONE);
                // TODO: 2016/8/31 添加imageview 的监听事件
                imageView.setVisibility(View.VISIBLE);
                // TODO: 2016/8/31 text view 内容需要添加到string文件中，需要添加英文版 
                textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // webView.loadUrl(url);
            }
        });

        return view;
    }

    private void initViews(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        webView = (WebView) view.findViewById(R.id.web_view);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView = (TextView) view.findViewById(R.id.text_view);
        imageView = (ImageView) view.findViewById(R.id.image_view);
    }
    
    private void initWebViewSettings(WebView webView) {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 2016/8/31 调用返回事件失败 
        if (item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
