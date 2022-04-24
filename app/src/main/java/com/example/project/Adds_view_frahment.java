package com.example.project;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.ui.Add.AddsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;


public class Adds_view_frahment extends Fragment {
    TextView name, preview, cost, date;
    CarouselView photo;
    Button call;
    ImageButton previous;
    ArrayList<Bitmap> photos = new ArrayList<>();
    int[] sampleImages = {R.drawable.test, R.drawable.test};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String id = bundle.getString("ID");
        Log.d("add_id", id);
        View view = inflater.inflate(R.layout.adds_view, container, false);
        name = view.findViewById(R.id.add_name_view);
        preview = view.findViewById(R.id.add_preview_view);
        cost = view.findViewById(R.id.add_cost_view);
        date = view.findViewById(R.id.add_date);
        photo = view.findViewById(R.id.carousel_add_View);
        call = view.findViewById(R.id.add_user_call);
        previous = view.findViewById(R.id.back_button_add);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myAddsRef = mDatabase.getReference("Adds");
        myAddsRef.child(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                Adds adds = snapshot1.getValue(Adds.class);
                assert adds != null;
                Log.d("bol'", "8");
                for (String i: adds.photo){
                    photos.add(StringToBitMap(i));
                }
                name.setText(adds.name);
                preview.setText(adds.preview);
                cost.setText(Integer.toString(adds.cost));
                date.setText(adds.date);
                photo.setPageCount(photos.size());
                photo.setImageListener(imageListener);
                photo.setImageClickListener(imageClickListener);
            }
            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageBitmap(photos.get(position));
                }
            };
            ImageClickListener imageClickListener = new ImageClickListener() {
                @Override
                public void onClick(int position) {
                    Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
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