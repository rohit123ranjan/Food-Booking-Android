package com.example.rohitranjan.foodonbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class fetchItemAdapter extends ArrayAdapter {
    private Activity context;
    private int resource;
    private List<PojoCart> listImage;

    public fetchItemAdapter(@NonNull Activity context, int resource, @NonNull List<PojoCart> objects) {
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
        TextView tvName = (TextView) v.findViewById(R.id.nameItem);
        TextView tvQuantity = (TextView) v.findViewById(R.id.nameQuantity);
        TextView tvTotal = (TextView) v.findViewById(R.id.namePrice);

        tvName.setText(listImage.get(position).getItemName());
        tvQuantity.setText(listImage.get(position).getStoreQuantity());
        tvTotal.setText(listImage.get(position).getStoreTotal());

        return v;
    }


}
