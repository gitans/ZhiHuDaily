package com.marktony.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.GuokrHandpickPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/6/14.
 */
public class GuokrPostAdapter extends RecyclerView.Adapter<GuokrPostAdapter.GuokrPostViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<GuokrHandpickPost> list;

    private OnRecyclerViewOnClickListener mListener;

    public GuokrPostAdapter(Context context, ArrayList<GuokrHandpickPost> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public GuokrPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.universal_item_layout,parent,false);

        return new GuokrPostViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(GuokrPostViewHolder holder, int position) {

        GuokrHandpickPost item = list.get(position);

        Glide.with(context)
                .load(item.getHeadlineImg())
                .asBitmap()
                .centerCrop()
                .into(holder.ivHeadlineImg);

        holder.tvTitle.setText(item.getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class GuokrPostViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {

        ImageView ivHeadlineImg;
        TextView tvTitle;

        OnRecyclerViewOnClickListener listener;

        public GuokrPostViewHolder(View itemView,OnRecyclerViewOnClickListener listener) {
            super(itemView);

            ivHeadlineImg = (ImageView) itemView.findViewById(R.id.universal_item_iv);
            tvTitle = (TextView) itemView.findViewById(R.id.universal_item_tv_title);

            this.listener = listener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.OnItemClick(v,getLayoutPosition());
            }
        }

    }

}
