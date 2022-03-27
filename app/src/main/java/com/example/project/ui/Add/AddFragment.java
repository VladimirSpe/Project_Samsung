package com.example.project.ui.Add;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.ImageAdapter;
import com.example.project.ImageTextAdapter;
import com.example.project.Ocn;
import com.example.project.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class AddFragment extends Fragment {
    private int c = 0;
    private final int Pick_image = 1;
    GridView gridview1;
    Bitmap[] a = new Bitmap[5];


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        gridview1 = (GridView) view.findViewById(R.id.gridview1);
        gridview1.setNumColumns(3);
        Log.d("Click", Arrays.toString(a));
        a[0] = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
        gridview1.setOnItemClickListener(gridviewOnItemClickListener);
        return view;
    }

    private final GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Log.d("Click", String.valueOf(position));
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Pick_image);
            c = (int) id;

        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        Context applicationContext = Ocn.getContextOfApplication();
                        final InputStream imageStream = applicationContext.getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        a[c] = selectedImage;
                        gridview1.setAdapter(new ImageAdapter(getActivity(), a, R.layout.add_menu1));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }}
}



