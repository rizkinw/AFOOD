package com.kosalgeek.android.androidphpmysql;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.nostra13.universalimageloader.core.ImageLoader;

public class CategoryActivity extends AppCompatActivity{

    ViewPager viewPager;
    ImageButton best, mostloved, promotions, newweek, slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().init(UILConfig.config(CategoryActivity.this));
        mostloved = (ImageButton) findViewById(R.id.mostloved);
        best = (ImageButton) findViewById(R.id.best);
        promotions = (ImageButton) findViewById(R.id.promotions);
        newweek = (ImageButton) findViewById(R.id.newweek);

        mostloved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextmostloved(view);
            }
        });
        best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextbest(view);
            }
        });
        promotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextpromotions(view);
            }
        });
        newweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextnewweek(view);
            }
        });

    }public void nextmostloved(View view) {
        Intent intent = new Intent(this, ListLovedActivity.class);
        startActivity(intent);
    }

    public void nextbest(View view) {
    Intent intent = new Intent(this, ListBestActivity.class);
    startActivity(intent);
    }

    public void nextpromotions(View view) {
        Intent intent = new Intent(this, ListNewweekActivity.class);
        startActivity(intent);
    }

    public void nextnewweek(View view) {
        Intent intent = new Intent(this, ListNewweekActivity.class);
        startActivity(intent);
    }
}
