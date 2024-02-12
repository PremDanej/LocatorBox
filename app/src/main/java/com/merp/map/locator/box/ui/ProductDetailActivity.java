package com.merp.map.locator.box.ui;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.window.OnBackInvokedDispatcher;

import com.merp.map.locator.box.R;
import com.merp.map.locator.box.adapter.SliderAdapter;
import com.merp.map.locator.box.databinding.ActivityProductDetailBinding;
import com.merp.map.locator.box.model.Product;
import com.merp.map.locator.box.model.ProductResponse;
import com.merp.map.locator.box.network.ApiClient;
import com.merp.map.locator.box.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private ApiInterface apiInterface;
    private SliderAdapter adapter;
    private int dotscount;
    private ImageView[] dots;
    private static final String TAG = "============> ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onInit();

        binding.imgBack.setOnClickListener(view -> onGoBack());

        // Handle backPressed
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onGoBack();
            }
        });
    }

    private void onGoBack() {
        finish();
        overridePendingTransition(R.anim.anim_left_to_right, R.anim.anim_right_to_left);
    }


    private void onInit() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        onCallProductByIdApi(getIntent().getIntExtra("productId", 1));
    }

    private void onCallProductByIdApi(int productId) {
        Call<Product> apiCall = apiInterface.getProductById(productId);
        apiCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.code() == 200 && response.body() != null) {
                    setProductDetail(response.body());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e(TAG, "API CALLING ERR -> " + t.getMessage());
            }
        });
    }

    private void setProductDetail(Product item) {
        binding.txtName.setText(item.getTitle());
        binding.txtTitle.setText(item.getTitle());
        binding.txtDescription.setText(item.getDescription());
        binding.txtDiscount.setText(setDiscount(item.getDiscountPercentage()));
        binding.txtPrice.setText(setPrice(item.getPrice()));
        binding.rate.setRating(item.getRating().floatValue());
        adapter = new SliderAdapter(item.getImages());
        binding.slider.setAdapter(adapter);
        setSliderIndicator();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.productLayout.setVisibility(View.VISIBLE);
    }

    private void setSliderIndicator() {
        dotscount = adapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(onSetImageDrawable(R.drawable.ic_in_active_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            binding.sliderDots.addView(dots[i], params);
        }
        dots[0].setImageDrawable(onSetImageDrawable(R.drawable.ic_active_dot));

        binding.slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(onSetImageDrawable(R.drawable.ic_in_active_dot));
                }
                dots[position].setImageDrawable(onSetImageDrawable(R.drawable.ic_active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private Drawable onSetImageDrawable(int drawable) {
        return ContextCompat.getDrawable(this, drawable);
    }

    private String setPrice(Integer price) {
        return "₹" + price;
    }

    private String setDiscount(Double discount) {
        return discount + "% off";
    }
}