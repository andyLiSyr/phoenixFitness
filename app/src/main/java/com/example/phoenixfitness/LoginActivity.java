package com.example.phoenixfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
//Login page
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Declaring variables
    private FirebaseAuth mAuth;
    private EditText logInEmailInput;
    private EditText logInPasswordInput;
    private Button logInButton;
    private Button goToSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initializing declared variables

        //Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance();

        //log in input
        logInEmailInput = findViewById(R.id.logInEmailInput);
        logInPasswordInput = findViewById(R.id.logInPasswordInput);
        logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(this);

        //go to sign up page button
        goToSignUp = findViewById(R.id.registerButton);
        goToSignUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //execute loginUser function after pressing log in button
            case R.id.logInButton:
                logInUser();
                break;
            //execute open sign up activity function after pressing sign up button
            case R.id.registerButton:
                openSignUpActivity();
                break;
            default:
                break;
        }
    }

    //log in user function
    private void logInUser(){
        //get email and password from user input
        String email = logInEmailInput.getText().toString();
        String password = logInPasswordInput.getText().toString();

        //error checking for invalid email
        if(TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            logInEmailInput.setError("Invalid email");
            logInEmailInput.requestFocus();

        }

        //error checking for empty password
        else if (TextUtils.isEmpty(password)){
            logInPasswordInput.setError("Empty Password not allowed");
            logInPasswordInput.requestFocus();
        }

        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //if log in successful redirect to user main activity page or home page
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Log in Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    //if there was an error logging in display error message
                    else{
                        Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    //open sign up page function
    private void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
