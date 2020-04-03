package com.diren.cryptoapi.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.diren.cryptoapi.R;
import com.diren.cryptoapi.adapter.RecyclerViewAdapter;
import com.diren.cryptoapi.data.Crypto;
import com.diren.cryptoapi.service.CryptoAPI;
import com.diren.cryptoapi.service.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        loadData();

    }

    private void loadData() {
        CryptoAPI cryptoAPI = RetrofitClient.retrofitClient().create(CryptoAPI.class);
        Call<Crypto> call = cryptoAPI.getData(10, "asc");

        call.enqueue(new Callback<Crypto>() {
            @Override
            public void onResponse(Call<Crypto> call, Response<Crypto> response) {
                assert response.body() != null;
                Crypto responseList = response.body();
                ArrayList<Crypto> liste = new ArrayList<>();

                for (int i = 0; i < responseList.getData().getCoins().size(); i++) {
                    liste.add(responseList);
                }
                Log.d("LISTE", responseList.status);
                //RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerViewAdapter = new RecyclerViewAdapter(liste, getApplicationContext(), new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onFailure(Call<Crypto> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

