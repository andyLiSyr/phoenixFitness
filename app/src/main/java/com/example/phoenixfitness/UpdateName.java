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

public class UpdateName extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText updateFirstName;
    private EditText updateLastName;
    private Button updateButton;
    private DocumentReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        updateFirstName = findViewById(R.id.updateFirstName);
        updateLastName = findViewById(R.id.updateLastName);
        db = FirebaseFirestore.getInstance();
        updateButton = findViewById(R.id.updateButton);
        userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateName();

            }
        });

    }

    private void updateName(){
        String firstName = updateFirstName.getText().toString();
        String lastName = updateLastName.getText().toString();
        if (TextUtils.isEmpty(firstName)){
            updateFirstName.setError("First name cannot be empty");
            updateFirstName.requestFocus();
        }
        else if (TextUtils.isEmpty(lastName)) {
            updateLastName.setError("Last name cannot be empty");
            updateLastName.requestFocus();
        }

        else{
            userRef.update("firstName",firstName);
            userRef.update("lastName",lastName);
            startActivity(new Intent(UpdateName.this,UserProfile.class));
        }


        }

}