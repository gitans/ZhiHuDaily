package com.marktony.zhihudaily.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marktony.zhihudaily.Adapters.HotPostAdapter;
import com.marktony.zhihudaily.Entities.HotPost;
import com.marktony.zhihudaily.Interfaces.IOnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.UI.Activities.ReadActivity;
import com.marktony.zhihudaily.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class HotPostFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private List<HotPost> list = new ArrayList<HotPost>();

    private HotPostAdapter adapter;

    private MaterialDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new MaterialDialog.Builder(getActivity())
                .content(getString(R.string.loading))
                .progress(true,0)
                .build();

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_post,container,false);

        dialog.show();

        initViews(view);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.HOT, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if ( !jsonObject.isNull("recent")){
                    try {
                        JSONArray array = jsonObject.getJSONArray("recent");
                        for (int i = 0;i < array.length();i++){
                            HotPost hotpost = new HotPost(array.getJSONObject(i).getString("news_id"),
                                    array.getJSONObject(i).getString("url"),
                                    array.getJSONObject(i).getString("title"),
                                    array.getJSONObject(i).getString("thumbnail"));

                            list.add(hotpost);
                        }

                        adapter = new HotPostAdapter(getActivity(),list);
                        recyclerView.setAdapter(adapter);
                        adapter.setItemClickListener(new IOnRecyclerViewOnClickListener() {
                            @Override
                            public void OnItemClick(View v, int position) {
                                Intent intent = new Intent(getActivity(),ReadActivity.class);
                                intent.putExtra("id",list.get(position).getNews_id());
                                intent.putExtra("title",list.get(position).getTitle());
                                intent.putExtra("image",list.get(position).getThumbnail());
                                startActivity(intent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(recyclerView,getString(R.string.wrong_process),Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        queue.add(request);

        return view;
    }

    private void initViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_hot_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
