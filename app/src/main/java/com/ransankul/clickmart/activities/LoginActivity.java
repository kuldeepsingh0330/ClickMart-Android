package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.ransankul.clickmart.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Dialog progressDialog;
    private static final String SHARED_PREFS_NAME = "ransankulClickmart";
    private static final String KEY_STRING_VALUE = "JWTToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String tok = getTokenValue(getApplicationContext());
        if(tok != ""){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity( intent);
            finish();
        }



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

        String url = Constants.VALIDATE_USER_URL;
        StringRequest  request = new StringRequest (Request.Method.POST, url,
                response -> {
                    // Request successful
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        String value = object.getString("token");
                        String msg = object.getString("message");
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        saveTokenValue(getApplicationContext(),value);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
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
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public static void saveTokenValue(Context context, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_STRING_VALUE, value);
        editor.apply();
    }

    public static String getTokenValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STRING_VALUE, "");
    }
}