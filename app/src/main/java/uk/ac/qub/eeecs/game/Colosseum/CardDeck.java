package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class CardDeck {

    //Variables for Card Deck
    private int mDeckID;
    private String mDeckName;
    private boolean isEmptyFlag;

    //Initialises GameScreen
    private GameScreen mGameScreen;

    //ArrayLists for contents of Deck, Hand and Graveyard:
    private ArrayList<Card> mDeck, mHand;


    //CardDeck Constructor:
    public CardDeck(int id, String name, GameScreen gamescreen) {
        this.mDeckID = id;
        this.mDeckName = name;
        this.mGameScreen = gamescreen;

        mDeck = new ArrayList<>(0);
        mHand = new ArrayList<>(0);

        buildDeck();

        drawCards(0);
    }

    //Public methods required for CardDeck Class:
    public void buildDeck() {
        //TODO: We'll need to have local methods such as "insertMinionCard()" and "insertWeaponCard()" in their own classes to achieve this
    }

    public void drawCard() {
        if (!mDeck.isEmpty()) {
            Card c = mDeck.remove(mDeck.size() - 1);
            mHand.add(c);
        }
        else {

        }
        //TODO: Ultimately this will need to be in the Hand Class
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
    public int getDeckID() { return this.mDeckID; }
    public String getDeckName() { return this.mDeckName; }
    public GameScreen getmGameScreen() { return this.mGameScreen; }
    public boolean getIsEmptyFlag() { return this.isEmptyFlag; }
    public ArrayList<Card> getDeck() { return this.mDeck; }

    public int setDeckID(int newID) { return this.mDeckID = newID; }
    public String setDeckName(String newName) { return this.mDeckName = newName; }
    public GameScreen setmGameScreen(GameScreen newGameScreen) { return this.mGameScreen; }
    public boolean setIsEmptyFlag(boolean isDeckEmpty) { return this.isEmptyFlag = isDeckEmpty; }
    public void setDeck(ArrayList<Card> deck) { this.mDeck = deck; }
}