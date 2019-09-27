package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otelpt.fastclas.R;
import com.versatile.fastclas.models.CommentsModel;

import java.util.ArrayList;

/**
 * Created by USER on 14-12-2017.
 */

public class DiscussionForumInnerAdapter extends RecyclerView.Adapter<DiscussionForumInnerAdapter.MyHolder> {
    Context context;
    ArrayList<CommentsModel> commentsModels;
    private OnItemClickListener listener;

    public DiscussionForumInnerAdapter(Context context, ArrayList<CommentsModel> commentsModels, OnItemClickListener listener) {
        this.context = context;
        this.commentsModels = commentsModels;
        this.listener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_discussion_forum_inner, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final CommentsModel commentsModel = commentsModels.get(position);
        holder.bind(commentsModel, listener);

        holder.mTextUsername.setText(commentsModel.user_name);
        holder.mTextAnswer.setText(commentsModel.answer);
        Character character = commentsModel.user_name.charAt(0);
        holder.mTextUserLetter.setText(character.toString());
        holder.mTextUserLetter.setAllCaps(true);
    }

    @Override
    public int getItemCount() {
        return commentsModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTextUsername, mTextAnswer, mTextUserLetter;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTextUsername = (TextView) itemView.findViewById(R.id.textUserName);
            mTextAnswer = (TextView) itemView.findViewById(R.id.textAnswer);
            mTextUserLetter = (TextView) itemView.findViewById(R.id.textUserLetter);
        }

        public void bind(final CommentsModel CommentsModel, final OnItemClickListener listener) {
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(CommentsModel, getAdapterPosition());
                }
            });*/

        }
    }

    public interface OnItemClickListener {
        void onItemClick(CommentsModel CommentsModel, int Position);
    }
}
