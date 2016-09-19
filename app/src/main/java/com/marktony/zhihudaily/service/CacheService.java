package com.marktony.zhihudaily.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.marktony.zhihudaily.app.VolleySingleton;
import com.marktony.zhihudaily.bean.StringModelImpl;
import com.marktony.zhihudaily.bean.ZhihuDailyStory;
import com.marktony.zhihudaily.interfaces.OnStringListener;
import com.marktony.zhihudaily.util.Api;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/18.
 */

public class CacheService extends Service implements OnStringListener{

    private ArrayList<Integer> zhihuIds = new ArrayList<Integer>();
    private ArrayList<Integer> guokrIds = new ArrayList<Integer>();
    private ArrayList<Integer> doubanIds = new ArrayList<Integer>();
    private Gson gson = new Gson();

    private static final String TAG = CacheService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    public void setZhihuIds(ArrayList<Integer> zhihuIds) {
        this.zhihuIds = zhihuIds;
    }

    public void startCache() {
        Log.d("story", "onStartCache" + "size" + zhihuIds.size());
        for (int i = 0; i < zhihuIds.size(); i++) {
            final int finalI = i;
            StringRequest request = new StringRequest(Request.Method.GET, Api.ZHIHU_NEWS + zhihuIds.get(i), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    ZhihuDailyStory story = gson.fromJson(s, ZhihuDailyStory.class);
                    // Log.d("story", story.getTitle());
                    if (finalI == zhihuIds.size() - 1) {
                        stopSelf();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (finalI == zhihuIds.size() - 1) {
                        stopSelf();
                    }
                }
            });

            VolleySingleton.getVolleySingleton(this).getRequestQueue().add(request);

        }
    }

    @Override
    public void onSuccess(String result) {
        ZhihuDailyStory story = gson.fromJson(result, ZhihuDailyStory.class);
        Log.d("story", story.getTitle());
    }

    @Override
    public void onError(VolleyError error) {
        Log.d("story", error.toString());
    }

    public class MyBinder extends Binder {
        public CacheService getService() {
            return CacheService.this;
        }
    }

}
