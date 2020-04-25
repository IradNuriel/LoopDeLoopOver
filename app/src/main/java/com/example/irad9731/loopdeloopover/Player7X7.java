package com.example.irad9731.loopdeloopover;

public class Player7X7 extends Player {//the exact same player but now we compare him by his record on 7X7
    public Player7X7(String name, String uid, long bestTime5X5, long bestTime7X7, long bestTime9X9,String urlPhoto) {
        super(name,uid,bestTime5X5,bestTime7X7,bestTime9X9,urlPhoto);
    }
    public Player7X7(){
        super();
    }

    @Override
    public int compareTo(Player o) {//compareTo function, compare two players, and will return -1 if this is better than the other in 7X7 level, 1 if the other better than this in the 7X7 level, and 0 if they equally good
        if(this.bestTime7X7 == o.bestTime7X7){
            return 0;
        }
        return (int)((this.bestTime7X7-o.bestTime7X7)/Math.abs(this.bestTime7X7-o.bestTime7X7));
    }
}
