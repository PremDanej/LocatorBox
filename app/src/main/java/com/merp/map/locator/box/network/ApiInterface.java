package com.merp.map.locator.box.network;

import com.merp.map.locator.box.model.Product;
import com.merp.map.locator.box.model.ProductResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("products/")
    Call<ProductResponse> getAllProduct();


    @GET("products/{productId}")
    Call<Product> getProductById(@Path("productId") int productId);


}


