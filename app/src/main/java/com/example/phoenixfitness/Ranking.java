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

public class Ranking extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference userCollectionRef;
    private ArrayList<Integer> steps;
    private ArrayList<String> name;
    private ArrayList<TextView> stepViews;
    private ArrayList<TextView> nameViews;
    private TextView rank1Name;
    private TextView rank1Steps;
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
        db = FirebaseFirestore.getInstance();
        steps = new ArrayList<Integer>();
        name = new ArrayList<String>();
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
        stepViews = new ArrayList<>(Arrays.asList(rank1Steps,rank2Steps,rank3Steps,rank4Steps,rank5Steps,rank6Steps,rank7Steps,
        rank8Steps,rank9Steps,rank10Steps));
        nameViews = new ArrayList<>(Arrays.asList(rank1Name,rank2Name,rank3Name,rank4Name,rank5Name,rank6Name,rank7Name,
                rank8Name,rank9Name,rank10Name));

    }
    private void showRankingName(ArrayList<String> array){
        for(int i = 0; i < array.size(); i++) {
            nameViews.get(i).setText(array.get(i).toString());
        }
        /*
        rank1Name.setText(array.get(0));
        rank2Name.setText(array.get(1));
        rank3Name.setText(array.get(2));
        rank4Name.setText(array.get(3));
        rank5Name.setText(array.get(4));
        rank6Name.setText(array.get(5));
        rank7Name.setText(array.get(6));
        rank8Name.setText(array.get(7));
        rank9Name.setText(array.get(8));
        rank10Name.setText(array.get(9));

         */
    }

    private void showRankingSteps(ArrayList<Integer> array){
        for(int i = 0; i < array.size(); i++) {
            stepViews.get(i).setText(array.get(i).toString());
        }
        /*
        rank1Steps.setText(array.get(0).toString());
        rank2Steps.setText(array.get(1).toString());
        rank3Steps.setText(array.get(2).toString());

        rank4Steps.setText(array.get(3));
        rank5Steps.setText(array.get(4));
        rank6Steps.setText(array.get(5));
        rank7Steps.setText(array.get(6));
        rank8Steps.setText(array.get(7));
        rank9Steps.setText(array.get(8));
        rank10Steps.setText(array.get(9));

         */
    }

    @Override
    protected void onStart(){
        super.onStart();
        userCollectionRef = db.collection("User");
        userCollectionRef.orderBy("calories", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){

                        name.add(document.getString("firstName") + " " + document.getString("lastName"));
                        steps.add(document.getLong("calories").intValue());

                    }
                    showRankingName(name);
                    showRankingSteps(steps);

                }
            }
        });

    }


}