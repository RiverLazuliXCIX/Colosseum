package uk.ac.qub.eeecs.game.Colosseum;

//
//      FatigueCounter class is used to a) count how much fatigue the player is due to take
//                                      b) take cumulative damage from the player, based upon
//                                         how many times they've taken fatigue
//

public class FatigueCounter {
    //Coded by Dearbhaile Walsh
    //Properties:
    private int mFatigueNum;

    //Constructor
    public FatigueCounter() {

        mFatigueNum = 0;
        //FatigueNum is initially set up at 0
    }

    //Methods:
    public int incrementFatigue() {
        mFatigueNum += 1;
        return mFatigueNum;
    }

    public void takeAppropriateDamage(Player player) {
        incrementFatigue(); //Number is increased, to record that Fatigue has been called
        player.receiveDamage(mFatigueNum); //Player takes appropriate amount of damage

        if (player.getCurrentHealth() < 0) {
            player.setCurrentHealth(0); //Player's health cannot drop below 0. If it hits 0, they die.
        }
    }

    //Getters and setters
    public int getmFatigueNum() {
        return mFatigueNum;
    }
}