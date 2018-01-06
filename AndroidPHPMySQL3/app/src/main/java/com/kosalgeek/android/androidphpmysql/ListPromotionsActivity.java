package com.kosalgeek.android.androidphpmysql;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
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

public class ListPromotionsActivity extends AppCompatActivity implements AsyncResponse, AdapterView.OnItemClickListener {
    final String LOG = "ListPromotionsActivity";

    private ArrayList<Product> productList;
    private ListView lvProduct;
    private FunDapter<Product> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ListPromotionsActivity.this, InsertActivity.class);
                startActivity(in);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageLoader.getInstance().init(UILConfig.config(ListPromotionsActivity.this));

        PostResponseAsyncTask taskRead = new PostResponseAsyncTask(ListPromotionsActivity.this, this);
        taskRead.execute("http://192.168.43.194/amikom/promotions.php");

        lvProduct = (ListView) findViewById(R.id.lvProduct);

        registerForContextMenu(lvProduct);

    }

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

        if (item.getItemId() == R.id.menuUpdate) {
            Intent in = new Intent(ListPromotionsActivity.this, DetailActivity.class);
            in.putExtra("product", selectedProduct);
            startActivity(in);
        } else if (item.getItemId() == R.id.menuRemove) {
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


                    PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(ListPromotionsActivity.this,
                            postData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            Log.d(LOG, s);
                            if (s.contains("success")) {
                                Toast.makeText(ListPromotionsActivity.this, "Event Berhasil Terhapus", Toast.LENGTH_LONG).show();
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
                ListPromotionsActivity.this, productList, R.layout.layout_list, dict);


        lvProduct.setAdapter(adapter);
        lvProduct.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Product selectedProduct = productList.get(position);
        Intent in = new Intent(ListPromotionsActivity.this, DetailActivity2.class);
        in.putExtra("product", (Serializable) selectedProduct);
        startActivity(in);
    }
}
