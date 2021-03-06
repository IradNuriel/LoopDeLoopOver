package com.example.irad9731.loopdeloopover;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoardAdapter9X9 extends RecyclerView.Adapter<LeaderBoardAdapter9X9.MyViewHolder> {//an adapter to leader board of the 9X9 level

    public ArrayList<Player9X9> mDataset = new ArrayList<>();//this list contains all the data that displayed in the leader board
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();//firebase instance
    private LeaderBoard context;//the activity


    private void getDataFromFB(){
        mDatabase.getReference().child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {//get all players that once finished the 9X9 level
                    Player9X9 p = ds.getValue(Player9X9.class);//get the player
                    if(p.bestTime9X9 < Long.MAX_VALUE) {//if the player actually played the level
                        mDataset.add(p);//add him the the displayed player
                    }
                }
                Collections.sort(mDataset);//sort the players by their best time
                notifyDataSetChanged();//display

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public LeaderBoardAdapter9X9(LeaderBoard context){//when loaded, display the data
        getDataFromFB();
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leaderboard_item,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {//when there is new information to display, display it
        viewHolder.bindData(mDataset.get(i),i);
        getImageFromFB(mDataset.get(i),viewHolder.img);
    }


    @Override
    public int getItemCount() {

        return mDataset.size();
    }



    public void getImageFromFB(Player player,ImageView img){//use glide to gat the current player image displayed
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();



// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with( context/* context */)//.asBitmap()
                .load(player.getUrlPhoto())
                .into(img);
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
        public void bindData(Player player,int i){//display data
            number.setText(String.valueOf(i+1));
            name.setText(player.getName());
            String x ="made it in: " + ClockClass.millisToClock(player.getBestTime9X9());
            time.setText(x);

        }





    }

}
