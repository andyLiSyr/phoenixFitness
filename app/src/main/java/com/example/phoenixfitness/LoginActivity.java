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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText logInEmailInput;
    private EditText logInPasswordInput;
    private Button logInButton;
    private TextView goToSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        logInEmailInput = findViewById(R.id.logInEmailInput);
        logInPasswordInput = findViewById(R.id.logInPasswordInput);
        logInButton = findViewById(R.id.logInButton);
        logInButton.setOnClickListener(this);
        goToSignUp = findViewById(R.id.goToSignUp);
        goToSignUp.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logInButton:
                logInUser();
                break;
            case R.id.goToSignUp:
                openSignUpActivity();
                break;
            default:
                break;
        }
    }

    private void logInUser(){
        String email = logInEmailInput.getText().toString();
        String password = logInPasswordInput.getText().toString();

        if(TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            logInEmailInput.setError("Invalid email");
            logInEmailInput.requestFocus();

        }
        else if (TextUtils.isEmpty(password)){
            logInPasswordInput.setError("Empty Password not allowed");
            logInPasswordInput.requestFocus();
        }

        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Log in Successful",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void openSignUpActivity(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
