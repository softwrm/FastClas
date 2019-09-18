package com.versatile.fastclas.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.adapters.SliderImagesAdapter;
import com.versatile.fastclas.utils.Utility;
import com.versatilemobitech.fastclas.R;

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewpagerImages;
    ArrayList<String> imagesArrayList = new ArrayList<>();
    String description, image_one, image_two, image_three, image_four, image_five;
    ImageView imgBack;
    TextView txtToolbar;
    TextView txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);


        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        image_one = intent.getStringExtra("one");
        image_two = intent.getStringExtra("two");
        image_three = intent.getStringExtra("three");
        image_four = intent.getStringExtra("four");
        image_five = intent.getStringExtra("five");

        if (!Utility.isValueNullOrEmpty(image_one)) {
            imagesArrayList.add(image_one);
        }
        if (!Utility.isValueNullOrEmpty(image_two)) {
            imagesArrayList.add(image_two);
        }
        if (!Utility.isValueNullOrEmpty(image_three)) {
            imagesArrayList.add(image_three);
        }
        if (!Utility.isValueNullOrEmpty(image_four)) {
            imagesArrayList.add(image_three);
        }
        if (!Utility.isValueNullOrEmpty(image_five)) {
            imagesArrayList.add(image_five);
        }

        initUI();

    }

    private void initUI() {
        viewpagerImages = findViewById(R.id.viewpagerImages);
        imgBack = findViewById(R.id.imgBack);
        txtToolbar = findViewById(R.id.txtToolbar);
        txtDescription = findViewById(R.id.txtDescription);

        txtDescription.setText(description);
        txtToolbar.setText("Notes");
        imgBack.setOnClickListener(this);

        if (imagesArrayList.size() >= 1) {
            viewpagerImages.setVisibility(View.VISIBLE);
            SliderImagesAdapter mSliderImagesAdapter = new SliderImagesAdapter(this, imagesArrayList);
            viewpagerImages.setAdapter(mSliderImagesAdapter);
        } else {
            viewpagerImages.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                finish();
                break;
            default:
                break;
        }
    }
}
