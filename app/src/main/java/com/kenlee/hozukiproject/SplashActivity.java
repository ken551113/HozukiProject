package com.kenlee.hozukiproject;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    int Splash_wait = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //然後這邊是做一個Intent從SplashActivity切到MainActivity
                Intent i = new Intent(SplashActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
            }
        },Splash_wait);

    }
}
