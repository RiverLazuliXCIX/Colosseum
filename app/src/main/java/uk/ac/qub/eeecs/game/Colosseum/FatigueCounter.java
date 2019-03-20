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

    public void takeAppropriateDamage(Player player) {
        incrementFatigue();
        player.receiveDamage(mFatigueNum);

        if (player.getCurrentHealth() < 0) {
            player.setCurrentHealth(0);
        }
    }

    public int getmFatigueNum() {
        return mFatigueNum;
    }
}