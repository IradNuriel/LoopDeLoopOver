package com.example.irad9731.loopdeloopover;

public class Player5X5 extends Player {//the exact same player but now we compare him by his record on 5X5
    public Player5X5(String name, String uid, long bestTime5X5, long bestTime7X7, long bestTime9X9,String urlPhoto) {
        super(name,uid,bestTime5X5,bestTime7X7,bestTime9X9,urlPhoto);
    }
    public Player5X5(){
        super();
    }

    @Override
    public int compareTo(Player o) {//compareTo function, compare two players, and will return -1 if this is better than the other in 5X5 level, 1 if the other better than this in the 5X5 level, and 0 if they equally good
        if(this.bestTime5X5 == o.bestTime5X5){
            return 0;
        }
        return (int)((this.bestTime5X5-o.bestTime5X5)/Math.abs(this.bestTime5X5-o.bestTime5X5));
    }
}
