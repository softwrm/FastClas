package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.versatile.fastclas.models.PackagesModel;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

/**
 * Created by Excentd11 on 4/16/2018.
 */

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.VideoHolder> implements RecyclerView.OnItemTouchListener {
    private Context mContext;
    private PackagesAdapter.OnItemClickListener mListener;
    private GestureDetector mGestureDetector;
    private ArrayList<PackagesModel> mPackagesModel;
    private int slotPosition = -1;

    public PackagesAdapter(Context context, ArrayList<PackagesModel> mPackagesModel) {
        mContext = context;
        this.mPackagesModel = mPackagesModel;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public PackagesAdapter(Context context, PackagesAdapter.OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public PackagesAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_packages, parent, false);
        return new PackagesAdapter.VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(final PackagesAdapter.VideoHolder holder, final int position) {
        final PackagesModel mPackagesModel = this.mPackagesModel.get(position);

        String subjects = "";

        holder.mTxtPackageName.setText(mPackagesModel.packageName);

        for (int i = 0; i < mPackagesModel.mSubjectsModel.size(); i++) {
            if (subjects.length() > 0) {
                subjects = subjects + ", " + mPackagesModel.mSubjectsModel.get(i).subjectName;
            } else {
                subjects = subjects + mPackagesModel.mSubjectsModel.get(i).subjectName;
            }
        }
        holder.mTxtSubjectName.setText(subjects);

        holder.mTxtDescription.setText(mPackagesModel.description);
        holder.mTxtAmount.setText(mPackagesModel.amount + "/-");

        if (slotPosition == position) {
            holder.mTxtActive.setBackgroundResource(R.drawable.package_active);
            holder.mTxtActive.setTextColor(ActivityCompat.getColor(mContext, R.color.color_white));
        } else {
            holder.mTxtActive.setBackgroundResource(R.drawable.package_inactive);
            holder.mTxtActive.setTextColor(ActivityCompat.getColor(mContext, R.color.active_text));
        }
        holder.mTxtActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slotPosition == position) {
                    holder.mTxtActive.setBackgroundResource(R.drawable.package_inactive);
                    holder.mTxtActive.setTextColor(ActivityCompat.getColor(mContext, R.color.active_text));
                    slotPosition = -1;
                } else {
                    slotPosition = position;
                    holder.mTxtActive.setBackgroundResource(R.drawable.package_active);
                    holder.mTxtActive.setTextColor(ActivityCompat.getColor(mContext, R.color.color_white));
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPackagesModel.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        TextView mTxtSubjectName, mTxtPackageName, mTxtDescription, mTxtAmount, mTxtActive;

        public VideoHolder(View view) {
            super(view);
            mTxtSubjectName = view.findViewById(R.id.txtSubjectName);
            mTxtPackageName = view.findViewById(R.id.txtPackageName);
            mTxtDescription = view.findViewById(R.id.txtDescription);
            mTxtAmount = view.findViewById(R.id.txtAmount);
            mTxtActive = view.findViewById(R.id.txtActive);
        }
    }

    public int selectedPackagePosition() {
        return slotPosition;
    }
}