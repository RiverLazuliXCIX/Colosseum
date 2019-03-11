package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;

public class CardDeck {

    //Variables for Card Deck
    private int mDeckID;
    private String mDeckName;
    private int mNumOfCards = 0, mSizeOfHand = 0, mSizeOfDiscard = 0;
    private GameScreen mGameScreen;

    //ArrayList to hold the deck of cards, and the card graveyard
    private ArrayList<Card> mDeck, mDiscardPile, mCardHand;

    //Boolean values required to discern the functionality of the deck:
    private boolean mIsEmptyFlag, mIsAIDeck = false;

    //Constant values relating to Card Deck and Hand
    public static final int MAX_DECK_CARDS = 30, MAX_HAND_CARDS = 5;

    //Random, to be used to 'choose' type of deck:
    private static final Random RANDOM = new Random();

    private static final float X_POSITION = 160f, Y_POSITION = 25f;

    //Integer variables to be used to determine deck 'type':
    private int mNumOfMinions, mNumOfSpells, mNumOfWeapons;

    // Corresponds to the associated hand region where cards will be drawn to
    private HandRegion mHandRegion;

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

    public CardDeck(int id, String name, GameScreen gameScreen, boolean isAIDeck, HandRegion handRegion) {
        //Fully constructed deck, with random assortments of minion, spell and weapon cards
        mDeck = new ArrayList<>(0);
        mDiscardPile = new ArrayList<>(0);
        mCardHand = new ArrayList<>(0);

        this.mDeckID = id;
        this.mDeckName = name;
        this.mGameScreen = gameScreen;
        this.mIsAIDeck = isAIDeck;
        this.mHandRegion = handRegion;
        this.buildDeck();
    }

     //Public methods required for CardDeck class:
    public void buildDeck() {
        chooseDeckType();
        insertMinionCard(mNumOfMinions);
        insertSpellCard(mNumOfSpells);
        insertWeaponCard(mNumOfWeapons);
    }

    public void setUpDeckOptions(int mMinNum, int mSpellNum, int mWeapNum) {
        mNumOfMinions = mMinNum;
        mNumOfCards += mNumOfMinions;
        mNumOfSpells = mSpellNum;
        mNumOfCards += mNumOfSpells;
        mNumOfWeapons = mWeapNum;
        mNumOfCards += mNumOfWeapons;
    }

    public void chooseDeckType() {
        int mType = RANDOM.nextInt(3);
        switch (mType) {
            case 0:
                setUpDeckOptions(10, 10, 10);
                break;
            case 1:
                setUpDeckOptions(15, 10, 5);
                break;
            case 2:
                setUpDeckOptions(10, 15, 5);
                break;
        }
    }

