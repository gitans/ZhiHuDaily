package com.marktony.zhihudaily.UI.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.marktony.zhihudaily.R;
import com.rey.material.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchLoadSplash;
    private Switch switchNoPictureMode;
    private Toolbar toolbar;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);
        editor = sp.edit();

        initViews();

        switchLoadSplash.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                switchLoadSplash.setChecked(checked);

                editor.putBoolean("load_splash",checked);
                editor.apply();
            }
        });

        switchNoPictureMode.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                switchNoPictureMode.setChecked(checked);

                editor.putBoolean("no_picture_mode",checked);
                editor.apply();
            }
        });

    }

    private void initViews() {

        // 初始化switch，通过读取sp中的值
        switchLoadSplash = (Switch) findViewById(R.id.switch_load_splash);
        switchLoadSplash.setChecked(sp.getBoolean("load_splash",false));

        switchNoPictureMode = (Switch) findViewById(R.id.switch_no_picture_mode);
        switchNoPictureMode.setChecked( sp.getBoolean("no_picture_mode",false));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
