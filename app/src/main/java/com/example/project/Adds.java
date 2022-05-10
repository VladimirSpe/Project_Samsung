package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adds{

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://help-me-e2de7-default-rtdb.europe-west1.firebasedatabase.app");
    //private DatabaseReference mDatabase;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public ArrayList<String> photo;
    public String name;
    public String preview;
    public String number;
    public Integer cost;
    public String userid;
    public String id;
    public String date;
    FirebaseUser userID = auth.getCurrentUser();
    public Adds(ArrayList<String> photo, String name, String preview, Integer cost, String number, String id) {

        this.name = name;
        this.cost = cost;
        this.preview = preview;
        this.number = number;
        this.photo = photo;
        this.id = id;
    }
    public Adds(){
    }
    public boolean Add_Adds(){
        MessageDigest digest = null;

        if (id.length() == 0){
            id = name + userID.getUid();
        }
        Log.d("id", id);
        Log.d("INP", userID.getUid());
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digest != null;
        Date data = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        byte[] encodedhash = digest.digest(id.getBytes(StandardCharsets.UTF_8));
        DatabaseReference Ref = mDatabase.getReference("users");
        Ref.child(userID.getUid()).child("Adds").child(id).setValue("true");
        DatabaseReference myRef = mDatabase.getReference("Adds");
        myRef.child((id));
        myRef.child(id).child("name").setValue(name);
        myRef.child(id).child("cost").setValue(cost);
        myRef.child(id).child("number").setValue(number);
        myRef.child(id).child("preview").setValue(preview);
        myRef.child(id).child("time").setValue(format.format(data));
        myRef.child(id).child("userid").setValue(userID.getUid());
        for (int i = 0; i < photo.size(); i++){
            if (photo.get(i) != null){
                myRef.child(id).child("photo").child(Integer.toString(i)).setValue(photo.get(i));
            }
        }

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
}

