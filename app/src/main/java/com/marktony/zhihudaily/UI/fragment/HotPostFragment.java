package com.marktony.zhihudaily.ui.fragment;

import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.adapters.HotPostAdapter;
import com.marktony.zhihudaily.bean.HotPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.ui.activity.ZhihuReadActivity;
import com.marktony.zhihudaily.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class HotPostFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<HotPost> list = new ArrayList<HotPost>();

    private HotPostAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    private static final String TAG = "HOT_POSTS";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_post,container,false);

        initViews(view);

        loadData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!list.isEmpty()){
                    list.clear();
                }

                adapter.notifyDataSetChanged();

                loadData();
            }
        });

        return view;
    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_hot_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);

        //设置下拉刷新的按钮的颜色
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置手指在屏幕上下拉多少距离开始刷新
        refreshLayout.setDistanceToTriggerSync(300);
        //设置下拉刷新按钮的背景颜色
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //设置下拉刷新按钮的大小
        refreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

    }

    private void loadData(){

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.HOT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if ( !jsonObject.isNull("recent")){
                    try {
                        JSONArray array = jsonObject.getJSONArray("recent");
                        for (int i = 0;i < array.length();i++){
                            HotPost hotpost = new HotPost(array.getJSONObject(i).getString("news_id"),
                                    array.getJSONObject(i).getString("url"),
                                    array.getJSONObject(i).getString("title"),
                                    array.getJSONObject(i).getString("thumbnail"));

                            list.add(hotpost);
                        }

                        adapter = new HotPostAdapter(getActivity(),list);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                            @Override
                            public void OnItemClick(View v, int position) {
                                Intent intent = new Intent(getActivity(),ZhihuReadActivity.class);
                                intent.putExtra("id",list.get(position).getNews_id());
                                intent.putExtra("title",list.get(position).getTitle());
                                intent.putExtra("image",list.get(position).getThumbnail());
                                startActivity(intent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(recyclerView,getString(R.string.wrong_process),Snackbar.LENGTH_SHORT).show();
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        request.setTag(TAG);
        queue.add(request);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (queue != null){
            queue.cancelAll(TAG);
        }

        if (refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
    }
}
