package com.ransankul.clickmart.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.adapter.AddressAdapter;
import com.ransankul.clickmart.adapter.CartAdapter;
import com.ransankul.clickmart.databinding.ActivityCheckoutBinding;
import com.ransankul.clickmart.databinding.AddEditAddressDialogBinding;
import com.ransankul.clickmart.model.Address;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;
import com.ransankul.clickmart.util.SaveAddressCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

public class CheckoutActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    AddressAdapter addressAdapter;
    ArrayList<Product> products;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    private ArrayList<Address> addressList;
    Cart cart;

    public static Address address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");
        // Initialize Razorpay Checkout
        Checkout.preload(getApplicationContext());

        products = new ArrayList<>();
        addressList = new ArrayList<>();

        cart = TinyCartHelper.getCart();

        for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        loadAllAddress();

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));
            }

            @Override
            public void onProductRemove() {
                if(products.isEmpty()){
                    binding.checkoutBtn.setEnabled(false);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));

        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("INR " + totalPrice);

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addressAdapter.isSelected) {
                    processOrder();
                }
                else
                    Toast.makeText(CheckoutActivity.this, "Please select delivery address", Toast.LENGTH_SHORT).show();
            }
        });

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

        String tokenValue = Constants.getTokenValue(CheckoutActivity.this);
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

        String url = Constants.ADD_NEW_ADDRESS_URL;

        String tokenValue = Constants.getTokenValue(CheckoutActivity.this);

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
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    // Error response
                }){
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



    void processOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        String tokenValue = Constants.getTokenValue(CheckoutActivity.this);

        Map<Item,Integer> productList = cart.getAllItemsWithQty();
        JSONArray product = new JSONArray();
        JSONArray quantity = new JSONArray();
        for(Map.Entry<Item, Integer> entry : productList.entrySet()){
            Item key = entry.getKey();
            int value = entry.getValue();

            int id = Integer.parseInt(key.getItemName());
            product.put(id);
            quantity.put(value);
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

        JSONObject dataObject = new JSONObject();
        try {
            dataObject.put("productList",product);
            dataObject.put("quantityList",quantity);
            dataObject.put("deliveryAddress",jsonAddress);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.e("hhhhhh",dataObject.toString());


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_CREATE_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("created")){
                        startPayment(response);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Security","secure_code");
                headers.put("Authorization", "Bearer " + tokenValue);
                return headers;
            }
        } ;

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void startPayment(JSONObject response) {
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logo);
        checkout.setKeyID(Constants.RAZORPAY_KEY_ID);
        checkout.setFullScreenDisable(true);

        try {
            JSONObject options = new JSONObject();

            options.put("name", "ClickMart");
            options.put("description", "ClickMart");
            options.put("order_id", response.getString("id"));//from response of step 3.
            options.put("theme.color", "#FF5722");
            options.put("currency", "INR");
            options.put("amount", totalPrice);//pass amount in currency subunits
            options.put("prefill.contact","7417371265");
            options.put("prefill.email", "");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);


            checkout.open(this, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        progressDialog.dismiss();
        Toast.makeText(this, "Payment unsuccessful", Toast.LENGTH_SHORT).show();

    }
}