package com.marktony.zhihudaily.home;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.adapter.ZhihuDailyPostAdapter;
import com.marktony.zhihudaily.app.VolleySingleton;
import com.marktony.zhihudaily.bean.ZhihuDailyPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.ui.DividerItemDecoration;
import com.marktony.zhihudaily.ui.activity.ZhihuReadActivity;
import com.marktony.zhihudaily.util.Api;
import com.marktony.zhihudaily.util.DateFormatter;
import com.marktony.zhihudaily.util.NetworkState;
import com.marktony.zhihudaily.db.DatabaseHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/21.
 * 最新消息
 * latest posts
 */
public class ZhihuDailyFragment extends Fragment {

    private RecyclerView rvLatestNews;
    private SwipeRefreshLayout refresh;
    private FloatingActionButton fab;

    private List<ZhihuDailyPost> list = new ArrayList<ZhihuDailyPost>();

    private ZhihuDailyPostAdapter adapter;

    private DatabaseHelper dbhelper;
    private SQLiteDatabase db;

    private SharedPreferences sp;

    // 2013.5.20是知乎日报api首次上线
    private int yearRecord = 2013;
    private int monthRecord = 5;
    private int dayRecord = 20;

    // 用于记录加载更多的次数
    private int groupCount = -1;

    private final String TAG = "ZhihuDailyFragment";

    public ZhihuDailyFragment() {

    }

    public static ZhihuDailyFragment newInstance() {
        return new ZhihuDailyFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbhelper = new DatabaseHelper(getActivity(),"History.db",null,3);
        db = dbhelper.getWritableDatabase();

        sp = getActivity().getSharedPreferences("user_settings", Context.MODE_PRIVATE);

        deleteTimeoutPosts();

        // 获取当前日期的前一天
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,-1);

