package com.marktony.zhihudaily.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.marktony.zhihudaily.UI.Fragments.CommentFragment;

/**
 * Created by lizhaotailang on 2016/3/22.
 */
public class CommentsPagerAdapter extends FragmentPagerAdapter{

    private String titles[] = {"长评论","短评论"};


    public CommentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CommentFragment.newInstance(position + 1);
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
