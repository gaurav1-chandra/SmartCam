package com.example.smartcam;

import static android.icu.util.TimeZone.*;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class dataAdapter extends  RecyclerView.Adapter<dataAdapter.ViewHolder> {

    public static final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
    MainActivity mn=new MainActivity();

    ArrayList<PersonData> personData;
    Context context;
    public  dataAdapter(ArrayList<PersonData> personData, MainActivity activity){
           this.personData=personData;
           this.context=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PersonData data = personData.get(position);
        holder.personName.setText(data.getFaces());
       String path =data.getPath();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        String location=path;
       location= location.replaceAll(":","-");
        location=location.replaceAll(" ","-");
        location="image/"+location+".jpg";
        Log.d("Location", "onBindViewHolder: "+ location);
        StorageReference photoReference= storageReference.child(location);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imageView.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Location", "onBindViewHolder: No such file found");
               // Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });



       //data and time formate
        Date date = new Date();
        try {
            date = inputFormat.parse(path);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String niceDateStr = (String) DateUtils.getRelativeTimeSpanString(date.getTime() , Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
      //  Log.d("onTimeChange",niceDateStr);
        holder.timeStamp.setText(niceDateStr);


        holder.videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"video will play",Toast.LENGTH_SHORT).show();
               Context mContext=context;
                Intent myIntent = new Intent(mContext, Model.class);
                myIntent.putExtra("path",path);
                mContext.startActivity(myIntent);

            }
        });



    }




    @Override
    public int getItemCount() {
        //Log.d("onDatachange", String.valueOf(personData.size()));
        return personData.size();

    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView personName;
        TextView  timeStamp;
        ImageView videoImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            personName = itemView.findViewById(R.id.txtnames);
            timeStamp = itemView.findViewById(R.id.txtdate);
            videoImage = itemView.findViewById(R.id.vdoImage);
        }



    }


}
