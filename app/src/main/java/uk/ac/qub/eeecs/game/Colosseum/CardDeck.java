package uk.ac.qub.eeecs.game.Colosseum;

import java.util.ArrayList;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.FatigueScreen;
import uk.ac.qub.eeecs.game.TestClasses.FatigueScreenForTesting;

/**
 * CardDeck Class, modelling the player's (User/AI Opponent's) Deck of Cards in the Game.
 * @author Dearbhaile Walsh, with contributions from
 * @author Sean McCloskey, said contributions are attributed
 */

public class CardDeck {
    //ArrayList to hold the deck of cards, player hand, and the card graveyard:
    private ArrayList<Card> mDeck, mDiscardPile, mCardHand;

    //Variables required for Card Deck:
    private int mDeckID;
    private String mDeckName;
    private int mNumOfCards = 0, mSizeOfHand = 0, mSizeOfDiscard = 0;
    private GameScreen mGameScreen;

    //Integer variables to be used to determine deck 'type':
    private int mNumOfMinions, mNumOfSpells, mNumOfWeapons;

    //Final values relating to Card Deck and Hand:
    private static final int MAX_DECK_CARDS = 30, MAX_HAND_CARDS = 5;

    //Boolean values required to discern the functionality of the deck:
    private boolean mIsEmptyFlag, mIsAIDeck = false;

    //Random, to be used to 'choose' type of deck:
    private static final Random RANDOM = new Random();

    /**
     /Values for positioning cards
     @author Sean McCloskey
     **/
    private static final float X_POSITION = 160f, Y_POSITION = 25f;
    private float x = X_POSITION;
    private float y = Y_POSITION;

    //Variables required for Initialising Card Values:
    private String name = "";
    private int coinCost = 0, attack = 0, health = 0, magnitude = 0, damage = 0, charges = 0;;
    private Effect effect;
    private Boolean enemyDeck = false;

