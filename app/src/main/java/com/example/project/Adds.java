package com.example.project;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Adds{

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
    //private DatabaseReference mDatabase;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public Bitmap[] Photo;
    public String name;
    String preview;
    String number;
    public Integer cost;
    FirebaseUser userid = auth.getCurrentUser();
    public Adds(Bitmap[] photo, String name, String preview, Integer cost, String number) {
        this.name = name;
        this.cost = cost;
        this.Photo = photo;
        this.preview = preview;
        this.number = number;
    }
    public boolean Add_Adds(){
        MessageDigest digest = null;
        String text = name + preview + Integer.toString(cost) + userid.getUid();
        Log.d("INP", userid.getUid());
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        byte[] encodedhash = digest.digest(
                text.getBytes(StandardCharsets.UTF_8));
        DatabaseReference myRef = mDatabase.getReference("Adds");
        myRef.child((bytesToHex(encodedhash)));
        myRef.child(bytesToHex(encodedhash)).child("name").setValue(name);
        myRef.child(bytesToHex(encodedhash)).child("cost").setValue(cost);
        myRef.child(bytesToHex(encodedhash)).child("number").setValue(number);
        myRef.child(bytesToHex(encodedhash)).child("preview").setValue(preview);
        myRef.child(bytesToHex(encodedhash)).child("userid").setValue(userid.getUid());
        for (int i = 0; i < 5; i ++){
            if (Photo[i] != null){
                myRef.child(bytesToHex(encodedhash)).child("photo").child(Integer.toString(i)).setValue(BitMapToString(Photo[i]));
            }
        }
        myRef = mDatabase.getReference("users");
        myRef.child("adds").child(text);
        return true;
    }
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}

