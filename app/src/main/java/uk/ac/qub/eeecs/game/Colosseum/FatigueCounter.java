package uk.ac.qub.eeecs.game.Colosseum;

public class FatigueCounter {

    int mFatigueNum;

    public FatigueCounter() {
        mFatigueNum = 0;
    }

    public int incrementFatigue() {
        mFatigueNum += 1;
        return mFatigueNum;
    }

    public int getmFatigueNum() {
        return mFatigueNum;
    }
}
