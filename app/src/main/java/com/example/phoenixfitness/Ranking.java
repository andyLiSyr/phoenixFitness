package com.example.phoenixfitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Leaderboard that displays top 10 users with the most daily steps
public class Ranking extends AppCompatActivity {
    //Declare variables
    private FirebaseFirestore db;
    private CollectionReference userCollectionRef;
    private ArrayList<Integer> steps;
    private ArrayList<String> name;
    private ArrayList<TextView> stepViews;
    private ArrayList<TextView> nameViews;
    private TextView rank1Name, rank1Steps;
    private TextView rank2Name, rank2Steps;
    private TextView rank3Name, rank3Steps;
    private TextView rank4Name, rank4Steps;
    private TextView rank5Name, rank5Steps;
    private TextView rank6Name, rank6Steps;
    private TextView rank7Name, rank7Steps;
    private TextView rank8Name, rank8Steps;
    private TextView rank9Name, rank9Steps;
    private TextView rank10Name, rank10Steps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rankings);
        //Initialize declared variables

        db = FirebaseFirestore.getInstance();

        //arraylist for storing the steps of users on the leaderboard
        steps = new ArrayList<Integer>();

        //arraylist for storing the names of users on the leaderboard
        name = new ArrayList<String>();

        //Leaderboard ranking text views for steps and names
        rank1Name = findViewById(R.id.rank1Name);
        rank1Steps = findViewById(R.id.rank1Steps);
        rank2Name = findViewById(R.id.rank2Name);
        rank2Steps = findViewById(R.id.rank2Steps);
        rank3Name = findViewById(R.id.rank3Name);
        rank3Steps = findViewById(R.id.rank3Steps);
        rank4Name = findViewById(R.id.rank4Name);
        rank4Steps = findViewById(R.id.rank4Steps);
        rank5Name = findViewById(R.id.rank5Name);
        rank5Steps = findViewById(R.id.rank5Steps);
        rank6Name = findViewById(R.id.rank6Name);
        rank6Steps = findViewById(R.id.rank6Steps);
        rank7Name = findViewById(R.id.rank7Name);
        rank7Steps = findViewById(R.id.rank7Steps);
        rank8Name = findViewById(R.id.rank8Name);
        rank8Steps = findViewById(R.id.rank8Steps);
        rank9Name = findViewById(R.id.rank9Name);
        rank9Steps = findViewById(R.id.rank9Steps);
        rank10Name = findViewById(R.id.rank10Name);
        rank10Steps = findViewById(R.id.rank10Steps);

        //Arraylist of of the text view of steps
        stepViews = new ArrayList<>(Arrays.asList(rank1Steps,rank2Steps,rank3Steps,rank4Steps,rank5Steps,rank6Steps,rank7Steps,
        rank8Steps,rank9Steps,rank10Steps));

        //Arraylist of the text view of names
        nameViews = new ArrayList<>(Arrays.asList(rank1Name,rank2Name,rank3Name,rank4Name,rank5Name,rank6Name,rank7Name,
                rank8Name,rank9Name,rank10Name));

    }
    //function for showing the top 10 ranked user names to the leaderboard
    private void showRankingName(ArrayList<String> array){
        for(int i = 0; i < array.size(); i++) {
            nameViews.get(i).setText(array.get(i).toString());
        }
    }
    //function for showing the top 10 ranked user steps to the leaderboard
    private void showRankingSteps(ArrayList<Integer> array){
        for(int i = 0; i < array.size(); i++) {
            stepViews.get(i).setText(array.get(i).toString());
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        userCollectionRef = db.collection("User");
        //query Firebase user database for the top 10 users with the most daily steps in descending order
        userCollectionRef.orderBy("dailySteps", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //if query is successful, add each user's name and steps to the name arraylist and steps arraylist
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        name.add(document.getString("firstName") + " " + document.getString("lastName"));
                        steps.add(document.getLong("dailySteps").intValue());
                    }
                    //Display top 10 users name and steps to the leaderboard
                    showRankingName(name);
                    showRankingSteps(steps);

                }
            }
        });

    }


}