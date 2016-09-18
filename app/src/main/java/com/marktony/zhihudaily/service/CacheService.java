package com.marktony.zhihudaily.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Lizhaotailang on 2016/9/18.
 */

public class CacheService extends Service {

    private ArrayList<Integer> zhihuIds = new ArrayList<Integer>();
    private ArrayList<Integer> guokrIds = new ArrayList<Integer>();
    private ArrayList<Integer> doubanIds = new ArrayList<Integer>();

    private static final String TAG = CacheService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

}
