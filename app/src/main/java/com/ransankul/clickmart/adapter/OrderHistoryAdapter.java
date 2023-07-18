package com.ransankul.clickmart.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ransankul.clickmart.R;
import com.ransankul.clickmart.databinding.ItemOrderHistoryBinding;
import com.ransankul.clickmart.databinding.ItemSavedAddressBinding;
import com.ransankul.clickmart.model.Address;
import com.ransankul.clickmart.model.OrderHistory;
import com.ransankul.clickmart.model.Transaction;
import com.ransankul.clickmart.util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{


    Context context;
    ArrayList<OrderHistory> orderList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryAdapter.OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderHistoryViewHolder holder, int position) {

        OrderHistory tr = orderList.get(position);

        Glide.with(context)
                .load(Constants.PRODUCTS_IMAGE_URL + tr.getProductId() + "/" +tr.getImageName())
                .into(holder.binding.productImage);

        DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm");

        holder.binding.productName.setText(tr.getProductName());
        holder.binding.orderTime.setText(obj.format(tr.getOrderTime()));

        if(tr.getPaymentStatus().equalsIgnoreCase("created"))
            tr.setPaymentStatus("failed");
        holder.binding.status.setText(tr.getPaymentStatus().toUpperCase());

        if(tr.getPaymentStatus().equalsIgnoreCase("failed"))
            holder.binding.status.setTextColor(Color.parseColor("#E61000"));
        else holder.binding.status.setTextColor(Color.parseColor("#42CF47"));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        ItemOrderHistoryBinding binding;
        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemOrderHistoryBinding.bind(itemView);
        }
    }
}

