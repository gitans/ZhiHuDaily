package com.marktony.zhihudaily.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.Entities.Themes;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/21.
 * 主题日报下viewpager部分
 */
public class PageFragment extends android.support.v4.app.Fragment {

    public static final String ARGS_PAGE = "args_page";
    private int pages;

    private RequestQueue queue;

    private List<String> ids = new ArrayList<String>();
    private List<String> thumbnails = new ArrayList<String>();

    private ImageView ivTheme;
    private RecyclerView rvThemePosts;

    public static PageFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE,page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PageFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pages = getArguments().getInt(ARGS_PAGE);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.theme_page,container,false);

        ivTheme = (ImageView) view.findViewById(R.id.iv_theme);
        rvThemePosts = (RecyclerView) view.findViewById(R.id.rv_theme_post);

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, Api.THEMES, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                        if (!jsonObject.getString("limit").isEmpty()) {
                            JSONArray array = jsonObject.getJSONArray("others");

                            for (int i = 0; i < array.length(); i++) {
                                String id = array.getJSONObject(i).getString("id");
                                String thumbnail = array.getJSONObject(i).getString("thumbnail");

                                ids.add(id);
                                thumbnails.add(thumbnail);
                            }

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.THEME + ids.get(pages-1), new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    try {
                                        if (jsonObject.has("stories")){
                                            Log.d("res",jsonObject.toString());
                                            Glide.with(getActivity()).load(jsonObject.getString("image")).into(ivTheme);
                                        } else {
                                            Log.d("res",jsonObject.toString());
                                        }
                                    }catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });

                            queue.add(request);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                Log.d("ids",ids.toString());
                Log.d("thums",thumbnails.toString());
                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        queue.add(request1);

        Toast.makeText(getActivity(),String.valueOf(pages),Toast.LENGTH_SHORT).show();



        return view;
    }

}
