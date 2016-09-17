package com.marktony.zhihudaily.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.adapter.MainPagerAdapter;

/**
 * Created by Lizhaotailang on 2016/8/23.
 */

public class MainFragment extends Fragment {

    private Context context;

    private MainPagerAdapter adapter;

    private TabLayout tabLayout;

    private OnViewPagerCreated mOnViewPagerCreated;

    public MainFragment() {

    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        mOnViewPagerCreated = (OnViewPagerCreated) context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        mOnViewPagerCreated.viewPagerCreated();
        return view;
    }


    private void initViews(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((MainActivity)context).setSupportActionBar(toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        adapter = new MainPagerAdapter(getChildFragmentManager(), context);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public interface OnViewPagerCreated {
        void viewPagerCreated();
    }

}