    // Corresponds to the associated hand region where cards will be drawn to:
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
        //BuildDeck method is called to populate the deck, as shown below:
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
        chooseDeckType(); //Use random generator to select the ratio of minion/spell/weapon cards in deck
        insertMinionCard(mNumOfMinions); //Insert the chosen number of minion cards to deck
        insertSpellCard(mNumOfSpells); //Insert the chosen number of spell cards to deck
        insertWeaponCard(mNumOfWeapons); //Insert the chosen number of weapon cards to deck
    }

    public void setUpDeckOptions(int mMinNum, int mSpellNum, int mWeapNum) {
        //Updates number of cards/number of minion/spell/weapon cards
        mNumOfMinions = mMinNum;
        mNumOfCards += mNumOfMinions;
        mNumOfSpells = mSpellNum;
        mNumOfCards += mNumOfSpells;
        mNumOfWeapons = mWeapNum;
        mNumOfCards += mNumOfWeapons;
    }

    public void chooseDeckType() {
        //Depending on outcome of random number generator, different
        //types of decks are constructed:
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
        //Method used to set the Minion Card values, once they are chosen:
        name = cardName;
        coinCost = costToPlay;
        attack = attackVal;
        health = healthVal;
    }

    public void insertMinionCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {

            if (getDeckID() == 2) {
                /**
                @author: Sean McCloskey
                //Enemy cards are positioned differently, and
                //have different attributes than the player's:
                 **/
                enemyDeck = true;
                y *= 9.8f;
            }
            int randomNum = RANDOM.nextInt(6);
            switch (randomNum) {
                //Inserts random minion cards into the deck:
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
        //Method used to set the Spell Card values, once they are chosen:
        name = cardName;
        coinCost = costToPlay;
        effect = cardEffect;
        magnitude = mag;
    }

    public void insertSpellCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            if(getDeckID() == 2) {
                /**
                 @author: Sean McCloskey
                 //Enemy cards are positioned differently, and
                 //have different attributes than the player's:
                 **/
                enemyDeck = true;
                y *= 9.8f;
            }
            int randomNum = RANDOM.nextInt(7);
            switch (randomNum) {
                //Inserts random Spell Cards into the deck:
                case 0:
                    setSpellValues("Card_Aegis", 3, Effect.NONE, 1);
                    break;
                case 1:
                    setSpellValues("Card_Cavalry", 3, Effect.NONE, 1);
                    break;
                case 2:
                    setSpellValues("Card_Commander", 6, Effect.NONE, 1);
                    break;
                case 3:
                    setSpellValues("Card_Touch", 5, Effect.NONE, 1);
                    break;
                case 4:
                    setSpellValues("Card_Aurora", 2, Effect.NONE, 1);
                    break;
                case 5:
                    setSpellValues("Card_Strike", 4, Effect.NONE, 1);
                    break;
                case 6:
                    setSpellValues("Card_Rete", 6, Effect.NONE, 1);
                    break;
            }
            SpellCard mSpell = new SpellCard(x, y, mGameScreen, 3, enemyDeck, name, Effect.NONE, 1);
            mDeck.add(i, mSpell);
        }
    }

    public void setWeaponValues(String cardName, int cardDamage, int costToPlay, int chargesLeft) {
        //Method used to set the Weapon Card values, once they are chosen:
        name = cardName;
        coinCost = costToPlay;
        damage = cardDamage;
        charges = chargesLeft;
    }

    public void insertWeaponCard(int cardAmt) {
        for (int i = 0; i < cardAmt; i++) {
            if (getDeckID() == 2) {
                /**
                 @author: Sean McCloskey
                 //Enemy cards are positioned differently, and
                 //have different attributes than the player's:
                 **/
                enemyDeck = true;
                y *= 9.8f;
            }

            int randomNum = RANDOM.nextInt(3);
            switch (randomNum) {
                case 0:
                    setWeaponValues("Card_Hasta", 4, 3, 1);
                    break;
                case 1:
                    setWeaponValues("Card_Scourge", 5, 5, 1);
                    break;
                case 2:
                    setWeaponValues("Card_Bow", 3, 2, 1);
                    break;
            }
                WeaponCard mWeapon = new WeaponCard(x, y, mGameScreen, coinCost, enemyDeck, name, damage, charges);
                mDeck.add(i, mWeapon);
            }
        }

    public void shuffleCards() { //Method to shuffle the cards so they are no longer just weapon cards
        //..on top of spell cards, on top of minion cards. Called after the decks are set up in the main
        //..game screen. A temporary deck is set up for the swap, which are then randomly placed into
        //..original card deck once again.

        ArrayList<Card> tempDeck = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < getDeck().size(); i++) {
            tempDeck.add(i, getDeck().get(i));
        }

        for (int j = 0; j < tempDeck.size(); j++) {
            int randomIndex = r.nextInt(tempDeck.size());
            getDeck().set(j, tempDeck.get(randomIndex));
        }
    }

    //Method required to draw a single card from the Deck:
    public Card drawTopCard() {
        //You cannot draw from an empty deck:
        if (!mDeck.isEmpty()) {
            //If deck contains cards, draw the last card:
            Card topCard = mDeck.get(mDeck.size() - 1);
            trackRemovalOfCards();
            trackAdditionOfCardsToHand();
            //Card is removed from Deck and added to Hand:
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
            drawTopCard(); //If there are cards in deck, draw top card
            destroyCardOverLimit(); //If there are already 5 cards in hand, destroy new card
        } else {
            counter.incrementFatigue();
            if (player instanceof AIOpponent == false) { //Screen should not be displayed when enemy's card
                //numbers drop below 0. This is not information the user needs to see.
                mGame.getScreenManager().addScreen(new FatigueScreen(mGame, counter.getmFatigueNum()));
            }
            counter.takeAppropriateDamage(player); //Appropriate damage is taken from player
        }
    }

    //This method destroys a card if player draws one when their hand is already full
    //Player hand can be of max size 5, and any additional cards drawn are an unfair advantage:
    public void destroyCardOverLimit() {
        if (mCardHand.size() > MAX_HAND_CARDS) { //Checks has player got >5 cards in their hand
            for (int i = 5; i < mCardHand.size(); i++) {
                Card cardOver = mCardHand.get(mCardHand.size() - 1);
                mCardHand.remove(cardOver); //Destroys every card above 5 in hand
            }
        }
    }

    //Method for a fixed number of cards, used at the beginning of each game to allot cards based on coin toss:
    public void drawSetNumCards(int numToDraw) {
        for (int i = 0; i < numToDraw; i++) {
            drawTopCard();
        }
    }

    public void discardCards(Card cardToDiscard) {
        trackRemovalOfCards();
        //Remove card from hand:
        mCardHand.remove(cardToDiscard);
        mSizeOfDiscard++;
        //...and add to discard pile:
        mDiscardPile.add(cardToDiscard);
    }

    public void discardCards_EndOfTurn(Card disCard) {
        discardCards(disCard);
    }

    public void checkForDeadCards() { //Called constantly on both decks throughout game
        for (int i = mCardHand.size()-1; i >= 0; i--) {
            discardCards_0Health(mCardHand.get(i)); //If health hits 0, card is immediately discarded
        }
    }

    public void discardCards_0Health(Card card) {
        //Check for type of card:
        if (card instanceof MinionCard) {
            if (((MinionCard) card).getHealth() == 0) {
                discardCards(card); //Discard if minion has no health
            }
        }
        //Check for type of card:
        if (card instanceof SpellCard) {
            if(((SpellCard)card).getMagnitude() == 0) {
                discardCards(card); //Discard if spell has no magnitude
            }
        }
        //Check for type of card:
        if (card instanceof WeaponCard) {
            if (((WeaponCard)card).getCharges() == 0) {
                discardCards(card); //Discard if weapon has no charges left
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