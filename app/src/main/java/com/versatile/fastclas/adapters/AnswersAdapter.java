package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.activities.AnswersActivity;
import com.versatile.fastclas.models.AnswersModel;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 02-01-2018.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.MyHolder> {
    Context context;
    ArrayList<AnswersModel> answersModelArrayList;

    public AnswersAdapter(Context context, ArrayList<AnswersModel> answersModelArrayList) {
        this.context = context;
        this.answersModelArrayList = answersModelArrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_answer, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final AnswersModel answersModel = answersModelArrayList.get(position);

        holder.textUserName.setText(answersModel.getUser_name());
        holder.textAnswer.setText(answersModel.getAnswer());


        if (answersModel.getUser_name().equals("Admin")) {
            holder.iv_admin.setVisibility(View.VISIBLE);
            holder.textUserLetter.setVisibility(View.GONE);
        } else {
            holder.iv_admin.setVisibility(View.GONE);
            holder.textUserLetter.setVisibility(View.VISIBLE);
            Character character = answersModel.getUser_name().charAt(0);
            holder.textUserLetter.setText(character.toString());
        }

    }

    @Override
    public int getItemCount() {
        return answersModelArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView textUserName, textAnswer, textUserLetter;
        ImageView iv_admin;

        public MyHolder(View itemView) {
            super(itemView);
            iv_admin = itemView.findViewById(R.id.iv_admin);
            textAnswer = itemView.findViewById(R.id.textAnswer);
            textUserName = itemView.findViewById(R.id.textUserName);
            textUserLetter = itemView.findViewById(R.id.textUserLetter);
        }
    }
}
