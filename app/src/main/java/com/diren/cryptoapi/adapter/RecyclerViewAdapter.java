package com.diren.cryptoapi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestBuilder;
import com.diren.cryptoapi.R;
import com.diren.cryptoapi.data.Crypto;
import com.diren.cryptoapi.svgParser.GlideApp;
import com.diren.cryptoapi.svgParser.SvgSoftwareLayerSetter;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    ArrayList<Crypto> cryptoList;
    OnItemClickListener clickListener;
    Context context;
    private RequestBuilder<PictureDrawable> requestBuilder;
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

    public RecyclerViewAdapter(Context context, OnItemClickListener clickListener) {
        cryptoList = new ArrayList<>();
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
        return cryptoList == null ? 0 :cryptoList.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder {
        TextView txtCryptoName;
        TextView txtCrypto;
        ImageView imgIcon;

        public RowHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Crypto crypto, int position) {
            try {
                txtCryptoName = itemView.findViewById(R.id.txtCryptoName);
                txtCrypto = itemView.findViewById(R.id.txtCrypto);
                imgIcon = itemView.findViewById(R.id.imgCrypto);
                txtCryptoName.setText(crypto.data.getCoins().get(position).getSymbol());
                txtCrypto.setText(String.format("%.2f", crypto.getData().getCoins().get(position).getPrice()));
                requestBuilder =
                        GlideApp.with(context)
                                .as(PictureDrawable.class)
                                .transition(withCrossFade())
                                .listener(new SvgSoftwareLayerSetter());

                Uri uri = Uri.parse(crypto.getData().getCoins().get(position).getIconUrl());
                requestBuilder.load(uri).into(imgIcon);
                if (crypto.getData().getCoins().get(position).getColor() != null) {
                    txtCryptoName.setTextColor(Color.parseColor(crypto.getData().getCoins().get(position).getColor().toUpperCase()));
                    txtCrypto.setTextColor(Color.parseColor(crypto.getData().getCoins().get(position).getColor().toUpperCase()));
                }
            }catch (IllegalArgumentException e){

            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public int getItemViewType(int position) {
        return (position == cryptoList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public void addAll(List<Crypto> mcList) {
        cryptoList.addAll(mcList);
        notifyItemInserted(cryptoList.size() - 1);
    }

   /* public void remove(Crypto city) {
        int position = cryptoList.indexOf(city);
        if (position > -1) {
            cryptoList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void add(Crypto mc) {
        notifyDataSetChanged();
        // notifyItemInserted(cryptoList.size() - 1);
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }
*/

    public void addLoadingFooter() {
        isLoadingAdded = true;
        //add(new CryptoAPI());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = cryptoList.size() - 1;
        Crypto item = getItem(position);

        if (item != null) {
            cryptoList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Crypto getItem(int position) {
        return cryptoList.get(position);
    }


}
