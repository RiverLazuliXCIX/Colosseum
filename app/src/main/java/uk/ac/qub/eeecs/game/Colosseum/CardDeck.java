package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

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

        mDeck = new ArrayList<>(30);
        mHand = new ArrayList<>(10);
        mGraveyard = new ArrayList<>(0);
        mMinionsLeft = 0;
        mSpecialsLeft = 0;
        mCardsInHand = 0;

        buildDeck();
        drawCards(3);
    }

    //Public methods required for CardDeck Class:
    public void buildDeck() {

        for (int i = 0; i < 30; i++) {
            //Random r = new Random();
            //Add 30 cards to deck, for initial build
            //Random assortment of minions, weapons and special cards
            //mDeck.add()
        }
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
    public void IsEmpty() {
        if (mDeck.size() == 0) {
            setIsEmptyFlag(true);
        }
    }

    //Accessor and Mutator methods:
    public int getDeckID() { return mDeckID; }

    public String getDeckName() { return mDeckName; }

    public int getMinionsLeft() {
        return mMinionsLeft;
    }

    public int getSpecialsLeft() {
        return mSpecialsLeft;
    }

    public int getCardsInHand() {
        return mCardsInHand;
    }

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