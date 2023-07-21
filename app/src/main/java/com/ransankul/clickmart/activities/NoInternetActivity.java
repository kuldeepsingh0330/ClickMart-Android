package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityNoInternetBinding;
import com.ransankul.clickmart.util.Constants;

public class NoInternetActivity extends AppCompatActivity {

    ActivityNoInternetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoInternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.refreshButton.setOnClickListener((view) -> {
            if (Constants.isConnectedToInternet(getApplicationContext())) {
                Intent intent = new Intent(NoInternetActivity.this, SplashActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity( intent);
                finish();
            }
        });

    }
}