package com.marktony.zhihudaily.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marktony.zhihudaily.R;

/**
 * Created by lizhaotailang on 2016/3/22.
 */
public class CommentFragment extends Fragment{

    public static final String ARGS_PAGE = "args_page";
    private int pages;

    public static CommentFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE,page);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pages = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comment_page,container,false);



        return view;
    }

}
