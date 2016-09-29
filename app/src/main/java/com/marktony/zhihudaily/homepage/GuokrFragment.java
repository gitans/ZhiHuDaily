package com.marktony.zhihudaily.homepage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marktony.zhihudaily.adapter.GuokrNewsAdapter;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.GuokrHandpickNews;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by lizhaotailang on 2016/6/13.
 * 果壳精选
 * guokr handpick
 */
public class GuokrFragment extends Fragment implements GuokrContract.View{

    private RecyclerView rvGuokr;
    private SwipeRefreshLayout refreshGuokr;
    private GuokrNewsAdapter adapter;
    private GuokrContract.Presenter presenter;

    // require an empty constructor
    public GuokrFragment(){

    }

    public static GuokrFragment newInstance() {
        return new GuokrFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guokr,container,false);

        initViews(view);

        presenter.start();

        refreshGuokr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });

        return view;
    }

    @Override
    public void setPresenter(GuokrContract.Presenter presenter) {
        if (presenter != null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        rvGuokr = (RecyclerView) view.findViewById(R.id.rv_guokr_handpick);
        rvGuokr.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGuokr.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        refreshGuokr = (SwipeRefreshLayout) view.findViewById(R.id.refresh_guokr);
        //设置下拉刷新的按钮的颜色
        refreshGuokr.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置手指在屏幕上下拉多少距离开始刷新
        refreshGuokr.setDistanceToTriggerSync(300);
        //设置下拉刷新按钮的背景颜色
        refreshGuokr.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //设置下拉刷新按钮的大小
        refreshGuokr.setSize(SwipeRefreshLayout.DEFAULT);
    }

    @Override
    public void showError() {
        Snackbar.make(rvGuokr,R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showResults(ArrayList<GuokrHandpickNews.result> list) {
        if (adapter == null) {
            adapter = new GuokrNewsAdapter(getContext(), list);
            adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    presenter.startReading(position);
                }
            });
            rvGuokr.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showLoading() {
        refreshGuokr.post(new Runnable() {
            @Override
            public void run() {
                refreshGuokr.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        refreshGuokr.post(new Runnable() {
            @Override
            public void run() {
                refreshGuokr.setRefreshing(false);
            }
        });
    }

}
