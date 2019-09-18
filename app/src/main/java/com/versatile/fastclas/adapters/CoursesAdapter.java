package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.versatile.fastclas.models.CourseModel;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 29-01-2018.
 */

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.MyHolder> {
    Context context;
    ArrayList<CourseModel> courseModelArrayList;

    public CoursesAdapter(Context context, ArrayList<CourseModel> courseModelArrayList) {
        this.context = context;
        this.courseModelArrayList = courseModelArrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CourseModel courseModel = courseModelArrayList.get(position);
        holder.textSubject.setText(courseModel.getCourseName());
    }

    @Override
    public int getItemCount() {
        return courseModelArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView textSubject;

        public MyHolder(View itemView) {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
        }
    }
}
