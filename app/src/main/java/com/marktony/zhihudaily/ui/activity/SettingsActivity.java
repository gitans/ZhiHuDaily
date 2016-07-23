package com.marktony.zhihudaily.ui.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.util.UtilFunctions;

public class SettingsActivity extends AppCompatActivity {

    private AppCompatCheckBox cbLoadSplash;
    private AppCompatCheckBox cbNoPictureMode;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {

        UtilFunctions.setTheme(SettingsActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);
        editor = sp.edit();

        initViews();

        cbLoadSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("load_splash",cbLoadSplash.isChecked());
                editor.apply();
            }
        });

        cbNoPictureMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("no_picture_mode",cbNoPictureMode.isChecked());
                editor.apply();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {

        cbLoadSplash = (AppCompatCheckBox) findViewById(R.id.cb_load_splash);
        cbLoadSplash.setChecked(sp.getBoolean("load_splash",false));

        cbNoPictureMode = (AppCompatCheckBox) findViewById(R.id.cb_no_picture_mode);
        cbNoPictureMode.setChecked(sp.getBoolean("no_picture_mode",false));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
