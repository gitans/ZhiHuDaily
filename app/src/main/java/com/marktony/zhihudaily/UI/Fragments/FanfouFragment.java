package com.marktony.zhihudaily.ui.Fragments;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.Adapters.FanfouDailyPostAdapter;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.FanfouDailyPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.ui.Activities.FanfouPostDetailActivity;
import com.marktony.zhihudaily.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/6/3.
 * 饭否精选
 */
public class FanfouFragment extends Fragment{

    private RecyclerView rvFanfouDaily;
    private RequestQueue queue;
    private SwipeRefreshLayout refreshLayout;

    private List<FanfouDailyPost> list = new ArrayList<FanfouDailyPost>();

    private FanfouDailyPostAdapter adapter;

    public static final String TAG = "FANFOU_DAILY";

    public FanfouFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fanfou,container,false);

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

        rvFanfouDaily = (RecyclerView) view.findViewById(R.id.rv_fanfou);
        rvFanfouDaily.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, Api.FANFOU_API, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                try {
                    for (int i = 0; i < jsonArray.length(); i++){
                        String s = jsonArray.getString(i);
                        if (s.substring(18,s.length()).equals("daily.json")){

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.FANFOU_DAILY + jsonArray.getString(i).substring(1), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    try {
                                        JSONArray array = jsonObject.getJSONArray("msgs");
                                        for (int i = 0; i < array.length(); i++){
                                            JSONObject o = array.getJSONObject(i);

                                            // 需要对o.getString("msg")进行格式处理
                                            String content = o.getString("msg");
                                            content = android.text.Html.fromHtml(content).toString();

                                            FanfouDailyPost item = new FanfouDailyPost(o.getString("avatar"),
                                                    o.getString("realname"),
                                                    content,
                                                    o.getString("time"),
                                                    o.getJSONObject("img").getString("preview").replace("/ff/m0/0c/","/ff/n0/0c/"));

                                            list.add(item);
                                        }

                                        adapter = new FanfouDailyPostAdapter(getActivity(),list);
                                        adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                                            @Override
                                            public void OnItemClick(View v, int position) {
                                                FanfouDailyPost item = list.get(position);

                                                Intent intent = new Intent(getContext(), FanfouPostDetailActivity.class);
                                                intent.putExtra("avatar",item.getAvatarUrl());
                                                intent.putExtra("content",item.getContent());
                                                intent.putExtra("time",item.getTime());
                                                intent.putExtra("author",item.getAuthor());
                                                intent.putExtra("imgUrl",list.get(position).getImgUrl());
                                                startActivity(intent);
                                            }
                                        });
                                        rvFanfouDaily.setAdapter(adapter);

                                        refreshLayout.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                refreshLayout.setRefreshing(false);
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Snackbar.make(rvFanfouDaily,getString(R.string.wrong_process),Snackbar.LENGTH_SHORT).show();
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

                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(rvFanfouDaily,R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
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
    }
}
