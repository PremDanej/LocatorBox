package com.merp.map.locator.box.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.merp.map.locator.box.R;
import com.merp.map.locator.box.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {

    private ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnMap.setOnClickListener(view -> goToNext(MapsActivity.class));
        binding.btnProduct.setOnClickListener(view -> goToNext(ProductActivity.class));
    }

    private void goToNext(Class<?> activity) {
        startActivity(new Intent(this, activity));
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
    }
}