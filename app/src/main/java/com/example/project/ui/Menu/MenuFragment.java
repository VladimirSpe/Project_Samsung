package com.example.project.ui.Menu;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Adds;
import com.example.project.Adds_view_frahment;
import com.example.project.Event;
import com.example.project.ImageAdapter;
import com.example.project.ImageTextAdapter;
import com.example.project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MenuFragment extends Fragment {
    SearchView searchView;
    CarouselView carouselView;
    ProgressBar progressBar;
    int c = 0;
    ArrayList<Bitmap> photos = new ArrayList<>();
    public static ArrayList<Event> events_list = new ArrayList<Event>();
    public static ArrayList<String> events_id_list = new ArrayList<String>();
    int[] sampleImages = {R.drawable.test, R.drawable.test};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        carouselView = view.findViewById(R.id.carouselView);
        searchView = view.findViewById(R.id.search_edit);
        carouselView.setVisibility(View.INVISIBLE);
        progressBar = view.findViewById(R.id.add_progress_carousel_menu);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setNumColumns(2);
        Bitmap[] a = new Bitmap[]{BitmapFactory.decodeResource(getResources(), R.drawable.test)};
        gridview.setAdapter(new ImageTextAdapter(getActivity(), a, R.layout.add_menu));
        gridview.setOnItemClickListener(gridviewOnItemClickListener);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment frag2 = new MenuSearchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("search", String.valueOf(query));
                Log.d("bundle", String.valueOf(query));
                frag2.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                ft.addToBackStack(frag2.toString());
                ft.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myAddsRef = mDatabase.getReference("Events");
        myAddsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    c++;
                    events_id_list.add(ds.getKey());
                    events_list.clear();
                    myAddsRef.child(String.valueOf(ds.getKey())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            Event event = snapshot1.getValue(Event.class);
                            assert event != null;
                            events_list.add(event);
                            photos.add(StringToBitMap(event.photo.get(0)));
                            Log.d("ii", String.valueOf(photos));
                            if (events_list.size() == c) {
                                Log.d("op", "po");
                                ImageListener imageListener = photos_listener(photos);
                                ImageClickListener imageClickListener1 = photos_click_listener(events_id_list);
                                progressBar.setVisibility(View.INVISIBLE);
                                carouselView.setVisibility(View.VISIBLE);
                                carouselView.setImageListener(imageListener);
                                carouselView.setImageClickListener(imageClickListener1);
                                carouselView.setPageCount(photos.size());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }


                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private final GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Log.d("Click", String.valueOf(position));
        }
    };
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public static  Bitmap  scaleImage(Bitmap bm, int newWidth, int newHeight){
        if (bm == null){
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
        if (bm != null & !bm.isRecycled()){
            bm.recycle();//Уничтожить исходное изображение
            bm = null;
        }
        return newbm;
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
    public ImageListener photos_listener(ArrayList<Bitmap> photos) {
        Log.d("Eve", String.valueOf(photos));
        return new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageBitmap(photos.get(position));
            }
        };
    }
    public ImageClickListener photos_click_listener(ArrayList<String> ev){
        ImageClickListener imageClickListener = new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Fragment frag2 = new Adds_view_frahment();
                Bundle bundle = new Bundle();
                bundle.putString("ID", String.valueOf(ev.get(position)));
                bundle.putString("Type", "event");
                Log.d("bundle", String.valueOf(bundle));
                events_id_list.clear();
                frag2.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                ft.addToBackStack(frag2.toString());
                ft.commit();
            }
        };
        return imageClickListener;
    }
}