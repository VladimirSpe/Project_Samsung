package com.example.project.ui.Add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Adds;
import com.example.project.Adds_view_frahment;
import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Start_addFragment extends Fragment{
    ImageButton ad_add;
    ProgressBar pr;
    boolean p = false;
    int c = 0;
    public static ArrayList<Adds> adds_list = new ArrayList<Adds>();
    public static ArrayList<String> adds_id_list = new ArrayList<String>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_add, container, false);
        ad_add = view.findViewById(R.id.ad_add);
        pr = view.findViewById(R.id.add_progress_b);
        ListView lv = (ListView) view.findViewById(R.id.adds_listview);
        final String[] user = {Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()};
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = mDatabase.getReference("users");
        DatabaseReference myAddsRef = mDatabase.getReference("Adds");
        myRef.child(user[0]).child("Adds").addValueEventListener(new ValueEventListener() {
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
                            adds_list.add(adds);
                            if (adds_list.size() == c) {
                                AddsAdapter adapter = new AddsAdapter(getActivity(), adds_list);
                                pr.setVisibility(View.INVISIBLE);
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                            long id) {
                                        Fragment frag2 = new Adds_view_frahment();
                                        Bundle bundle = new Bundle();
                                        Log.d("bundle", String.valueOf(adds_id_list.get(position)));
                                        bundle.putString("ID", String.valueOf(adds_id_list.get(position)));
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
        ad_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag2 = new AddFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_activity_main, frag2);
                ft.addToBackStack(frag2.toString());
                ft.commit();
            }
        });
        return view;
    }
}

