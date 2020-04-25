package com.example.irad9731.loopdeloopover;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LeaderBoard extends AppCompatActivity {

    private RecyclerView recyclerView;//the leader board itself
    private RecyclerView.Adapter mAdapter;//the adapter
    private RecyclerView.LayoutManager layoutManager;//the layout maneger
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        ((Button)findViewById(R.id.backToMain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//if the user press the button, return to main menu
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
            mAdapter = new LeaderBoardAdapter5X5(this);
        }else if(s.equals("7X7")) {
            mAdapter = new LeaderBoardAdapter7X7(this);
        }else if(s.equals("9X9")){
            mAdapter = new LeaderBoardAdapter9X9(this);
        }else{
            throw new IllegalArgumentException("leader board must have a level");
        }
        ((TextView)findViewById(R.id.ld)).setText("LeaderBoard" + s);
        recyclerView.setAdapter(mAdapter);
    }





}
