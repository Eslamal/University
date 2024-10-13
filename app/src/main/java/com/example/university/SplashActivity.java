package com.example.university;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView verifidBannerSplash;
    private final long sleep = 3000;
    Thread mThread;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        verifidBannerSplash = findViewById(R.id.verifidBannerSplash);


        verifidBannerSplash.setAnimation(topAnim);


        startMain();

        try {
            PackageInfo pInfo =
                    getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void startMain() {
        mThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(sleep);
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };

        mThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mThread != null) mThread.interrupt();
    }
}