package com.marktony.zhihudaily.settings;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.util.Theme;

public class SettingsPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.activity_settings);

        Theme.setStatusBarColor(this);

        initViews();

        Fragment fragment = SettingsPreferenceFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, fragment)
                .commit();

        new SettingsPresenter(SettingsPreferenceActivity.this, (SettingsContract.View) fragment);
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
