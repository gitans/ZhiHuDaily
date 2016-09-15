package com.marktony.zhihudaily.interfaces;

import com.android.volley.VolleyError;

/**
 * Created by Lizhaotailang on 2016/9/15.
 */

public interface OnStringListener {

    /**
     * 请求成功时回调
     * @param result
     */
    void onSuccess(String result);

    /**
     * 请求失败时回调
     * @param error
     */
    void onError(VolleyError error);

}
