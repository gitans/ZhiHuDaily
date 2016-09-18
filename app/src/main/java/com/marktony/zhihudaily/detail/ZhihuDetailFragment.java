package com.marktony.zhihudaily.detail;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;

/**
 * Created by Lizhaotailang on 2016/9/17.
 */

public class ZhihuDetailFragment extends Fragment
        implements ZhihuDetailContract.View {

    private ImageView imageView;
    private WebView webView;
    private FloatingActionButton fab;
    private TextView textView;
    private CollapsingToolbarLayout toolbarLayout;

    private AlertDialog dialog;

    public ZhihuDetailFragment() {}

    public static ZhihuDetailFragment newInstance() {
        return new ZhihuDetailFragment();
    }

    private ZhihuDetailContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setId(getActivity().getIntent().getIntExtra("id", 0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.universal_read_layout, container, false);

        initViews(view);
        setHasOptionsMenu(true);

        presenter.requestData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.share();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_read, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_open_in_browser) {
            presenter.openInBrowser();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(ZhihuDetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

        dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setView(getActivity().getLayoutInflater().inflate(R.layout.loading_layout,null));

        webView = (WebView) view.findViewById(R.id.web_view);
        webView.setScrollbarFadingEnabled(true);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        AppCompatActivity activity = (ZhihuDetailActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        textView = (TextView) view.findViewById(R.id.text_view);
        toolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);

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
        Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showShareError() {
        Snackbar.make(fab,R.string.share_error,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showResult(String result) {
        webView.loadDataWithBaseURL("x-data://base",result,"text/html","utf-8",null);
    }

    // if the result not contain the 'body', call this function to load another url
    @Override
    public void showResultWithoutBody(String url) {
        webView.loadUrl(url);
    }

    @Override
    public void showMainImage(String url) {
        Glide.with(getActivity())
                .load(url)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public void setMainImageRes() {
        imageView.setImageResource(R.drawable.no_img);
    }

    @Override
    public void setTitle(String title) {
        setCollapsingToolbarLayoutTitle(title);
    }

    @Override
    public void setMainImageSource(String source) {
        textView.setText(source);
    }

    @Override
    public void setImageMode(boolean showImage) {
        webView.getSettings().setBlockNetworkImage(showImage);
    }

    @Override
    public void useInnerBrowser(final boolean use) {
        //不调用第三方浏览器即可进行页面反应
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                presenter.openUrl(view, url);
                return use;
            }

        });
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
