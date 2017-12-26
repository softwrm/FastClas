package com.versatile.fastclas.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.versatile.fastclas.BaseActivity;
import com.versatile.fastclas.R;
import com.versatile.fastclas.utils.Constants;
import com.versatile.fastclas.utils.Utility;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initComponents();
    }

    private void initComponents() {
        setSplashScreen();
    }

    private void setSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utility.isValueNullOrEmpty(Utility.getSharedPreference(SplashActivity.this, Constants.USER_ID))) {
                    Intent splash = new Intent(SplashActivity.this, LoginActivity.class);
                    navigateActivity(splash, true);
                } else {
                    Intent splash = new Intent(SplashActivity.this, HomeActivity.class);
                    navigateActivity(splash, true);
                }
            }
        }, 3000);

    }
}
