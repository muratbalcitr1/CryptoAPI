package com.diren.cryptoapi.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diren.cryptoapi.R;
import com.diren.cryptoapi.adapter.RecyclerViewAdapter;
import com.diren.cryptoapi.data.Crypto;
import com.diren.cryptoapi.listener.PaginationScrollListener;
import com.diren.cryptoapi.service.CryptoAPI;
import com.diren.cryptoapi.service.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    Context context;
    LinearLayoutManager linearLayoutManager;
    private KProgressHUD hud;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 4;
    private int currentPage = PAGE_START;
    ArrayList<Crypto> liste;
    int succesCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        liste = new ArrayList<>();
        context = this;
        recyclerView = findViewById(R.id.recyclerView);
        loadData(currentPage);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage(currentPage);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void showRecycler() {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewAdapter.addAll(liste);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void loadData(int currentPage) {
        CryptoAPI cryptoAPI = RetrofitClient.retrofitClient().create(CryptoAPI.class);
        Call<Crypto> call = cryptoAPI.getData(40, "desc", currentPage);

        call.enqueue(new Callback<Crypto>() {
            @Override
            public void onResponse(Call<Crypto> call, Response<Crypto> response) {
                assert response.body() != null;
                succesCode = response.code();
                Crypto responseList = response.body();

                for (int i = 0; i < responseList.getData().getCoins().size(); i++) {
                    liste.add(responseList);
                }

                if (liste.size() > 0){

                    showRecycler();
                    showHideProgressHud(true);
                    liste.clear();
                    showHideProgressHud(false);

                }
            }

            @Override
            public void onFailure(Call<Crypto> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadNextPage(int currentPage) {
        recyclerViewAdapter.removeLoadingFooter();
        isLoading = false;
        loadData(currentPage);
        if (this.currentPage != TOTAL_PAGES) recyclerViewAdapter.addLoadingFooter();
        else isLastPage = true;

    }

    private void showHideProgressHud(Boolean bool){
        if (bool){
            if (hud == null) {
                hud = KProgressHUD.create(context)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("Loading...")
                        .setCancellable(true)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();
            }
        }
        else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    if (hud != null) {
                        hud.dismiss();
                        hud = null;
                    }
                }
            }, 1000);


        }
    }
}

