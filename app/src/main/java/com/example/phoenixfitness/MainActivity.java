package com.example.phoenixfitness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button rankingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rankingButton=findViewById(R.id.rankingButton);
        rankingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rankingButton:
                openRankingActivity();
                break;
            default:
                break;
        }

    }
    public void openRankingActivity(){
        Intent intent= new Intent(this,Ranking.class);
        startActivity(intent);
    }
}