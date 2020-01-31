package com.example.irad9731.loopdeloopover;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SelectLevelToPlay.PlayHandleButtons,SelectLevelToLeaderBoard.LeaderBoardHandleButtons {

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
        Button btPlay = (Button)findViewById(R.id.playButton);
        btPlay.setOnClickListener(this);
        Button btLeaderboard = (Button)findViewById(R.id.leaderBoardBt);
        btLeaderboard.setOnClickListener(this);
        if(!isMyServiceRunning(NewSheriffInTownService.class)){
            Intent intent = new Intent(this,NewSheriffInTownService.class);
            startService(intent);
        }
        Intent i = getIntent();
        ((TextView)findViewById(R.id.playerName)).setText(i.getStringExtra("name"));

    }




    @SuppressWarnings("deprecation")
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
            case R.id.playButton:
                SelectLevelToPlay levelSelection = new SelectLevelToPlay();
                levelSelection.show(getSupportFragmentManager(),"select level");
                break;
            case R.id.leaderBoardBt:
                SelectLevelToLeaderBoard levelToLeaderBoard = new SelectLevelToLeaderBoard();
                levelToLeaderBoard.show(getSupportFragmentManager(),"select level");
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.father, new MySettingsFragment()).addToBackStack(null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public void playPress5X5() {
        Intent intent = new Intent(this,Board5X5Activity.class);
        startActivityForResult(intent,GAME_5X5);

    }

    @Override
    public void playPress7X7() {
        Intent intent = new Intent(this,Board7X7Activity.class);
        startActivityForResult(intent,GAME_7X7);

    }

    @Override
    public void playPress9X9() {
        Intent intent = new Intent(this,Board9X9Activity.class);
        startActivityForResult(intent,GAME_9X9);
    }

    @Override
    public void leaderboardPress5X5() {
        Intent intent = new Intent(this,LeaderBoard.class);
        intent.putExtra("level","5X5");
        startActivityForResult(intent,LEADERBOARD_5X5);

    }

    @Override
    public void leaderboardPress7X7() {
        Intent intent = new Intent(this,LeaderBoard.class);
        intent.putExtra("level","7X7");
        startActivityForResult(intent,LEADERBOARD_7X7);

    }

    @Override
    public void leaderboardPress9X9() {
        Intent intent = new Intent(this,LeaderBoard.class);
        intent.putExtra("level","9X9");
        startActivityForResult(intent,LEADERBOARD_9X9);

    }

    public static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            setPreferencesFromResource(R.xml.preferences, s);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            view.setBackgroundColor(Color.RED);
            super.onViewCreated(view, savedInstanceState);
        }
    }

}
