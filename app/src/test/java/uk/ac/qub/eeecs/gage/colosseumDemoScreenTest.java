package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Assert;
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
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.Turn;
import uk.ac.qub.eeecs.game.Colosseum.UserWhoStarts;
import uk.ac.qub.eeecs.game.TestClasses.colosseumDemoScreenForTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class colosseumDemoScreenTest {

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
    //Tests on the constructor:
    //
    @Test
    public void createInstanceOfColDemoScreen() {
        Player testPlayer = new Player(mDemoScreen, "Sagira");
        AIOpponent testOpponent = new AIOpponent(mDemoScreen, "Sagira");
        Turn testTurn = new Turn();
        UserWhoStarts enemyStarts = UserWhoStarts.ENEMYSTARTS;
        long enemyStartTime = System.currentTimeMillis();
        CardDeck playerDeck = new CardDeck();
        CardDeck enemyDeck = new CardDeck();
        HandRegion hRegion = new HandRegion(0, 0, 0, 0);

        colosseumDemoScreenForTesting testScreen = new colosseumDemoScreenForTesting(testPlayer, testOpponent, testTurn, enemyStarts,
                enemyStartTime, playerDeck, enemyDeck, hRegion, hRegion, mGame);

        assertNotNull(testScreen);
    }

    @Test
    public void parametersPassed_Success() {
        Player testPlayer = new Player(mDemoScreen, "Sagira");
        AIOpponent testOpponent = new AIOpponent(mDemoScreen, "Sagira");
        Turn testTurn = new Turn();
        UserWhoStarts enemyStarts = UserWhoStarts.ENEMYSTARTS;
        long enemyStartTime = System.currentTimeMillis();
        CardDeck playerDeck = new CardDeck();
        CardDeck enemyDeck = new CardDeck();
        HandRegion hRegion = new HandRegion(0, 0, 0, 0);

        colosseumDemoScreenForTesting testScreen = new colosseumDemoScreenForTesting(testPlayer, testOpponent, testTurn, enemyStarts,
                enemyStartTime, playerDeck, enemyDeck, hRegion, hRegion, mGame);

        assertEquals(testScreen.getmPlayer(), testPlayer);
        assertEquals(testScreen.getmOpponent(), testOpponent);
        assertEquals(testScreen.getmCurrentTurn(), testTurn);
        assertEquals(testScreen.getUserWhoStarts(), enemyStarts);
        assertEquals(testScreen.getmPlayerDeck(), playerDeck);
        assertEquals(testScreen.getmEnemyDeck(), enemyDeck);
        assertEquals(testScreen.getPlayerHandRegion(), hRegion);
        assertEquals(testScreen.getOpponentHandRegion(), hRegion);

    }

    @Test
    public void gameObjectsSetUp() {
        Player testPlayer = new Player(mDemoScreen, "Sagira");
        AIOpponent testOpponent = new AIOpponent(mDemoScreen, "Sagira");
        Turn testTurn = new Turn();
        UserWhoStarts enemyStarts = UserWhoStarts.ENEMYSTARTS;
        long enemyStartTime = System.currentTimeMillis();
        CardDeck playerDeck = new CardDeck();
        CardDeck enemyDeck = new CardDeck();
        HandRegion hRegion = new HandRegion(0, 0, 0, 0);

        colosseumDemoScreenForTesting testScreen = new colosseumDemoScreenForTesting(testPlayer, testOpponent, testTurn, enemyStarts,
                enemyStartTime, playerDeck, enemyDeck, hRegion, hRegion, mGame);

        testScreen.setUpGameObjects();

        //setUpGameObjects method should set up FPS counter,
        //an instance of player, and an instance of opponent
        assertNotNull(testScreen.getFpsCounter());
        assertNotNull(testScreen.getmPlayer());
        assertNotNull(testScreen.getmOpponent());
    }

    //Tests on the endPlayerTurn() method:

    @Test
    public void endPlayerTurn_Success() {
        Player testPlayer = new Player(mDemoScreen, "Sagira");
        AIOpponent testOpponent = new AIOpponent(mDemoScreen, "Sagira");
        Turn testTurn = new Turn();
        UserWhoStarts enemyStarts = UserWhoStarts.ENEMYSTARTS;
        long enemyStartTime = System.currentTimeMillis();
        HandRegion hRegion = new HandRegion(0, 0, 0, 0);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, hRegion);
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mDemoScreen, false, hRegion);

        colosseumDemoScreenForTesting testScreen = new colosseumDemoScreenForTesting(testPlayer, testOpponent, testTurn, enemyStarts,
                enemyStartTime, playerDeck, enemyDeck, hRegion, hRegion, mGame);

        //Turn is set to being player's turn
        testPlayer.setYourTurn(true);
        testOpponent.setYourTurn(false);

        testScreen.endPlayerTurn();

        //endPlayerTurn method should switch to being enemy's turn
        assertFalse(testPlayer.getYourTurn());
        assertTrue(testOpponent.getYourTurn());
        //...should update the enemy's "turn begins" time
        Assert.assertNotEquals(testScreen.getmEnemyTurnBegins(), enemyStartTime);
        //...should give the enemy an extra card, as it's the beginning of their turn:
        assertEquals(enemyDeck.getmCardHand().size(), 1);
        //and SINCE THE ENEMY WAS THE STARTING PLAYER, should increment turn num from 1->2:
        assertEquals(testTurn.getmTurnNum(), 2);
    }

}