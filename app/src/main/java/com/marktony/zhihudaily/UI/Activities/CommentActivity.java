package com.marktony.zhihudaily.UI.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.marktony.zhihudaily.Adapters.CommentsPagerAdapter;
import com.marktony.zhihudaily.R;

public class CommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager pager = (ViewPager) findViewById(R.id.comment_viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.comment_tab);

        CommentsPagerAdapter adapter = new CommentsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabLayout.setupWithViewPager(pager);
    }

}
