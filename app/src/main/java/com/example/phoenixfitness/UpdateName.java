package com.example.phoenixfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
//Update name page
public class UpdateName extends AppCompatActivity {
    //Declare variables
    private FirebaseFirestore db;
    private EditText updateFirstName;
    private EditText updateLastName;
    private Button updateButton;
    private DocumentReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        //Initialize declared variables
        updateFirstName = findViewById(R.id.updateFirstName);
        updateLastName = findViewById(R.id.updateLastName);
        db = FirebaseFirestore.getInstance();
        updateButton = findViewById(R.id.updateButton);
        //reference to a user in the user database
        userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //click on update button to execute updateName function
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateName();
            }
        });
    }
    //updateName function
    private void updateName(){
        //get user input
        String firstName = updateFirstName.getText().toString();
        String lastName = updateLastName.getText().toString();

        //error checking for empty first name input
        if (TextUtils.isEmpty(firstName)){
            updateFirstName.setError("First name cannot be empty");
            updateFirstName.requestFocus();
        }

        //error checking for empty last name input
        else if (TextUtils.isEmpty(lastName)) {
            updateLastName.setError("Last name cannot be empty");
            updateLastName.requestFocus();
        }

        //update user's first and last name in Firebase database
        else{
            userRef.update("firstName",firstName);
            userRef.update("lastName",lastName);
            //redirect to user profile page after user has updated name
            startActivity(new Intent(UpdateName.this,UserProfile.class));
        }

        }

}