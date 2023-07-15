package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.adapter.AddressAdapter;
import com.ransankul.clickmart.databinding.ActivityAllAddressBinding;
import com.ransankul.clickmart.databinding.AddEditAddressDialogBinding;
import com.ransankul.clickmart.model.Address;
import com.ransankul.clickmart.util.Constants;
import com.ransankul.clickmart.util.SaveAddressCallback;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AllAddressActivity extends AppCompatActivity {

    private ArrayList<Address> addressList;
    AddressAdapter addressAdapter;
    ActivityAllAddressBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        addressList = new ArrayList<>();
        loadAllAddress();

        if(addressList.isEmpty()){
            binding.addressRecyclerView.setVisibility(View.GONE);
        }else{
            binding.tvEmpty.setVisibility(View.GONE);
        }


        getSupportActionBar().setTitle("Saved Addresses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addressAdapter = new AddressAdapter(this,addressList);
        binding.addressRecyclerView.setAdapter(addressAdapter);
        binding.addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.addNewAddress.setOnClickListener(view -> {
            AddEditAddressDialogBinding addressDialogBInding = AddEditAddressDialogBinding.inflate(LayoutInflater.from(this));
            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setView(addressDialogBInding.getRoot())
                    .create();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, android.R.color.white)));

            addressDialogBInding.textView5.setText("ADD ADDRESS");
            addressDialogBInding.saveAddress.setText("Save Address");

            addressDialogBInding.saveAddress.setOnClickListener(view1 -> {
                String street = addressDialogBInding.streetET.getText().toString().trim();
                String city = addressDialogBInding.cityET.getText().toString().trim();
                String state = addressDialogBInding.stateET.getText().toString().trim();
                String postalCode = addressDialogBInding.postalCodeET.getText().toString().trim();
                String country = addressDialogBInding.country.getText().toString().trim();

                Address ad = new Address(street,city, state, postalCode, country);
                saveAddress(ad, new SaveAddressCallback() {
                    @Override
                    public void onSuccess(Address address) {
                        if (address.getAddressId() != 0){
                            addressList.add(address);
                            addressAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });

            });
            addressDialogBInding.closeIV.setOnClickListener(view1 -> {
                dialog.dismiss();
            });

            dialog.show();
        });




    }

    private void loadAllAddress() {
        String tokenValue = Constants.getTokenValue(AllAddressActivity.this);
        String url = Constants.GET_ALL_ADDRESS_URL;
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        JSONArray dataArray = responseObj.getJSONArray("data");
                        for(int i = 0;i<dataArray.length();i++){
                            JSONObject addressObj = dataArray.getJSONObject(i);
                            Address ad = new Address(
                                    Integer.parseInt(addressObj.getString("addressId")),
                                    addressObj.getString("street"),
                                    addressObj.getString("city"),
                                    addressObj.getString("state"),
                                    addressObj.getString("postalCode"),
                                    addressObj.getString("country")
                            );
                            addressList.add(ad);
                        }
                        addressAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                },error -> {

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

    private void saveAddress(Address address, SaveAddressCallback callback) {


        String tokenValue = Constants.getTokenValue(AllAddressActivity.this);

        String url = Constants.ADD_NEW_ADDRESS_URL;

        if(Objects.equals(address.getStreet(), "") ||
                Objects.equals(address.getCity(), "") ||
                Objects.equals(address.getState(), "") ||
                Objects.equals(address.getPostalCode(), "") ||
                Objects.equals(address.getCountry(), "")){
            Toast.makeText(this, "All fields is required", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonAddress = new JSONObject();
        try {
            jsonAddress.put("street", address.getStreet());
            jsonAddress.put("city", address.getCity());
            jsonAddress.put("state", address.getState());
            jsonAddress.put("postalCode", address.getPostalCode());
            jsonAddress.put("country", address.getCountry());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonAddress,
                response -> {
                    try {
                        String statusCode = response.getString("statusCode");
                        JSONObject object = response.getJSONObject("data");
                        Log.d("hhhhhh",statusCode);
                        if(statusCode.equals("201")){
                            Log.d("hhhhhh", "hjffhefh");
                            address.setAddressId(Integer.parseInt(object.getString("addressId")));
                            address.setStreet(object.getString("street"));
                            address.setCity(object.getString("city"));
                            address.setState(object.getString("state"));
                            address.setPostalCode(object.getString("postalCode"));
                            address.setCountry(object.getString("country"));
                        }else{
                            address.setAddressId(0);
                        }
                        callback.onSuccess(address);
                        Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Log.d("hhhhhh",e.toString());
                        throw new RuntimeException(e);
                    }
                },
                error -> {

                    Log.d("hhhhhh",error.toString());
                    // Error response
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


}