package com.marktony.zhihudaily.ui.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.marktony.zhihudaily.utils.Api;
import com.marktony.zhihudaily.utils.NetworkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lizhaotailang on 2016/3/18.
 * 欢迎页
 */
public class SplashActivity extends AppCompatActivity {

    private ImageView ivWelcome;
    private TextView tvWelcomeName;

    private RequestQueue queue;

    private SharedPreferences sp;

    private final String TAG = "SplashActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("user_settings",MODE_PRIVATE);

        if (sp.getBoolean("load_splash",false)){

            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            } else {
                queue = Volley.newRequestQueue(getApplicationContext());

                setContentView(R.layout.activity_splash);

                initViews();

                if (NetworkState.networkConneted(SplashActivity.this)){
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.START_IMAGE, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                if (jsonObject.getString("img").isEmpty() || jsonObject.isNull("img")){
                                    ivWelcome.setImageResource(R.drawable.welcome);
                                    tvWelcomeName.setText(R.string.welcome_to_zhihudaily);
                                } else {
                                    Glide.with(SplashActivity.this).load(jsonObject.getString("img")).error(R.drawable.no_img).into(ivWelcome);
                                    tvWelcomeName.setText(jsonObject.getString("text"));
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

                    request.setTag(TAG);
                    queue.add(request);
                } else {
                    ivWelcome.setImageResource(R.drawable.welcome);
                    tvWelcomeName.setText(R.string.welcome_to_zhihudaily);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initViews() {

        ivWelcome = (ImageView) findViewById(R.id.iv_welcome);
        tvWelcomeName = (TextView) findViewById(R.id.tv_welcome_name);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (queue != null){
            queue.cancelAll(TAG);
        }
    }
}
