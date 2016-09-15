package com.marktony.zhihudaily.bean;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.marktony.zhihudaily.app.VolleySingleton;
import com.marktony.zhihudaily.interfaces.OnStringListener;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public class StringModelImpl {

    private Context context;

    public StringModelImpl(Context context) {
        this.context = context;
    }

    public void load(String url, final OnStringListener listener) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listener.onError(volleyError);
            }
        });
        VolleySingleton.getVolleySingleton(context).addToRequestQueue(request);
    }

}
