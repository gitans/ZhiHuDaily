package com.marktony.zhihudaily.ui.Fragments;

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
import com.marktony.zhihudaily.Adapters.FanfouDailyPostAdapter;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.FanfouDailyPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/6/3.
 * 饭否精选
 */
public class FanfouFragment extends Fragment{

    private RecyclerView rvFanfouDaily;
    private RequestQueue queue;
    private List<FanfouDailyPost> list = new ArrayList<FanfouDailyPost>();

    private FanfouDailyPostAdapter adapter;

    private MaterialDialog dialog;

    public static final String TAG = "FANFOU_DAILY";

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
        View view = inflater.inflate(R.layout.fragment_fanfou,container,false);

        dialog.show();

        initViews(view);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://blog.fanfou.com/digest/json/2015-11-13.daily.json", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("msgs");
                    for (int i = 0; i < array.length(); i++){
                        JSONObject o = array.getJSONObject(i);
                        FanfouDailyPost item = new FanfouDailyPost(o.getString("avatar"),
                                o.getString("realname"),
                                o.getString("msg"),
                                o.getString("time"));

                        list.add(item);
                    }

                    adapter = new FanfouDailyPostAdapter(getActivity(),list);
                    rvFanfouDaily.setAdapter(adapter);

                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(rvFanfouDaily,getString(R.string.wrong_process),Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        request.setTag(TAG);
        queue.add(request);

        return view;
    }

    private void initViews(View view) {

        rvFanfouDaily = (RecyclerView) view.findViewById(R.id.rv_fanfou);
        rvFanfouDaily.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

}
