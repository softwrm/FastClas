package com.versatile.fastclas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.R;
import com.versatile.fastclas.adapters.DiscussionForumAdapter;
import com.versatile.fastclas.models.DiscussionForumModel;

import java.util.ArrayList;

public class DiscussionForumActivity extends BaseActivity implements View.OnClickListener, DiscussionForumAdapter.OnItemClickListener {

    private static final String TAG = DiscussionForumActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView txtToolbar;
    private ImageView mImgBack;
    private String heading;
    private Button btnAskQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum);

        initViews();
    }

    private void initViews() {
        setReferences();
        setData();
        setClickListeners();
    }

    private void setReferences() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtToolbar = (TextView) findViewById(R.id.txtToolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        btnAskQuestion = (Button) findViewById(R.id.btnAskQuestion);

//        heading = getIntent().getStringExtra("heading");
        txtToolbar.setText("DISCUSSION FORUM");
    }

    private void setData() {
        ArrayList<DiscussionForumModel> discussionForumModelArrayList = new ArrayList<>();
        ArrayList<String> userNameArraylist = new ArrayList<>();
        ArrayList<String> answersArraylist = new ArrayList<>();

        userNameArraylist.add("John Legend");
        userNameArraylist.add("Guru Mann");

        answersArraylist.add("When an unknown printer took a gallery of type and scrambled it to make a type specimen book.");
        answersArraylist.add("This is answer.");


        for (int i = 0; i < 3; i++) {
            discussionForumModelArrayList.add(new DiscussionForumModel("Introduction", "1",
                    "Multidimensional Aspects of Food Consumption and Production ?", "Ray Parker",
                    "1 hour ago", "2", userNameArraylist, answersArraylist));
        }

        DiscussionForumAdapter discussionForumAdapter = new DiscussionForumAdapter(this, discussionForumModelArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(discussionForumAdapter);
    }

    private void setClickListeners() {
        mImgBack.setOnClickListener(this);
        btnAskQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnAskQuestion:
                Intent intent = new Intent(this, AskQuestionActivity.class);
                navigateActivity(intent, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(DiscussionForumModel discussionForumModel, int Position) {

    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
//        navigateActivityBack(new Intent(this, HomeActivity.class), false);
    }

}
