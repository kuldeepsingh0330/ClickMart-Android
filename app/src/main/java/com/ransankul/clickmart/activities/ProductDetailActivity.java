package com.ransankul.clickmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityProductDetailBinding;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductDetailActivity extends AppCompatActivity {


    ActivityProductDetailBinding binding;
    Product currentProduct;

    Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cart = TinyCartHelper.getCart();

        String name = getIntent().getStringExtra("name");
        int id = getIntent().getIntExtra("id",0);


        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProductDetails(id);
        initializeLayout(id);
        isProductintoWishList(id);



        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct,1);
                binding.addToCartBtn.setVisibility(View.GONE);
                binding.removeToCartBtn.setVisibility(View.VISIBLE);
            }
        });

        binding.removeToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<Item> set = cart.getItemNames();

                for(Item i : set){
                    if(i.getItemName() == String.valueOf(id)){
                        cart.removeItem(i);
                        binding.addToCartBtn.setVisibility(View.VISIBLE);
                        binding.removeToCartBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        binding.addToWishlist.setOnClickListener(view -> {
            addToWishlist(id,Constants.getTokenValue(ProductDetailActivity.this));
        });

        binding.removeToWishlist.setOnClickListener(view -> {
            removeToWishList(id);
        });
    }

    private void initializeLayout(int id) {
        boolean b = false;
        Set<Item> set = cart.getItemNames();

        for(Item i : set){
            if(i.getItemName() == String.valueOf(id)){b = true;break;}
            else b = false;
        }

        if(b){
            binding.addToCartBtn.setVisibility(View.GONE);
            binding.removeToCartBtn.setVisibility(View.VISIBLE);
        }else{
            binding.removeToCartBtn.setVisibility(View.GONE);
            binding.addToCartBtn.setVisibility(View.VISIBLE);
        }
    }

    private void addToWishlist(int id, String tokenValue) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create the JSONObject
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = Constants.POST_ADD_TO_WISHLIST_URL;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    // Handle the response
                    binding.addToWishlist.setVisibility(View.GONE);
                    binding.removeToWishlist.setVisibility(View.VISIBLE);
                },
                error -> {
                    // Handle the error
                    Toast.makeText(ProductDetailActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
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


    private void isProductintoWishList(int id){
        String token = Constants.getTokenValue(ProductDetailActivity.this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = Constants.POST_PRODUCT_EXIST_TO_WISHLIST_URL+"/"+id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString("statusCode").equals("201")){
                            binding.addToWishlist.setVisibility(View.GONE);
                            binding.removeToWishlist.setVisibility(View.VISIBLE);
                        }else{
                            binding.addToWishlist.setVisibility(View.VISIBLE);
                            binding.removeToWishlist.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    Toast.makeText(ProductDetailActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void removeToWishList(int id){
        String token = Constants.getTokenValue(ProductDetailActivity.this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = Constants.POST_REMOVE_TO_WISHLIST_URL+"/"+id;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response
                    try {
                        JSONObject object = new JSONObject(response);
                        if(object.getString("statusCode").equals("202")){
                            binding.addToWishlist.setVisibility(View.VISIBLE);
                            binding.removeToWishlist.setVisibility(View.GONE);
                            Toast.makeText(this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    // Handle the error
                    Log.e("hhhhhh",error.toString());
                    Toast.makeText(ProductDetailActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void loadProductImages(JSONObject object) {

        try {
            JSONArray arr = object.getJSONArray("images");
            int  id = object.getInt("productId");
            for(int i = 0;i<arr.length();i++){
                binding.productImage.addData(
                        new CarouselItem(
                                Constants.PRODUCTS_IMAGE_URL + id + "/" +arr.getString(i)
                        )
                );
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cart) {
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    void getProductDetails(int id) {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.GET_PRODUCT_DETAILS_URL + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject product = new JSONObject(response);
                    loadProductImages(product);

                    currentProduct = new Product(
                            product.getString("name"),
                            Constants.PRODUCTS_IMAGE_URL + product.getString("productId"),
                            product.getString("available"),
                            product.getDouble("price"),
                            product.getDouble("discount"),
                            product.getInt("quantity"),
                            product.getInt("productId")
                    );


                    String description = product.getString("description");
                    binding.productDescription.setText(
                            Html.fromHtml(description)
                    );
                    binding.totalPrice.setText("Price : "+product.getString("price"));
                    binding.discount.setText("Discount : "+product.getString("discount"));
                    binding.finalPrice.setText("Final Price : "+String.valueOf(product.getDouble("price")-product.getDouble("discount")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}