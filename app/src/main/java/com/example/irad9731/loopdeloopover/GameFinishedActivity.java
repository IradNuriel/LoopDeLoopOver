package com.example.irad9731.loopdeloopover;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class GameFinishedActivity extends AppCompatActivity {
    private TextView congratulate;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private TextView mBestTime;
    private Button mainMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);
        congratulate = (TextView)findViewById(R.id.congratulation);
        mBestTime = (TextView)findViewById(R.id.bestTimeForThisLevel);
        mainMenu = (Button)findViewById(R.id.mainMenu);
        Intent i = getIntent();
        final String level = i.getStringExtra("level");
        final long beatingTime = i.getLongExtra("beatingTime",0);

        final String CLOCK = ClockClass.milisToClock(beatingTime);

        String congratulationMessage = "Congratulation, you have beaten the " + level + " level in: " + CLOCK + " minuets!!";
        congratulate.setText(congratulationMessage);
        database.getReference().child("players").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long bestTime = (long)((Map)dataSnapshot.getValue()).get("bestTime"+level);
                if(bestTime > beatingTime){
                    database.getReference().child("players").child(mAuth.getCurrentUser().getUid()).child("bestTime"+level).setValue(beatingTime);
                    bestTime = beatingTime;
                }
                String sBestTime = "Your best time is: " + ClockClass.milisToClock(bestTime);
                mBestTime.setText(sBestTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}

