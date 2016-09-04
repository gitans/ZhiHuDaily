package com.marktony.zhihudaily.about;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.util.Theme;

public class AboutPreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.activity_about);

        Theme.setStatusBarColor(this);

        initViews();

        AboutPreferenceFragment fragment = new AboutPreferenceFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.about_container,fragment)
                .commit();

        new AboutPresenter(AboutPreferenceActivity.this, fragment);

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
