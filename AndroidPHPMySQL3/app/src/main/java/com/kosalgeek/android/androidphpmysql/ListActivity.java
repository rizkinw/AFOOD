package com.kosalgeek.android.androidphpmysql;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.DynamicImageLoader;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity implements AsyncResponse, AdapterView.OnItemClickListener {
    final String LOG = "ListActivity";

    private ArrayList<Product> productList;
    private ListView lvProduct;
    private FunDapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ListActivity.this, InsertActivity.class);
                startActivity(in);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().init(UILConfig.config(ListActivity.this));

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(ListActivity.this, this);
        taskRead.execute("http://192.168.43.194/amikom/event.php");

        lvProduct = (ListView)findViewById(R.id.lvProduct);

        registerForContextMenu(lvProduct);

    }

    //navigasi//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_category) {
            Intent in = new Intent(ListActivity.this, CategoryActivity.class);
            startActivity(in);
        }else if (id == R.id.action_home){
            Intent in = new Intent(ListActivity.this, ListActivity.class);
            startActivity(in);
        }

        return super.onOptionsItemSelected(item);
    }
//navigasi berhenti//



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Product selectedProduct = adapter.getItem(info.position);
        Log.d(LOG, selectedProduct.name);

        if(item.getItemId() == R.id.menuUpdate){
            Intent in = new Intent(ListActivity.this, DetailActivity.class);
            in.putExtra("product", selectedProduct);
            startActivity(in);
        }
        else if(item.getItemId() == R.id.menuRemove){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Apa anda yakin akan menghapus " + selectedProduct.name + "?");
            alert.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    productList.remove(selectedProduct);
                    adapter.notifyDataSetChanged();

                    HashMap postData = new HashMap();
                    postData.put("pid", "" + selectedProduct.pid);
                    postData.put("mobile", "android");


                    PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(ListActivity.this,
                            postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(LOG, s);
                            if(s.contains("success")){
                                Toast.makeText(ListActivity.this, "Event Berhasil Terhapus", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    taskUpdate.execute("http://192.168.43.194/amikom/remove.php");
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void processFinish(String s) {
        productList = new JsonConverter<Product>().toArrayList(s, Product.class);

        BindDictionary<Product> dict = new BindDictionary<Product>();
        dict.addStringField(R.id.tvName, new StringExtractor<Product>() {
            @Override
            public String getStringValue(Product product, int position) {
                return product.name;
            }
        });

        dict.addDynamicImageField(R.id.ivImage,
                new StringExtractor<Product>() {
                    @Override
                    public String getStringValue(Product product, int position) {
                        return product.image_url;
                    }
                }, new DynamicImageLoader() {
                    @Override
                    public void loadImage(String url, ImageView imageView) {
//                        Picasso.with(ListActivity.this)
//                                .load(url)
//                                .placeholder(android.R.drawable.star_big_on)
//                                .error(android.R.drawable.stat_sys_download)
//                                .into(imageView);

                        ImageLoader.getInstance().displayImage(url, imageView);
                        imageView.setPadding(0, 0, 0, 0);
                        imageView.setAdjustViewBounds(true);
                    }
                });

        adapter = new FunDapter<>(
                ListActivity.this, productList, R.layout.layout_list, dict);


        lvProduct.setAdapter(adapter);
        lvProduct.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product selectedProduct = productList.get(position);
        Intent in = new Intent(ListActivity.this, ListActivity.class);
        in.putExtra("product", (Serializable) selectedProduct);
        startActivity(in);
    }
}
