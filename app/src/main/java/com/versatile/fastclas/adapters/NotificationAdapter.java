package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otelpt.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 05-01-2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyHolder> {
    Context context;
    ArrayList<String> messageArrayList;

    public NotificationAdapter(Context context, ArrayList<String> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.txtMessage.setText(messageArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;

        public MyHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }
}
