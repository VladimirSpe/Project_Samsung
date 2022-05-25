package com.example.project.ui.Menu;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Adds;
import com.example.project.Adds_view_frahment;
import com.example.project.Event;
import com.example.project.ImageTextAdapter;
import com.example.project.R;
import com.example.project.ui.Add.AddsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Locale;
public class MenuSearchFragment extends Fragment {
    ListView listView;
    ImageButton back;
    ProgressBar pr;
    FrameLayout frameLayout;
    TextView nothing;
    ArrayList<String> adds_id_list = new ArrayList<String>();
    ArrayList<Adds> adds_list = new ArrayList<Adds>();
    ArrayList<Adds> adds_list_tr = new ArrayList<Adds>();
    int c = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_search, container, false);
        Bundle bundle = getArguments();
        String search_text = bundle.getString("search");
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myAddsRef = mDatabase.getReference("Adds");
        back = view.findViewById(R.id.back_button_search);
        listView = view.findViewById(R.id.adds_listview_menu);
        pr = view.findViewById(R.id.add_progress_b_menu);
        SearchView searchView = view.findViewById(R.id.search_edit);
        frameLayout = view.findViewById(R.id.search_view1);
        nothing = view.findViewById(R.id.nothing);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag2 = new MenuFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                ft.addToBackStack(frag2.toString());
                ft.commit();
            }
        });
        myAddsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    c++;
                    adds_id_list.add(ds.getKey());
                    adds_list.clear();
                    myAddsRef.child(String.valueOf(ds.getKey())).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            Adds adds = snapshot1.getValue(Adds.class);
                            assert adds != null;
                            if (adds.name.toLowerCase().contains(search_text.toLowerCase())) {
                                adds_list_tr.add(adds);
                            }
                            adds_list.add(adds);
                            if (adds_list.size() == c) {
                                AddsAdapter adapter = new AddsAdapter(getActivity(), adds_list_tr);
                                pr.setVisibility(View.INVISIBLE);
                                listView.setAdapter(adapter);
                                if (adds_list_tr.size() == 0){
                                    pr.setVisibility(View.INVISIBLE);
                                    nothing.setVisibility(View.VISIBLE);
                                }
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                            long id) {
                                        Fragment frag2 = new Adds_view_frahment();
                                        Bundle bundle = new Bundle();
                                        Log.d("bundle", adds_list_tr.get(position).name + adds_list_tr.get(position).userid);
                                        bundle.putString("ID", adds_list_tr.get(position).name + adds_list_tr.get(position).userid);
                                        bundle.putString("Type", "menu");
                                        bundle.putString("Query", search_text);
                                        adds_id_list.clear();
                                        frag2.setArguments(bundle);
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                                        ft.addToBackStack(frag2.toString());
                                        ft.commit();
                                    }
                                });
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

}