package com.merp.map.locator.box.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merp.map.locator.box.databinding.ItemViewCategoryBinding;
import com.merp.map.locator.box.listener.onCategoryClick;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private final List<String> list;
    private final onCategoryClick listener;

    public CategoryAdapter(onCategoryClick listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemViewCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = list.get(position);
        holder.binding.txtCategory.setText(item);
        holder.binding.categoryCard.setOnClickListener(view -> listener.onCategoryItemClick(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<String> categories) {
        list.addAll(categories);
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ItemViewCategoryBinding binding;

        public MyViewHolder(@NonNull ItemViewCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
