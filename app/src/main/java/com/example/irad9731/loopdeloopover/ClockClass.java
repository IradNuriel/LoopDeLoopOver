package com.example.irad9731.loopdeloopover;

public class ClockClass {
    public static String milisToClock(long bestTime){
        long timeInSeconds = bestTime / 1000;
        long min = timeInSeconds / 60;
        long seconds = timeInSeconds % 60;
        String m = String.valueOf(min);
        m = (m.length() > 1) ? m : "0" + m;
        String s = String.valueOf(seconds);
        s = (s.length() > 1) ? s : "0" + s;
        final String CLOCK = m + ":" + s;
        String sBestTime = CLOCK;
        return sBestTime;
    }



}