    //TODO: change x and y pos to use screen widths - Sean
    public void insertMinionCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            float x = X_POSITION;
            float y = Y_POSITION;
            Boolean enemyDeck = false;
            if (getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            //x += 33.4f * i;
            Random rand = new Random();
            String name = "";
            int coinCost = 0;
            int attack = 0;
            int health = 0;

            int no = rand.nextInt(7);
            switch (no) {
                case 0:
                    name = "Card_Archer";
                    coinCost = 2;
                    attack = 3;
                    health = 2;
                    break;
                case 1:
                    name = "Card_Centurion";
                    coinCost = 3;
                    attack = 2;
                    health = 2;
                    break;
                case 2:
                    name = "Card_Cerberus";
                    coinCost = 1;
                    attack = 2;
                    health = 1;
                    break;
                case 3:
                    name = "Card_Lion";
                    coinCost = 6;
                    attack = 5;
                    health = 4;
                    break;
                case 4:
                    name = "Card_Veles";
                    coinCost = 4;
                    attack = 3;
                    health = 4;
                    break;
                case 5:
                    name = "Card_Veteran";
                    coinCost = 2;
                    attack = 1;
                    health = 3;
                    break;
                case 6:
                    name = "Card_Elephant";
                    coinCost = 9;
                    attack = 3;
                    health = 8;
                case 7:
                    name = "Card_Hound";
                    coinCost = 1;
                    attack = 1;
                    health = 4;
            }

            MinionCard mMinion = new MinionCard(x, y, mGameScreen, coinCost, enemyDeck, name, attack, health);
            mDeck.add(i, mMinion);
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
            Random rand = new Random();
            String name = "";
            int coinCost = 0;
            Effect effect;
            int magnitude = 0;
            int no = rand.nextInt(8);

            switch (no) {
                case 0:
                    name = "Card_Aegis";
                    coinCost = 3;
                    effect = Effect.NONE;
                    magnitude = 3;
                    break;
                case 1:
                    name = "Card_Battleshout";
                    coinCost = 5;
                    effect = Effect.NONE;
                    magnitude = 2;
                    break;
                case 2:
                    name = "Card_Cavalry";
                    coinCost = 3;
                    effect = Effect.NONE;
                    magnitude = 2;
                    break;
                case 3:
                    name = "Card_Commander";
                    coinCost = 6;
                    effect = Effect.NONE;
                    magnitude = 4;
                    break;
                case 4:
                    name = "Card_Touch";
                    coinCost = 5;
                    effect = Effect.NONE;
                    magnitude = 4;
                    break;
                case 5:
                    name = "Card_Aurora";
                    coinCost = 2;
                    effect = Effect.NONE;
                    magnitude = 1;
                    break;
                case 6:
                    name = "Card_LionWarCry";
                    coinCost = 4;
                    effect = Effect.NONE;
                    magnitude = 3;
                    break;
                case 7:
                    name = "Card_Strike";
                    coinCost = 4;
                    effect = Effect.NONE;
                    magnitude = 2;
                    break;
                case 8:
                    name = "Card_Rete";
                    coinCost = 6;
                    effect = Effect.NONE;
                    magnitude = 5;
                    break;
            }

            SpellCard mSpell = new SpellCard(x, y, mGameScreen, 3, enemyDeck, name, Effect.NONE, 1);

            mDeck.add(i, mSpell);
        }
    }

    public void insertWeaponCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            float x = X_POSITION;
            float y = Y_POSITION;
            Boolean enemyDeck = false;
            if (getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            //x += 33.4f * i;
            Random rand = new Random();
            String name = "";
            int coinCost = 0, damage = 0, charges = 0;
            int no = rand.nextInt(3);
            switch (no) {
                case 0:
                    name = "Card_Hasta";
                    coinCost = 4;
                    damage = 3;
                    charges = 2;
                    break;
                case 1:
                    name = "Card_Axe";
                    coinCost = 5;
                    damage = 4;
                    charges = 3;
                    break;
                case 2:
                    name = "Card_Scourge";
                    coinCost = 5;
                    damage = 5;
                    charges = 1;
                    break;
                case 3:
                    name = "Card_Bow";
                    coinCost = 3;
                    damage = 2;
                    charges = 3;
                    break;
            }
                WeaponCard mWeapon = new WeaponCard(x, y, mGameScreen, coinCost, enemyDeck, name, damage, charges);
                mDeck.add(i, mWeapon);
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
            mHandRegion.addCard(topCard);
            return topCard;
        }
        return null;
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

    private void trackAdditionOfCardsToHand () {
        mSizeOfHand ++;
    }

    private void trackRemovalOfCards() {
        mNumOfCards --;
    }

    //Accessor and Mutator methods:
    public ArrayList<Card> getDeck() { return this.mDeck; }
    public ArrayList<Card> getmDiscardPile() { return this.mDiscardPile; }
    public ArrayList<Card> getmCardHand() { return this.mCardHand; }
    public int getDeckID() { return this.mDeckID; }
    public String getDeckName() { return this.mDeckName; }
    public GameScreen getmGameScreen() { return this.mGameScreen; }
    public int getmSizeOfHand() { return this.mSizeOfHand; }
    public int getmSizeOfDiscard() { return this.mSizeOfDiscard; }
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
    public int setmSizeOfDiscard(int newDiscardSize) { return this.mSizeOfDiscard = newDiscardSize; }
    public boolean setmIsEmptyFlag(boolean isDeckEmpty) { return this.mIsEmptyFlag = isDeckEmpty; }
    public int setNumOfCards(int newNumOfCards) { return this.mNumOfCards = newNumOfCards; }
    public boolean setmIsAIDeck(boolean newIsAIDeck) { return this.mIsAIDeck = newIsAIDeck; }
    public int setmNumOfMinions(int newNumOfMinions) { return this.mNumOfMinions = newNumOfMinions; }
    public int setmNumOfSpells(int newNumOfSpells) { return this.mNumOfSpells = newNumOfSpells; }
    public int setmNumOfWeapons(int newNumOfWeapons) { return this.mNumOfWeapons = newNumOfWeapons; }

}