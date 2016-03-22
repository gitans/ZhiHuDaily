package com.marktony.zhihudaily.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.marktony.zhihudaily.Adapters.ThemePagerAdapter;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class ThemeFragment extends Fragment {

    private RequestQueue queue;
    private String[] titles;
    private int titleCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme,container,false);

        final ViewPager pager = (ViewPager) view.findViewById(R.id.viewPager);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.THEMES, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (!jsonObject.getString("limit").isEmpty()){
                        JSONArray array = jsonObject.getJSONArray("others");
                        titleCount = array.length();
                        titles = new String[titleCount];
                        for (int i = 0;i < array.length();i++){
                            titles[i] = array.getJSONObject(i).getString("name");
                            Log.d("name",titles[i]);
                        }
                        ThemePagerAdapter adapter = new ThemePagerAdapter(getActivity().getSupportFragmentManager(),getActivity(),titles,titleCount);
                        pager.setAdapter(adapter);

                        tabLayout.setupWithViewPager(pager);
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



        return view;
    }

}
