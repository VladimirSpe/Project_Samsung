package com.example.project.ui.Add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.Adds;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Start_addFragment extends Fragment{


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddsAdapter adapter = new AddsAdapter(getActivity(), makeAdds());
        View view = inflater.inflate(R.layout.fragment_start_add, container, false);
        ListView lv = (ListView) view.findViewById(R.id.adds_listview);
        lv.setAdapter(adapter);
        return view;
    }
    Adds[] makeAdds(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = mDatabase.getReference("users");
        Adds[] adds = new Adds[7];
        myRef.child("adds").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
        return adds;
    }
}

