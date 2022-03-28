package com.example.project.ui.Add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Adds;
import com.example.project.R;

public class AddsAdapter extends ArrayAdapter<Adds> {
    public AddsAdapter(Context context, Adds[] arr){
        super(context, R.layout.fragment_start_add, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Adds adds = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_listview, null);
        }
        ((ImageView) convertView.findViewById(R.id.photo)).setImageBitmap(adds.Photo[0]);
        ((TextView) convertView.findViewById(R.id.name)).setText(String.valueOf(adds.name));
        ((TextView) convertView.findViewById(R.id.money)).setText(String.valueOf(adds.cost));
        return convertView;
    }
}
