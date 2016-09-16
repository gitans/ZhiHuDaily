package com.marktony.zhihudaily.douban;

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

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.adapter.DoubanMomentAdapter;
import com.marktony.zhihudaily.bean.DoubanMomentPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.ui.DividerItemDecoration;
import com.marktony.zhihudaily.util.DateFormatter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentFragment extends Fragment implements DoubanMomentContract.View {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;

    private DateFormatter formatter = new DateFormatter();

    private int yearRecord, monthRecord, dayRecord; // 用于加载对应日期的消息

    private DoubanMomentAdapter adapter;

    private DoubanMomentContract.Presenter presenter;

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
        yearRecord = c.get(Calendar.YEAR);
        monthRecord = c.get(Calendar.MONTH);
        dayRecord = c.get(Calendar.DAY_OF_MONTH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douban_zhihu_daily, container, false);

        initViews(view);

        presenter.loadPosts(Calendar.getInstance().getTimeInMillis());

        // requestData(formatter.DoubanDateFormat(Calendar.getInstance().getTimeInMillis()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Calendar now = Calendar.getInstance();
                now.set(yearRecord, monthRecord, dayRecord);
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        yearRecord = year;
                        monthRecord = monthOfYear;
                        dayRecord = dayOfMonth;
                        Calendar temp = Calendar.getInstance();
                        temp.clear();
                        temp.set(year, monthOfYear, dayOfMonth);
                        requestData(formatter.DoubanDateFormat(temp.getTimeInMillis()));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dialog.setMaxDate(Calendar.getInstance());
                Calendar minDate = Calendar.getInstance();
                minDate.set(2014, 5, 12);
                dialog.setMinDate(minDate);
                // set the dialog not vibrate when date change, default value is true
                dialog.vibrate(false);

                dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");*/

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // requestData(formatter.DoubanDateFormat(Calendar.getInstance().getTimeInMillis()));
            }
        });

        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
                        Date date = new Date(yearRecord - 1900, monthRecord, --dayRecord);
                        //loadMore(format.format(date));
                    }
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isSlidingToLast = dy > 0;
            }
        });*/

        return view;
    }

    @Override
    public void setPresenter(DoubanMomentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {

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

    /*private void requestData(String date){

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
    }*/

    // 写的有些重复，可以合并到上面的request 中去
    /*private void loadMore(String date){

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
    }*/

    @Override
    public void startLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showLoadError() {
        Snackbar.make(fab,R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showResults(ArrayList<DoubanMomentPost.posts> list) {
        if (adapter == null) {
            adapter = new DoubanMomentAdapter(getContext(), list);
            adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    presenter.startReading(position);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

}
