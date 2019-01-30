package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.world.GameScreen;

public class CardDeck {

    //Variables for Card Deck
    private int mDeckID;
    private String mDeckName;

    //Initialises Card
    private Card mCard;

    private GameScreen mGameScreen;

    //ArrayLists for contents of Deck, Hand and Graveyard:
    private ArrayList<Card> mDeck, mHand, mGraveyard;

    //Private variables required by CardDeck:
    private int mMinionsLeft, mSpecialsLeft, mCardsInHand;
    private boolean isEmptyFlag;

    //CardDeck Constructor:
    public CardDeck(int id, String name, GameScreen gamescreen) {
        this.mDeckID = id;
        this.mDeckName = name;
        this.mGameScreen = gamescreen;

        mDeck = new ArrayList<>(0);
        buildDeck();
        //drawCards(3);
    }

    //Public methods required for CardDeck Class:
    public void buildDeck() {

        // We'll need to have local methods such as "drawMinionCard()" and
        // "drawWeaponCard()" in their own classes to achieve this

    }

    public void drawCard() {
        if (mDeck.size() > 0) {
            Card c = mDeck.remove(mDeck.size() - 1);
            mHand.add(c);
        }
    }

    //Method that allows player to draw cards from deck
    public void drawCards(int noOfCards) {
        for (int i = 0; i < noOfCards; i++) {
            drawCard();
        }
    }

    //Method that returns true if deck is empty
    //To be used when applying fatigue consequence
    public boolean isEmpty() {
        if (mDeck.size() == 0) {
            setIsEmptyFlag(true);
        }
        return isEmptyFlag;
    }

    //Accessor and Mutator methods:
    public int getDeckID() { return mDeckID; }
    public String getDeckName() { return mDeckName; }
    public boolean getIsEmptyFlag() { return isEmptyFlag; }

    public ArrayList<Card> getDeck() {
        return this.mDeck;
    }

    public boolean setIsEmptyFlag(boolean isDeckEmpty) {
        return isEmptyFlag = isDeckEmpty;
    }

    public int setMinionsLeft(int newMinionsLeft) {
        return mMinionsLeft = newMinionsLeft;
    }

    public int setSpecialsLeft(int newSpecialsLeft) {
        return mSpecialsLeft = newSpecialsLeft;
    }

    public int setCardsInHand(int newCardsInHand) { return mCardsInHand = newCardsInHand; }

    public void setDeck(ArrayList<Card> deck) {
        this.mDeck = deck;
    }
}