package com.example.project.ui.Menu;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;

import com.example.project.ImageAdapter;
import com.example.project.ImageTextAdapter;
import com.example.project.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

public class MenuFragment extends Fragment {

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.test, R.drawable.test};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        carouselView = view.findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
        carouselView.setImageClickListener(imageClickListener);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setNumColumns(2);
        Bitmap[] a = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.test)};
        gridview.setAdapter(new ImageTextAdapter(getActivity(), a, R.layout.add_menu));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);
        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };
    ImageClickListener imageClickListener = new ImageClickListener() {
        @Override
        public void onClick(int position) {
            Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
        }
    };
    private final GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Log.d("Click", String.valueOf(position));
        }
    };

}