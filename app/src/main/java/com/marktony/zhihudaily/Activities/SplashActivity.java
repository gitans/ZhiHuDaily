package com.marktony.zhihudaily.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;
import com.marktony.zhihudaily.Utils.NetworkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lizhaotailang on 2016/3/18.
 * 欢迎页
 */
public class SplashActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getApplicationContext());

        setContentView(R.layout.activity_splash);

        final ImageView ivWelcome = (ImageView) findViewById(R.id.iv_welcome);
        final TextView tvWelcomeName = (TextView) findViewById(R.id.tv_welcome_name);

        if (NetworkState.networkConneted(SplashActivity.this)){
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.START_IMAGE, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {
                        if (!jsonObject.getString("img").isEmpty()){
                            Glide.with(SplashActivity.this).load(jsonObject.getString("img")).into(ivWelcome);
                            tvWelcomeName.setText(jsonObject.getString("text"));
                        } else {
                            ivWelcome.setImageResource(R.drawable.welcome);
                            tvWelcomeName.setText("欢迎来到知乎小报");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });

            queue.add(request);
        } else {
            ivWelcome.setImageResource(R.drawable.welcome);
            tvWelcomeName.setText("欢迎来到知乎小报");
        }



        final Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };
        timer.schedule(timerTask, 1000 * 3);
    }
}
