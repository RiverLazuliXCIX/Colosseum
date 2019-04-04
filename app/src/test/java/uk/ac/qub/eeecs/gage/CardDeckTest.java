package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.Effect;
import uk.ac.qub.eeecs.game.Colosseum.FatigueCounter;
import uk.ac.qub.eeecs.game.Colosseum.MinionCard;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.SpellCard;
import uk.ac.qub.eeecs.game.Colosseum.WeaponCard;
import uk.ac.qub.eeecs.game.TestClasses.FatigueScreenForTesting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardDeckTest {

    //Mocking up various objects, screens etc, required for tests
    @Mock
    private GameScreen mDemoScreen;
    @Mock
    private Game mGame;
    @Mock
    private AssetManager mAssetManager;
    @Mock
    private ScreenManager mScreenManager;
    @Mock
    private Bitmap mBitmap;
    @Mock
    private Input mInput;
    @Mock
    private LayerViewport mLayerViewport;
    @Mock
    private FatigueScreenForTesting testScreen;

    @Before
    public void setUp() {

        when(mGame.getAssetManager()).thenReturn(mAssetManager);
        when(mAssetManager.getBitmap(any(String.class))).thenReturn(mBitmap);
        when(mGame.getScreenManager()).thenReturn(mScreenManager);
        when(mGame.getInput()).thenReturn(mInput);
        when(mDemoScreen.getGame()).thenReturn(mGame);
        when(mDemoScreen.getName()).thenReturn("colosseumDemoScreen");
        when(mDemoScreen.getDefaultLayerViewport()).thenReturn(mLayerViewport);
    }

    //
    //Tests on the Constructors:
    //
    @Test
    public void cardDeckCreate() {
        //This test is to ensure that when the empty CardDeck constructor is called, a Deck ArrayList is created:

        CardDeck newDeck = new CardDeck();
        assertNotNull(newDeck.getDeck());
    }

    @Test
    public void cardDeckCreate_WithData() {
        //This test is to ensure that when the extended CardDeck constructor is called, a Deck ArrayList is created:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertNotNull(newDeck.getDeck());
    }

    @Test
    public void cardHandCreate() {
        //This test is to ensure that when the empty CardDeck constructor is called, a Hand ArrayList is created:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertNotNull(newDeck.getmCardHand());
    }

    @Test
    public void cardHandCreate_WithData() {
        //This test is to ensure that when the extended CardDeck constructor is called, a Hand ArrayList is created:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertNotNull(newDeck.getmCardHand());
    }

    @Test
    public void DiscardCreate() {
        //This test is to ensure that when the empty CardDeck constructor is called, a Discard ArrayList is created:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertNotNull(newDeck.getmDiscardPile());
    }

    @Test
    public void DiscardCreate_WithData() {
        //This test is to ensure that when the extended CardDeck constructor is called, a Discard ArrayList is created:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertNotNull(newDeck.getmDiscardPile());
    }

    //Initialisation check: Does building a deck add 30 cards to the Deck array?
    @Test
    public void cardDeck_30Cards() {
        //Test to ensure that 30 cards are added on initializaion of the extended deck constructor:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        int expectedNumOfCards = 30;

        assertEquals(newDeck.getDeck().size(), expectedNumOfCards);
    }

    //Initialisation check: does the numOfCards variable increase as cards are added to the Deck array?
    @Test
    public void cardDeckCreate_CardNumsCorrect() {
        //This test is to ensure that when a new deck is created, the NumOfCards variable increases to the size of the Deck ArrayList:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertEquals(newDeck.getNumOfCards(), newDeck.getDeck().size());
    }

    //
    // Tests on my 'drawTopCard()' method:
    //
    @Test
    public void drawTopCard_DeckTest() {
        //Set up a deck of 30 cards:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Draw one card from the deck:
        newDeck.drawTopCard();

        int expectedNumOfCards = 29;

        assertEquals(newDeck.getDeck().size(), expectedNumOfCards);
    }

    @Test
    public void drawTopCard_HandTest() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Draw a card into the hand once:
        newDeck.drawTopCard();

        int expectedHandSize = 1;

        assertEquals(newDeck.getmCardHand().size(), expectedHandSize);
    }

    @Test
    public void drawTopCard_MinDraw() {
        //Set up a deck of 30 cards:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        for (int i = 0; i < 31; i++) {
            newDeck.drawTopCard();
        }

        int expectedNumOfCards = 0;

        //This should not go below zero, i.e. -1, it should stop drawing when number hits 0
        assertEquals(newDeck.getDeck().size(), expectedNumOfCards);
    }

    //
    //Tests on my destroyCardOverLimit() method:
    //
    @Test
    public void destroyCardOverLimit_Success() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        for (int i = 0; i < 9; i++) {
            newDeck.drawTopCard();
        }

        //Since 9 cards were drawn, all cards over the limit of 8, ie 1 card, should be destroyed:
        newDeck.destroyCardOverLimit();

        int expectedHandSize = 8;

        //Thus, at the end there should only be 8 cards in the hand:
        assertEquals(expectedHandSize, newDeck.getmCardHand().size());
    }


    //
    //Tests on my 'checkIsEmpty()' method:
    //
    @Test
    public void cardDeckIsEmpty_Success() {
        //This test is to ensure that the isEmpty() method works as expected:
        CardDeck newDeck = new CardDeck();
        //This deck should have loaded in 0 cards

        assertTrue(newDeck.checkIsEmpty());
    }

    @Test
    public void cardDeckIsEmpty_Fails() {
        //This test is to ensure that the isEmpty() method works as expected:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);
        //This deck should have loaded in 30 cards

        assertFalse(newDeck.checkIsEmpty());
    }

    //
    // Tests on my 'drawSetNumCards()' method
    //
    @Test
    public void drawSetNumCards_Success() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Try to draw 15 cards into an empty hand
        newDeck.drawSetNumCards(15);

        //15 cards should now be in the hand
        int expectedNumOfCards = 15;

        assertEquals(newDeck.getmCardHand().size(), expectedNumOfCards);
    }

    @Test
    public void tryToDrawFromEmptyDeck() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Draw all 30 cards from deck
        newDeck.drawSetNumCards(30);

        //Check that deck is now empty
        assertEquals(newDeck.getDeck().size(), 0);

        //Check that 30 cards in hand
        assertEquals(newDeck.getmCardHand().size(), 30);

        //Draw one further card:
        newDeck.drawSetNumCards(1);

        //Should still only be 30 cards in hand:
        assertEquals(newDeck.getmCardHand().size(), 30);
    }

    //
    // Tests on drawCard() method, which adds fatigue:
    //
    @Test
    public void drawCard_DeckNotEmpty() {
        //I set up an ordinary deck of 30 cards
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);
        FatigueCounter counter = new FatigueCounter();
        Player player = new Player(mDemoScreen,"c");

        //I try to draw from this deck
        newDeck.drawCard(player, counter, mGame);

        int expectedHandSize = 1;
        int expectedDeckSize = 29;

        assertEquals(newDeck.getmCardHand().size(), expectedHandSize);
        assertEquals(newDeck.getDeck().size(), expectedDeckSize);
    }

    @Test
    public void drawCard_DeckIsEmpty() {
        FatigueCounter counter = new FatigueCounter();
        AIOpponent player = new AIOpponent(mDemoScreen,"c");
        mScreenManager.addScreen(testScreen);

        //Set up an empty deck:
        CardDeck newDeck = new CardDeck();

        //Call the method to draw one card:
        newDeck.drawCard(player, counter, mGame);

        //Fatigue should increase by 1:
        int expectedFatigueCounter = 1;

        assertEquals(counter.getmFatigueNum(), expectedFatigueCounter);
    }

    @Test
    public void cumulativeFatigue() {
        FatigueCounter counter = new FatigueCounter();
        AIOpponent player = new AIOpponent(mDemoScreen,"c");
        mScreenManager.addScreen(testScreen);

        //Set up an empty deck:
        CardDeck newDeck = new CardDeck();

        //Method is called 3 times, so current fatigue number should be 3:
        for (int i = 0; i < 3; i++) {
            newDeck.drawCard(player, counter, mGame);
        }

        int expectedFatigueCounter = 3;

        //We expect that the fatigue number will be 3:
        assertEquals(counter.getmFatigueNum(), expectedFatigueCounter);
    }

    @Test
    public void fatigue_TakesDamage() {
        FatigueCounter counter = new FatigueCounter();
        AIOpponent player = new AIOpponent(mDemoScreen,"c");
        mScreenManager.addScreen(testScreen);

        //Set up an empty deck:
        CardDeck newDeck = new CardDeck();

        //Method is called 5 times, so 1+2+3+4+5 should be taken:
        for (int i = 0; i < 5; i++) {
            newDeck.drawCard(player, counter, mGame);
        }

        int expectedPlayerHealth = 15;

        //We expect that the player should have lost 15 health (30-15=15):
        assertEquals(player.getCurrentHealth(), expectedPlayerHealth);
    }

    //
    // Tests on the discardCards() method:
    //

    @Test
    public void discardCards_RemovesAndAdds() {
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Draw 5 cards to the Hand
        for (int i = 0; i < 5; i++) {
            newDeck.drawTopCard();
        }

        int cardsInHand = 5;
        int cardsInGraveyard = 0;

        //There should now be 5 cards in the Hand and 0 in Graveyard:
        assertEquals(newDeck.getmCardHand().size(), cardsInHand);
        assertEquals(newDeck.getmDiscardPile().size(), cardsInGraveyard);

        //Discard last card from the Card Hand:
        newDeck.discardCards(newDeck.getmCardHand().get(newDeck.getmCardHand().size()-1));

        int newCardsInHand = 4;
        int newCardsInGraveyard = 1;

        //The last card should now have been removed from the hand, and placed into the graveyard:
        assertEquals(newDeck.getmCardHand().size(), newCardsInHand);
        assertEquals(newDeck.getmDiscardPile().size(), newCardsInGraveyard);

    }

    //
    // Tests on 'discardCards_EndOfTurn()' method:
    //

    @Test
    public void discardCards_EndOfTurn() {
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Draw 5 cards to the Hand
        for (int i = 0; i < 5; i++) {
            newDeck.drawTopCard();
        }

        //Set the first 3 cards as 'toBeDiscarded'
        for (int i = 0; i < 3; i++) {
            newDeck.getmCardHand().get(i).setToBeDiscarded(true);
        }

        //When this method is called, all cards in deck that are set as
        //'to be discarded' will be discarded, ie those the player selects
        for (int i = 0; i < newDeck.getmCardHand().size(); i++) {
            newDeck.discardCards_EndOfTurn(newDeck.getmCardHand().get(i));
        }

        int newCardsInHand = 2;
        int newCardsInGraveyard = 3;

        //These first 3 cards in the Card Hand should now be in the Graveyard:
        assertEquals(newDeck.getmCardHand().size(), newCardsInHand);
        assertEquals(newDeck.getmDiscardPile().size(), newCardsInGraveyard);
    }

    //
    // Tests on 'discardCards_0Health' method:
    //

    @Test
    public void discardCards_0Health_MinionCard_HealthAbove0() {
        //Set up deck with 30 cards:
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Remove all cards from deck for testing purposes:
        for (int i = newDeck.getDeck().size()-1; i >= 0; i--) {
            newDeck.getDeck().remove(newDeck.getDeck().size() - 1);
        }

        //Add a single Minion Card:
        MinionCard newMinion = new MinionCard(100, 100, mDemoScreen, 3, true, "Test_Card", 3, 3);
        newDeck.getDeck().add(0, newMinion);

        //Draw the minion card to the Card Hand:
        newDeck.drawTopCard();

        int expectedCardsInHand = 1;
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);

        //This card should have health of 3, and thus should not be discarded:
        newDeck.discardCards_0Health(newDeck.getmCardHand().get(0));

        //Thus there should still be 1 card in the Card Hand:
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);
    }

    @Test
    public void discardCards_0Health_MinionCard_HealthAt0() {
        //Set up deck with 30 cards:
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Remove all cards from deck for testing purposes:
        for (int i = newDeck.getDeck().size()-1; i >= 0; i--) {
            newDeck.getDeck().remove(newDeck.getDeck().size() - 1);
        }

        //Add a single Minion Card:
        MinionCard newMinion = new MinionCard(100, 100, mDemoScreen, 3, true, "Test_Card", 3, 0);
        newDeck.getDeck().add(0, newMinion);

        //Draw the minion card to the Card Hand:
        newDeck.drawTopCard();

        int expectedCardsInHand = 1;
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);

        //This card should has 0 health, and thus should be discarded:
        newDeck.discardCards_0Health(newDeck.getmCardHand().get(0));

        int newExpectedCardsInHand = 0;

        //There should now be 0 cards in the Card Hand:
        assertEquals(newDeck.getmCardHand().size(), newExpectedCardsInHand);
    }


    @Test
    public void discardCards_0Health_SpellCard_MagnitudeAbove0() {
        //Set up deck with 30 cards:
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Remove all cards from deck for testing purposes:
        for (int i = newDeck.getDeck().size()-1; i >= 0; i--) {
            newDeck.getDeck().remove(newDeck.getDeck().size() - 1);
        }

        //Add a single Spell Card:
        SpellCard newSpell = new SpellCard(100, 100, mDemoScreen, 3, true, "Test_Card", Effect.HEAL, 3);
        newDeck.getDeck().add(0, newSpell);

        //Draw the Spell Card to the Card Hand:
        newDeck.drawTopCard();

        int expectedCardsInHand = 1;
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);

        //This card should have magnitude of 3, and thus should not be discarded:
        newDeck.discardCards_0Health(newDeck.getmCardHand().get(0));

        //Thus there should still be 1 card in the Card Hand:
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);
    }

    @Test
    public void discardCards_0Health_SpellCard_MagnitudeAt0() {
        //Set up deck with 30 cards:
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Remove all cards from deck for testing purposes:
        for (int i = newDeck.getDeck().size()-1; i >= 0; i--) {
            newDeck.getDeck().remove(newDeck.getDeck().size() - 1);
        }

        //Add a single Spell Card:
        SpellCard newSpell = new SpellCard(100, 100, mDemoScreen, 3, true, "Test_Card", Effect.HEAL, 0);
        newDeck.getDeck().add(0, newSpell);

        //Draw the Spell Card to the Card Hand:
        newDeck.drawTopCard();

        int expectedCardsInHand = 1;
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);

        //This card should has 0 magnitude, and thus should be discarded:
        newDeck.discardCards_0Health(newDeck.getmCardHand().get(0));

        int newExpectedCardsInHand = 0;

        //There should now be 0 cards in the Card Hand:
        assertEquals(newDeck.getmCardHand().size(), newExpectedCardsInHand);
    }

    @Test
    public void discardCards_0Health_WeaponCard_ChargesAbove0() {
        //Set up deck with 30 cards:
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Remove all cards from deck for testing purposes:
        for (int i = newDeck.getDeck().size()-1; i >= 0; i--) {
            newDeck.getDeck().remove(newDeck.getDeck().size() - 1);
        }

        //Add a single Weapon Card:
        WeaponCard newWeapon = new WeaponCard(0, 0, mDemoScreen, 3, true, "Test_Card",3, 3 );
        newDeck.getDeck().add(0, newWeapon);

        //Draw the Weapon Card to the Card Hand:
        newDeck.drawTopCard();

        int expectedCardsInHand = 1;
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);

        //This card should have 3 charges left, and thus should not be discarded:
        newDeck.discardCards_0Health(newDeck.getmCardHand().get(0));

        //Thus there should still be 1 card in the Card Hand:
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);
    }

    @Test
    public void discardCards_0Health_WeaponCard_At0() {
        //Set up deck with 30 cards:
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Remove all cards from deck for testing purposes:
        for (int i = newDeck.getDeck().size()-1; i >= 0; i--) {
            newDeck.getDeck().remove(newDeck.getDeck().size() - 1);
        }

        //Add a single Weapon Card:
        WeaponCard newWeapon = new WeaponCard(0, 0, mDemoScreen, 3, true, "Test_Card",3, 0);
        newDeck.getDeck().add(0, newWeapon);

        //Draw the weapon card to the Card Hand:
        newDeck.drawTopCard();

        int expectedCardsInHand = 1;
        assertEquals(newDeck.getmCardHand().size(), expectedCardsInHand);

        //This card should has 0 charges left, and thus should be discarded:
        newDeck.discardCards_0Health(newDeck.getmCardHand().get(0));

        int newExpectedCardsInHand = 0;

        //There should now be 0 cards in the Card Hand:
        assertEquals(newDeck.getmCardHand().size(), newExpectedCardsInHand);
    }

    //
    // Tests on the Getters & Setters:
    //
    @Test
    public void cardDeckGetID() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        int expectedID = 1;

        assertEquals(newDeck.getDeckID(), expectedID);
    }

    @Test
    public void cardDeckGetName() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        String expectedName = "aCardDeck";

        assertEquals(newDeck.getDeckName(), expectedName);
    }

    @Test
    public void cardDeckGetScreen() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        GameScreen expectedScreen = mDemoScreen;

        assertEquals(newDeck.getmGameScreen(), expectedScreen);
    }

    @Test
    public void cardDeckGetIsAI() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertFalse(newDeck.getmIsAIDeck());
    }
}