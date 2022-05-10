package com.example.project;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Event {
    public ArrayList<String> photo;
    public String name;
    public String preview;
    public String date;
    public Event(){
    }
    public void Create_New(String name, String preview, String date, ArrayList<String> photo){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference Ref = mDatabase.getReference("Events");
        String id = name;
        Ref.child(id).child("name").setValue(name);
        Ref.child(id).child("preview").setValue(preview);
        for (int i = 0; i < photo.size(); i++){
            if (photo.get(i) != null){
                Ref.child(id).child("photo").child(Integer.toString(i)).setValue(photo.get(i));
            }
        }
        Ref.child(id).child("date").setValue(date);
    }
}
