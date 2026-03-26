package com.cinefast.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SnackAdapter extends BaseAdapter {

    public interface OnSnackQuantityChangedListener {
        void onQuantityChanged();
    }

    private final Context context;
    private final List<Snack> snacks;
    private final LayoutInflater inflater;
    private final OnSnackQuantityChangedListener listener;

    public SnackAdapter(Context context, List<Snack> snacks, OnSnackQuantityChangedListener listener) {
        this.context = context;
        this.snacks = snacks;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return snacks.size();
    }

    @Override
    public Snack getItem(int position) {
        return snacks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder vh;
        if (view == null) {
            view = inflater.inflate(R.layout.item_snack, parent, false);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        Snack snack = getItem(position);
        vh.iv.setImageResource(snack.getImageResId());
        vh.tvName.setText(snack.getName());
        vh.tvPrice.setText(String.format("$%.2f", snack.getPrice()));
        vh.tvQty.setText(String.valueOf(snack.getQuantity()));

        vh.btnMinus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() - 1);
            vh.tvQty.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) listener.onQuantityChanged();
        });

        vh.btnPlus.setOnClickListener(v -> {
            snack.setQuantity(snack.getQuantity() + 1);
            vh.tvQty.setText(String.valueOf(snack.getQuantity()));
            if (listener != null) listener.onQuantityChanged();
        });

        return view;
    }

    private static class ViewHolder {
        final ImageView iv;
        final TextView tvName;
        final TextView tvPrice;
        final TextView tvQty;
        final Button btnMinus;
        final Button btnPlus;

        ViewHolder(View root) {
            iv = root.findViewById(R.id.ivSnackImage);
            tvName = root.findViewById(R.id.tvSnackName);
            tvPrice = root.findViewById(R.id.tvSnackPrice);
            tvQty = root.findViewById(R.id.tvQty);
            btnMinus = root.findViewById(R.id.btnMinus);
            btnPlus = root.findViewById(R.id.btnPlus);
        }
    }
}

