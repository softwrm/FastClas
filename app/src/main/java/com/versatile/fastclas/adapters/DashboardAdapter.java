package com.versatile.fastclas.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.versatile.fastclas.activities.DashBoardActivity;
import com.versatile.fastclas.activities.DashBoardCourseActivity;
import com.versatile.fastclas.models.UniversityModel;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 29-01-2018.
 */

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyHldr> {

    Context context;
    ArrayList<UniversityModel> universityModelArrayList;

    public DashboardAdapter(Context context, ArrayList<UniversityModel> universityModelArrayList) {
        this.context = context;
        this.universityModelArrayList = universityModelArrayList;
    }

    @Override
    public MyHldr onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_dashboard, parent, false);
        return new MyHldr(view);
    }

    @Override
    public void onBindViewHolder(MyHldr holder, int position) {
        final UniversityModel universityModel = universityModelArrayList.get(position);
        holder.txtDashboardUniversity.setText(universityModel.getUniversity_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DashBoardCourseActivity.class);
                intent.putExtra("university_id", universityModel.getUniversity_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return universityModelArrayList.size();
    }

    public class MyHldr extends RecyclerView.ViewHolder {
        TextView txtDashboardUniversity;

        public MyHldr(View itemView) {
            super(itemView);
            txtDashboardUniversity = itemView.findViewById(R.id.txtDashboardUniversity);
        }
    }
}
