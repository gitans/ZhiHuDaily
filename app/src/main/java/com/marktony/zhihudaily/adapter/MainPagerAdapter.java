package com.marktony.zhihudaily.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.ui.fragment.GuokrFragment;
import com.marktony.zhihudaily.ui.fragment.ZhihuDailyFragment;

/**
 * Created by Lizhaotailang on 2016/8/10.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private final Context context;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        titles = context.getResources().getStringArray(R.array.page_titles);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return GuokrFragment.newInstance();
        }
        return ZhihuDailyFragment.newInstance();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
