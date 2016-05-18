package com.marktony.zhihudaily.UI.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.Adapters.CommentsAdapter;
import com.marktony.zhihudaily.Entities.Comment;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;
import com.marktony.zhihudaily.Utils.UtilFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvComments;

    private String id;

    private List<Comment> list = new ArrayList<Comment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        UtilFunctions.setTheme(CommentsActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        initViews();

        id = getIntent().getStringExtra("id");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.COMMENTS + id +"/" + "long-comments", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("comments");
                    if (array.length()!=0){
                        for (int i = array.length()-1;i >= 0; i--){
                            JSONObject o = array.getJSONObject(i);
                            Comment item = new Comment(o.getString("avatar"),
                                    o.getString("author"),
                                    o.getString("content"),
                                    o.getString("time"),
                                    o.getString("likes"));
                            list.add(item);
                        }
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

        Volley.newRequestQueue(CommentsActivity.this.getApplicationContext()).add(request);

        JsonObjectRequest re = new JsonObjectRequest(Request.Method.GET, Api.COMMENTS + id +"/" + "short-comments", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("comments");
                    if (array.length()!=0){
                        for (int i = array.length()-1;i >= 0 ;i--){
                            JSONObject o = array.getJSONObject(i);
                            Comment item = new Comment(o.getString("avatar"),
                                    o.getString("author"),
                                    o.getString("content"),
                                    o.getString("time"),
                                    o.getString("likes"));
                            list.add(item);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                CommentsAdapter adapter = new CommentsAdapter(CommentsActivity.this,list);
                rvComments.setAdapter(adapter);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        Volley.newRequestQueue(CommentsActivity.this.getApplicationContext()).add(re);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvComments = (RecyclerView) findViewById(R.id.rv_comments);
        rvComments.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
    }


}
