package com.marktony.zhihudaily.custom_tab;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by Lizhaotailang on 2016/9/4.
 */

public class KeepAliveService extends Service {

    private static final Binder sBinder = new Binder();

    @Override
    public IBinder onBind(Intent intent) {
        return sBinder;
    }

}
