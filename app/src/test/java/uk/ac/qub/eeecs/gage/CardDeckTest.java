package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CardDeckTest {

    @Mock
    private colosseumDemoScreen mDemoScreen;


    @Before
    public void setUp() {


    }

    @Test
    public void cardDeckCreate() {
        //This test is to ensure that when the CardDeck constructor is called, a deck is created:
        CardDeck newDeck = new CardDeck(001, "NewCardDeck", mDemoScreen);

        assertNotNull(newDeck);
    }

    @Test
    public void cardDeckSetID() {
        //This test is to ensure that the CardDeckID is set correctly:
        CardDeck newDeck2 = new CardDeck(002, "BetterCardDeck", mDemoScreen);

        assertEquals(newDeck2.getDeckID(),002);
    }

    @Test
    public void cardDeckSetName() {
        //This test is to ensure that the CardDeckName is set correctly:
        CardDeck newDeck3 = new CardDeck(003, "BestestCardDeck", mDemoScreen);

        assertEquals(newDeck3.getDeckName(), "BestestCardDeck");
    }

    @Test
    public void cardDeckIsEmpty() {
        //This test is to ensure that the isEmpty() method works as expected:
        CardDeck newDeck4 = new CardDeck(004, "ThisIsADeck", mDemoScreen);

        assertEquals(newDeck4.isEmpty(), true);
    }

/**    @Test
    public void cardDeckIsEmpty_Fails() {
        CardDeck newDeck5 = new CardDeck(004, "CardDeckHere", mDemoScreen);
        newDeck5.buildDeck();

        assertFalse(newDeck5.isEmpty());
    }**/
}
