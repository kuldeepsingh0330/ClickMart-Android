package com.ransankul.clickmart.activities;

import static com.ransankul.clickmart.util.Constants.PRODUCTS_IMAGE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.adapter.ProductAdapter;
import com.ransankul.clickmart.databinding.ActivityWishlistBinding;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishlistActivity extends AppCompatActivity {

    ActivityWishlistBinding binding;
    ArrayList<Product> products;

    ProductAdapter productAdapter;

    ProgressDialog progressDialog;

    int pageNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Wishlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");

        GridLayoutManager layoutManager = initProducts();
        initLayout();

        binding.refreshWishlist.setOnRefreshListener(() -> {
            products.clear();
            productAdapter.notifyDataSetChanged();
            progressDialog.show();
            pageNumber = 0;
            initProducts();
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
                    getAllWishlistProduct();
                }
            }
        });
    }

    private void getAllWishlistProduct() {

        String tokenValue = Constants.getTokenValue(WishlistActivity.this);

        String url = Constants.POST_LOAD_ALL_TO_WISHLIST_URL+pageNumber;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.e("hhhhhhh1","bjh");

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response
                    try {
                        JSONObject obj = new JSONObject(response);

                        String statusCode = obj.getString("statusCode");
                        if(statusCode.equals("201")){
                            JSONArray array = obj.getJSONArray("data");
                            for(int i = 0;i<array.length();i++){
                                JSONObject object = array.getJSONObject(i);
                                Product  product = new Product(object.getString("name"),
                                        PRODUCTS_IMAGE_URL+object.getInt("productId"),
                                        object.getDouble("price"),
                                        object.getDouble("discount"),
                                        object.getInt("productId"));
                                products.add(product);


                                productAdapter.notifyDataSetChanged();
                            }
                        }
                        initLayout();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    progressDialog.dismiss();
                    binding.refreshWishlist.setRefreshing(false);
                },
                error -> {
            initLayout();
                    progressDialog.dismiss();
                    binding.refreshWishlist.setRefreshing(false);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);

    }

    GridLayoutManager initProducts() {
        progressDialog.show();
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);


        getAllWishlistProduct();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
        return layoutManager;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initLayout(){
        if(products.isEmpty()){
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.productList.setVisibility(View.GONE);
        }else{
            binding.tvEmpty.setVisibility(View.GONE);
            binding.productList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initProducts();
    }
}