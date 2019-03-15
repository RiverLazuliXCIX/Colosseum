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
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.FatigueCounter;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.TestScreens.FatigueScreenForTesting;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
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

        for (int i = 0; i < 6; i++) {
            newDeck.drawTopCard();
        }

        //Since 6 cards were drawn, all cards over the limit of 5, ie 1 card, should be destroyed:
        newDeck.destroyCardOverLimit();

        int expectedHandSize = 5;

        //Thus, at the end there should only be 5 cards in the hand:
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
    // Tests on my 'discardCard()' method:
    //
    @Test
    public void discardCard_Test() {
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //'Draw' 5 cards to the mCardHand ArrayList:
        newDeck.drawSetNumCards(5);

        //Assert that now 5 cards are in the hand:
        int expectedSizeOfHand = 5;
        assertEquals(newDeck.getmCardHand().size(), expectedSizeOfHand);

        //Remove the last card from the hand:
        newDeck.discardCard(newDeck.getmCardHand().get(4));

        //There should now be 4 cards in the hand:
        expectedSizeOfHand--;
        assertEquals(newDeck.getmCardHand().size(), expectedSizeOfHand);
    }

    @Test
    public void discardCard_DiscardPileIncreases() {
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        //Draw 5 cards to the mCardHand ArrayList:
        newDeck.drawSetNumCards(5);

        //Remove the last card from the hand:
        newDeck.discardCard(newDeck.getmCardHand().get(4));

        int expectedSizeOfDiscardPile = 1;

        assertEquals(newDeck.getmDiscardPile().size(), expectedSizeOfDiscardPile);
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
        Player player = new Player(mDemoScreen,"c");
        mScreenManager.addScreen(testScreen);

        //Set up an empty deck:
        CardDeck newDeck = new CardDeck();

        //Call the method, but with graphical aspects removed:
        newDeck.drawCard_testing(player, counter, mGame);

        //Fatigue should increase by 1:
        int expectedFatigueCounter = 1;

        assertEquals(counter.getmFatigueNum(), expectedFatigueCounter);
    }

    @Test
    public void cumulativeFatigue() {
        FatigueCounter counter = new FatigueCounter();
        Player player = new Player(mDemoScreen,"c");
        mScreenManager.addScreen(testScreen);

        //Set up an empty deck:
        CardDeck newDeck = new CardDeck();

        //Method is called 3 times, so current fatigue number should be 3:
        for (int i = 0; i < 3; i++) {
            newDeck.drawCard_testing(player, counter, mGame);
        }

        int expectedFatigueCounter = 3;

        //We expect that the fatigue number will be 3:
        assertEquals(counter.getmFatigueNum(), expectedFatigueCounter);
    }

    @Test
    public void fatigue_TakesDamage() {
        FatigueCounter counter = new FatigueCounter();
        Player player = new Player(mDemoScreen,"Mars");
        mScreenManager.addScreen(testScreen);

        //Set up an empty deck:
        CardDeck newDeck = new CardDeck();

        //Method is called 5 times, so 1+2+3+4+5 should be taken:
        for (int i = 0; i < 5; i++) {
            newDeck.drawCard_testing(player, counter, mGame);
        }

        int expectedPlayerHealth = 15;

        //We expect that the player should have lost 15 health (30-15=15):
        assertEquals(player.getCurrentHealth(), expectedPlayerHealth);
    }

    //
    // Tests on the Getters & Setters:
    //
    @Test
    public void cardDeckGetID() {
        //This test is to ensure that the CardDeck ID is set correctly:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        int expectedID = 1;

        assertEquals(newDeck.getDeckID(), expectedID);
    }

    @Test
    public void cardDeckGetName() {
        //This test is to ensure that the CardDeck Name is set correctly:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        String expectedName = "aCardDeck";

        assertEquals(newDeck.getDeckName(), expectedName);
    }

    @Test
    public void cardDeckGetScreen() {
        //This test is to ensure that the CardDeck GameScreen is set correctly:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        GameScreen expectedScreen = mDemoScreen;

        assertEquals(newDeck.getmGameScreen(), expectedScreen);
    }

    @Test
    public void cardDeckGetIsAI() {
        //This test is to ensure that the CardDeck AI ownership settings are set correctly:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertFalse(newDeck.getmIsAIDeck());
    }
}