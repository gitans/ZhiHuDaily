package com.marktony.zhihudaily.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.marktony.zhihudaily.R;

/**
 * Created by lizha on 2016/7/26.
 */

public class SettingsPreferenceFragment extends PreferenceFragmentCompat {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.settings_preference_fragment);

        sp = getActivity().getSharedPreferences("user_settings", Context.MODE_PRIVATE);
        editor = sp.edit();

        findPreference("no_picture_mode").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                editor.putBoolean("no_picture_mode",preference.getSharedPreferences().getBoolean("no_picture_mode",false));
                editor.apply();
                return false;
            }
        });

        findPreference("in_app_browser").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                editor.putBoolean("in_app_browser",preference.getSharedPreferences().getBoolean("in_app_browser",false));
                editor.apply();
                return false;
            }
        });

        findPreference("load_splash").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                editor.putBoolean("load_splash",preference.getSharedPreferences().getBoolean("load_splash",false));
                editor.apply();
                return false;
            }
        });

    }
}
