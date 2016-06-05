package com.marktony.zhihudaily.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.bean.FanfouDailyPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.ui.Views.CircleImageView;

import java.util.List;

/**
 * Created by lizhaotailang on 2016/6/3.
 */
public class FanfouDailyPostAdapter extends RecyclerView.Adapter<FanfouDailyPostAdapter.FanfouDailyPostViewHolder> {

    private List<FanfouDailyPost> list;
    private final Context context;
    private final LayoutInflater inflater;

    private OnRecyclerViewOnClickListener mListener;

    public FanfouDailyPostAdapter(Context context, List<FanfouDailyPost> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public FanfouDailyPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fanfou_item_layout,parent,false);
        return new FanfouDailyPostViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(FanfouDailyPostViewHolder holder, int position) {

        FanfouDailyPost item = list.get(position);

        Glide.with(context).load(item.getAvatarUrl()).into(holder.civAvatar);

        if (item.getImgUrl().equals("")){
            holder.ivMain.setVisibility(View.GONE);
        } else {
            holder.ivMain.setVisibility(View.VISIBLE);
        }
        holder.tvAuthor.setText(item.getAuthor());
        holder.tvContent.setText(item.getContent());

        holder.tvTime.setText(item.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class FanfouDailyPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView civAvatar;
        TextView tvAuthor;
        TextView tvContent;
        TextView tvTime;
        ImageView ivMain;

        private OnRecyclerViewOnClickListener listener;

        public FanfouDailyPostViewHolder(View itemView,OnRecyclerViewOnClickListener listener) {
            super(itemView);

            civAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivMain = (ImageView) itemView.findViewById(R.id.iv_main);

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
