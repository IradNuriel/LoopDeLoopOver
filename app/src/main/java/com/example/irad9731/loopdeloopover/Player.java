package com.example.irad9731.loopdeloopover;

public class Player {
    String name;
    String uid;
    long bestTime5X5;
    long bestTime7X7;
    long bestTime9X9;

    public Player(String name, String uid, long bestTime5X5, long bestTime7X7, long bestTime9X9) {
        this.name = name;
        this.uid = uid;
        this.bestTime5X5 = bestTime5X5;
        this.bestTime7X7 = bestTime7X7;
        this.bestTime9X9 = bestTime9X9;
    }

    public String getUid() {
        return uid;
    }

    public long getBestTime5X5() {
        return bestTime5X5;
    }

    public long getBestTime7X7() {
        return bestTime7X7;
    }

    public long getBestTime9X9() {
        return bestTime9X9;
    }

    public Player() {
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setBestTime5X5(long bestTime5X5) {
        this.bestTime5X5 = bestTime5X5;
    }

    public void setBestTime7X7(long bestTime7X7) {
        this.bestTime7X7 = bestTime7X7;
    }

    public void setBestTime9X9(long bestTime9X9) {
        this.bestTime9X9 = bestTime9X9;
    }

    public String getName() {
        return name;
    }
}
