package uk.ac.qub.eeecs.game.Colosseum;

public class Turn {

    private int mTurnNum;

    public Turn() {
        //Initially, turn number is always 1.
        mTurnNum = 1;
    }

    private void incrementTurnNum() {
        this.mTurnNum++;
    };

    private void newTurnFunc(Turn turn, Player player) {
        turn.incrementTurnNum();
        player.reduceCurrentMana(1);
    }

    //Getters and setters:
    public int getmTurnNum() {return this.mTurnNum;}

}
