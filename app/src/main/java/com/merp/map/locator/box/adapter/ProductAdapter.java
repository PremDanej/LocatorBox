package com.merp.map.locator.box.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merp.map.locator.box.databinding.ItemViewProductBinding;
import com.merp.map.locator.box.listener.onProductClick;
import com.merp.map.locator.box.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Product> mainList;
    private List<Product> list;
    private final onProductClick listener;

    public ProductAdapter(onProductClick listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product item = list.get(position);
        Picasso.get().load(item.getThumbnail()).fit().into(holder.binding.imgThumb);

        holder.binding.txtName.setText(item.getTitle());
        holder.binding.txtDiscount.setText(setDiscount(item.getDiscountPercentage()));
        holder.binding.txtPrice.setText(setPrice(item.getPrice()));
        holder.binding.rate.setRating(item.getRating().floatValue());

        holder.binding.cardProduct.setOnClickListener(view -> {
            listener.onProductItemClick(item.getId());
        });
    }

    private String setPrice(Integer price) {
        return "₹" + price;
    }

    private String setDiscount(Double discount) {
        return discount +"% off";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Product> products) {
        mainList = new ArrayList<>();
        list = new ArrayList<>();
        mainList.addAll(products);
        list.addAll(products);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilter(String filter) {
        if (filter == null || filter.trim().equals("")) {
            list = new ArrayList<>();
            list.addAll(mainList);
        }
        else {
            list = new ArrayList<>();
            for(Product item : mainList){
                if(item.getTitle().contains(filter))
                    list.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ItemViewProductBinding binding;
        public MyViewHolder(@NonNull ItemViewProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
