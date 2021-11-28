package com.example.intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button camera;
    Button gallery;
    ImageView imageView;
    boolean cameraOpen=false;
    boolean galleryOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialization
        camera=findViewById(R.id.camera);
        gallery=findViewById(R.id.gallery);
        imageView=findViewById(R.id.imageView);

        //ActivityResult launcher
        ActivityResultLauncher<Intent> launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                 if (result.getData()!=null) {
                     if (cameraOpen) {
                         Intent cameraIntent = result.getData();
                         Bundle b = cameraIntent.getExtras();
                         //inside a bundle we can get the captured image and extract bitmap
                         Bitmap bitmap = (Bitmap) b.get("data");
                         imageView.setImageBitmap(bitmap);
                         cameraOpen = false;
                     }
                     if (galleryOpen) {
                         Intent galleryIntent = result.getData();
                         Uri uri = galleryIntent.getData();
                         imageView.setImageURI(uri);
                         galleryOpen = false;
                     }
                 }else{
                     Toast.makeText(getApplicationContext(), "Upload An image4", Toast.LENGTH_SHORT).show();
                 }
            }
        });
        camera.setOnClickListener(v->{
            cameraOpen=true;
            launcher.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
        });
        gallery.setOnClickListener(v->{
            galleryOpen=true;
            launcher.launch(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"));
        });
    }
}