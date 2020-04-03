package com.diren.cryptoapi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diren.cryptoapi.R;
import com.diren.cryptoapi.data.Crypto;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGBuilder;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    private List<Crypto> cryptoList;
    OnItemClickListener clickListener;
    Context context;

    public RecyclerViewAdapter(List<Crypto> cryptoList, Context context, OnItemClickListener clickListener) {
        this.cryptoList = cryptoList;
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout, parent, false);
        final RowHolder holder = new RowHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
        return new RowHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.bind(cryptoList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        TextView txtCryptoName;
        TextView txtCrypto;
        ImageView imgIcon;

        public RowHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Crypto crypto, int position) {
            txtCryptoName = itemView.findViewById(R.id.txtCryptoName);
            txtCrypto = itemView.findViewById(R.id.txtCrypto);
            imgIcon = itemView.findViewById(R.id.imgCrypto);
            txtCryptoName.setText(crypto.data.getCoins().get(position).getName());
            txtCrypto.setText(String.valueOf(crypto.getData().getCoins().get(position).getPrice()));
             Glide.with(context).asGif().load(crypto.getData().getCoins().get(position).getIconUrl()).into(imgIcon);

            //txtCryptoName.setTextColor(Integer.parseInt(crypto.data.getCoins().get(position).getColor()));
            //txtCrypto.setTextColor(Color.parseColor(crypto.data.getCoins().get(position).getColor()));

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
