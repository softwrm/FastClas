package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.versatilemobitech.fastclas.R;
import com.versatile.fastclas.models.SessionsModel;
import java.util.ArrayList;

/**
 * Created by USER on 13-12-2017.
 */

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.MyHolder> {
    Context context;
    ArrayList<SessionsModel> sessionPojos;
    private OnItemClickListener listener;

    public SessionsAdapter(Context context, ArrayList<SessionsModel> sessionPojos, OnItemClickListener listener) {
        this.context = context;
        this.sessionPojos = sessionPojos;
        this.listener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_sessions, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        final SessionsModel sessionPojo = sessionPojos.get(position);
        holder.bind(sessionPojo, listener);

        holder.mTextLabel.setText("Session "+sessionPojo.getSessionNumber());
        holder.mTextPages.setText(sessionPojo.getItemsViewed() + "/" + sessionPojo.getItemCount()+" Pages");
        holder.mTxtDescription.setText(sessionPojo.getSessionTitle());
    }

    @Override
    public int getItemCount() {
        return sessionPojos.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView mTextLabel, mTextPages, mTxtDescription;

        public MyHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            mTextLabel = (TextView) itemView.findViewById(R.id.textSessionLabel);
            mTextPages = (TextView) itemView.findViewById(R.id.textPages);
            mTxtDescription = (TextView) itemView.findViewById(R.id.textDescription);
        }

        public void bind(final SessionsModel sessionPojo, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(sessionPojo, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(SessionsModel sessionPojo, int Position);
    }
}
