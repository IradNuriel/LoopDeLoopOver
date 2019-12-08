package com.example.irad9731.loopdeloopover;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {



    public ArrayList<Player> mDataset = new ArrayList<>();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();



    private void getDataFromFB(){
        mDatabase.getReference().child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Player p = ds.getValue(Player.class);
                    mDataset.add(p);
                }
                Collections.sort(mDataset);
                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static String milisIntoClock(long bestTime){
        long timeInSeconds = bestTime / 1000;
        long min = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        String m = String.valueOf(min);
        m = (m.length() > 1) ? m : "0" + m;
        String s = String.valueOf(seconds);
        s = (s.length() > 1) ? s : "0" + s;
        final String CLOCK = m + ":" + s;
        String sBestTime = "Your best time is: " + CLOCK;
        return sBestTime;
    }


    public LeaderBoardAdapter(ArrayList<Player> data){
        getDataFromFB();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_item,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.bindData(mDataset.get(i),i);
    }


    @Override
    public int getItemCount() {

        return mDataset.size();
    }

    public static class MyViewHolder  extends RecyclerView.ViewHolder{
        public TextView number;
        public TextView name;
        public TextView time;
        public ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = (TextView)itemView.findViewById(R.id.rowNumber);
            name = (TextView)itemView.findViewById(R.id.playerName);
            time = (TextView)itemView.findViewById(R.id.playerTime);
            img = (ImageView)itemView.findViewById(R.id.playerImg);

        }
        public void bindData(Player player,int i){
            number.setText(i);
            name.setText(player.getName());
            time.setText(milisIntoClock(player.getBestTime5X5()));
        }

    }


}
