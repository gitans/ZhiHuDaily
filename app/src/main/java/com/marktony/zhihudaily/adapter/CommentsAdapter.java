package com.marktony.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marktony.zhihudaily.bean.Comment;
import com.marktony.zhihudaily.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhaotailang on 2016/5/18.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private List<Comment> list;
    private final Context context;
    private final LayoutInflater inflater;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public CommentsAdapter(Context context,List<Comment> list){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_item,parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment comment = list.get(position);

        Glide.with(context)
                .load(comment.getAvatarUrl())
                .asBitmap()
                .into(holder.ivAvatar);

        holder.tvComment.setText(comment.getComment());
        holder.tvAuthor.setText(comment.getAuthor());

        // 注意这里的时间戳一定要乘以1000，在这里被坑了好久
        // attention,timestamp must * 1000
        Date date = new Date(Long.valueOf(comment.getTime())*1000 );
        holder.tvTime.setText(format.format(date));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvAuthor;
        private TextView tvComment;
        private TextView tvTime;

        public CommentsViewHolder(View itemView) {
            super(itemView);

            ivAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
