package com.marktony.zhihudaily.ui.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.Adapters.GuokrPostAdapter;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.GuokrHandpickPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.ui.Activities.GuokrReadActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/6/13.
 * 果壳精选
 * guokr handpick
 */
public class GuokrFragment extends Fragment {

    private RecyclerView rvGuokr;

    private RequestQueue queue;

    private ArrayList<GuokrHandpickPost> guokrList = new ArrayList<GuokrHandpickPost>();

    private GuokrPostAdapter adapter;

    private static final String TAG = "GUOKR";

    // require an empty constructor
    public GuokrFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guokr,container,false);

        initViews(view);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://apis.guokr.com/handpick/article.json?retrieve_type=by_since&category=all&limit=20&ad=1", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    if (jsonObject.getString("ok").equals("true")){
                        JSONArray array = jsonObject.getJSONArray("result");
                        for (int i = 0; i < array.length(); i++){
                            JSONObject o = array.getJSONObject(i);
                            GuokrHandpickPost item = new GuokrHandpickPost(
                                    o.getString("id"),
                                    o.getString("title"),
                                    o.getString("headline_img"));

                            guokrList.add(item);
                        }
                    }

                    if (guokrList.size() != 0){

                        adapter = new GuokrPostAdapter(getActivity(),guokrList);
                        adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                            @Override
                            public void OnItemClick(View v, int position) {

                                startActivity(new Intent(getActivity(), GuokrReadActivity.class).putExtra("id",guokrList.get(position).getId()));

                            }
                        });

                        rvGuokr.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(rvGuokr,R.string.wrong_process,Snackbar.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        queue.add(request);

        return view;
    }

    private void initViews(View view) {

        rvGuokr = (RecyclerView) view.findViewById(R.id.rv_guokr_handpick);
        rvGuokr.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onStop() {
        super.onStop();

        if (queue != null){
            queue.cancelAll(TAG);
        }
    }
}
