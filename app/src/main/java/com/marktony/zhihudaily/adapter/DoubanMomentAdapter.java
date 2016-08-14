package com.marktony.zhihudaily.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.DoubanMomentPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentAdapter extends RecyclerView.Adapter<DoubanMomentAdapter.DoubanMomentViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<DoubanMomentPost> list;

    private OnRecyclerViewOnClickListener listener;

    public DoubanMomentAdapter(@NonNull Context context, @NonNull ArrayList<DoubanMomentPost> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public DoubanMomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DoubanMomentViewHolder(inflater.inflate(R.layout.guokr_douban_post_layout, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(DoubanMomentViewHolder holder, int position) {
        DoubanMomentPost item = list.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvSummary.setText(item.getAbs());
        Glide.with(context).load(item.getThumb()).asBitmap().error(R.drawable.no_img).centerCrop().into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.listener = listener;
    }

    public class DoubanMomentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnRecyclerViewOnClickListener listener;
        ImageView imageView;
        TextView tvTitle;
        TextView tvSummary;

        public DoubanMomentViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSummary = (TextView) itemView.findViewById(R.id.tv_summary);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

}
