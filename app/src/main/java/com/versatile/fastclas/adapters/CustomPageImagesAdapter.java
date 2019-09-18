package com.versatile.fastclas.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.versatile.fastclas.customviews.TouchImageView;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

/**
 * Created by USER on 02-02-2018.
 */

public class CustomPageImagesAdapter extends PagerAdapter {
    Context context;
    private ArrayList<String> getData;
    private LayoutInflater layoutInflater;

    public CustomPageImagesAdapter(Context context, ArrayList<String> getData) {
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
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.images_zoom_layout, container, false);
        TouchImageView touchImageView = (TouchImageView) view.findViewById(R.id.touchImageView);
        TextView imageText = (TextView) view.findViewById(R.id.txt_imagescount);

        Picasso.with(context)
                .load("" + getData.get(position))
                .into(touchImageView);


        imageText.setText((position + 1) + " / " + getData.size());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
