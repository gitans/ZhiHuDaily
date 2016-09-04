package com.marktony.zhihudaily.open_source_license;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.marktony.zhihudaily.R;

/**
 * Created by Lizhaotailang on 2016/9/3.
 */

public class OpenSourceLicenseFragment extends Fragment
        implements OpenSourceListenContract.View{

    private OpenSourceListenContract.Presenter presenter;
    private WebView webView;

    public OpenSourceLicenseFragment() {
        // requires an empty constructor
    }

    public static OpenSourceLicenseFragment newInstance() {
        return new OpenSourceLicenseFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_source_license, container, false);

        initViews(view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void setPresenter(OpenSourceListenContract.Presenter presenter) {
        if (presenter != null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        AppCompatActivity activity = ((OpenSourceLicenseActivity)getActivity());
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView = (WebView) view.findViewById(R.id.web_view);
    }

    @Override
    public void loadLicense(String path) {
        webView.loadUrl(path);
    }

}
