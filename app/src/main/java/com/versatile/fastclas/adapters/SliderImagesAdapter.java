package com.versatile.fastclas.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.versatile.fastclas.activities.FullImagesSlidingActivity;
import com.versatile.fastclas.utils.Utility;
import com.otelpt.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 02-02-2018.
 */

public class SliderImagesAdapter extends PagerAdapter {
    Context context;
    private ArrayList<String> getData;
    private LayoutInflater layoutInflater;

    public SliderImagesAdapter(Context context, ArrayList<String> getData) {
        this.context = context;
        this.getData = getData;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return getData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = this.layoutInflater.inflate(R.layout.slider_images_layout, container, false);
        ImageView touchImageView = (ImageView) view.findViewById(R.id.touchImageView);
//        TextView imageText = (TextView) view.findViewById(R.id.txt_imagescount);

        touchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullImagesSlidingActivity.class);
                intent.putStringArrayListExtra("imageArrayList", getData);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
        Utility.showLog("pics", "" + getData.get(position));
        Picasso.with(context)
                .load("" + getData.get(position))
                .placeholder(R.drawable.app_icon)
                .into(touchImageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

