package com.marktony.zhihudaily.UI.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.marktony.zhihudaily.R;

public class AboutActivity extends AppCompatActivity {

    private TextView tvThanks;
    private TextView tvScore;
    private TextView tvFeedback;
    private TextView tvDonate;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initViews();

        tvThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(AboutActivity.this)
                        .title(R.string.thanksto)
                        .content(R.string.thanksto_content)
                        .neutralText(R.string.got_it)
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                              dialog.dismiss();
                            }
                        }).build();

                dialog.show();
            }
        });

        tvScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                try {
                    Uri uri = Uri.parse("market://details?id="+getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(tvScore, R.string.no_app_store_found,Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Uri uri = Uri.parse(getString(R.string.sendto));
                    Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.mail_topic));
                    intent.putExtra(Intent.EXTRA_TEXT,
                            getString(R.string.device_model) + Build.MODEL
                                    + "\n" + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n");
                    startActivity(intent);
                }catch (android.content.ActivityNotFoundException ex){
                    Snackbar.make(tvFeedback, R.string.no_mail_app,Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        tvDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(AboutActivity.this)
                        .title(R.string.donate)
                        .content(R.string.donate_content)
                        .positiveText(R.string.positive)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //将指定账号添加到剪切板
                                ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("text", getString(R.string.donate_account));
                                manager.setPrimaryClip(clipData);

                                dialog.dismiss();
                            }
                        })
                        .negativeText(R.string.negative)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).build();

                dialog.show();

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

    private void initViews() {

        tvThanks = (TextView) findViewById(R.id.tv_thanks);
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvFeedback = (TextView) findViewById(R.id.tv_feedback);
        tvDonate = (TextView) findViewById(R.id.tv_donate);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
