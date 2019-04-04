package uk.ac.qub.eeecs.game.TestClasses;

public abstract class LogicCard {

    private int coinCost;

    public LogicCard(int coinCost) {
        setCoinCost(coinCost);
    }

    public int getCoinCost() { return this.coinCost; }
    public void setCoinCost(int coinCost) { this.coinCost = coinCost; }

}
