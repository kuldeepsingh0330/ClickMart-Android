package com.ransankul.clickmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.adapter.ProductAdapter;
import com.ransankul.clickmart.databinding.ActivitySearchBinding;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {


    ActivitySearchBinding binding;
    ProductAdapter productAdapter;
    ArrayList<Product> products;
    int pageNumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);


        String query = getIntent().getStringExtra("query");

        getSupportActionBar().setTitle(query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProducts(query);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);

        binding.refreshSearchList.setOnRefreshListener(() -> {
            products.clear();
            productAdapter.notifyDataSetChanged();
            pageNumber = 0;
            getProducts(query);
        });

        binding.productList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    pageNumber++;
                    getProducts(query);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    void getProducts(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.SEARCH_PRODUCTS_URL+pageNumber + "?name=" + query;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                    JSONArray productsArray = new JSONArray(response);
                    for(int i =0; i< productsArray.length(); i++) {
                        JSONObject childObj = productsArray.getJSONObject(i);
                        Product product = new Product(
                                childObj.getString("name"),
                                Constants.PRODUCTS_IMAGE_URL + childObj.getInt("productId"),
                                childObj.getString("available"),
                                childObj.getDouble("price"),
                                childObj.getDouble("discount"),
                                childObj.getInt("quantity"),
                                childObj.getInt("productId")

                        );
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            binding.refreshSearchList.setRefreshing(false);
        }, error -> {binding.refreshSearchList.setRefreshing(false); });

        queue.add(request);
    }
}