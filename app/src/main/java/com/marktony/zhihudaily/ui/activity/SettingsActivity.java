package com.marktony.zhihudaily.ui.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.util.UtilFunctions;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private AppCompatCheckBox cbLoadSplash;
    private AppCompatCheckBox cbNoPictureMode;
    private AppCompatCheckBox cbInAppBrowser;

    private TextView tvTimeToSaveDes;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {

        UtilFunctions.setTheme(SettingsActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);
        editor = sp.edit();

        initViews();

        findViewById(R.id.cb_load_splash).setOnClickListener(this);
        findViewById(R.id.layout_load_splash).setOnClickListener(this);

        findViewById(R.id.layout_time_to_save).setOnClickListener(this);

        findViewById(R.id.cb_load_splash).setOnClickListener(this);
        findViewById(R.id.layout_load_splash).setOnClickListener(this);

        findViewById(R.id.cb_no_picture_mode).setOnClickListener(this);
        findViewById(R.id.layout_no_picture_mode).setOnClickListener(this);

        findViewById(R.id.cb_in_app_browser).setOnClickListener(this);
        findViewById(R.id.layout_in_app_browser).setOnClickListener(this);


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
        cbLoadSplash.setChecked( sp.getBoolean("load_splash",false));

        cbNoPictureMode = (AppCompatCheckBox) findViewById(R.id.cb_no_picture_mode);
        cbNoPictureMode.setChecked( sp.getBoolean("no_picture_mode",false));

        cbInAppBrowser = (AppCompatCheckBox) findViewById(R.id.cb_in_app_browser);
        cbInAppBrowser.setChecked( sp.getBoolean("in_app_browser",true));

        tvTimeToSaveDes = (TextView) findViewById( R.id.tv_time_to_save_description);

        tvTimeToSaveDes.setText(String.valueOf( sp.getInt("time_to_save",3) + getString(R.string.time_to_save_description)));

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void pickAndSaveNumber() {

        AlertDialog dialog = new AlertDialog.Builder(SettingsActivity.this).create();
        dialog.setTitle(R.string.choose);

        LinearLayout layout = new LinearLayout(SettingsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        float dp = getResources().getDisplayMetrics().density;
        layout.setPadding((int) (16*dp), (int) (16*dp), (int) (16*dp), (int) (16*dp));

        final NumberPicker numberPicker = new NumberPicker(SettingsActivity.this);

        numberPicker.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        numberPicker.setValue(3);
        numberPicker.setWrapSelectorWheel(false);

        layout.addView(numberPicker);

        dialog.setView(layout);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editor.putInt("time_to_save", numberPicker.getValue());
                editor.apply();
                tvTimeToSaveDes.setText(String.valueOf(numberPicker.getValue() + getString(R.string.time_to_save_description)));
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.cb_load_splash:
            case R.id.layout_load_splash:

                cbLoadSplash.setChecked( !cbLoadSplash.isChecked());

                editor.putBoolean("load_splash", cbLoadSplash.isChecked());
                editor.apply();
                break;

            case R.id.cb_no_picture_mode:
            case R.id.layout_no_picture_mode:

                cbNoPictureMode.setChecked( !cbNoPictureMode.isChecked());

                editor.putBoolean("no_picture_mode", cbNoPictureMode.isChecked());
                editor.apply();
                break;

            case R.id.cb_in_app_browser:
            case R.id.layout_in_app_browser:

                cbInAppBrowser.setChecked( !cbInAppBrowser.isChecked());

                editor.putBoolean("in_app_browser", cbInAppBrowser.isChecked());
                editor.apply();
                break;

            case R.id.layout_time_to_save:
                pickAndSaveNumber();
                break;

            default:
                break;
        }
    }
}
