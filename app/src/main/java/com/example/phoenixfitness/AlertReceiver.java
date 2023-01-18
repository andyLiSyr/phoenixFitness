package com.example.phoenixfitness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//Reset steps and calorie counter to 0 at the end of the day
public class AlertReceiver extends BroadcastReceiver {
    @Override
    //updates calories and dailysteps to 0 at the end of the day
    public void onReceive(Context context, Intent intent){
        FirebaseFirestore.getInstance().collection("User").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                document.getReference().update("calories",0);
                                document.getReference().update("dailySteps",0);
                            }
                        }
                    }
                });
    }
}
