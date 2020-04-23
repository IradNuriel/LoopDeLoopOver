package com.example.irad9731.loopdeloopover;

public class Player9X9 extends Player {//the exact same player but now we compare him by his record on 9X9
    public Player9X9(String name, String uid, long bestTime5X5, long bestTime7X7, long bestTime9X9,String urlPhoto) {
        super(name,uid,bestTime5X5,bestTime7X7,bestTime9X9,urlPhoto);
    }
    public Player9X9(){
        super();
    }

    @Override
    public int compareTo(Player o) {
        if(this.bestTime9X9 == o.bestTime9X9){
            return 0;
        }
        return (int)((this.bestTime9X9-o.bestTime9X9)/Math.abs(this.bestTime9X9-o.bestTime9X9));
    }
}
