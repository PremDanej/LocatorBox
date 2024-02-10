package com.merp.map.locator.box.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.merp.map.locator.box.databinding.ItemViewImageSliderBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    List<String> images;

    public SliderAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ItemViewImageSliderBinding binding = ItemViewImageSliderBinding.inflate(LayoutInflater.from(container.getContext()),container,false);
        Picasso.get().load(images.get(position)).fit().into(binding.imgSlider);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
