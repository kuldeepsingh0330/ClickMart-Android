package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new Dialog(LoginActivity.this);
        progressDialog.setContentView(R.layout.please_wait);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));

        binding.buttonLogin.setOnClickListener(view -> {
            progressDialog.show();
            String userName = binding.editTextUsername.getText().toString();
            String password = binding.editTextPassword.getText().toString();

            createRequest(userName,password);

        });

        binding.textViewSignUp.setOnClickListener(view -> {

            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        });

    }


    void createRequest(String username, String password){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.91.235:8080/validate";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Request successful
                    progressDialog.dismiss();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                },
                error -> {
                    // Request failed
                    progressDialog.dismiss();
                    if (error.networkResponse != null) {
                        showDialogWithOKButton("Invalid username or password.");
                    } else {
                        showDialogWithOKButton("An error occurred. Please try again.");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(request);

    }

    private void showDialogWithOKButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Start your intent here
                    alertDialog.dismiss();
                })
                .setCancelable(false)
                .create();
                alertDialog.show();
    }
}