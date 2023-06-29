package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityRegisterBinding;
import com.ransankul.clickmart.model.User;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding activityRegisterBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

        activityRegisterBinding.buttonsignUp.setOnClickListener(view -> {
           String name = activityRegisterBinding.editTextFullName.getText().toString();
           Long phoneNumber = Long.valueOf(activityRegisterBinding.editTextphoneNumber.getText().toString());
           String email = activityRegisterBinding.editTextemailId.getText().toString();
           String password = activityRegisterBinding.editTextpassword.getText().toString();
           String username = activityRegisterBinding.editTextUsername.getText().toString();

           createRequest(name,email,password,username,phoneNumber);
        });

        activityRegisterBinding.textViewSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void createRequest(String name, String email, String password, String username, Long phoneNumber){
        JSONObject userJson = new JSONObject();
        try {
            userJson.put("name", name);
            userJson.put("phoneNumber", phoneNumber);
            userJson.put("emailId", email);
            userJson.put("password", password);
            userJson.put("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://192.168.218.235:8080/register";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, userJson,
                response -> {
                    try {
                        String userNameR = response.getString("userName");
                        String message = "Registered successfully with UserName"+userNameR;
                        showDialogWithOKButton(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Request failed
                    // Handle the error
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void showDialogWithOKButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Start your intent here
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                })
                .setCancelable(false)
                .create()
                .show();
    }

}