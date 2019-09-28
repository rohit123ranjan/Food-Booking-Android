package com.example.rohitranjan.foodonbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class cartAdapter extends ArrayAdapter {

    private Activity context;
    private int resource;
    private List<PojoCart> listImage;

    public cartAdapter(@NonNull Activity context, int resource, @NonNull List<PojoCart> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.cartName);
        ImageView img = (ImageView) v.findViewById(R.id.imageLoad);
        TextView tvDesc = (TextView) v.findViewById(R.id.cartDesc);
        TextView tvPrice = (TextView) v.findViewById(R.id.cartPrice);
        TextView tvQuantity = (TextView) v.findViewById(R.id.cartQuantity);
        TextView tvTotal = (TextView) v.findViewById(R.id.cartTotal);

        tvName.setText(listImage.get(position).getItemName());
        tvDesc.setText(listImage.get(position).getItemInfo());
        tvPrice.setText(listImage.get(position).getItemPrice());
        tvQuantity.setText(listImage.get(position).getStoreQuantity());
        tvTotal.setText(listImage.get(position).getStoreTotal());
        Glide.with(context).load(listImage.get(position).getImageUrl()).into(img);

        return v;
    }


}
