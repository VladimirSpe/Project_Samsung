package com.example.project;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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

import com.example.project.ui.Add.AddFragment;
import com.example.project.ui.Add.AddsAdapter;
import com.example.project.ui.Add.Start_addFragment;
import com.example.project.ui.Menu.MenuFragment;
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
    ImageButton previous, edit;
    ArrayList<Bitmap> photos = new ArrayList<>();
    int[] sampleImages = {R.drawable.test, R.drawable.test};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String id = bundle.getString("ID");
        String type = bundle.getString("Type");
        Log.d("add_id", id);
        View view = inflater.inflate(R.layout.adds_view, container, false);
        name = view.findViewById(R.id.add_name_view);
        preview = view.findViewById(R.id.add_preview_view);
        cost = view.findViewById(R.id.add_cost_view);
        date = view.findViewById(R.id.add_date);
        photo = view.findViewById(R.id.carousel_add_View);
        call = view.findViewById(R.id.add_user_call);
        previous = view.findViewById(R.id.back_button_add);
        edit = view.findViewById(R.id.edit_adds);
        if (type.equals("event")){
            edit.setVisibility(View.INVISIBLE);
        }
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        if (type.equals("adds")) {
            DatabaseReference myAddsRef = mDatabase.getReference("Adds");
            myAddsRef.child(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                    Adds adds = snapshot1.getValue(Adds.class);
                    assert adds != null;
                    Log.d("bol'", "8");
                    for (String i : adds.photo) {
                        photos.add(StringToBitMap(i));
                    }
                    name.setText(adds.name);
                    preview.setText(adds.preview);
                    cost.setText(Integer.toString(adds.cost));
                    date.setText(adds.date);
                    ImageListener imageListener = photos_listener(photos);
                    photo.setImageListener(imageListener);
                    photo.setImageClickListener(imageClickListener);
                    photo.setPageCount(photos.size());
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Fragment frag2 = new AddFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Name", adds.name);
                            bundle.putString("Preview", adds.preview);
                            bundle.putString("Cost", Integer.toString(adds.cost));
                            bundle.putStringArrayList("Photo", adds.photo);
                            bundle.putString("ID", id);
                            frag2.setArguments(bundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                            ft.addToBackStack(frag2.toString());
                            ft.commit();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if (type.equals("event")){
            DatabaseReference myAddsRef = mDatabase.getReference("Events");
            myAddsRef.child(String.valueOf(id)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                    Event event = snapshot1.getValue(Event.class);
                    assert event != null;
                    Log.d("bol'", "8");
                    for (String i : event.photo) {
                        photos.add(StringToBitMap(i));
                    }
                    name.setText(event.name);
                    preview.setText(event.preview);
                    date.setText(event.date);
                    ImageListener imageListener = photos_listener(photos);
                    photo.setImageListener(imageListener);
                    photo.setImageClickListener(imageClickListener);
                    photo.setPageCount(photos.size());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }
    ImageClickListener imageClickListener = new ImageClickListener() {
        @Override
        public void onClick(int position) {
        }
    };
    public ImageListener photos_listener(ArrayList<Bitmap> photo) {
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageBitmap(photo.get(position));
            }
        };
        return imageListener;
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