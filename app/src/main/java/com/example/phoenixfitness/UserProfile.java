package com.example.phoenixfitness;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;


public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    private Button logOutButton;
    private FirebaseFirestore db;
    private TextView userName;
    private DocumentReference userRef;
    private Button updatePhoto;
    private Button nameButton;
    private ImageView imageView;
    private StorageReference imageStorageRef;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile);

        logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(this);
        updatePhoto =findViewById(R.id.updatePhoto);
        updatePhoto.setOnClickListener(this);
        nameButton = findViewById(R.id.nameButton);
        nameButton.setOnClickListener(this);
        imageView= findViewById(R.id.imageView);
        userName = findViewById(R.id.userName);
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageStorageRef = FirebaseStorage.getInstance().getReference("images");


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.logOutButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, MainActivity.class));
                break;
            case R.id.updatePhoto:
                chooseImage();
                break;
            case R.id.nameButton:
                startActivity(new Intent(UserProfile.this,UpdateName.class));
                break;
            default:
                break;

        }
    }
    private void uploadImage(Uri uri){
        StorageReference fileRef = imageStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        userRef.update("image", uri.toString());
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startImagePickerActivity.launch(intent);

    }

    ActivityResultLauncher<Intent> startImagePickerActivity=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result-> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        uploadImage(imageUri);

                    }
                }
            });



    @Override
    protected void onStart(){
        super.onStart();

        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                userName.setText(value.getString("firstName") + " " + value.getString("lastName"));
                String url=value.getString("image");
                Picasso.get().load(url).into(imageView);

            }
        });

    }

}