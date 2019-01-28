package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

public class CardDeck {

    //Initialises Card
    private Card mCard;

    //ArrayLists for contents of Deck, Hand and Graveyard:
    private ArrayList<Card> mDeck, mHand, mGraveyard;

    //Private variables required by CardDeck:
    private int mMinionsLeft, mSpecialsLeft, mCardsInHand;
    private boolean isEmptyFlag;

    //CardDeck Constructor:
    public CardDeck() {
        mDeck = new ArrayList<>(30);
        mHand = new ArrayList<>(10);
        mGraveyard = new ArrayList<>(0);
        mMinionsLeft = 0;
        mSpecialsLeft = 0;
        mCardsInHand = 0;

        buildDeck();
        drawCards(10);
    }



    public boolean setIsEmptyFlag(boolean isDeckEmpty) {
        return isEmptyFlag = isDeckEmpty;
    }

    //Public methods required for CardDeck Class:
    public void buildDeck() {

        for (int i = 0; i < 30; i++) {
            Random r = new Random();

        }
        //Add 30 cards to deck, for initial build
        //Random assortment of minions, weapons and special cards
        //mDeck.add()

    }

    public void drawCard() {
        if (mDeck.size() > 0) {
            Card c = mDeck.remove(mDeck.size() - 1);


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
    public int getMinionsLeft() {
        return mMinionsLeft;
    }

    public int getSpecialsLeft() {
        return mSpecialsLeft;
    }

    public int getCardsInHand() {
        return mCardsInHand;
    }

    public boolean getIsEmptyFlag() {
        return isEmptyFlag;
    }

    public ArrayList<Card> getDeck() {
        return this.mDeck;
    }

    public int setMinionsLeft() {
        return mMinionsLeft;
    }

    public int setSpecialsLeft() {
        return mSpecialsLeft;
    }

    public int setCardsInHand() {
        return mCardsInHand;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.mDeck = deck;
    }
}

