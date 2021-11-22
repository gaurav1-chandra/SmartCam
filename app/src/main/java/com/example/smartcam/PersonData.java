package com.example.smartcam;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;

public class PersonData {


    String faces="faces :";
    String path;



    public PersonData(String faces,  String path) {
        this.faces="faces :"+faces;
        this.path =path;
    }

    public String getFaces() {
        return faces;
    }

    public void setFaces(String faces) {
        this.faces = faces;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }




}
