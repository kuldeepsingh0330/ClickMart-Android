package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonLogin.setOnClickListener(view -> {
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

        String url = "http://192.168.218.235:8080/validate";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Request successful
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                },
                error -> {
                    // Request failed
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