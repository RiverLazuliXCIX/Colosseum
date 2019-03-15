package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.FatigueScreen;
import uk.ac.qub.eeecs.game.TestScreens.FatigueScreenForTesting;

//CardDeck class, coded by Dearbhaile Walsh
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

    //Integer variables to be used to determine deck 'type':
    private int mNumOfMinions, mNumOfSpells, mNumOfWeapons;

    // Corresponds to the associated hand region where cards will be drawn to
    private HandRegion mHandRegion;

    //Values for positioning re cards:
    private static final float X_POSITION = 160f, Y_POSITION = 25f;
    float x = X_POSITION;
    float y = Y_POSITION;

    //Variables initialized for card values:
    private String name = "";
    private int coinCost = 0, attack = 0, health = 0, magnitude = 0, damage = 0, charges = 0;;
    private Effect effect;
    private Boolean enemyDeck = false;

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
        //At present, cards load in with minions on top, then spells, then weapons.
        //TODO: Implement a feature to shuffle cards. - Dearbhaile
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

    public void setMinionValues(String cardName, int costToPlay, int attackVal, int healthVal) {
        name = cardName;
        coinCost = costToPlay;
        attack = attackVal;
        health = healthVal;
    }

    public void insertMinionCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {

            if (getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            int no = RANDOM.nextInt(5);
            switch (no) {
                case 0:
                    setMinionValues("Card_Cerberus", 1, 2, 1);
                    break;
                case 1:
                    setMinionValues("Card_Lion", 6, 5, 4);
                    break;
                case 2:
                    setMinionValues("Card_Veles", 4, 3, 4);
                    break;
                case 3:
                    setMinionValues("Card_Veteran", 2, 1, 3);
                    break;
                case 4:
                    setMinionValues("Card_Elephant", 9, 3, 8);
                    break;
                case 5:
                    setMinionValues("Card_Hound", 1, 1, 4);
                    break;
            }
            MinionCard mMinion = new MinionCard(x, y, mGameScreen, coinCost, enemyDeck, name, attack, health);
            mDeck.add(i, mMinion);
        }
    }

    public void setSpellValues(String cardName, int costToPlay, Effect cardEffect, int mag) {
        name = cardName;
        coinCost = costToPlay;
        effect = cardEffect;
        magnitude = mag;
    }

    public void insertSpellCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            if(getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }
            int no = RANDOM.nextInt(6);
            switch (no) {
                case 0:
                    setSpellValues("Card_Aegis", 3, Effect.NONE, 3);
                    break;
                case 1:
                    setSpellValues("Card_Cavalry", 3, Effect.NONE, 2);
                    break;
                case 2:
                    setSpellValues("Card_Commander", 6, Effect.NONE, 4);
                    break;
                case 3:
                    setSpellValues("Card_Touch", 5, Effect.NONE, 4);
                    break;
                case 4:
                    setSpellValues("Card_Aurora", 2, Effect.NONE, 1);
                    break;
                case 5:
                    setSpellValues("Card_Strike", 4, Effect.NONE, 2);
                    break;
                case 6:
                    setSpellValues("Card_Rete", 6, Effect.NONE, 5);
                    break;
            }
            SpellCard mSpell = new SpellCard(x, y, mGameScreen, 3, enemyDeck, name, Effect.NONE, 1);
            mDeck.add(i, mSpell);
        }
    }

    public void setWeaponValues(String cardName, int cardDamage, int costToPlay, int chargesLeft) {
        name = cardName;
        coinCost = costToPlay;
        damage = cardDamage;
        charges = chargesLeft;
    }

    public void insertWeaponCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            if (getDeckID() == 2) {
                enemyDeck = true;
                y *= 9.8f;
            }

            int no = RANDOM.nextInt(2);
            switch (no) {
                case 0:
                    setWeaponValues("Card_Hasta", 4, 3, 2);
                    break;
                case 1:
                    setWeaponValues("Card_Scourge", 5, 5, 1);
                    break;
                case 2:
                    setWeaponValues("Card_Bow", 3, 2, 3);
                    break;
            }
                WeaponCard mWeapon = new WeaponCard(x, y, mGameScreen, coinCost, enemyDeck, name, damage, charges);
                mDeck.add(i, mWeapon);
            }
        }

    //Method required to draw a single card from the Deck:
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

    //If player draws a card once their deck is at 0, they will be navigated to 'FatigueScreen'
    //On this screen, it will be displayed how much health they will lose (cumulative value)
    //Screen then disappears after 5 seconds
    public void drawCard(Player player, FatigueCounter counter, Game mGame) {
        if (!getDeck().isEmpty()) {
            drawTopCard();
            destroyCardOverLimit();
        } else {
            counter.incrementFatigue();
            mGame.getScreenManager().addScreen(new FatigueScreen(mGame, counter.getmFatigueNum()));
            player.receiveDamage(counter.getmFatigueNum());
        }
    }

    //A second instance of this method that does not require the graphical aspects of FatigueScreen
    //Will make testing a more efficient process, without having to mock up graphics/load them in differently:
    public void drawCard_testing(Player player, FatigueCounter counter, Game mGame) {
        if (!getDeck().isEmpty()) {
            drawTopCard();
            destroyCardOverLimit();
        } else {
            counter.takeAppropriateDamage(player);
            mGame.getScreenManager().addScreen(new FatigueScreenForTesting(mGame, counter.getmFatigueNum()));
        }
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

    public void drawSetNumCards(int numToDraw) {
        for (int i = 0; i < numToDraw; i++) {
            drawTopCard();
        }
    }

    public void discardCards_EndOfTurn() {
        //Remove discarded cards from hand
        for (int i = 0; i < mCardHand.size(); i++) {
            if (mCardHand.get(i).gettoBeDiscarded()) {
                discardCards(mCardHand.get(i));
            }
        }
    }

    public void discardCards(Card mCardToDiscard) {
        trackRemovalOfCards();
        mCardHand.remove(mCardToDiscard);
        mSizeOfDiscard++;
        mDiscardPile.add(mCardToDiscard);
    }
        //cardToDiscard.setToBeDiscarded(true);
        /*
            trackRemovalOfCards();
            mCardHand.remove(cardToDiscard);

            //Add card to discard pile
            mSizeOfDiscard++;
            mDiscardPile.add(cardToDiscard);
            */


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