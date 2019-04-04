package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.FatigueCounter;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.Turn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FatigueCounterClassTest {

    //Mocking up various objects, screens etc, required for tests
    @Mock
    private GameScreen mGameScreen;
    @Mock
    private Game mGame;
    @Mock
    private AssetManager mAssetManager;
    @Mock
    private Bitmap mBitmap;
    @Mock
    private Input mInput;
    @Mock
    private LayerViewport mLayerViewport;

    @Before
    public void setUp() {
        when(mGame.getAssetManager()).thenReturn(mAssetManager);
        when(mAssetManager.getBitmap(any(String.class))).thenReturn(mBitmap);
        when(mGame.getInput()).thenReturn(mInput);
        when(mGameScreen.getGame()).thenReturn(mGame);
        when(mGameScreen.getName()).thenReturn("colosseumDemoScreen");
        when(mGameScreen.getDefaultLayerViewport()).thenReturn(mLayerViewport);
    }

    //
    //Tests on the Constructor:
    //
    @Test
    public void fatigueCounterCreate() {
        //This test is to ensure that when the FatigueCounter constructor is called, a counter is created:
        FatigueCounter counter = new FatigueCounter();

        assertNotNull(counter);
    }

    @Test
    public void variablesInitialisedCorrectly() {
        //This test is to ensure that the counter always begins at 0 when initialised
        FatigueCounter counter = new FatigueCounter();

        int expectedStartingFatigue = 0;

        assertEquals(counter.getmFatigueNum(), expectedStartingFatigue);
    }

    //
    //Tests on the 'incrementFatigueCounter()' method
    //

    @Test
    public void incrementFatigueCounter_Works() {
        //This test is to ensure that when the method is called, the fatigueNum is incremented
        FatigueCounter counter = new FatigueCounter();

        counter.incrementFatigue();

        //Should have gone from 0 up to 1:
        int expectedFatigueNow = 1;

        assertEquals(counter.getmFatigueNum(),expectedFatigueNow);
    }

    //
    //Tests on the 'takeAppropriateDamage()' method:
    //

    @Test
    public void takeAppropriateDamage_Test() {
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        int startingHealth = newPlayer.getCurrentHealth();

        FatigueCounter counter = new FatigueCounter();

        counter.incrementFatigue();
        counter.takeAppropriateDamage(newPlayer);

        int newHealth = startingHealth - 1;

        assertEquals(newPlayer.getCurrentHealth(), newHealth);
    }

    @Test
    public void takeAppropriateDamage_TakesToZero() {
        //Set up new player, who takes 30 health by default initialisation:
        Player newPlayer = new Player(mGameScreen, "Brutalus");

        FatigueCounter counter = new FatigueCounter();

        for (int i = 0; i < 8; i++) {
            counter.incrementFatigue();
            counter.takeAppropriateDamage(newPlayer);
        }

        //After 8 runs, new health should be 30 - (8+7+6+5+4+3+2+1) = minus 6.
        //This method should prevent the health reading from falling below 0.
        int expectedHealth = 0;

        assertEquals(newPlayer.getCurrentHealth(), expectedHealth);
    }
}