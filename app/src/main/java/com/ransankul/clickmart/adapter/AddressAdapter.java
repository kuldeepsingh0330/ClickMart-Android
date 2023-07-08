package com.ransankul.clickmart.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.AddEditAddressDialogBinding;
import com.ransankul.clickmart.databinding.ItemAddressBinding;
import com.ransankul.clickmart.databinding.ItemCartBinding;
import com.ransankul.clickmart.model.Address;
import com.ransankul.clickmart.model.Product;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    Context context;
    ArrayList<Address> addressList;

    private int expandedPosition = RecyclerView.NO_POSITION;
    private boolean isSelected = false;

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
}
