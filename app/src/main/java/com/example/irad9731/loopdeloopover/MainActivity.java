package com.example.irad9731.loopdeloopover;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final int GAME_5X5 = 1;
    public final int GAME_7X7 = 2;
    public final int GAME_9X9 = 3;
    public final int LEADERBOARD_5X5 = 4;
    public final int LEADERBOARD_7X7 = 5;
    public final int LEADERBOARD_9X9 = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt5X5 = (Button)findViewById(R.id.Button5X5);
        Button bt7X7 = (Button)findViewById(R.id.Button7X7);
        Button bt9X9 = (Button)findViewById(R.id.Button9X9);
        bt5X5.setOnClickListener(this);
        bt7X7.setOnClickListener(this);
        bt9X9.setOnClickListener(this);
        Button b5X5 = (Button)findViewById(R.id.leaderboard5_x5);
        Button b7X7 = (Button)findViewById(R.id.leaderboard7_x7);
        Button b9X9 = (Button)findViewById(R.id.leaderboard9_x9);
        b5X5.setOnClickListener(this);
        b7X7.setOnClickListener(this);
        b9X9.setOnClickListener(this);
        if(!isMyServiceRunning(NewSheriffInTownService.class)){
            Intent intent = new Intent(this,NewSheriffInTownService.class);
            startService(intent);
        }
        Intent i = getIntent();
        ((TextView)findViewById(R.id.playerName)).setText(i.getStringExtra("name"));

    }





    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Button5X5:
                SharedPreferences pref5X5 = getSharedPreferences("Board5X5Activity",MODE_PRIVATE);
                SharedPreferences.Editor edit = pref5X5.edit();
                edit.clear();
                edit.apply();
                Intent intent = new Intent(this,Board5X5Activity.class);
                startActivityForResult(intent,GAME_5X5);
                break;
            case R.id.Button7X7:
                SharedPreferences pref7X7 = getSharedPreferences("Board7X7Activity",MODE_PRIVATE);
                SharedPreferences.Editor edit1 = pref7X7.edit();
                edit1.clear();
                edit1.apply();
                Intent intent1 = new Intent(this,Board7X7Activity.class);
                startActivityForResult(intent1,GAME_7X7);
                break;
            case R.id.Button9X9:
                SharedPreferences pref9X9 = getSharedPreferences("Board9X9Activity",MODE_PRIVATE);
                SharedPreferences.Editor edit2 = pref9X9.edit();
                edit2.clear();
                edit2.apply();
                Intent intent2 = new Intent(this,Board9X9Activity.class);
                startActivityForResult(intent2,GAME_9X9);
                break;
            case R.id.leaderboard5_x5:
                Intent intent3 = new Intent(this,LeaderBoard.class);
                intent3.putExtra("level","5X5");
                startActivityForResult(intent3,LEADERBOARD_5X5);
                break;
            case R.id.leaderboard7_x7:
                Intent intent4 = new Intent(this,LeaderBoard.class);
                intent4.putExtra("level","7X7");
                startActivityForResult(intent4,LEADERBOARD_7X7);
                break;
            case R.id.leaderboard9_x9:
                Intent intent5 = new Intent(this,LeaderBoard.class);
                intent5.putExtra("level","9X9");
                startActivityForResult(intent5,LEADERBOARD_9X9);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case GAME_5X5:
                    SharedPreferences pref5X5 = getSharedPreferences("Board5X5Activity",MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref5X5.edit();
                    edit.clear();
                    edit.apply();
                    break;
                case GAME_7X7:
                    SharedPreferences pref7X7 = getSharedPreferences("Board7X7Activity",MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = pref7X7.edit();
                    edit1.clear();
                    edit1.apply();
                    break;
                case GAME_9X9:
                    SharedPreferences pref9X9 = getSharedPreferences("Board9X9Activity",MODE_PRIVATE);
                    SharedPreferences.Editor edit2 = pref9X9.edit();
                    edit2.clear();
                    edit2.apply();
                    break;
            }
        }
    }
}
