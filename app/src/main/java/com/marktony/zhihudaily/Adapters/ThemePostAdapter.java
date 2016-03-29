package com.marktony.zhihudaily.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.Entities.ThemePost;
import com.marktony.zhihudaily.Interfaces.IOnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;

import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/24.
 */
public class ThemePostAdapter  extends RecyclerView.Adapter<ThemePostAdapter.ThemePostViewHolder>{

    private final List<ThemePost> list;
    private final LayoutInflater inflater;
    private Context context;
    private IOnRecyclerViewOnClickListener mListener;

    public ThemePostAdapter(Context context, List<ThemePost> list){
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ThemePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.latest_item_layout,parent,false);
        ThemePostViewHolder holder = new ThemePostViewHolder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ThemePostViewHolder holder, int position) {
        ThemePost themePost = list.get(position);
        if (themePost.getFirstImg() == null){
            holder.ivItemImg.setImageResource(R.drawable.no_img);
        } else {
            Glide.with(context).load(themePost.getFirstImg()).centerCrop().into(holder.ivItemImg);
        }

        holder.tvLatestNewsTitle.setText(themePost.getTitle());

        setAnimation(holder.item,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(IOnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class ThemePostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivItemImg;
        private TextView tvLatestNewsTitle;
        private IOnRecyclerViewOnClickListener listener;
        private CardView item;

        public ThemePostViewHolder(View itemView,IOnRecyclerViewOnClickListener listener) {
            super(itemView);

            ivItemImg = (ImageView) itemView.findViewById(R.id.latest_item_iv);
            tvLatestNewsTitle = (TextView) itemView.findViewById(R.id.latest_item_tv_title);
            item = (CardView) itemView.findViewById(R.id.card_view_item);
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

    private void setAnimation(View viewToAnimation,int position){
        if (position > -1){
            Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
            viewToAnimation.startAnimation(animation);
        }
    }

}
