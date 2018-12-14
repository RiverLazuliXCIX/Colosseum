package uk.ac.qub.eeecs.game.prototypeClasses;

/**
 * Created by Matthew, 05/12/2018
 */
public abstract class Card {

    private int coinCost;

    public Card() {
        setCoinCost(0);
    }

    public Card(int coinCost) {
        setCoinCost(coinCost);
    }

    public int getCoinCost() { return this.coinCost; }
    public void setCoinCost(int coinCost) { this.coinCost = coinCost; }

}