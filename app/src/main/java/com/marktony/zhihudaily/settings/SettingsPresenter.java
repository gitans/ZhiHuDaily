package com.marktony.zhihudaily.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.Preference;

import com.bumptech.glide.Glide;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Lizhaotailang on 2016/9/5.
 */

public class SettingsPresenter implements SettingsContract.Presenter {

    private Context context;
    private SettingsContract.View view;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SettingsPresenter(Context context, SettingsContract.View view){
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        sp = context.getSharedPreferences("user_settings",MODE_PRIVATE);
        editor = sp.edit();
    }

    @Override
    public void setNoPictureMode(Preference preference) {
        editor.putBoolean("no_picture_mode",preference.getSharedPreferences().getBoolean("no_picture_mode",false));
        editor.apply();
    }

    @Override
    public void setInAppBrowser(Preference preference) {
        editor.putBoolean("in_app_browser",preference.getSharedPreferences().getBoolean("in_app_browser",false));
        editor.apply();
    }

    @Override
    public void cleanGlideCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO: 2016/9/5 better it 
                Glide.get(context).clearDiskCache();
                
            }
        }).start();
    }

    @Override
    public void start() {

    }

}
