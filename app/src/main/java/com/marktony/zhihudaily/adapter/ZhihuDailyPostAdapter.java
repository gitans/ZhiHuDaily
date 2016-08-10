package com.marktony.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.bean.ZhihuDailyPost;
import com.marktony.zhihudaily.interfaces.OnRecyclerViewOnClickListener;
import com.marktony.zhihudaily.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/3/18.
 * 知乎日报消息适配器
 * latest posts adapter
 */
public class ZhihuDailyPostAdapter extends RecyclerView.Adapter<ZhihuDailyPostAdapter.LatestItemViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private List<ZhihuDailyPost> list = new ArrayList<ZhihuDailyPost>();
    private OnRecyclerViewOnClickListener mListener;

    public ZhihuDailyPostAdapter(Context context, List<ZhihuDailyPost> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public LatestItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.universal_item_layout,parent,false);
        return new LatestItemViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(LatestItemViewHolder holder, int position) {
        ZhihuDailyPost item = list.get(position);

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

        public LatestItemViewHolder(View itemView,OnRecyclerViewOnClickListener listener) {
            super(itemView);

            itemImg = (ImageView) itemView.findViewById(R.id.universal_item_iv);
            tvLatestNewsTitle = (TextView) itemView.findViewById(R.id.universal_item_tv_title);
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
