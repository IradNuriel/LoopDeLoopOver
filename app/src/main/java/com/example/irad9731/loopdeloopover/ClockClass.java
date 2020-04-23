package com.example.irad9731.loopdeloopover;

public class ClockClass {//class that contains only one static function.
    public static String millisToClock(long timeInMilliseconds){//since this piece of code was used a lot in this project, Iv'e decided to make a class that contains it and any place will be able to call it
        long timeInSeconds = timeInMilliseconds / 1000;
        long min = timeInSeconds / 60;//number of whole minutes in timeInMilliseconds
        long seconds = timeInSeconds % 60;//number of whole seconds in timeInMilliseconds
        //making the time look like a digital clock of minutes:seconds
        String m = String.valueOf(min);
        m = (m.length() > 1) ? m : "0" + m;
        String s = String.valueOf(seconds);
        s = (s.length() > 1) ? s : "0" + s;
        final String CLOCK = m + ":" + s;
        String sBestTime = CLOCK;
        return sBestTime;
    }



}
