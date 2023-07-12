package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityWishlistBinding;
import com.ransankul.clickmart.model.Product;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {

    ActivityWishlistBinding binding;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWishlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        products = new ArrayList<>();

        getSupportActionBar().setTitle("Wishlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(products.isEmpty()){
            binding.productList.setVisibility(View.GONE);
        }else{
            binding.tvEmpty.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}