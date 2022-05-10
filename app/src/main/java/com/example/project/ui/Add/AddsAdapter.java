package com.example.project.ui.Add;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Adds;
import com.example.project.R;

import java.util.ArrayList;

public class AddsAdapter extends ArrayAdapter<Adds> {
    public AddsAdapter(Context context, ArrayList<Adds> arr){
        super(context, R.layout.fragment_start_add, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Adds adds = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_listview, null);
        }
        try {
            ((ImageView) convertView.findViewById(R.id.photo)).setImageBitmap(StringToBitMap(adds.photo.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView) convertView.findViewById(R.id.name)).setText(String.valueOf(adds.name));
        ((TextView) convertView.findViewById(R.id.money)).setText(String.valueOf(adds.cost) + " ₽");
        return convertView;
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
