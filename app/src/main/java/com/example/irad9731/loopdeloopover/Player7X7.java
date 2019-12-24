package com.example.irad9731.loopdeloopover;

public class Player7X7 extends Player {
    public Player7X7(String name, String uid, long bestTime5X5, long bestTime7X7, long bestTime9X9,String urlPhoto) {
        super(name,uid,bestTime5X5,bestTime7X7,bestTime9X9,urlPhoto);
    }
    public Player7X7(){
        super();
    }

    @Override
    public int compareTo(Player o) {
        if(this.bestTime7X7 == o.bestTime7X7){
            return 0;
        }
        return (int)((this.bestTime7X7-o.bestTime7X7)/Math.abs(this.bestTime7X7-o.bestTime7X7));
    }
}
