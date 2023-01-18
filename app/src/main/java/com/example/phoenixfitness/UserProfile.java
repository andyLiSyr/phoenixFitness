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

//User profile page
public class UserProfile extends AppCompatActivity implements View.OnClickListener {
    //Declaring variables
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
        //Initializing declared variables

        //log out button
        logOutButton = findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(this);

        //update photo button
        updatePhoto =findViewById(R.id.updatePhoto);
        updatePhoto.setOnClickListener(this);

        //update name button
        nameButton = findViewById(R.id.nameButton);
        nameButton.setOnClickListener(this);

        //image profile
        imageView= findViewById(R.id.imageView);

        //user name
        userName = findViewById(R.id.userName);

        //Firebase
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //reference to Firebase Cloud Storage where user profile images are stored
        imageStorageRef = FirebaseStorage.getInstance().getReference("images");


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            //log out user after clicking on logout button
            case R.id.logOutButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserProfile.this, MainActivity.class));
                break;
            //execute choose image function when clicking on update photo button
            case R.id.updatePhoto:
                chooseImage();
                break;
            //redirect to update name page after clicking on update name button
            case R.id.nameButton:
                startActivity(new Intent(UserProfile.this,UpdateName.class));
                break;
            default:
                break;

        }
    }
    //upload image function
    private void uploadImage(Uri uri){
        //put image uri in Firebase Cloud Storage
        StorageReference fileRef = imageStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                //get permanent image url of the profile image that is stored in Firebase Cloud Storage
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //update user profile image in the Firebase database with the image url from Cloud Storage
                        userRef.update("image", uri.toString());
                    }
                });
            }
        });
    }
    //get file extension for type of image
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //choose image function from user's phone
    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startImagePickerActivity.launch(intent);

    }
    //get the image uri
    ActivityResultLauncher<Intent> startImagePickerActivity=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result-> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri imageUri = data.getData();
                        //upload image uri to Firebase Cloud Storage where user profile images are stored
                        uploadImage(imageUri);

                    }
                }
            });


    //function for when user profile page appears on the screen
    @Override
    protected void onStart(){
        super.onStart();

        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //get username and profile image from Firebase and load to user profile page
                userName.setText(value.getString("firstName") + " " + value.getString("lastName"));
                String url=value.getString("image");
                Picasso.get().load(url).into(imageView);

            }
        });

    }

}