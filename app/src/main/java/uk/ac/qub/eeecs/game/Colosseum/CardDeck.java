package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class CardDeck {

    //Variables for Card Deck
    private int mDeckID;
    private String mDeckName;
    private int numOfCards = 0;
    private GameScreen mGameScreen;

    //ArrayList to hold the deck of cards
    private ArrayList<Card> mDeck = new ArrayList<>(numOfCards);

    //Boolean values required to discern the functionality of the deck:
    private boolean isEmptyFlag;
    private boolean isAIDeck = false;

    //Constant value, to set max deck size to 30
    public static final int MAX_CARDS = 30;

    //Random, to be used to 'choose' type of deck:
    private static final Random RANDOM = new Random();

    //Integer variables to be used to determine deck 'type':
    int numOfMinions;
    int numOfSpells;
    int numOfWeapons;

    //CardDeck Constructors:
    public CardDeck() {
        //Empty Deck
    }

    public CardDeck(int id, String name, GameScreen gameScreen, boolean isAIDeck) {
        this.mDeckID = id;
        this.mDeckName = name;
        this.mGameScreen = gameScreen;
        this.isAIDeck = isAIDeck;
        this.buildDeck();
    }

    //Public methods required for CardDeck class:
    public void buildDeck() {
        chooseDeckType();
        addToCardNum();

        /**
        for (int i = 0; i < numOfMinions; i++) {
            insertMinionCard(numOfMinions);
        }

        for (int i = 0; i < numOfSpells; i++) {
            insertSpellCard(numOfSpells);
        }

        for (int i = 0;i < numOfWeapons; i++) {
            insertWeaponCard(numOfWeapons);
        }**/
    }

    public void chooseDeckType() {
        int mType = RANDOM.nextInt(3);

        switch (mType) {
            case 0:
                numOfMinions = 10;
                numOfSpells = 10;
                numOfWeapons = 10;
                break;
            case 1:
                numOfMinions = 15;
                numOfSpells = 5;
                numOfWeapons = 10;
                break;
            case 2:
                numOfMinions = 10;
                numOfSpells = 10;
                numOfWeapons = 5;
                break;
        }
    }

    // Methods to add different types of cards to the deck:
    public void addToCardNum() {
        numOfCards += numOfMinions;
        numOfCards += numOfWeapons;
        numOfCards += numOfSpells;
    }



    //Method that returns true if deck is empty - to be used when applying fatigue
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
    public int getNumOfCards() { return this.numOfCards; }
    public int getMaxCards() { return MAX_CARDS; }
    public boolean getIsAIDeck() { return this.isAIDeck; }
    public int getNumOfMinions() { return this.numOfMinions; }
    public int getNumOfSpells() { return this.numOfSpells; }
    public int getNumOfWeapons() { return this.numOfWeapons; }

    public int setDeckID(int newID) { return this.mDeckID = newID; }
    public String setDeckName(String newName) { return this.mDeckName = newName; }
    public GameScreen setmGameScreen(GameScreen newGameScreen) { return this.mGameScreen; }
    public boolean setIsEmptyFlag(boolean isDeckEmpty) { return this.isEmptyFlag = isDeckEmpty; }
    public void setDeck(ArrayList<Card> deck) { this.mDeck = deck; }
    public void setNumOfCards(int newNumOfCards) {this.numOfCards = newNumOfCards; }
}