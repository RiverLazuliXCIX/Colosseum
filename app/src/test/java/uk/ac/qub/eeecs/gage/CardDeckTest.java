package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class CardDeckTest {

    //Mocking up various objects, screens etc, required for tests
    @Mock
    private Game mGame;
    @Mock
    private GameScreen mGameScreen;
    @Mock
    private AssetManager mAssetManager;
    @Mock
    private Bitmap mBitmap;

    @Before
    public void setUp() {
        //when(mGame.getAssetManager()).thenReturn(mAssetManager);
        //when(mAssetManager.getBitmap(any(String.class))).thenReturn(mBitmap);
        //when(mGameScreen.getGame()).thenReturn(mGame);
        //when(mGameScreen.getName()).thenReturn("colosseumDemoScreen");
    }

    @Test
    public void cardDeckCreate() {
        //This test is to ensure that when the empty CardDeck constructor is called, a deck is created:
        CardDeck newDeck = new CardDeck();

        assertNotNull(newDeck);
    }

    @Test
    public void cardDeckCreate_WithData() {
        //This test is to ensure that the extended CardDeck constructor is called, a deck is created:
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertNotNull(newDeck);
    }

    @Test
    public void cardDeckCreate_AddsCards() {
        //This test is to ensure that when a new deck is created, the numOfCards variable changes:
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertNotEquals(newDeck.getNumOfCards(), 0);
    }

    @Test
    public void cardDeckGetID() {
        //This test is to ensure that the CardDeck ID is set correctly:
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertEquals(newDeck.getDeckID(), 1);
    }

    @Test
    public void cardDeckGetName() {
        //This test is to ensure that the CardDeck Name is set correctly:
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertEquals(newDeck.getDeckName(), "aCardDeck");
    }

    @Test
    public void cardDeckGetScreen() {
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertEquals(newDeck.getmGameScreen(), mGameScreen);
    }

    @Test
    public void cardDeckGetIsAI() {
        //This test is to ensure that the CardDeck AI ownership settings are set correctly:
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertFalse(newDeck.getIsAIDeck());
    }

    @Test
    public void cardDeckIsEmpty() {
        //This test is to ensure that the isEmpty() method works as expected:
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);

        assertEquals(newDeck.getDeck().isEmpty(), true);
    }

    //
    // The following 3 tests are to check that my chooseDeckType method works as expensive:
    //

    @Test
    public void chooseDeckType_Minions() {
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);
        newDeck.chooseDeckType();

        assertNotEquals(newDeck.getNumOfMinions(),0);
    }

    @Test
    public void chooseDeckType_Spells() {
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);
        newDeck.chooseDeckType();

        assertNotEquals(newDeck.getNumOfSpells(),0);
    }

    @Test
    public void chooseDeckType_Weapons() {
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);
        newDeck.chooseDeckType();

        assertNotEquals(newDeck.getNumOfWeapons(),0);
    }

    @Test
    public void insertGenericCard_Test() {
        CardDeck newDeck = new CardDeck(1, "aCardDeck", mGameScreen, false);
        newDeck.insertGenericCard_Test(1);

        assertEquals(newDeck.getDeck().size(), 1);
    }


    /**@Test
    public void cardDeckIsEmpty_Fails() {
        CardDeck newDeck5 = new CardDeck(004, "CardDeckHere", gameScreen);

        Card testCard = new Card(0, 0, gameScreen);
        newDeck5.addToDeck(testCard);

        assertFalse(newDeck5.isEmpty());
    }**/
}
