package com.marktony.zhihudaily.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.marktony.zhihudaily.Entities.ThemeList;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/21.
 */
public class ThemeFragment extends Fragment {

    private RequestQueue queue;
    private List<ThemeList> themes = new ArrayList<ThemeList>();

    private final String TAG = "ThemeFragment";

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

                        for (int i = 0;i < array.length();i++){
                            String id = array.getJSONObject(i).getString("id");
                            String thumbnail = array.getJSONObject(i).getString("thumbnail");
                            String description = array.getJSONObject(i).getString("description");
                            String name = array.getJSONObject(i).getString("name");

                            ThemeList theme = new ThemeList(id,thumbnail,description,name);
                            themes.add(theme);

                        }

                        ThemePagerAdapter adapter = new ThemePagerAdapter(getActivity().getSupportFragmentManager(),getActivity(),themes);
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
                Snackbar.make(tabLayout,getString(R.string.wrong_process),Snackbar.LENGTH_SHORT).show();
            }
        });

        request.setTag(TAG);
        queue.add(request);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        if (queue != null){
            queue.cancelAll(TAG);
        }
    }
}
