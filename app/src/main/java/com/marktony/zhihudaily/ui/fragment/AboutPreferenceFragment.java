package com.marktony.zhihudaily.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.ui.activity.OpenSourceLicenseActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by lizha on 2016/7/26.
 */

public class AboutPreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.about_preference_fragment);

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        findPreference("rate").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(toolbar, R.string.no_app_store_found,Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        findPreference("open_source_license").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                startActivity(new Intent(getActivity(),OpenSourceLicenseActivity.class));
                return false;
            }
        });

        findPreference("follow_me_on_github").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {

                CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder();
                customTabsIntent.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                customTabsIntent.setShowTitle(true);
                customTabsIntent.build().launchUrl(getActivity(), Uri.parse(getString(R.string.github_url)));

                /*try{
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.github_url))));
                } catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(toolbar, R.string.no_browser_found,Snackbar.LENGTH_SHORT).show();
                }*/
                return false;
            }
        });

        findPreference("follow_me_on_zhihu").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {

                CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder();
                customTabsIntent.setToolbarColor(getResources().getColor(R.color.colorPrimary));
                customTabsIntent.setShowTitle(true);
                customTabsIntent.build().launchUrl(getActivity(), Uri.parse(getString(R.string.zhihu_url)));

                // startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(getString(R.string.zhihu_url))));
                return false;
            }
        });

        findPreference("feedback").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                try{
                    Uri uri = Uri.parse(getString(R.string.sendto));
                    Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.mail_topic));
                    intent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.device_model) + Build.MODEL + "\n"
                                    + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                    + getString(R.string.version));
                    startActivity(intent);
                }catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(toolbar, R.string.no_mail_app,Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        findPreference("coffee").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {

                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle(R.string.donate);
                dialog.setMessage(getString(R.string.donate_content));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //将指定账号添加到剪切板
                        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", getString(R.string.donate_account));
                        manager.setPrimaryClip(clipData);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

                return false;
            }
        });

    }
}
