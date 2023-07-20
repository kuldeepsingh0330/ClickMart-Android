package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String tok = getTokenValue(getApplicationContext());
        if(!tok.equals("")){
            tokenExpired(tok);
        }

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }

    public static String getTokenValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.KEY_STRING_VALUE, "");
    }

    private void expiredToken() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity( intent);
        finish();
    }

    private void validToken() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity( intent);
        finish();
    }

    void tokenExpired(String token){

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = Constants.POST_IS_TOKEN_EXPIRED_URL;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",token);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,jsonObject,
                object -> {
                    try {
                        String status = object.getString("statusCode");
                        if(status.equals("200")) validToken();
                        else expiredToken();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                });
        queue.add(request);
    }
}