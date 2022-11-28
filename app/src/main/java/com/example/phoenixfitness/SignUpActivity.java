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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
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
        mAuth = FirebaseAuth.getInstance();
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        signUpEmailInput = findViewById(R.id.signUpEmailInput);
        signUpPasswordInput = findViewById(R.id.signUpPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(this);
        goToLogin = findViewById(R.id.goToLogin);
        goToLogin.setOnClickListener(this);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.signUpButton:
                createUser();
                break;
            case R.id.goToLogin:
                openLoginActivity();
                break;
            default:
                break;
        }
    }

    private void createUser(){
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String email = signUpEmailInput.getText().toString();
        String password = signUpPasswordInput.getText().toString();

        if (TextUtils.isEmpty(firstName)){
            firstNameInput.setError("First name cannot be empty");
            firstNameInput.requestFocus();
        }
        else if (TextUtils.isEmpty(lastName)){
            lastNameInput.setError("Last name cannot be empty");
            lastNameInput.requestFocus();
        }
        else if (TextUtils.isEmpty(email) || !(Patterns.EMAIL_ADDRESS.matcher(email).matches())){
            signUpEmailInput.setError("Invalid email");
            signUpEmailInput.requestFocus();
        }
        else if (TextUtils.isEmpty(password)){
            signUpPasswordInput.setError("Empty password not allowed");
            signUpPasswordInput.requestFocus();
        }
        else if (password.length() < 5) {
            signUpPasswordInput.setError("Password should be at least 5 characters");
            signUpPasswordInput.requestFocus();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Map<String, Object> user = new HashMap<>();
                                user.put("firstName", firstName);
                                user.put("lastName", lastName);
                                user.put("email", email);

                                db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this, "User Registered Successfully",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        }
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

    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}