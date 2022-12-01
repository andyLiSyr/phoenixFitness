package com.example.phoenixfitness;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private Button rankingButton;
    private ImageButton profileButton;
    private Button friendsOnline;
    private Button weeklyButton;
    private Button inputCalsButton;

    //FireBase
    private FirebaseAuth mAuth;
    private DocumentReference userRef;
    private FirebaseFirestore db;
    private TextView accountName;

    //StepCounter
    private TextView textStepCounter;
    private SensorManager sensorManager;
    private Sensor mStepCounter;
    private boolean stepCounterWorking;
    int steps = 0;

    //Input Calories
    private TextView textCalsCounter;
    private TextView textCalsEntered;
    int caloriesEnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buttons
        rankingButton = findViewById(R.id.rankingButton);
        rankingButton.setOnClickListener(this);

        profileButton = findViewById(R.id.profile);
        profileButton.setOnClickListener(this);

        friendsOnline = findViewById(R.id.friends);
        friendsOnline.setOnClickListener(this);

        weeklyButton = findViewById(R.id.weekly);
        weeklyButton.setOnClickListener(this);

        inputCalsButton = findViewById(R.id.InputCals);
        inputCalsButton.setOnClickListener(this);

        accountName = findViewById(R.id.accountName);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user= mAuth.getCurrentUser();
        if (user!=null){

            userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    accountName.setText(value.getString("firstName") + " " + value.getString("lastName"));
                }
            });

     }

        //StepCounter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
                PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        textStepCounter = findViewById(R.id.stepscount);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            stepCounterWorking = true;
        }
        else {
            textStepCounter.setText("Counter Sensor is not Working");
            stepCounterWorking = false;
        }

        //Calories Counter
        textCalsCounter = findViewById(R.id.totalcaloriescount);
        textCalsEntered = findViewById(R.id.enterCals);

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
            case R.id.InputCals:
                updateCals();
                break;
            default:
                break;
        }

    }

    private void updateCals() {
        caloriesEnt = caloriesEnt + Integer.parseInt(textCalsEntered.getText().toString());
        textCalsCounter.setText(String.valueOf(caloriesEnt));
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


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor == mStepCounter){
            steps = (int) sensorEvent.values[0];
            textStepCounter.setText(String.valueOf(steps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
            sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
    }
}