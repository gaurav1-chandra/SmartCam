package com.example.smartcam;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
   // private static StorageReference mstorageReference;
    private DatabaseReference mDatabase;

    FirebaseStorage mfirebaseStorage;
    StorageReference mstorageReference;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirebaseStorage=FirebaseStorage.getInstance();
        mstorageReference=mfirebaseStorage.getReference();


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<PersonData> personData = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // My top posts by number of stars
        mDatabase.child("video").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    personData.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String faces ="";

                        for (DataSnapshot face : postSnapshot.child("faces").getChildren()){
                            String temp=face.getValue(String.class);
                            if(faces.isEmpty())
                                faces=temp;
                            else
                            faces=faces+", "+temp;
                            Log.d("onBindViewHolder",temp);
                        }
                            String path = postSnapshot.child("path").getValue(String.class);
                        Boolean isRecognized=postSnapshot.child("isRecognized").getValue(Boolean.class);
                        // Log.d("onDataChange",path+faces+personData.size());
                        //PersonData personData2=new PersonData(faces,path);
                        // personData.add(faces,path);
                        if(isRecognized)
                        personData.add(new PersonData(faces, path));
                    }
                    sortlist(personData);
                    dataAdapter mydataAdapter = new dataAdapter(personData, MainActivity.this);
                    recyclerView.setAdapter(mydataAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        dataAdapter mydataAdapter= new dataAdapter(personData,MainActivity.this);
        recyclerView.setAdapter(mydataAdapter);




    }




    private void sortlist(ArrayList<PersonData> personData) {
        Collections.sort(personData, new Comparator<PersonData>() {
            @Override
            public int compare(PersonData o1, PersonData o2) {
                return o2.getPath().compareTo(o1.path);
            }
        });
    }




}