package com.marktony.zhihudaily.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.adapter.DoubanMomentAdapter;
import com.marktony.zhihudaily.app.VolleySingleton;
import com.marktony.zhihudaily.bean.DoubanMomentPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.ui.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;

    private ArrayList<DoubanMomentPost> list = new ArrayList<>();

    private DoubanMomentAdapter adapter;

    private static final String TAG = DoubanMomentFragment.class.getSimpleName();

    public DoubanMomentFragment() {

    }

    public static DoubanMomentFragment newInstance() {
        return new DoubanMomentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douban_zhihu_daily, container, false);

        initViews(view);

        requestData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void requestData(){

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://moment.douban.com/api/stream/date/2016-08-11", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if ( !jsonObject.isNull("posts")){
                        JSONArray array = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < array.length(); i++){

                            JSONObject o = array.getJSONObject(i);

                            String thumb_medium = null;
                            if (o.getJSONArray("thumbs").length() != 0) {
                                thumb_medium = o.getJSONArray("thumbs").getJSONObject(0).getJSONObject("medium").getString("url");
                            }

                            DoubanMomentPost item = new DoubanMomentPost(
                                    o.getInt("id"),
                                    o.getString("title"),
                                    o.getString("abstract"),
                                    thumb_medium
                            );

                            list.add(item);
                        }
                    }

                    if (list.size() != 0){

                        adapter = new DoubanMomentAdapter(getActivity(),list);
                        adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                            @Override
                            public void OnItemClick(View v, int position) {
                                Snackbar.make(fab,String.valueOf(position),Snackbar.LENGTH_SHORT).show();
                            }
                        });

                        recyclerView.setAdapter(adapter);

                        refreshLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshLayout.setRefreshing(false);
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(fab,R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });

            }
        });

        request.setTag(TAG);
        VolleySingleton.getVolleySingleton(getContext()).addToRequestQueue(request);
    }

}
