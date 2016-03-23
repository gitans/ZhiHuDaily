package com.marktony.zhihudaily.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.R;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class PageFragment extends android.support.v4.app.Fragment {

    public static final String ARGS_PAGE = "args_page";
    private int pages;

    private RequestQueue queue;

    public static PageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE,page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pages = getArguments().getInt(ARGS_PAGE);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.theme_page,container,false);

        ImageView ivTheme = (ImageView) view.findViewById(R.id.iv_theme);
        RecyclerView rvThemePosts = (RecyclerView) view.findViewById(R.id.rv_theme_post);


        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        if (pages == tabLayout.getSelectedTabPosition()){
            ivTheme.setImageResource(R.drawable.no_img);
        }



        return view;
    }
}
