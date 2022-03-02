package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageTextAdapter extends BaseAdapter {
    private Context mContext;

    public ImageTextAdapter(Context c) {
        mContext = c;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View grid;

        if (convertView == null) {
            grid = new View(mContext);
            //LayoutInflater inflater = getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.add_menu, parent, false);
        } else {
            grid = (View) convertView;
        }

        ImageView imageView = (ImageView) grid.findViewById(R.id.imagepart);
        TextView textView = (TextView) grid.findViewById(R.id.textpart);
        TextView textView_price = (TextView) grid.findViewById(R.id.textpart_price);
        TextView textView_place = (TextView) grid.findViewById(R.id.textpart_place);
        imageView.setImageResource(mThumbIds[position]);
        textView_price.setText("0000Р");
        textView_place.setText("Где-то там");
        textView.setText("Название");

        return grid;
    }

    // references to our images
    public Integer[] mThumbIds = { R.drawable.test, R.drawable.test,
            R.drawable.test, R.drawable.test, R.drawable.test,
            R.drawable.test, R.drawable.test};
}
