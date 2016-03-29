package com.marktony.zhihudaily.UI.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.Adapters.LatestPostAdapter;
import com.marktony.zhihudaily.Entities.LatestPost;
import com.marktony.zhihudaily.Interfaces.IOnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.UI.Activities.ReadActivity;
import com.marktony.zhihudaily.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class LatestFragment extends Fragment {

    private RecyclerView rvLatestNews;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton fab;
    private RequestQueue queue;
    private List<LatestPost> list = new ArrayList<LatestPost>();

    private LatestPostAdapter adapter;

    private MaterialDialog dialog;

    private String date = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        dialog = new MaterialDialog.Builder(getActivity())
                .content(R.string.loading)
                .progress(true,0)
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest,container,false);

        initViews(view);

        dialog.show();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                load(null);
            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear <= 8 && dayOfMonth < 10){
                            date = String.valueOf(year) + "0" + (monthOfYear + 1) + "0" + dayOfMonth;
                        } else if (monthOfYear <= 8 && dayOfMonth >= 10){
                            date = String.valueOf(year) + "0" + (monthOfYear + 1) + dayOfMonth;
                        } else if (monthOfYear > 8 && dayOfMonth < 10){
                            date = String.valueOf(year) + (monthOfYear + 1) + "0" + dayOfMonth;
                        } else {
                            date = String.valueOf(year) + (monthOfYear + 1) + dayOfMonth;
                        }

                        load(date);

                    }
                },year,month,day);

                // 给datepickerdialog设置选择范围
                DatePicker picker = dialog.getDatePicker();
                picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                Calendar calendar = Calendar.getInstance();
                calendar.set(2013,5,20);
                picker.setMinDate(calendar.getTimeInMillis());

                dialog.setTitle(getString(R.string.choose_history_post));

                dialog.show();

            }
        });

        //设置下拉刷新的按钮的颜色
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置手指在屏幕上下拉多少距离开始刷新
        refresh.setDistanceToTriggerSync(300);
        //设置下拉刷新按钮的背景颜色
        refresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //设置下拉刷新按钮的大小
        refresh.setSize(SwipeRefreshLayout.DEFAULT);


        load(null);

        return view;
    }

    private void initViews(View view) {

        rvLatestNews = (RecyclerView) view.findViewById(R.id.rv_main);
        rvLatestNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

    }

    private void load(String date){

        String url = null;

        if (date == null){
            url =  Api.LATEST;
        } else {
            url = Api.HISTORY + date;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if ( !list.isEmpty()){
                    list.clear();
                }

                try {
                    if ( !jsonObject.getString("date").isEmpty()){

                        JSONArray array = jsonObject.getJSONArray("stories");
                        for (int i = 0; i < array.length(); i++){
                            JSONArray images = array.getJSONObject(i).getJSONArray("images");
                            List<String> stringList = new ArrayList<String>();
                            for (int j = 0; j < images.length(); j++){
                                String imgUrl = images.getString(j);
                                stringList.add(imgUrl);
                            }

                            LatestPost item = new LatestPost(
                                    array.getJSONObject(i).getString("title"),
                                    stringList,
                                    array.getJSONObject(i).getString("type"),
                                    array.getJSONObject(i).getString("id"));

                            list.add(item);
                        }
                    }

                    if (refresh.isRefreshing()){
                        Snackbar.make(refresh, R.string.refresh_done,Snackbar.LENGTH_SHORT).show();
                        refresh.setRefreshing(false);
                    }

                    adapter = new LatestPostAdapter(getActivity(),list);
                    rvLatestNews.setAdapter(adapter);
                    adapter.setItemClickListener(new IOnRecyclerViewOnClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            Intent intent = new Intent(getActivity(),ReadActivity.class);
                            intent.putExtra("id",list.get(position).getId());
                            intent.putExtra("title",list.get(position).getTitle());
                            startActivity(intent);
                        }
                    });

                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dialog.dismiss();
            }
        });

        queue.add(request);
    }


}
