package com.versatile.fastclas.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.versatile.fastclas.activities.AnswersActivity;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.models.CommentsModel;
import com.versatile.fastclas.models.DiscussionForumModel;

import java.util.ArrayList;

/**
 * Created by USER on 14-12-2017.
 */

public class DiscussionForumAdapter extends RecyclerView.Adapter<DiscussionForumAdapter.MyHolder> {
    Context context;
    ArrayList<DiscussionForumModel> discussionForumModels;

    public DiscussionForumAdapter(Context context, ArrayList<DiscussionForumModel> discussionForumModels) {
        this.context = context;
        this.discussionForumModels = discussionForumModels;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_discussion_forum, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final DiscussionForumModel discussionForumModel = discussionForumModels.get(position);

        holder.mTextName.setText(discussionForumModel.getUser_name());
        holder.mTextPostedTime.setText(discussionForumModel.getPost_on());
        holder.mTextQuestion.setText(discussionForumModel.getQuestion());
        holder.mTextAnswerCount.setText(discussionForumModel.getNumber_of_answers() + " Answer");
        Character character = discussionForumModel.getUser_name().charAt(0);
        holder.mTextUserLetter.setText(character.toString());
        holder.txtAnswer.setText(discussionForumModel.getNumber_of_answers());
        holder.txtAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnswersActivity.class);
                intent.putExtra("user_name", discussionForumModel.getUser_name());
                intent.putExtra("post_on", discussionForumModel.getPost_on());
                intent.putExtra("question", discussionForumModel.getQuestion());
                intent.putExtra("question_id", discussionForumModel.getQuestion_id());
                intent.putExtra("session_id", discussionForumModel.getSession_id());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });


    }

    @Override
    public int getItemCount() {
        return discussionForumModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTextName, mTextPostedTime, mTextQuestion, mTextAnswerCount, mTextUserLetter, txtAnswer;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTextName = itemView.findViewById(R.id.textName);
            mTextPostedTime = itemView.findViewById(R.id.textPostedTime);
            mTextQuestion = itemView.findViewById(R.id.textQuestion);
            mTextAnswerCount = itemView.findViewById(R.id.textAnswerCount);
            mTextUserLetter = itemView.findViewById(R.id.textUserLetter);
            txtAnswer = itemView.findViewById(R.id.txtAnswer);
        }


    }
}
