package com.marktony.zhihudaily.UI.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.marktony.zhihudaily.R;

public class SettingsActivity extends AppCompatActivity {

    private CheckBox checkBox;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);
        editor = sp.edit();

        initViews();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBox.setChecked(isChecked);

                editor.putBoolean("load_splash",isChecked);
                editor.apply();
            }
        });

    }

    private void initViews() {

        checkBox = (CheckBox) findViewById(R.id.checkbox_load_splash);
        checkBox.setChecked(sp.getBoolean("load_splash",false));
    }

}
