package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityOrderHistoryBinding;
import com.ransankul.clickmart.model.Product;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding binding;

    ArrayList<Product> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayList = new ArrayList<>();

        getSupportActionBar().setTitle("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(arrayList.isEmpty()){
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