package com.versatile.fastclas.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.adapters.CoursesAdapter;
import com.versatile.fastclas.models.CourseModel;
import com.versatile.fastclas.models.UniversityModel;
import com.otelpt.fastclas.R;

import java.util.ArrayList;

public class DashBoardCourseActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<UniversityModel> universityModelArrayList;
    ArrayList<CourseModel> courseModelArrayList;
    RecyclerView coursesRecyclerView;
    TextView txtNoDataFound;
    String university_id;
    TextView mTitle;
    ImageView mImgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_course);

        universityModelArrayList = DashBoardActivity.getUniversityModelArrayList();

        if (getIntent().hasExtra("university_id")) {
            university_id = getIntent().getStringExtra("university_id");
        }

        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coursesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        coursesRecyclerView.setHasFixedSize(true);

        txtNoDataFound = findViewById(R.id.txtNoDataFound);

        mImgBack = findViewById(R.id.imgBack);
        mTitle = findViewById(R.id.txtToolbar);
        mTitle.setText(getResources().getString(R.string.course));

        mImgBack.setOnClickListener(this);

        setData(universityModelArrayList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void setData(ArrayList<UniversityModel> universityModelArrayList) {
        for (int i = 0; i < universityModelArrayList.size(); i++) {
            if (university_id.equals(universityModelArrayList.get(i).getUniversity_id())) {

                courseModelArrayList = universityModelArrayList.get(i).getCourseModelArrayList();
            }
        }
        if (courseModelArrayList.size() <= 0) {
            txtNoDataFound.setVisibility(View.VISIBLE);
        } else {
            txtNoDataFound.setVisibility(View.GONE);
            CoursesAdapter coursesAdapter = new CoursesAdapter(this, courseModelArrayList);
            coursesRecyclerView.setAdapter(coursesAdapter);
        }
    }
}
