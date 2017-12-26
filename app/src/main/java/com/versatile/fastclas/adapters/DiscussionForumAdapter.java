package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.versatile.fastclas.R;
import com.versatile.fastclas.models.CommentsModel;
import com.versatile.fastclas.models.DiscussionForumModel;

import java.util.ArrayList;

/**
 * Created by USER on 14-12-2017.
 */

public class DiscussionForumAdapter extends RecyclerView.Adapter<DiscussionForumAdapter.MyHolder> {
    Context context;
    ArrayList<DiscussionForumModel> discussionForumModels;
    private OnItemClickListener listener;

    public DiscussionForumAdapter(Context context, ArrayList<DiscussionForumModel> discussionForumModels, OnItemClickListener listener) {
        this.context = context;
        this.discussionForumModels = discussionForumModels;
        this.listener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_discussion_forum, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final DiscussionForumModel discussionForumModel = discussionForumModels.get(position);
        holder.bind(discussionForumModel, listener);

        holder.mTextName.setText(discussionForumModel.user_name);
        holder.mTextPostedTime.setText(discussionForumModel.posted_time);
        holder.mTxtHeading.setText(discussionForumModel.session_name);
        holder.mTextQuestion.setText(discussionForumModel.question);
        holder.mTextAnswerCount.setText(discussionForumModel.answer_count + " Answer");
        holder.mTxtHeading.setText(discussionForumModel.session_name);
        Character character = discussionForumModel.user_name.charAt(0);
        holder.mTextUserLetter.setText(character.toString());

        ArrayList<CommentsModel> commentsModels = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(discussionForumModel.answer_count); i++) {
            commentsModels.add(new CommentsModel(discussionForumModel.user_name_arraylist.get(i),
                    discussionForumModel.answers_arraylist.get(i)));
        }

        DiscussionForumInnerAdapter discussionForumInnerAdapter = new DiscussionForumInnerAdapter(context, commentsModels, null);
        holder.mRecyclerViewInner.setLayoutManager(new LinearLayoutManager(context));
        holder.mRecyclerViewInner.setHasFixedSize(true);
        holder.mRecyclerViewInner.setAdapter(discussionForumInnerAdapter);
    }

    @Override
    public int getItemCount() {
        return discussionForumModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTextName, mTextPostedTime, mTxtHeading, mTextQuestion, mTextAnswerCount, mTextPost, mTextUserLetter;
        EditText mEdtComment;
        ImageView mImgUser;
        RecyclerView mRecyclerViewInner;
        LinearLayout mLayoutComents;
        ImageButton mImgMore;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTextName = (TextView) itemView.findViewById(R.id.textName);
            mTextPostedTime = (TextView) itemView.findViewById(R.id.textPostedTime);
            mTxtHeading = (TextView) itemView.findViewById(R.id.textHeading);
            mTextQuestion = (TextView) itemView.findViewById(R.id.textQuestion);
            mTextAnswerCount = (TextView) itemView.findViewById(R.id.textAnswerCount);
            mTextPost = (TextView) itemView.findViewById(R.id.textPost);
            mEdtComment = (EditText) itemView.findViewById(R.id.edtComments);
            mImgUser = (ImageView) itemView.findViewById(R.id.imgUser);
            mTextUserLetter = (TextView) itemView.findViewById(R.id.textUserLetter);
            mImgMore = (ImageButton) itemView.findViewById(R.id.imgMore);
            mRecyclerViewInner = (RecyclerView) itemView.findViewById(R.id.recyclerViewInner);
            mLayoutComents = (LinearLayout) itemView.findViewById(R.id.layoutComments);
        }

        public void bind(final DiscussionForumModel discussionForumModel, final OnItemClickListener listener) {
            mImgMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    listener.onItemClick(discussionForumModel, getAdapterPosition());
                    if (mLayoutComents.getVisibility() == View.VISIBLE) {
                        mLayoutComents.setVisibility(View.GONE);
                    } else if (mLayoutComents.getVisibility() == View.GONE) {
                        mLayoutComents.setVisibility(View.VISIBLE);
                        mRecyclerViewInner.setFocusable(false);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DiscussionForumModel discussionForumModel, int Position);
    }
}
