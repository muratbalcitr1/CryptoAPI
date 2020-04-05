package com.diren.cryptoapi.service;

import com.diren.cryptoapi.data.Crypto;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface CryptoAPI {

    @GET("v1/public/coins")
    Call<Crypto> getData(
            @Query("limit") int limit,
            @Query("order") String order,
            @Query("offset") int offset
    );
}
