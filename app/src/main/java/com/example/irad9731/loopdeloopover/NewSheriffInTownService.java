package com.example.irad9731.loopdeloopover;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NewSheriffInTownService extends Service {//service that when someone got to the world top(1st place), it notify all the other people in the world
    public static final String FOREGROUND_PROGRESS = "com.exemple.irad9731.ex17.foregroundProgress";
    public static final int NOT = 154;
    NotificationCompat.Builder builder;
    public Worker worker;
    NotificationManager notifyManager;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Player7X7 best7X7;//best player of 7X7
    Player5X5 best5X5;//best player of 5X5
    Player9X9 best9X9;//best player of 9X9


    public NewSheriffInTownService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(worker == null){//start the listening to the database
            worker = new Worker();
            worker.start();
        }
        return super.onStartCommand(intent, flags, startId);

    }


    private void init() {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence name = "LoopDeLoopover";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("MyChannel", name, importance);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder = new NotificationCompat.Builder(this, "MyChannel")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("New Sheriff")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true).setOngoing(false);
        mDatabase.getReference().child("players").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//who are the best in the beginning?
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Player5X5 p5X5 = ds.getValue(Player5X5.class);
                    Player7X7 p7X7 = ds.getValue(Player7X7.class);
                    Player9X9 p9X9 = ds.getValue(Player9X9.class);
                    if (p5X5.bestTime5X5 < Long.MAX_VALUE) {
                        if(best5X5!=null){
                            best5X5 = (best5X5.compareTo(p5X5)== -1)? best5X5:p5X5;
                        }else{
                            best5X5 = p5X5;
                        }
                    }
                    if (p7X7.bestTime7X7 < Long.MAX_VALUE) {
                        if(best7X7!=null){
                            best7X7 = (best7X7.compareTo(p7X7)== -1)? best7X7:p7X7;
                        }else{
                            best7X7 = p7X7;
                        }
                    }if (p9X9.bestTime9X9 < Long.MAX_VALUE) {
                        if(best9X9!=null){
                            best9X9 = (best9X9.compareTo(p9X9)== -1)? best9X9:p9X9;
                        }else{
                            best9X9 = p9X9;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Notification createNotification(String info){//build a notification
        builder.setContentText(info) .setOnlyAlertOnce(true).setAutoCancel(true).setOngoing(false);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
        synchronized (notifyManager) {
            notifyManager.notify();
        }
        return notification;
    }



    public class Worker extends Thread{//the actual doing things class
        public Worker(){}

        @Override
        public void run() {
            super.run();
            if(mAuth.getCurrentUser()==null){//if no user is connected, stop the service&the thread
                stopSelf();
                NewSheriffInTownService.this.stopSelf();
            }

            mDatabase.getReference().child("players").addValueEventListener(new ValueEventListener() {//whenever there is a change in the database
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //who was the best players before
                        Player5X5 lastBest5X5=best5X5;
                        Player7X7 lastBest7X7=best7X7;
                        Player9X9 lastBest9X9=best9X9;

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {//for each player
                            Player5X5 p5X5 = ds.getValue(Player5X5.class);//the player as a 5X5 player
                            Player7X7 p7X7 = ds.getValue(Player7X7.class);//the player as a 7X7 player
                            Player9X9 p9X9 = ds.getValue(Player9X9.class);//the player as a 9X9 player
                            if (p5X5.bestTime5X5 < Long.MAX_VALUE) {//if he finished the 5X5 level
                                if (best5X5 != null) {//and there was a best player before
                                    best5X5 = (best5X5.compareTo(p5X5) == -1) ? best5X5 : p5X5;//the best player should be the better between the two
                                } else {
                                    best5X5 = p5X5;
                                }
                            }
                            if (p7X7.bestTime7X7 < Long.MAX_VALUE) {//if he finished the 7X7 level
                                if (best7X7 != null) {//and there was a best player before
                                    best7X7 = (best7X7.compareTo(p7X7) == -1) ? best7X7 : p7X7;//the best player should be the better between the two
                                } else {
                                    best7X7 = p7X7;
                                }
                            }
                            if (p9X9.bestTime9X9 < Long.MAX_VALUE) {//if he finished the 9X9 level(which, lets be real, he didn't)
                                if (best9X9 != null) {//and there was a best player before
                                    best9X9 = (best9X9.compareTo(p9X9) == -1) ? best9X9 : p9X9;//the best player should be the better between the two
                                } else {
                                    best9X9 = p9X9;
                                }
                            }
                        }
                        if(lastBest5X5==null){//if there was no best
                            lastBest5X5=best5X5;//the new best is the last best
                        }
                        if(lastBest5X5!=null) {
                            if (!lastBest5X5.uid.equals(best5X5.uid) && !mAuth.getCurrentUser().getUid().equals(best5X5.uid)) {//if there is new best player, and he isn't me
                                startForeground(NOT, createNotification("There is a new sheriff in the town!\n" + best5X5.name +
                                        " beaten the 5X5 level in " + ClockClass.millisToClock(best5X5.bestTime5X5)));//send notification that there is new sheriff
                                stopForeground(false);
                            } else if (!lastBest5X5.uid.equals(best5X5.uid)) {//if there is new best, but he is me!
                                startForeground(NOT, createNotification("You have made it!!!! You are the best in the world now in the 5X5 level!!"));//send congratulation message
                                stopForeground(false);
                            }
                        }
                        if(lastBest7X7==null){//if there was no best
                            lastBest7X7=best7X7;//the new best is the last best
                        }
                        if(lastBest7X7!=null) {
                            if (!lastBest7X7.uid.equals(best7X7.uid) && !mAuth.getCurrentUser().getUid().equals(best7X7.uid)) {//if there is new best player, and he isn't me
                                startForeground(NOT, createNotification("There is a new sheriff in the town!\n" + best7X7.name +
                                        " beaten the 7X7 level in " + ClockClass.millisToClock(best7X7.bestTime7X7)));//send notification that there is new sheriff
                                stopForeground(false);
                            } else if (!lastBest7X7.uid.equals(best7X7.uid)) {//if there is new best, but he is me!
                                startForeground(NOT, createNotification("You have made it!!!! You are the best in the world now in the 7X7 level!!"));//send congratulation message
                                stopForeground(false);
                            }
                        }
                        if(lastBest9X9==null){//if there was no best(which is what will be in a very long time)
                            lastBest9X9=best9X9;//the new best is the last best
                        }
                        if(lastBest9X9!=null) {
                            if (!lastBest9X9.uid.equals(best9X9.uid) && !mAuth.getCurrentUser().getUid().equals(best9X9.uid)) {//if there is new best player, and he isn't me
                                startForeground(NOT, createNotification("There is a new sheriff in the town!\n" + best9X9.name +
                                        " beaten the 9X9 level in " + ClockClass.millisToClock(best9X9.bestTime9X9)));//send notification that there is new sheriff
                                stopForeground(false);
                            } else if (!lastBest9X9.uid.equals(best9X9.uid)) {//if there is new best, but he is me!
                                startForeground(NOT, createNotification("You have made it!!!! You are the best in the world now in the 9X9 level!!"));//send congratulation message
                                stopForeground(false);
                            }
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

    }

}
