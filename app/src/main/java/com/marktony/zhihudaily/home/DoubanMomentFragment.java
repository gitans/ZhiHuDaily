package com.marktony.zhihudaily.home;

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
// import com.rey.material.app.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*final DatePickerDialog dialog = new DatePickerDialog(getContext());
                dialog.date(day, month, year);
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
                        day = dialog.getDay();

                        requestData(formatter.DoubanDateFormat(dialog.getDate()));

                        dialog.dismiss();
                    }
                });

                dialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/

                Calendar now = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dialog.show(getActivity().getFragmentManager(), "TAG");

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Calendar c = Calendar.getInstance();

                // init the date
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                requestData(formatter.DoubanDateFormat(Calendar.getInstance().getTimeInMillis()));
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取最后一个完全显示的itemposition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部并且是向下滑动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date(year - 1900, month, --day);
                        loadMore(format.format(date));
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });

        return view;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));

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

    private void requestData(String date){

        if (!refreshLayout.isRefreshing()){
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });
        }

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
                    recyclerView.setAdapter(adapter);
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
                Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
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

    // 写的有些重复，可以合并到上面的request 中去
    private void loadMore(String date){

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

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("OnDateSet", "You choosed " + year + "/" + monthOfYear + "/" + dayOfMonth);
    }

}
