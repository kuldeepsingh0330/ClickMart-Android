package com.ransankul.clickmart.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.activities.CheckoutActivity;
import com.ransankul.clickmart.databinding.AddEditAddressDialogBinding;
import com.ransankul.clickmart.databinding.ItemAddressBinding;
import com.ransankul.clickmart.databinding.ItemCartBinding;
import com.ransankul.clickmart.model.Address;
import com.ransankul.clickmart.model.Product;
import com.ransankul.clickmart.util.Constants;
import com.ransankul.clickmart.util.SaveAddressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    Context context;
    ArrayList<Address> addressList;

    private int expandedPosition = RecyclerView.NO_POSITION;
    public boolean isSelected = false;

    public AddressAdapter(Context context, ArrayList<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressAdapter.AddressViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.binding.streetTextView.setText(address.getStreet());
        holder.binding.cityTextView.setText(address.getCity());
        holder.binding.stateTextView.setText(address.getState());
        holder.binding.postalCodeTextView.setText(address.getPostalCode());
        holder.binding.countryTextView.setText(address.getCountry());

        holder.binding.addressDetailLayout.setVisibility(View.GONE);

        if(expandedPosition == holder.getAdapterPosition())
            holder.binding.addressDetailLayout.setVisibility(View.VISIBLE);

        holder.binding.checkBoxAddress.setOnClickListener(view -> {

            boolean isChecked = holder.binding.checkBoxAddress.isChecked();
            CheckoutActivity.address = address;

            if(!isSelected){
                if(isChecked) isSelected = true;
                else isSelected = false;
            }else{
                if(isChecked) isSelected = true;
                else isSelected = false;
                holder.binding.checkBoxAddress.setChecked(false);
                if(isChecked)
                    Toast.makeText(context, "You can select one Delivery Address", Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(view -> {
            int previousExpandedPosition = expandedPosition;
            expandedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousExpandedPosition);
            holder.binding.addressDetailLayout.setVisibility(View.VISIBLE);
        });

        holder.binding.editAddress.setOnClickListener(view -> {
            AddEditAddressDialogBinding addressDialogBInding = AddEditAddressDialogBinding.inflate(LayoutInflater.from(context));
            androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setView(addressDialogBInding.getRoot())
                    .create();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.white)));

            addressDialogBInding.textView5.setText("EDIT ADDRESS");
            addressDialogBInding.saveAddress.setText("Update Address");
            addressDialogBInding.streetET.setText(address.getStreet());
            addressDialogBInding.cityET.setText(address.getCity());
            addressDialogBInding.stateET.setText(address.getState());
            addressDialogBInding.postalCodeET.setText(address.getPostalCode());
            addressDialogBInding.country.setText(address.getCountry());

            addressDialogBInding.closeIV.setOnClickListener(view1 -> {
                dialog.dismiss();
            });

            addressDialogBInding.saveAddress.setOnClickListener(view1 -> {
                String street = addressDialogBInding.streetET.getText().toString().trim();
                String city = addressDialogBInding.cityET.getText().toString().trim();
                String state = addressDialogBInding.stateET.getText().toString().trim();
                String postalCode = addressDialogBInding.postalCodeET.getText().toString().trim();
                String country = addressDialogBInding.country.getText().toString().trim();

                Address ad = new Address(address.getAddressId(),street,city, state, postalCode, country);
                updateAddress(ad, new SaveAddressCallback() {
                    @Override
                    public void onSuccess(Address address) {
                        if (address.getAddressId() != 0){
                            addressList.remove(holder.getAdapterPosition());
                            addressList.add(holder.getAdapterPosition(),address);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    }
                });

            });

            dialog.show();
        });


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {

        ItemAddressBinding binding;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAddressBinding.bind(itemView);
        }
    }

    private void updateAddress(Address address, SaveAddressCallback callback) {

        String url = Constants.UPDATE_ADDRESS_URL;

        String tokenValue = Constants.getTokenValue(context);


        if(Objects.equals(address.getStreet(), "") ||
                Objects.equals(address.getCity(), "") ||
                Objects.equals(address.getState(), "") ||
                Objects.equals(address.getPostalCode(), "") ||
                Objects.equals(address.getCountry(), "")){
            Toast.makeText(context, "All fields is required", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonAddress = new JSONObject();
        try {
            jsonAddress.put("addressId", address.getAddressId());
            jsonAddress.put("street", address.getStreet());
            jsonAddress.put("city", address.getCity());
            jsonAddress.put("state", address.getState());
            jsonAddress.put("postalCode", address.getPostalCode());
            jsonAddress.put("country", address.getCountry());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonAddress,
                response -> {
                    try {
                        String statusCode = response.getString("statusCode");
                        Log.d("hhhhhh",statusCode);
                        if(statusCode.equals("201")){
                            Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                        }else{
                            address.setAddressId(0);
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        callback.onSuccess(address);

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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }


}
