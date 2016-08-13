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
import com.marktony.zhihudaily.ui.activity.DoubanReadActivity;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.DateFormatter;
import com.rey.material.app.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;

    private DateFormatter formatter = new DateFormatter();

    private int year, month, day; // 用于加载对应日期的消息

    private ArrayList<DoubanMomentPost> list = new ArrayList<>();

    private DoubanMomentAdapter adapter;

    private static final String TAG = DoubanMomentFragment.class.getSimpleName();

    public DoubanMomentFragment() {

    }

    public static DoubanMomentFragment newInstance() {
        return new DoubanMomentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();

        // init the date
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douban_zhihu_daily, container, false);

        initViews(view);

        requestData(formatter.DoubanDateFormat(Calendar.getInstance().getTimeInMillis()));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(formatter.DoubanDateFormat(Calendar.getInstance().getTimeInMillis()));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.clear();

                final DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.date(day + 1, month, year);
                Calendar calendar = Calendar.getInstance();
                // birthday of douban moment is 2014,5,12
                calendar.set(2014, 5, 12);
                dialog.dateRange(calendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
                dialog.show();

                dialog.positiveAction(getString(R.string.positive));
                dialog.negativeAction(getString(R.string.negative));

                dialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        year = dialog.getYear();
                        month = dialog.getMonth();
                        day = dialog.getDay() - 1;

                        requestData(formatter.DoubanDateFormat(dialog.getDate()));

                        dialog.dismiss();
                    }
                });

                dialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

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
        refreshLayout.setDistanceToTriggerSync(300);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }

    private void requestData(String date){

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });

        if (!list.isEmpty()){
            list.clear();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.DOUBAN_MOMENT + date, new Response.Listener<JSONObject>() {
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

                    adapter = new DoubanMomentAdapter(getActivity(),list);
                    adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            DoubanMomentPost item = list.get(position);
                            Intent i = new Intent(getActivity(), DoubanReadActivity.class);
                            i.putExtra("id", item.getId());
                            i.putExtra("title", item.getTitle());
                            i.putExtra("image", item.getThumb());
                            startActivity(i);
                        }
                    });

                    recyclerView.setAdapter(adapter);

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
