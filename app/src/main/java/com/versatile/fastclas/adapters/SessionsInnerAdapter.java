package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.R;
import com.versatile.fastclas.models.SessionsInnerModel;

import java.util.ArrayList;

/**
 * Created by USER on 13-12-2017.
 */

public class SessionsInnerAdapter extends RecyclerView.Adapter<SessionsInnerAdapter.MyHolder> {
    Context context;
    ArrayList<SessionsInnerModel> sessionsInnerModels;
    private OnItemClickListener listener;

    public SessionsInnerAdapter(Context context, ArrayList<SessionsInnerModel> sessionsInnerModels, OnItemClickListener listener) {
        this.context = context;
        this.sessionsInnerModels = sessionsInnerModels;
        this.listener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sessions_inner, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final SessionsInnerModel sessionsInnerModel = sessionsInnerModels.get(position);
        holder.bind(sessionsInnerModel, listener);

        if (sessionsInnerModel.status.equals("Watched")) {
            holder.mTxtStatus.setVisibility(View.VISIBLE);
            holder.mTxtStatus.setText(sessionsInnerModel.status);
        } else {
            holder.mTxtStatus.setVisibility(View.GONE);
            holder.mImgWatchReplay.setImageResource(R.drawable.play_icon);
        }
        holder.mTxtDescription.setText(sessionsInnerModel.getDescription());
        holder.mImgYoutube.setImageResource(R.drawable.video_image1);
    }

    @Override
    public int getItemCount() {
        return sessionsInnerModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTxtDescription, mTxtStatus, mTxtReadMore;
        ImageButton mImgWatchReplay;
        ImageView mImgYoutube;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTxtStatus = (TextView) itemView.findViewById(R.id.textStatus);
            mTxtDescription = (TextView) itemView.findViewById(R.id.textDescription);
            mTxtReadMore = (TextView) itemView.findViewById(R.id.textReadMore);
            mImgWatchReplay = (ImageButton) itemView.findViewById(R.id.imgWatchReplay);
            mImgYoutube = (ImageView) itemView.findViewById(R.id.imgYoutube);
        }

        public void bind(final SessionsInnerModel sessionsInnerModel, final OnItemClickListener listener) {
            mTxtReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(sessionsInnerModel, getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(SessionsInnerModel sessionsInnerModel, int Position);
    }
}
