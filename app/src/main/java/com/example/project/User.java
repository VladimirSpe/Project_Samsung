package com.example.project;

import android.graphics.Bitmap;

public class User implements Cloneable{
    public String Name;
    public String Number;
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
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

