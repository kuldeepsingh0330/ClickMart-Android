package com.ransankul.clickmart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityProductDetailBinding;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.util.TinyCartHelper;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductDetailActivity extends AppCompatActivity {


    ActivityProductDetailBinding binding;
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        int id = getIntent().getIntExtra("id",0);
        double price = getIntent().getDoubleExtra("price",0);


        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getProductDetails(id);

        Cart cart = TinyCartHelper.getCart();


        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.addItem(currentProduct,1);
                binding.addToCartBtn.setEnabled(false);
                binding.addToCartBtn.setText("Added in cart");
            }
        });
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
                    String description = product.getString("description");
                    binding.productDescription.setText(
                            Html.fromHtml(description)
                    );

                    currentProduct = new Product(
                            product.getString("name"),
                            Constants.PRODUCTS_IMAGE_URL + product.getString("productId"),
                            product.getString("available"),
                            product.getDouble("price"),
                            product.getDouble("discount"),
                            product.getInt("quantity"),
                            product.getInt("productId")
                    );
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