package com.versatile.fastclas.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.versatile.fastclas.adapters.CustomPageImagesAdapter;
import com.otelpt.fastclas.R;

import java.util.ArrayList;

public class FullImagesSlidingActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> imageArrayList;
    ImageView iv_cancel;
    ViewPager viewpagerImages;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_images_sliding);
        initUI();
    }

    private void initUI() {
        Intent intent = getIntent();
        imageArrayList = intent.getStringArrayListExtra("imageArrayList");
        position = intent.getIntExtra("position", 0);
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(this);


        viewpagerImages = (ViewPager) findViewById(R.id.viewpagerImages);
        CustomPageImagesAdapter mCustomPagerAdapter = new CustomPageImagesAdapter(this, imageArrayList);
        viewpagerImages.setAdapter(mCustomPagerAdapter);
        viewpagerImages.setCurrentItem(position);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel: {
                finish();
                break;
            }
        }
    }
}
