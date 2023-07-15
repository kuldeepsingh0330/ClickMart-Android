package com.ransankul.clickmart.activities;

import static com.ransankul.clickmart.util.Constants.PRODUCTS_IMAGE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

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

        initProducts();
    }

    private void getAllWishlistProduct() {

        String tokenValue = Constants.getTokenValue(WishlistActivity.this);

        String url = Constants.POST_LOAD_ALL_TO_WISHLIST_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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

                                binding.tvEmpty.setVisibility(View.GONE);

                                productAdapter.notifyDataSetChanged();
                            }
                        }else{

                            binding.tvEmpty.setVisibility(View.VISIBLE);
                            binding.productList.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    progressDialog.dismiss();
                },
                error -> {
                    progressDialog.dismiss();
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

    void initProducts() {
        progressDialog.show();
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);


        getAllWishlistProduct();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initProducts();
    }
}