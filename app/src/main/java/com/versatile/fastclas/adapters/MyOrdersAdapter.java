package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.versatile.fastclas.activities.MyOrdersActivity;
import com.versatile.fastclas.models.SubjectModel;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 29-01-2018.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyHlder> {
    Context context;
    ArrayList<SubjectModel> subjectModelArrayList;

    public MyOrdersAdapter(Context context, ArrayList<SubjectModel> subjectModelArrayList) {
        this.context = context;
        this.subjectModelArrayList = subjectModelArrayList;
    }

    @Override
    public MyHlder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new MyHlder(v);
    }

    @Override
    public void onBindViewHolder(MyHlder holder, int position) {
        final SubjectModel subjectModel = subjectModelArrayList.get(position);
        switch (position) {
            case 0:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
            case 1:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.indigo));
                break;
            case 2:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.deeppurple));
                break;
            case 3:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.lightblue));
                break;
            case 4:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.cyan));
                break;
            case 5:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.teal));
                break;
            case 6:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.green));
                break;
            case 7:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.blue));
                break;
            case 8:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                break;
            case 9:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.teal));
                break;
            case 10:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.lightblue));
                break;
            case 11:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.cyan));
                break;
            case 12:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.teal));
                break;
            case 13:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.green));
                break;
            default:
                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.color_blue));

        }

        holder.mTextSubject.setText(subjectModel.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return subjectModelArrayList.size();
    }

    public class MyHlder extends RecyclerView.ViewHolder {
        TextView mTextSubject;
        public MyHlder(View itemView) {
            super(itemView);
            mTextSubject =  itemView.findViewById(R.id.textSubject);
        }
    }
}
