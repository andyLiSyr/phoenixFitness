package com.example.phoenixfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
//Sign up page
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    //Declaring variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText signUpEmailInput;
    private EditText signUpPasswordInput;
    private Button signUpButton;
    private TextView goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Initializing declared variables

        //Firebase authentication instance
        mAuth = FirebaseAuth.getInstance();

        //sign up input
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        signUpEmailInput = findViewById(R.id.signUpEmailInput);
        signUpPasswordInput = findViewById(R.id.signUpPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);

        //direct to login page button
        goToLogin = findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(this);

        //instance of Firebase database instance
        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            //execute create user function after clicking sign up button
            case R.id.signUpButton:
                createUser();
                break;
            //execute open log in function after clicking go to log in button
            case R.id.goToLogin:
                openLoginActivity();
                break;
            default:
                break;
        }
    }

   //create user function
    private void createUser(){
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = signUpEmailInput.getText().toString();
        String password = signUpPasswordInput.getText().toString();
        //Url for default profile image
        String url = "https://firebasestorage.googleapis.com/v0/b/phoenixfitness-d8537.appspot.com/o/images%2Ftestprofile.png?alt=media&token=960a5e06-a7ce-4a3b-baaf-c6191397115f";

        //error checking for empty first name input
        if (TextUtils.isEmpty(firstName)){
            firstNameInput.setError("First name cannot be empty");
            firstNameInput.requestFocus();
        }
        //error checking for empty last name input
        else if (TextUtils.isEmpty(lastName)){
            lastNameInput.setError("Last name cannot be empty");
            lastNameInput.requestFocus();
        }
        //error checking for empty email address or not following email address pattern
        else if (TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            signUpEmailInput.setError("Invalid email");
            signUpEmailInput.requestFocus();
        }
        //error checking for empty password input
        else if (TextUtils.isEmpty(password)){
            signUpPasswordInput.setError("Empty password not allowed");
            signUpPasswordInput.requestFocus();
        }
        //error checking for password length less than 5 characters long
        else if (password.length() < 5) {
            signUpPasswordInput.setError("Password should be at least 5 characters");
            signUpPasswordInput.requestFocus();
        }
        //if all input is acceptable create the user with email and password as authentication for logging in
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //create user and update user database with new entry
                            if(task.isSuccessful()){
                                Map<String, Object> user = new HashMap<>();
                                user.put("firstName", firstName);
                                user.put("lastName", lastName);
                                user.put("email", email);
                                user.put("image", url);
                                user.put("calories", 0);
                                user.put("dailySteps", 0);
                                db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //if account created successfully display text saying user registered successfully
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this, "User Registered Successfully",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        }
                                        // if there was an error with creating the account, display error message
                                        else{
                                            Toast.makeText(SignUpActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
        }
    }
    //open login page function
    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}