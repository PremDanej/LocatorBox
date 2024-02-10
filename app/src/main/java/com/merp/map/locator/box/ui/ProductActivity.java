package com.merp.map.locator.box.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.merp.map.locator.box.R;
import com.merp.map.locator.box.adapter.ProductAdapter;
import com.merp.map.locator.box.databinding.ActivityProductBinding;
import com.merp.map.locator.box.listener.onProductClick;
import com.merp.map.locator.box.model.Product;
import com.merp.map.locator.box.model.ProductResponse;
import com.merp.map.locator.box.network.ApiClient;
import com.merp.map.locator.box.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductActivity extends AppCompatActivity implements onProductClick {

    private ActivityProductBinding binding;
    private ApiInterface apiInterface;
    // private List<Product> list = new ArrayList<>();
    private ProductAdapter adapter;
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
                adapter.setFilter(s.toString());
            }
        });
    }

    private void onInit() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        adapter = new ProductAdapter(this);
        binding.productRcv.setLayoutManager(new GridLayoutManager(this, 2));
        binding.productRcv.setAdapter(adapter);
    }

    private void onCallAllProductApi() {
        Call<ProductResponse> apiCall = apiInterface.getAllProduct();
        apiCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.code() == 200 && response.body() != null) {
                    adapter.updateList(response.body().getProducts());
                    binding.shimmerLayout.setVisibility(View.GONE);
                    binding.productLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.e(TAG, "API CALLING ERR -> " + t.getMessage());
            }
        });
    }

    @Override
    public void onProductItemClick(int productId) {
        // open detail activity
        startActivity(new Intent(this, ProductDetailActivity.class).putExtra("productId", productId));
    }
}