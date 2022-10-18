package com.example.phoenixfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button rankingButton;
    private ImageButton profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rankingButton=findViewById(R.id.rankingButton);
        rankingButton.setOnClickListener(this);
        profileButton=findViewById(R.id.profile);
        profileButton.setOnClickListener(this);

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
            default:
                break;
        }

    }

    private void openProfileActivity() {
        Intent intent = new Intent(this,UserProfile.class);
        startActivity(intent);
    }

    public void openRankingActivity(){
        Intent intent= new Intent(this,Ranking.class);
        startActivity(intent);
    }




}