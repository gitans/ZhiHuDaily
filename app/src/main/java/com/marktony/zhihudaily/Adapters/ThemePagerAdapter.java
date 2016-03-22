package com.marktony.zhihudaily.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.marktony.zhihudaily.UI.Fragments.PageFragment;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class ThemePagerAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private int titleCount;
    private Context context;

    public ThemePagerAdapter(FragmentManager fm, Context context, String[] titles, int titleCount) {
        super(fm);
        this.titleCount = titleCount;
        this.titles = titles;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return titleCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
