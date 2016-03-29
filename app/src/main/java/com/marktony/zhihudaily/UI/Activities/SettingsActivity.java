package com.marktony.zhihudaily.UI.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.marktony.zhihudaily.R;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout layout;
    private CheckBox checkBox;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);
        editor = sp.edit();

        initViews();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked())
                    checkBox.setChecked(false);
                else
                    checkBox.setChecked(true);

                editor.putBoolean("load_splash",checkBox.isChecked());
                editor.apply();
            }
        });

    }

    private void initViews() {

        layout = (LinearLayout) findViewById(R.id.LL_setting_load_splash);
        checkBox = (CheckBox) findViewById(R.id.checkbox_load_splash);

        checkBox.setChecked(sp.getBoolean("load_splash",true));
    }
}
