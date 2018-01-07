package com.kosalgeek.android.androidphpmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG = "InsertActivity";

    private EditText etName, etImageUrl,etKategori, etDeskrip;
    private Button btnInsert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (EditText)findViewById(R.id.etName);
        etImageUrl = (EditText)findViewById(R.id.etImageUrl);
        etKategori = (EditText)findViewById(R.id.etKategori);
        etDeskrip = (EditText)findViewById(R.id.etDeskrip);
        btnInsert = (Button)findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        HashMap postData = new HashMap();
        postData.put("txtName", etName.getText().toString());
        postData.put("txtImageUrl", etImageUrl.getText().toString());
        postData.put("txtKategori", etKategori.getText().toString());
        postData.put("txtDeskrip", etDeskrip.getText().toString());
        postData.put("mobile", "android");


        PostResponseAsyncTask taskInsert = new PostResponseAsyncTask(InsertActivity.this,
                postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Log.d(LOG, s);
                if(s.contains("success")){
                    Toast.makeText(InsertActivity.this, "Insert Successfully", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(InsertActivity.this, ListActivity.class);
                    startActivity(in);
                }
            }
        });
        taskInsert.execute("http://192.168.43.194/amikom/insert.php");
    }
}
