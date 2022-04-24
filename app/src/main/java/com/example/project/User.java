package com.example.project;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class User implements Cloneable{
    public String Name = "Name";
    public String Number = "800000000";
    Integer Age;
    String Email;
    public String Photo;
    String uid;
    Adds[] adds;
    public Integer Wallet;
    public User(String Name, String Photo, String Number){
        this.Name = Name;
        this.Photo = Photo;
        this.Number = Number;
    }
    public User(){
    }
    public boolean changePhoto(Bitmap photo){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = mDatabase.getReference("users");
        assert user != null;
        myRef.child(user.getUid()).child("Photo").setValue(BitMapToString(photo));
        return true;
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}

