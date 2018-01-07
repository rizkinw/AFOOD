package com.kosalgeek.android.androidphpmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    final String LOG = "DetailActivity";

    EditText etName, etImageUrl,etKategori, etDeskrip;
    ImageView ivImage;
    Button btnUpdate;

    private Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().init(UILConfig.config(DetailActivity.this));

        product = (Product) getIntent().getSerializableExtra("product");

        etName = (EditText)findViewById(R.id.etName);
        etImageUrl = (EditText)findViewById(R.id.etImageUrl);
        ivImage = (ImageView)findViewById(R.id.ivImage);
        etKategori = (EditText)findViewById(R.id.etKategori);
        etDeskrip = (EditText)findViewById(R.id.etDeskrip);

        if(product != null){
            etName.setText(product.name);
            etImageUrl.setText(product.image_url);
            etKategori.setText(product.kategori);
            etDeskrip.setText(product.deskrip);

            ImageLoader.getInstance().displayImage(product.image_url, ivImage);
            ivImage.setPadding(0, 0, 0, 0);
            ivImage.setAdjustViewBounds(true);
        }

        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        HashMap postData = new HashMap();
        postData.put("txtPid", "" + product.pid);
        postData.put("txtName", etName.getText().toString());
        postData.put("txtImageUrl", etImageUrl.getText().toString());
        postData.put("txtKategori", etKategori.getText().toString());
        postData.put("txtDeskrip", etDeskrip.getText().toString());
        postData.put("mobile", "android");


        PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(DetailActivity.this,
                postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(LOG, s);
                if(s.contains("success")){
                    Toast.makeText(DetailActivity.this, "Update Successfully", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(DetailActivity.this, ListActivity.class);
                    startActivity(in);
                }
            }
        });
        taskUpdate.execute("http://192.168.43.41/amikom/update.php");
    }
}
