package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ActivityRegisterBinding;
import com.ransankul.clickmart.model.User;
import com.ransankul.clickmart.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding activityRegisterBinding;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(activityRegisterBinding.getRoot());

        progressDialog = new Dialog(RegisterActivity.this);
        progressDialog.setContentView(R.layout.please_wait);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.transparent)));


        activityRegisterBinding.buttonsignUp.setOnClickListener(view -> {
           String name = activityRegisterBinding.editTextFullName.getText().toString();
           int phoneNumber = Integer.valueOf(activityRegisterBinding.editTextphoneNumber.getText().toString());
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

    private void createRequest(String name, String email, String password, String username, int phoneNumber) {
        progressDialog.show();
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

        String url = Constants.REGISTER_USER_URL;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    progressDialog.dismiss();
                    showDialogWithOKButton(response);
                },
                error -> {
                    progressDialog.dismiss();
                    showDialogWithErrorButton(error.toString());
                }){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return userJson.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void showDialogWithOKButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void showDialogWithErrorButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .create()
                .show();
    }

}