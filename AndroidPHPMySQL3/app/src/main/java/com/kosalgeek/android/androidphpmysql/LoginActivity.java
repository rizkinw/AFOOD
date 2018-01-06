package com.kosalgeek.android.androidphpmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String LOG = "LoginActivity";
    Button btnLogin;
    EditText etNim, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etNim = (EditText)findViewById(R.id.etNim);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        HashMap postData = new HashMap();

        String nim = etNim.getText().toString();
        String password = etPassword.getText().toString();

        postData.put("txtNim", nim);
        postData.put("txtPassword", password);

        PostResponseAsyncTask task1 = new PostResponseAsyncTask(LoginActivity.this, postData,
                new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Log.d(LOG, s);
                        if(s.contains("success")){
                            Toast.makeText(LoginActivity.this, "Selamat Anda Berhasil Login", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(LoginActivity.this, CategoryActivity.class);
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Nim atau Password anda salah", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        task1.execute("http://192.168.43.194/amikom/");
    }
}
