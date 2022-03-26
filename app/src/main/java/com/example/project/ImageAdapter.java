package com.example.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public Bitmap[] mThumbIds;
    public Integer z;

    public ImageAdapter(Context c, Bitmap[] s, int z) {
        mContext = c; mThumbIds = s; this.z = z;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public void Update(Bitmap[] ar){
        this.mThumbIds = ar;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View grid;

        if (convertView == null) {
            grid = new View(mContext);
            //LayoutInflater inflater = getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(z, parent, false);
        } else {
            grid = (View) convertView;
        }

        ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
        TextView textView = (TextView) grid.findViewById(R.id.textpart);
        TextView textView_price = (TextView) grid.findViewById(R.id.textpart_price);
        TextView textView_place = (TextView) grid.findViewById(R.id.textpart_place);
        imageView.setImageBitmap(mThumbIds[position]);
        return grid;
    }


}