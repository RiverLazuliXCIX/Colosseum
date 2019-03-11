package uk.ac.qub.eeecs.game.Colosseum;

public class Turn {

    //int required to hold turn number:
    private int mTurnNum;

    //Constructor for Turn:
    public Turn() {
        //Initially, turn number is always 1.
        mTurnNum = 1;
    }

    public void incrementTurnNum() {
        this.mTurnNum++;
    };

    public void newTurnFunc(Player player) {
        incrementTurnNum();
        player.increaseCurrentMana(1);
        player.increaseCurrentManaCap();
    }

    //Getters and setters:
    public int getmTurnNum() {return this.mTurnNum;}

}
