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
import com.marktony.zhihudaily.adapters.GuokrPostAdapter;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.GuokrHandpickPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.ui.activity.GuokrReadActivity;
import com.marktony.zhihudaily.utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lizhaotailang on 2016/6/13.
 * 果壳精选
 * guokr handpick
 */
public class GuokrFragment extends Fragment {

    private RecyclerView rvGuokr;
    private SwipeRefreshLayout refreshGuokr;

    private RequestQueue queue;

    private ArrayList<GuokrHandpickPost> guokrList = new ArrayList<GuokrHandpickPost>();

    private GuokrPostAdapter adapter;

    private static final String TAG = "GUOKR";

    // require an empty constructor
    public GuokrFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guokr,container,false);

        initViews(view);

        requestData();

        refreshGuokr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (!guokrList.isEmpty()){
                    guokrList.clear();
                }

                adapter.notifyDataSetChanged();

                requestData();

            }
        });

        return view;
    }

    private void initViews(View view) {

        rvGuokr = (RecyclerView) view.findViewById(R.id.rv_guokr_handpick);
        rvGuokr.setLayoutManager(new LinearLayoutManager(getActivity()));

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

    private void requestData(){

        refreshGuokr.post(new Runnable() {
            @Override
            public void run() {
                refreshGuokr.setRefreshing(true);
            }
        });

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.GUOKR_ARTICLES, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("ok").equals("true")){
                        JSONArray array = jsonObject.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            GuokrHandpickPost item = new GuokrHandpickPost(
                                    o.getString("id"),
                                    o.getString("title"),
                                    o.getString("headline_img"));

                            guokrList.add(item);
                        }
                    }

                    if (guokrList.size() != 0){

                        adapter = new GuokrPostAdapter(getActivity(),guokrList);
                        adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                            @Override
                            public void OnItemClick(View v, int position) {

                                Intent intent = new Intent(getActivity(),GuokrReadActivity.class);
                                intent.putExtra("id",guokrList.get(position).getId());
                                intent.putExtra("headlineImageUrl",guokrList.get(position).getHeadlineImg());
                                intent.putExtra("title",guokrList.get(position).getTitle());

                                startActivity(intent);
                            }
                        });

                        rvGuokr.setAdapter(adapter);

                        refreshGuokr.post(new Runnable() {
                            @Override
                            public void run() {
                                refreshGuokr.setRefreshing(false);
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
                Snackbar.make(rvGuokr,R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
                refreshGuokr.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshGuokr.setRefreshing(false);
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

        if (refreshGuokr.isRefreshing()){
            refreshGuokr.setRefreshing(false);
        }
    }
}
