package uk.ac.qub.eeecs.game.Colosseum;


public class FatigueCounter {
    /**
    * The FatigueCounter Class models the counter required to monitor both player and enemy's current fatigue, and thus damage to be taken.
    * @author Dearbhaile Walsh
    **/

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