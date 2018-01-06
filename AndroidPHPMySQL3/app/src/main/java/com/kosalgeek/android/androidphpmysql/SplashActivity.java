package com.kosalgeek.android.androidphpmysql;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv, iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView) findViewById(R.id.iv);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.transition);
        iv.startAnimation(myanim);

        iv2 = (ImageView) findViewById(R.id.iv2);
        Animation myanim2 = AnimationUtils.loadAnimation(this,R.anim.transition2);
        iv2.startAnimation(myanim2);

        final Intent i = new Intent(this,LoginActivity.class);

        Thread timer = new Thread(){
            public void run (){
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}