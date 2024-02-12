package com.merp.map.locator.box.ui;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.merp.map.locator.box.R;
import com.merp.map.locator.box.adapter.CategoryAdapter;
import com.merp.map.locator.box.adapter.ProductAdapter;
import com.merp.map.locator.box.databinding.ActivityProductBinding;
import com.merp.map.locator.box.listener.onCategoryClick;
import com.merp.map.locator.box.listener.onProductClick;
import com.merp.map.locator.box.model.Product;
import com.merp.map.locator.box.model.ProductResponse;
import com.merp.map.locator.box.network.ApiClient;
import com.merp.map.locator.box.network.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity implements onProductClick, onCategoryClick {

    private ActivityProductBinding binding;
    private ApiInterface apiInterface;
    private final List<Product> productList = new ArrayList<>();
    private List<String> categoryList;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final String TAG = "============> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onInit();
        onCallAllProductApi();

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productAdapter.setFilter(s.toString());
            }
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.anim_left_to_right, R.anim.anim_right_to_left);
            }
        });
    }



    private void onInit() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        productAdapter = new ProductAdapter(this);
        binding.productRcv.setLayoutManager(new GridLayoutManager(this, 2));
        binding.productRcv.setAdapter(productAdapter);

        categoryAdapter = new CategoryAdapter(this);
        binding.categoryRcv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        binding.categoryRcv.setAdapter(categoryAdapter);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
    }

    private void onCallAllProductApi() {
        Call<ProductResponse> apiCall = apiInterface.getAllProduct();
        apiCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductResponse> call, @NonNull Response<ProductResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    productList.addAll(response.body().getProducts());
                    manageCategory(productList);
                    productAdapter.updateList(productList);
                    binding.shimmerLayout.setVisibility(View.GONE);
                    binding.productLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "API CALLING ERR -> " + t.getMessage());
            }
        });
    }

    private void manageCategory(List<Product> products) {
        categoryList = new ArrayList<>();
        categoryList.add("All");
        products.forEach(item ->{
            if(!categoryList.contains(item.getCategory())) categoryList.add(item.getCategory());
        });
        categoryAdapter.updateList(categoryList);
    }

    @Override
    public void onProductItemClick(int productId) {
        startActivity(new Intent(this, ProductDetailActivity.class).putExtra("productId", productId));
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
    }

    @Override
    public void onCategoryItemClick(String category) {
        if(category.contains("All")){
            productAdapter.updateList(productList);
        }else {
            List<Product> newList = new ArrayList<>();
            productList.forEach(item -> {
                if (item.getCategory().contains(category)) {
                    newList.add(item);
                }
            });
            productAdapter.updateList(newList);
        }
    }

    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}