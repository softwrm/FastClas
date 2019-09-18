package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.models.AllUnitsModel;

import java.util.ArrayList;

/**
 * Created by USER on 14-12-2017.
 */

public class AllUnitsAdapter extends RecyclerView.Adapter<AllUnitsAdapter.MyHolder> {
    Context context;
    ArrayList<AllUnitsModel> allUnitsModels;
    private OnItemClickListener listener;

    public AllUnitsAdapter(Context context, ArrayList<AllUnitsModel> allUnitsModels, OnItemClickListener listener) {
        this.context = context;
        this.allUnitsModels = allUnitsModels;
        this.listener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_units, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final AllUnitsModel allUnitsModel = allUnitsModels.get(position);
        holder.bind(allUnitsModel, listener);

        holder.mTextLabel.setText(allUnitsModel.getUnitNumber());
        holder.mTextHeading.setText(allUnitsModel.getUnitTitle());
        holder.mTxtDescription.setText(allUnitsModel.getDescription());
        if (!Utility.isValueNullOrEmpty(allUnitsModel.getDueDate())) {
            holder.txtDueDate.setText("Will be updated on "+allUnitsModel.getDueDate());
        } else {
            holder.txtDueDate.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return allUnitsModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTextLabel, mTextHeading, mTxtDescription, txtDueDate;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTextLabel = itemView.findViewById(R.id.textLabel);
            mTextHeading = itemView.findViewById(R.id.textHeading);
            mTxtDescription = itemView.findViewById(R.id.textDescription);
            txtDueDate = itemView.findViewById(R.id.txtDueDate);
        }

        public void bind(final AllUnitsModel allUnitsModel, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(allUnitsModel, getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(AllUnitsModel allUnitsModel, int Position);
    }
}
