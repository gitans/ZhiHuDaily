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
import com.marktony.zhihudaily.bean.LatestPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/18.
 * 最新消息适配器
 */
public class LatestPostAdapter extends RecyclerView.Adapter<LatestPostAdapter.LatestItemViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<LatestPost> list = new ArrayList<LatestPost>();
    private OnRecyclerViewOnClickListener mListener;

    public LatestPostAdapter(Context context, List<LatestPost> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public LatestItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.universal_item_layout,parent,false);
        LatestItemViewHolder holder = new LatestItemViewHolder(view,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(LatestItemViewHolder holder, int position) {
        LatestPost item = list.get(position);

        if (item.getFirstImg() == null){
            holder.itemImg.setImageResource(R.drawable.no_img);
        } else {
            Glide.with(context)
                    .load(item.getFirstImg())
                    .error(R.drawable.no_img)
                    .centerCrop()
                    .into(holder.itemImg);
        }
        holder.tvLatestNewsTitle.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class LatestItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImg;
        private TextView tvLatestNewsTitle;
        private OnRecyclerViewOnClickListener listener;
        private CardView item;

        public LatestItemViewHolder(View itemView,OnRecyclerViewOnClickListener listener) {
            super(itemView);

            itemImg = (ImageView) itemView.findViewById(R.id.universal_item_iv);
            tvLatestNewsTitle = (TextView) itemView.findViewById(R.id.universal_item_tv_title);
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

}
