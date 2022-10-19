package com.example.phoenixfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button rankingButton;
    private ImageButton profileButton;
    private Button friendsOnline;

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
            default:
                break;
        }

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




}