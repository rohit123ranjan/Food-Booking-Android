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

public class ItemListAdapter extends ArrayAdapter {

    private Activity context;
    private int resource;
    private List<ItemUpload> listImage;

    public ItemListAdapter(@NonNull Activity context, int resource, @NonNull List<ItemUpload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(R.id.tvListName);
        ImageView img = (ImageView) v.findViewById(R.id.listView);
        TextView tvDesc = (TextView) v.findViewById(R.id.tvImageDesc);
        TextView tvPrice = (TextView) v.findViewById(R.id.ListPrice);

        tvName.setText(listImage.get(position).getName());
        tvDesc.setText(listImage.get(position).getInfoItem());
        tvPrice.setText(listImage.get(position).getPriceItem());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);
        return v;
    }
}
