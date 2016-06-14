package com.marktony.zhihudaily.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.bean.HotPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/26.
 * 热门日报adapter
 */
public class HotPostAdapter extends RecyclerView.Adapter<HotPostAdapter.HotPostViewHolder>{

    private List<HotPost> list = new ArrayList<HotPost>();
    private final Context context;
    private final LayoutInflater inflater;

    private OnRecyclerViewOnClickListener mListener;

    public HotPostAdapter(Context context,List<HotPost> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public HotPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.universal_item_layout,parent,false);
        HotPostViewHolder holder = new HotPostViewHolder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(HotPostViewHolder holder, int position) {
        HotPost hotPost = list.get(position);
        if (hotPost.getThumbnail() == null){
            holder.ivThumbnail.setImageResource(R.drawable.no_img);
        } else {
            Glide.with(context).load(hotPost.getThumbnail()).centerCrop().into(holder.ivThumbnail);
        }
        holder.tvTitle.setText(hotPost.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class HotPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivThumbnail;
        private TextView tvTitle;
        private OnRecyclerViewOnClickListener listener;
        private CardView item;

        public HotPostViewHolder(View itemView,OnRecyclerViewOnClickListener listener) {
            super(itemView);

            ivThumbnail = (ImageView) itemView.findViewById(R.id.universal_item_iv);
            tvTitle = (TextView) itemView.findViewById(R.id.universal_item_tv_title);
            this.listener = listener;
            itemView.setOnClickListener(this);

            item = (CardView) itemView.findViewById(R.id.card_view_item);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.OnItemClick(v,getLayoutPosition());
            }
        }
    }

}
