package com.marktony.zhihudaily.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marktony.zhihudaily.Entities.HotPost;

import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/26.
 */
public class HotPostAdapter extends RecyclerView.Adapter<HotPostAdapter.HotPostViewHolder>{

    private List<HotPost> list;
    private final Context context;
    private final LayoutInflater inflater;

    public HotPostAdapter(Context context,LayoutInflater inflater){
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public HotPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(HotPostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class HotPostViewHolder extends RecyclerView.ViewHolder {
        public HotPostViewHolder(View itemView) {
            super(itemView);
        }
    }
}
