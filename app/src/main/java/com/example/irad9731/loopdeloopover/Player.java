package com.example.irad9731.loopdeloopover;

public class Player implements Comparable<Player>{//the class that present how the database see a player
    String name;//player has a name
    String uid;//he has a player identifier
    String urlPhoto;//he has a photo stored in the firestore, so we will just take the url to the photo
    long bestTime5X5;//he has a best time for the 5X5 level(=Long.MAX_VALUE if the player didn't finished the level yet)
    long bestTime7X7;//he has a best time for the 7X7 level(=Long.MAX_VALUE if the player didn't finished the level yet)
    long bestTime9X9;//he has a best time for the 9X9 level(=Long.MAX_VALUE if the player didn't finished the level yet(which will be the case in the next few month at least))





    public Player(String name, String uid, long bestTime5X5, long bestTime7X7, long bestTime9X9,String urlPhoto) {//he can initialize his attributes
        this.name = name;
        this.uid = uid;
        this.bestTime5X5 = bestTime5X5;
        this.bestTime7X7 = bestTime7X7;
        this.bestTime9X9 = bestTime9X9;
        this.urlPhoto = urlPhoto;
    }
    //and he has all the other things that you need to be able to upload a custom data type to firebase
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

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    @Override
    public int compareTo(Player o) {//and he has a compareTo function(will not be used for this class, only for the extended class(i.e Player5X5,Player7X7,Player9X9)
        if(this.bestTime5X5 == o.bestTime5X5){
            return 0;
        }
        return (int)((this.bestTime5X5-o.bestTime5X5)/Math.abs(this.bestTime5X5-o.bestTime5X5));
    }
}
