package com.marktony.zhihudaily.inner_browser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.marktony.zhihudaily.R;

/**
 * Created by Lizhaotailang on 2016/8/30.
 */

public class InnerBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, InnerBrowserFragment.getInstance()).commit();

    }

}
