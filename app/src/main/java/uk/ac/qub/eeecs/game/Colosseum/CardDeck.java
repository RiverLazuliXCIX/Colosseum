package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;

public class CardDeck {

    //Variables for Card Deck
    private int mDeckID;
    private String mDeckName;
    private int mNumOfCards = 0;
    private int mSizeOfHand = 0;
    private GameScreen mGameScreen;

    //ArrayList to hold the deck of cards, and the card graveyard
    private ArrayList<Card> mDeck;
    private ArrayList<Card> mDiscardPile;
    private ArrayList<Card> mCardHand;

    //Boolean values required to discern the functionality of the deck:
    private boolean mIsEmptyFlag;
    private boolean mIsAIDeck = false;

    //Constant values relating to Card Deck and Hand
    public static final int MAX_DECK_CARDS = 30;
    public static final int MAX_HAND_CARDS = 5;

    //Random, to be used to 'choose' type of deck:
    private static final Random RANDOM = new Random();

    private static final float X_POSITION = 160f;
    private static final float Y_POSITION = 25f;


    //Integer variables to be used to determine deck 'type':
    int mNumOfMinions;
    int mNumOfSpells;
    int mNumOfWeapons;

     /**
     /CardDeck Constructors:
     **/
    public CardDeck() {
        //Creates an empty deck with no ID, Name, GameScreen or AI settings.
        //Possibly to be used for players building their own deck

        mDeck = new ArrayList<>(0);
        mDiscardPile = new ArrayList<>(0);
        mCardHand = new ArrayList<>(0);
    }

    public CardDeck(int id, String name, GameScreen gameScreen, boolean isAIDeck) {
        //Fully constructed deck, with random assortments of minion, spell and weapon cards
        mDeck = new ArrayList<>(0);
        mDiscardPile = new ArrayList<>(0);
        mCardHand = new ArrayList<>(0);

        this.mDeckID = id;
        this.mDeckName = name;
        this.mGameScreen = gameScreen;
        this.mIsAIDeck = isAIDeck;
        this.buildDeck();
    }

     /**
     /Public methods required for CardDeck class:
     **/

    public void buildDeck() {
        chooseDeckType();
        addToCardNum();
        insertMinionCard(mNumOfMinions);
        insertSpellCard(mNumOfSpells);
        insertWeaponCard(mNumOfWeapons);
    }

    public void chooseDeckType() {
        int mType = RANDOM.nextInt(3);

        switch (mType) {
            case 0:
                mNumOfMinions = 10;
                mNumOfSpells = 10;
                mNumOfWeapons = 10;
                break;
            case 1:
                mNumOfMinions = 15;
                mNumOfSpells = 10;
                mNumOfWeapons = 5;
                break;
            case 2:
                mNumOfMinions = 10;
                mNumOfSpells = 15;
                mNumOfWeapons = 5;
                break;
        }
    }

    // Methods to add different types of cards to the deck:
    public void addToCardNum() {
        mNumOfCards += mNumOfMinions;
        mNumOfCards += mNumOfWeapons;
        mNumOfCards += mNumOfSpells;
    }


