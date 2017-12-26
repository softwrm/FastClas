package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.versatile.fastclas.R;
import com.versatile.fastclas.models.SubjectModel;

import java.util.ArrayList;

/**
 * Created by USER on 20-11-2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyHolder> {
    Context context;
    ArrayList<SubjectModel> subjectModels;
    private OnItemClickListener listener;

    public HomeAdapter(Context context, ArrayList<SubjectModel> subjectModels, OnItemClickListener listener) {
        this.context = context;
        this.subjectModels = subjectModels;
        this.listener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final SubjectModel subjectModel = subjectModels.get(position);
        holder.bind(subjectModel, listener);

        holder.mTextSubject.setText(subjectModel.getSubjectName());

    }

    @Override
    public int getItemCount() {
        return subjectModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTextSubject;
        LinearLayout mLayout;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTextSubject = (TextView) itemView.findViewById(R.id.textSubject);
        }

        public void bind(final SubjectModel subjectModel, final OnItemClickListener listener) {
            mTextSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(subjectModel, getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(SubjectModel subjectModel, int position);
    }
}
