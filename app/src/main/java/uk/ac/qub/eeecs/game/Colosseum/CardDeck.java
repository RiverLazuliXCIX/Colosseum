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
    private int sizeOfHand = 0;
    private GameScreen mGameScreen;

    //ArrayList to hold the deck of cards, and the card graveyard
    private ArrayList<Card> mDeck;
    private ArrayList<Card> mDiscardPile;
    private ArrayList<Card> mCardHand;

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

     /**
     /CardDeck Constructors:
     **/
    public CardDeck() {
        //Empty Deck
    }

    public CardDeck(int id, String name, GameScreen gameScreen, boolean isAIDeck) {
        mDeck = new ArrayList<>(0);
        mDiscardPile = new ArrayList<>(0);
        mCardHand = new ArrayList<>(0);

        this.mDeckID = id;
        this.mDeckName = name;
        this.mGameScreen = gameScreen;
        this.isAIDeck = isAIDeck;
        this.buildDeck();
    }

     /**
     /Public methods required for CardDeck class:
     **/

    public void buildDeck() {
        chooseDeckType();
        addToCardNum();
        insertMinionCard(numOfMinions);
        //insertSpellCard(numOfSpells);
        //insertWeaponCard(numOfWeapons);
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
                numOfSpells = 10;
                numOfWeapons = 5;
                break;
            case 2:
                numOfMinions = 10;
                numOfSpells = 15;
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

    public void insertMinionCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            Card testCard = new Card(0, 0, mGameScreen, 3);
            mDeck.add(i, testCard);
        }
    }

    //Methods required to draw from Deck:
    public Card drawTopCard() {
            Card topCard = mDeck.get(mDeck.size() - 1);
            trackRemovalOfCards();
            trackAdditionOfCardsToHand();
            mDeck.remove(mDeck.size() - 1);
            mCardHand.add(topCard);
                return topCard;
    }

    private void trackAdditionOfCardsToHand () {
        sizeOfHand++;
    }

    private void trackRemovalOfCards() {
        numOfCards--;
    }


    //Method that returns true if deck is empty - to be used when applying fatigue
    public boolean isEmpty() {
        if (mDeck.size() == 0) {
            setIsEmptyFlag(true);
        }
        return isEmptyFlag;
    }


    //Accessor and Mutator methods:
    public ArrayList<Card> getDeck() { return this.mDeck; }
    public ArrayList<Card> getmDiscardPile() { return this.mDiscardPile; }
    public ArrayList<Card> getmCardHand() { return this.mCardHand; }
    public int getDeckID() { return this.mDeckID; }
    public String getDeckName() { return this.mDeckName; }
    public GameScreen getmGameScreen() { return this.mGameScreen; }
    public boolean getIsEmptyFlag() { return this.isEmptyFlag; }
    public int getNumOfCards() { return this.numOfCards; }
    public int getMaxCards() { return MAX_CARDS; }
    public boolean getIsAIDeck() { return this.isAIDeck; }
    public int getNumOfMinions() { return this.numOfMinions; }
    public int getNumOfSpells() { return this.numOfSpells; }
    public int getNumOfWeapons() { return this.numOfWeapons; }

    public ArrayList<Card> setDeck(ArrayList<Card> newDeck) { return this.mDeck = newDeck; }
    public ArrayList<Card> setmDiscardPile(ArrayList<Card> newDiscard) {return this.mDiscardPile = newDiscard; }
    public ArrayList<Card> setmCardHand(ArrayList<Card> newHand) { return this.mCardHand = newHand; }
    public int setDeckID(int newDeckID) { return this.mDeckID = newDeckID; }
    public String setDeckName(String newDeckName) { return this.mDeckName = newDeckName; }
    public GameScreen setGameScreen(GameScreen newGameScreen) {return this.mGameScreen = newGameScreen; }
    public boolean setIsEmptyFlag(boolean isDeckEmpty) { return this.isEmptyFlag = isDeckEmpty; }
    public int setNumOfCards(int newNumOfCards) { return this.numOfCards = newNumOfCards; }
    public boolean setIsAIDeck(boolean newIsAIDeck) { return this.isAIDeck = newIsAIDeck; }
    public int setNumOfMinions(int newNumOfMinions) { return this.numOfMinions = newNumOfMinions; }
    public int setNumOfSpells(int newNumOfSpells) { return this.numOfSpells = newNumOfSpells; }
    public int setNumOfWeapons(int newNumOfWeapons) { return this.numOfWeapons = newNumOfWeapons; }
}