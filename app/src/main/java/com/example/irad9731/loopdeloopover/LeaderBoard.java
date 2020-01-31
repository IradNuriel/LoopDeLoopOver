package com.example.irad9731.loopdeloopover;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    ArrayList<Player> mDataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        ((Button)findViewById(R.id.backToMain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.leaderBoard);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent i = getIntent();
        String s=i.getStringExtra("level");


        // specify an adapter
        if(s.equals("5X5")) {
            mAdapter = new LeaderBoardAdapter5X5(mDataset,this);
        }else if(s.equals("7X7")) {
            mAdapter = new LeaderBoardAdapter7X7(mDataset,this);
        }else if(s.equals("9X9")){
            mAdapter = new LeaderBoardAdapter9X9(mDataset,this);
        }else{
            throw new IllegalArgumentException("leader board must have a level");
        }
        ((TextView)findViewById(R.id.ld)).setText("LeaderBoard" + s);
        recyclerView.setAdapter(mAdapter);
    }





}
