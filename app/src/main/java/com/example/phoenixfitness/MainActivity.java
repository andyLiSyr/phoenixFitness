package com.example.phoenixfitness;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button rankingButton;
    private ImageButton profileButton;
    private Button friendsOnline;
    private Button weeklyButton;
    private FirebaseAuth mAuth;
    private DocumentReference userRef;
    private FirebaseFirestore db;
    private TextView accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rankingButton = findViewById(R.id.rankingButton);
        rankingButton.setOnClickListener(this);

        profileButton = findViewById(R.id.profile);
        profileButton.setOnClickListener(this);

        friendsOnline = findViewById(R.id.friends);
        friendsOnline.setOnClickListener(this);

        weeklyButton = findViewById(R.id.weekly);
        weeklyButton.setOnClickListener(this);

        accountName = findViewById(R.id.accountName);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                accountName.setText(value.getString("firstName") + " " + value.getString("lastName"));
            }
        });

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rankingButton:
                openRankingActivity();
                break;
            case R.id.profile:
                openProfileActivity();
                break;
            case R.id.friends:
                openFriendsActivity();
                Toast.makeText(this,"Friends Are TextView for now", Toast.LENGTH_LONG).show();
                break;
            case R.id.weekly:
                openWeeklyActivity();
                break;
            default:
                break;
        }

    }

    private void openWeeklyActivity() {
        Intent intent = new Intent(this,WeeklyReport.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);
    }

    public void openRankingActivity(){
        Intent intent = new Intent(this,Ranking.class);
        startActivity(intent);
    }
    public void openFriendsActivity(){
        Intent intent = new Intent(this,Friends.class);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user= mAuth.getCurrentUser();
        if (user==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

    }




}