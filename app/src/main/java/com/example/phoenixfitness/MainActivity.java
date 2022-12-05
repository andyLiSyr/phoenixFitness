package com.example.phoenixfitness;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    private Button rankingButton;
    private ImageView profileImage;
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
    private Sensor mStepDetector;
    private Sensor mStepCounter;
    private boolean stepCounterWorking;
    int steps=0;
    int counterSteps = 0;
    int detectorSteps= 0;
    private TextView textCalsBurned;

    //Input Calories
    private TextView textCalsCounter;
    private EditText textCalsEntered;
    int caloriesEnt = 0;



    //SharedPreferences
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.phoenixfitness";
    private final String TODAY_DATE = "todayDate";
    private final String DAILYSTEPS_KEY = "dailysteps";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buttons
        rankingButton = findViewById(R.id.rankingButton);
        rankingButton.setOnClickListener(this);

        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(this);

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

        //StepCounter
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
                PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        textStepCounter = findViewById(R.id.stepscount);
        textCalsBurned = findViewById(R.id.caloriesBurncount);


        textStepCounter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // int calsB = Integer.parseInt(textStepCounter.getText().toString());
               // textCalsBurned.setText(String.valueOf(calsB/20));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            mStepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            stepCounterWorking = true;
        }
        else {
            textStepCounter.setText("Counter Sensor is not Working");
            stepCounterWorking = false;
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            stepCounterWorking = true;
        }
        else {
            textStepCounter.setText("Counter Sensor is not Working");
            stepCounterWorking = false;
        }

        //Calories Counter
        textCalsCounter = findViewById(R.id.totalcaloriescount);
        textCalsEntered = findViewById(R.id.enterCals);

        //Shared Preference
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        steps = mPreferences.getInt(DAILYSTEPS_KEY, 0);
        //textStepCounter.setText(String.valueOf(steps)); //steps - 0
        resetStepsAndCals();
    }



    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.rankingButton:
                openRankingActivity();
                break;
            case R.id.profileImage:
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
    private void resetStepsAndCals(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        //c.set(Calendar.MILLISECOND,0);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent =PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC,c.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }

    private void updateCals() {
        if (TextUtils.isEmpty(textCalsEntered.getText())){
            textCalsEntered.setError("No calories input not allowed");
            textCalsEntered.requestFocus();
        }



        else {
            caloriesEnt = Integer.parseInt(textCalsEntered.getText().toString());

            //textCalsCounter.setText(String.valueOf(caloriesEnt));
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        int calories = documentSnapshot.getLong("calories").intValue();
                        userRef.update("calories", caloriesEnt + calories);
                    }
                }
            });
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
        else {
            userRef = db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    accountName.setText(value.getString("firstName") + " " + value.getString("lastName"));
                    String url=value.getString("image");
                    Picasso.get().load(url).into(profileImage);
                    textCalsCounter.setText(value.getLong("calories").toString());
                    //textStepCounter.setText(value.getLong("dailySteps").toString());
                }
            });
        }

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_STEP_DETECTOR:
                detectorSteps++;
                break;
            case Sensor.TYPE_STEP_COUNTER:
                if (counterSteps < 1){
                    counterSteps = (int) sensorEvent.values [0];
                }
                steps = (int) sensorEvent.values[0] - counterSteps;
                //userRef.update("dailySteps",steps);
                break;
        }

        textStepCounter.setText(String.valueOf(steps));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null)
            sensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null)
            sensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //userRef.update("dailySteps",7);
        SharedPreferences.Editor preferenceEditor = mPreferences.edit();
        //preferenceEditor.putInt(CALSENT_KEY, caloriesEnt);
        preferenceEditor.putInt(DAILYSTEPS_KEY, steps);

       // preferenceEditor.putString(TODAY_DATE, storedDate);

        preferenceEditor.apply();

    }









}