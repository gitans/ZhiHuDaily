package com.marktony.zhihudaily.open_source_license;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.util.Theme;

public class OpenSourceLicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.frame);

        Theme.setStatusBarColor(this);

        OpenSourceLicenseFragment fragment = OpenSourceLicenseFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        new OpenSourceLicensePresenter(fragment);

    }

}