        yearRecord = c.get(Calendar.YEAR);
        monthRecord = c.get(Calendar.MONTH);
        dayRecord = c.get(Calendar.DAY_OF_MONTH);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douban_zhihu_daily,container,false);

        initViews(view);

        if ( !NetworkState.networkConnected(getActivity())){
            showNoNetwork();
            loadFromDB();
        } else {
            load(new DateFormatter().ZhihuDailyDateFormat(Calendar.getInstance().getTimeInMillis()));
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (!list.isEmpty()){
                    list.clear();
                }

                if ( !NetworkState.networkConnected(getActivity())){
                    showNoNetwork();
                    loadFromDB();
                } else {
                    load(new DateFormatter().ZhihuDailyDateFormat(Calendar.getInstance().getTimeInMillis()));
                }

                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH,-1);

                yearRecord = c.get(Calendar.YEAR);
                monthRecord = c.get(Calendar.MONTH);
                dayRecord = c.get(Calendar.DAY_OF_MONTH);

                groupCount = -1;

            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
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
                        load(new DateFormatter().ZhihuDailyDateFormat(temp.getTimeInMillis()));
                    }
                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dialog.setMaxDate(Calendar.getInstance());
                Calendar minDate = Calendar.getInstance();
                minDate.set(2013, 5, 20);
                dialog.setMinDate(minDate);
                dialog.vibrate(false);

                dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });

        rvLatestNews.setOnScrollListener(new RecyclerView.OnScrollListener() {

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

                        loadMore();

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

        rvLatestNews = (RecyclerView) view.findViewById(R.id.rv_main);
        rvLatestNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLatestNews.addItemDecoration(new DividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));

        //设置下拉刷新的按钮的颜色
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置手指在屏幕上下拉多少距离开始刷新
        refresh.setDistanceToTriggerSync(300);
        //设置下拉刷新按钮的背景颜色
        refresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        //设置下拉刷新按钮的大小
        refresh.setSize(SwipeRefreshLayout.DEFAULT);

    }

    /**
     * 用于加载最新日报或者历史日报
     * @param date 日期
     */
    private void load(final String date){

        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });

        String url = Api.HISTORY + date;

        if ( !list.isEmpty()){
            list.clear();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if ( !jsonObject.getString("date").isEmpty()){

                        JSONArray array = jsonObject.getJSONArray("stories");
                        for (int i = 0; i < array.length(); i++){

                            JSONArray images = array.getJSONObject(i).getJSONArray("images");
                            String id = array.getJSONObject(i).getString("id");
                            String type = array.getJSONObject(i).getString("type");
                            String title = array.getJSONObject(i).getString("title");
                            List<String> stringList = new ArrayList<String>();
                            for (int j = 0; j < images.length(); j++){
                                String imgUrl = images.getString(j);
                                stringList.add(imgUrl);
                            }

                            ZhihuDailyPost item = new ZhihuDailyPost(title, stringList, type, id);

                            list.add(item);

                            if (!queryIDExists("LatestPosts",id)){

                                ContentValues values = new ContentValues();
                                values.put("id",Integer.valueOf(id));
                                values.put("title",title);
                                values.put("type",Integer.valueOf(type));
                                values.put("img_url",stringList.get(0));

                                if (date == null){
                                    String d = jsonObject.getString("date");
                                    values.put("date",Integer.valueOf(d));
                                    storeContent(id,d);
                                } else {
                                    values.put("date",Integer.valueOf(date));
                                    storeContent(id,date);
                                }

                                db.insert("LatestPosts",null,values);

                                values.clear();
                            }

                        }
                    }

                    adapter = new ZhihuDailyPostAdapter(getActivity(),list);
                    rvLatestNews.setAdapter(adapter);
                    adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            Intent intent = new Intent(getActivity(),ZhihuReadActivity.class);
                            intent.putExtra("id",list.get(position).getId());
                            intent.putExtra("title",list.get(position).getTitle());
                            startActivity(intent);
                        }
                    });

                    if (refresh.isRefreshing()){

                        refresh.post(new Runnable() {
                            @Override
                            public void run() {
                                refresh.setRefreshing(false);
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
                if (refresh.isRefreshing()){
                    Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
                    refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            refresh.setRefreshing(false);
                        }
                    });
                }
            }
        });

        request.setTag(TAG);
        VolleySingleton.getVolleySingleton(getContext()).addToRequestQueue(request);
    }

    // 通过snackbar提示没有网络连接
    public void showNoNetwork(){
        Snackbar.make(fab,R.string.no_network_connected,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.go_to_set, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                }).show();
    }


    /**
     * 从数据库中加载已经保存的数据
     */
    private void loadFromDB(){

        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(true);
            }
        });

        Cursor cursor = db.query("LatestPosts",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                List<String> list = new ArrayList<String>();
                list.add(cursor.getString(cursor.getColumnIndex("img_url")));
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex("id")));
                String type = String.valueOf(cursor.getInt(cursor.getColumnIndex("type")));

                if ((title != null) && (list.get(0) != null) && (!id.equals("")) && (!type.equals(""))){
                    ZhihuDailyPost item = new ZhihuDailyPost(title,list,type,id);
                    this.list.add(item);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ZhihuDailyPostAdapter(getActivity(),list);
        rvLatestNews.setAdapter(adapter);
        adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),ZhihuReadActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("title",list.get(position).getTitle());
                startActivity(intent);
            }
        });

        refresh.post(new Runnable() {
            @Override
            public void run() {
                refresh.setRefreshing(false);
            }
        });

    }

    /**
     * 查询数据库中是否已经存在此id
     * @param tableName 要查询的表的名称
     * @param id 要查询的string id
     * @return 返回是否存在boolean
     */
    private boolean queryIDExists(String tableName,String id){
        Cursor cursor = db.query(tableName,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {

                if (id.equals(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))))){
                    return true;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return false;
    }

    /**
     * 将指定id的内容储存值数据库中
     * @param id 所要保存内容的id
     * @param date 日期
     */
    private void storeContent(final String id, final String date){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.NEWS + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                if (queryIDExists("LatestPosts",id)){
                    ContentValues values = new ContentValues();

                    try {
                        if ( !jsonObject.isNull("body")) {
                            values.put("id",Integer.valueOf(id));
                            values.put("content", jsonObject.getString("body"));
                            values.put("date",Integer.valueOf(date));
                            db.insert("Contents",null,values);
                            values.clear();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        request.setTag(TAG);
        VolleySingleton.getVolleySingleton(getContext()).addToRequestQueue(request);

    }

    private void deleteTimeoutPosts(){

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,-2);

        String[] whereArgs = {new DateFormatter().ZhihuDailyDateFormat(c.getTimeInMillis())};

        db.delete("LatestPosts","date<?",whereArgs);
        db.delete("Contents","date<?",whereArgs);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (VolleySingleton.getVolleySingleton(getContext()).getRequestQueue() != null){
            VolleySingleton.getVolleySingleton(getContext()).getRequestQueue().cancelAll(TAG);
        }

        if (refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
    }

    // 用于加载更多
    private void loadMore() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date d = new Date(yearRecord -1900, monthRecord, dayRecord - groupCount);
        final String date = format.format(d);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,Api.HISTORY + date, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if ( !jsonObject.getString("date").isEmpty()){

                        JSONArray array = jsonObject.getJSONArray("stories");
                        for (int i = 0; i < array.length(); i++){

                            JSONArray images = array.getJSONObject(i).getJSONArray("images");
                            String id = array.getJSONObject(i).getString("id");
                            String type = array.getJSONObject(i).getString("type");
                            String title = array.getJSONObject(i).getString("title");
                            List<String> stringList = new ArrayList<String>();
                            for (int j = 0; j < images.length(); j++){
                                String imgUrl = images.getString(j);
                                stringList.add(imgUrl);
                            }

                            ZhihuDailyPost item = new ZhihuDailyPost(title, stringList, type, id);

                            list.add(item);

                            if (!queryIDExists("LatestPosts",id)){

                                ContentValues values = new ContentValues();
                                values.put("id",Integer.valueOf(id));
                                values.put("title",title);
                                values.put("type",Integer.valueOf(type));
                                values.put("img_url",stringList.get(0));

                                if (date == null){
                                    String d = jsonObject.getString("date");
                                    values.put("date",Integer.valueOf(d));
                                    storeContent(id,d);
                                } else {
                                    values.put("date",Integer.valueOf(date));
                                    storeContent(id,date);
                                }

                                db.insert("LatestPosts",null,values);

                                values.clear();
                            }

                        }
                    }

                    adapter.notifyDataSetChanged();

                    groupCount++;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (refresh.isRefreshing()){
                    Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
                    refresh.post(new Runnable() {
                        @Override
                        public void run() {
                            refresh.setRefreshing(false);
                        }
                    });
                }
            }
        });

        request.setTag(TAG);
        VolleySingleton.getVolleySingleton(getContext()).addToRequestQueue(request);

    }
}
