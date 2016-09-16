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
import com.marktony.zhihudaily.bean.DoubanMomentNews;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lizhaotailang on 2016/8/11.
 */

public class DoubanMomentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<DoubanMomentNews.posts> list;

    private static final int TYPE_NORMAL = 0x00;
    private static final int TYPE_NO_IMG = 0x01;

    private OnRecyclerViewOnClickListener listener;

    public DoubanMomentAdapter(@NonNull Context context, @NonNull ArrayList<DoubanMomentNews.posts> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new NormalViewHolder(inflater.inflate(R.layout.guokr_douban_post_layout, parent, false), listener);
            case TYPE_NO_IMG:
                return new NoImgViewHolder(inflater.inflate(R.layout.guokr_douban_post_layout_without_img, parent, false), listener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DoubanMomentNews.posts item = list.get(position);
        if (holder instanceof NormalViewHolder) {
            Glide.with(context)
                    .load(item.getThumbs().get(0).getMedium().getUrl())
                    .asBitmap()
                    .centerCrop()
                    .into(((NormalViewHolder)holder).ivHeadlineImg);
            ((NormalViewHolder)holder).tvTitle.setText(item.getTitle());
            ((NormalViewHolder)holder).tvSummary.setText(item.getAbs());
        } else if (holder instanceof NoImgViewHolder) {
            ((NoImgViewHolder)holder).tvTitle.setText(item.getTitle());
            ((NoImgViewHolder)holder).tvSummary.setText(item.getAbs());
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getThumbs().size() == 0) {
            return TYPE_NO_IMG;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.listener = listener;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivHeadlineImg;
        TextView tvTitle;
        TextView tvSummary;

        OnRecyclerViewOnClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            ivHeadlineImg = (ImageView) itemView.findViewById(R.id.image_view);
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

    public class NoImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvSummary;

        OnRecyclerViewOnClickListener listener;

        public NoImgViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
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
