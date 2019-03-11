package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

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
    private colosseumDemoScreen mDemoScreen;
    @Mock
    private Game mGame;
    @Mock
    private AssetManager mAssetManager;
    @Mock
    private Bitmap mBitmap;


    @Before
    public void setUp() {
        when(mDemoScreen.getGame()).thenReturn(mGame);
        when(mGame.getAssetManager()).thenReturn(mAssetManager);
        when(mAssetManager.getBitmap(any(String.class))).thenReturn(mBitmap);
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

    @Test
    public void cardDeck_30Cards() {
        //Test to ensure that 30 cards are added on initializaion of the extended deck constructor:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertEquals(newDeck.getDeck().size(), 30);
    }

    @Test
    public void cardDeckCreate_CardNumsCorrect() {
        //This test is to ensure that when a new deck is created, the numOfCards variable increases to the size of the Deck ArrayList:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertEquals(newDeck.getNumOfCards(), newDeck.getDeck().size());
    }

    @Test
    public void cardHandCreate_CardNumsCorrect() {
        //This test is to ensure that when a card hand is created, the sizeOfHand variable increases to the size of the Hand ArrayList:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        assertEquals(newDeck.getmCardHand().size(), newDeck.getmSizeOfHand());
    }

    //
    // The following 3 tests are to check that my chooseDeckType method works as expected:
    //

    @Test
    public void chooseDeckType_Minions() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);
        newDeck.chooseDeckType();

        assertNotEquals(newDeck.getmNumOfMinions(), 0);
    }

    @Test
    public void chooseDeckType_Spells() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);
        newDeck.chooseDeckType();

        assertNotEquals(newDeck.getmNumOfSpells(), 0);
    }

    @Test
    public void chooseDeckType_Weapons() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);
        newDeck.chooseDeckType();

        assertNotEquals(newDeck.getmNumOfWeapons(), 0);
    }


    //
    // Tests on my 'drawTopCard()' method:
    //

    @Test
    public void drawTopCard_DeckTest() {
        //Set up a deck of 30 cards:
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

        newDeck.drawTopCard();

        int expectedNumOfCards = 29;

        assertEquals(newDeck.getDeck().size(), expectedNumOfCards);
    }

    @Test
    public void drawTopCard_HandTest() {
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, handRegion);

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

        newDeck.destroyCardOverLimit();

        int expectedHandSize = 5;

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