    //TODO: change x and y pos to use screen widths
    public void insertMinionCard(int cardAmt) {



        for (int i = 0; i < cardAmt; i++) {
            float x = X_POSITION;
            float y = Y_POSITION;
            Boolean enemyDeck = false;
            if(getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            //x += 33.4f * i;
            MinionCard mTestMinion = new MinionCard(x, y, mGameScreen, 3, enemyDeck, 2, 1);

            mDeck.add(i, mTestMinion);
        }
    }

    public void insertSpellCard(int cardAmt) {



        for (int i = 0; i < cardAmt; i++) {
            float x = X_POSITION;
            float y = Y_POSITION;
            Boolean enemyDeck = false;
            if(getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            //x += 33.4 * i;
            SpellCard mTestSpell = new SpellCard(x, y, mGameScreen, 3, enemyDeck, Effect.NONE, 1);

            mDeck.add(i, mTestSpell);
        }
    }

    public void insertWeaponCard(int cardAmt) {



        for (int i = 0; i < cardAmt; i++) {
            float x = X_POSITION;
            float y = Y_POSITION;
            Boolean enemyDeck = false;
            if(getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            //x += 33.4f * i;
            WeaponCard mTestWeapon = new WeaponCard(x, y, mGameScreen, 3, enemyDeck, 2, 1);

            mDeck.add(i, mTestWeapon);
        }
    }

    //Methods required to draw from Deck:
    public Card drawTopCard() {
        if (!mDeck.isEmpty()) {
            Card topCard = mDeck.get(mDeck.size() - 1);
            trackRemovalOfCards();
            trackAdditionOfCardsToHand();
            mDeck.remove(mDeck.size() - 1);
            mCardHand.add(topCard);
            return topCard;
        }
        return null;
    }

    private void trackAdditionOfCardsToHand () {
        mSizeOfHand ++;
    }

    private void trackRemovalOfCards() {
        mNumOfCards --;
    }

    //This method destroys a card if player draws one, when their hand is already full:
    public void destroyCardOverLimit() {
        if (mCardHand.size() > MAX_HAND_CARDS) {
            for (int i = 5; i < mCardHand.size(); i++) {
                Card cardOver = mCardHand.get(mCardHand.size() - 1);
                mCardHand.remove(cardOver);
            }
        }
    }

    //Method that returns true if deck is empty - to be used when applying fatigue
    public boolean checkIsEmpty() {
        if (mDeck.size() == 0) {
            setmIsEmptyFlag(true);
        }
        return mIsEmptyFlag;
    }

    //Accessor and Mutator methods:
    public ArrayList<Card> getDeck() { return this.mDeck; }
    public ArrayList<Card> getmDiscardPile() { return this.mDiscardPile; }
    public ArrayList<Card> getmCardHand() { return this.mCardHand; }
    public int getDeckID() { return this.mDeckID; }
    public String getDeckName() { return this.mDeckName; }
    public GameScreen getmGameScreen() { return this.mGameScreen; }
    public int getmSizeOfHand() { return this.mSizeOfHand; }
    public boolean getmIsEmptyFlag() { return this.mIsEmptyFlag; }
    public int getNumOfCards() { return this.mNumOfCards ; }
    public int getMaxDeckCards() { return MAX_DECK_CARDS; }
    public int getMaxHandCards() { return MAX_HAND_CARDS; }
    public boolean getmIsAIDeck() { return this.mIsAIDeck; }
    public int getmNumOfMinions() { return this.mNumOfMinions; }
    public int getmNumOfSpells() { return this.mNumOfSpells; }
    public int getmNumOfWeapons() { return this.mNumOfWeapons; }

    public ArrayList<Card> setDeck(ArrayList<Card> newDeck) { return this.mDeck = newDeck; }
    public ArrayList<Card> setmDiscardPile(ArrayList<Card> newDiscard) {return this.mDiscardPile = newDiscard; }
    public ArrayList<Card> setmCardHand(ArrayList<Card> newHand) { return this.mCardHand = newHand; }
    public int setDeckID(int newDeckID) { return this.mDeckID = newDeckID; }
    public String setDeckName(String newDeckName) { return this.mDeckName = newDeckName; }
    public GameScreen setGameScreen(GameScreen newGameScreen) {return this.mGameScreen = newGameScreen; }
    public int setmSizeOfHand(int newHandSize) { return this.mSizeOfHand = newHandSize; }
    public boolean setmIsEmptyFlag(boolean isDeckEmpty) { return this.mIsEmptyFlag = isDeckEmpty; }
    public int setNumOfCards(int newNumOfCards) { return this.mNumOfCards = newNumOfCards; }
    public boolean setmIsAIDeck(boolean newIsAIDeck) { return this.mIsAIDeck = newIsAIDeck; }
    public int setmNumOfMinions(int newNumOfMinions) { return this.mNumOfMinions = newNumOfMinions; }
    public int setmNumOfSpells(int newNumOfSpells) { return this.mNumOfSpells = newNumOfSpells; }
    public int setmNumOfWeapons(int newNumOfWeapons) { return this.mNumOfWeapons = newNumOfWeapons; }

}