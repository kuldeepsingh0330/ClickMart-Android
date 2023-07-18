package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.adapter.AllAddressAdapter;
import com.ransankul.clickmart.adapter.OrderHistoryAdapter;
import com.ransankul.clickmart.databinding.ActivityOrderHistoryBinding;
import com.ransankul.clickmart.model.Address;
import com.ransankul.clickmart.model.OrderHistory;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding binding;

    ArrayList<OrderHistory> transactionList;
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactionList = new ArrayList<>();

        getSupportActionBar().setTitle("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderHistoryAdapter = new OrderHistoryAdapter(this,transactionList);
        binding.productList.setAdapter(orderHistoryAdapter);
        binding.productList.setLayoutManager(new LinearLayoutManager(this));

        loadAllTransaction();
        initLayout();
    }

    private void loadAllTransaction() {
        String tokenValue = Constants.getTokenValue(OrderHistoryActivity.this);
        String url = Constants.POST_LOAD_ALL_TRANSACTION_URL;
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        JSONArray dataArray = responseObj.getJSONArray("data");
                        for(int i = 0;i<dataArray.length();i++){
                            JSONObject addressObj = dataArray.getJSONObject(i);
                            OrderHistory tr = new OrderHistory(
                                  addressObj.getString("imageName"),
                                    addressObj.getString("productId"),
                                    addressObj.getString("productName"),
                                    new Date(Long.parseLong(addressObj.getString("orderTime"))),
                                    addressObj.getString("paymentStatus"),
                                    addressObj.getString("transactionId")
                            );
                            transactionList.add(tr);
                        }
                        initLayout();
                        orderHistoryAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },error -> {
            Log.e("hhhhhhh",error.toString());

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void initLayout() {
        if(transactionList.isEmpty()){
            binding.productList.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }else{
            binding.tvEmpty.setVisibility(View.GONE);
            binding.productList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}