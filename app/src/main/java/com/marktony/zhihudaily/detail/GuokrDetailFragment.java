package com.marktony.zhihudaily.detail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class GuokrDetailFragment extends Fragment
        implements GuokrDetailContract.View {

    private WebView webView;
    private ImageView imageView;
    private FloatingActionButton fab;
    private CollapsingToolbarLayout toolbarLayout;
    private AlertDialog dialog;

    private GuokrDetailContract.Presenter presenter;

    public GuokrDetailFragment() {

    }

    public static GuokrDetailFragment newInstance() {
        return new GuokrDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        presenter.setTitle(intent.getStringExtra("title"));
        presenter.setId(intent.getIntExtra("id", 0));
        presenter.setImageUrl(intent.getStringExtra("headlineImageUrl"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_read_layout, container, false);

        initViews(view);
        setHasOptionsMenu(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.share();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_read, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            getActivity().onBackPressed();
        } else if (id == R.id.action_open_in_browser) {
            presenter.openInBrowser();
        } else if (id == R.id.action_copy_text) {
            presenter.copyText();
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        presenter.reLoad();
    }

    @Override
    public void setPresenter(GuokrDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

        dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setView(getActivity().getLayoutInflater().inflate(R.layout.loading_layout,null));

        AppCompatActivity activity = (GuokrDetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        webView = (WebView) view.findViewById(R.id.web_view);

        WebSettings settings = webView.getSettings();
        // 能够和js交互
        settings.setJavaScriptEnabled(true);
        // 缩放,设置为不能缩放可以防止页面上出现放大和缩小的图标
        settings.setBuiltInZoomControls(true);
        // 缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启DOM storage API功能
        settings.setDomStorageEnabled(true);
        // 开启application Cache功能
        settings.setAppCacheEnabled(false);

        // 设置为不使用本webview进行反应
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                presenter.openUrl(webView, url);
                return true;
            }

        });

    }

    @Override
    public void showLoading() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void stopLoading() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showLoadError() {
        Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        presenter.reLoad();
                    }
                })
                .show();
    }

    @Override
    public void showShareError() {
        Snackbar.make(fab,R.string.share_error,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showResult(String result) {
        webView.loadDataWithBaseURL("x-data://base",result,"text/html","utf-8",null);
    }

    @Override
    public void setTitle(String title) {
        setCollapsingToolbarLayoutTitle(title);
    }

    @Override
    public void showMainImage(String imageUrl) {
        Glide.with(getActivity())
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void setUsingLocalImage() {
        imageView.setImageResource(R.drawable.no_img);
    }

    @Override
    public void setWebViewImageMode(boolean showImage) {
        // 设置是否加载图片，true不加载，false加载图片sp.getBoolean("no_picture_mode",false)
        webView.getSettings().setBlockNetworkImage(showImage);
    }

    @Override
    public void showBrowserNotFoundError() {
        Snackbar.make(fab, R.string.no_browser_found,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showTextCopied() {
        Snackbar.make(fab, R.string.text_copied, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showCopyTextError() {
        Snackbar.make(fab, R.string.text_copied_error, Snackbar.LENGTH_SHORT).show();
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
