package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.UserWhoStarts;
import uk.ac.qub.eeecs.game.TestClasses.CoinTossScreenForTesting;

@RunWith(MockitoJUnitRunner.class)
public class CoinTossScreenTest {

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

    @Test
    public void screen_SetUpFully() {
        //This is a test on the constructor, to see when it is called,
        //is an instance of CoinTossScreen created or not -
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        assertNotNull(c1);
    }

    @Test
    public void screen_NameCorrect() {
        //This test is to ensure that the correct name is assigned to the new CoinTossScreen
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        String expectedResult = "CoinTossScreen";

        assertEquals(expectedResult, c1.getName());
    }

    @Test
    public void setUpGameObjects_Success() {
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        //Call method to set up Game Objects:
        c1.setUpGameObjects();

        //Ensure player and opponent are initialised:
        assertNotNull(c1.getmPlayer());
        assertNotNull(c1.getmOpponent());

        //Ensure mana and mana cap are set to 4/4 for all players:
        int expectedValue = 4;
        assertEquals(c1.getmPlayer().getCurrentMana(), expectedValue);
        assertEquals(c1.getmPlayer().getCurrentManaCap(), expectedValue);
        assertEquals(c1.getmOpponent().getCurrentMana(), expectedValue);
        assertEquals(c1.getmOpponent().getCurrentManaCap(), expectedValue);

        //Ensure both decks, i.e. player and opponent, are initialised:
        assertNotNull(c1.getmPlayerDeck());
        assertNotNull(c1.getmEnemyDeck());
    }

    @Test
    public void coinToss_PromptsCorrectMessage() {
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        int result = c1.setResult();
        c1.chooseTextToDisplay();

        if (result == 0) { // Ie, heads, player starts
            String expectedLine1 = "The coin landed on heads! You get to play first.";
            String expectedLine2 = "The other player draws 4 cards, and gets 1 additional mana.";

            assertEquals(expectedLine1, c1.getmCoinTossMsg1());
            assertEquals(expectedLine2, c1.getmCoinTossMsg2());
        } else if (result == 1) {// Ie, tails, enemy starts
            String expectedLine1 = "The coin landed on tails! The enemy plays first.";
            String expectedLine2 = "You draw an extra card and additional mana for your troubles.";

            assertEquals(expectedLine1, c1.getmCoinTossMsg1());
            assertEquals(expectedLine2, c1.getmCoinTossMsg2());
        } else if (result == 2) {
            String expectedLine1 = "The coin landed on its edge!";
            String expectedLine2 = "You automatically win the game for being lucky!";

            assertEquals(expectedLine1, c1.getmCoinTossMsg1());
            assertEquals(expectedLine2, c1.getmCoinTossMsg2());
        }
    }

    @Test
    public void coinFlipResult_SetsWhoStartsCorrectly_Player() {
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        c1.coinFlipResult(0); // Ie, heads, player starts

        UserWhoStarts expectedStarter = UserWhoStarts.PLAYERSTARTS;

        assertEquals(c1.getmUserWhoStarts(), expectedStarter);
    }

    @Test
    public void coinFlipResult_SetsWhoStartsCorrectly_Enemy() {
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        c1.coinFlipResult(1); // Ie, heads, player starts

        UserWhoStarts expectedStarter = UserWhoStarts.ENEMYSTARTS;

        assertEquals(c1.getmUserWhoStarts(), expectedStarter);
    }

    @Test
    public void coinFlipResult_GeneratesStatsCorrectly_PlayerStarts() {
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        c1.coinFlipResult(0); // Heads, i.e., player starts

        //Make sure 'yourTurn' values are set correctly:
        assertTrue(c1.getmPlayer().getYourTurn());
        assertFalse(c1.getmOpponent().getYourTurn());

        //Make sure mana values are set correctly, i.e. 5/5 for opponent:
        int expectedOpponentMana = 5;
        assertEquals(c1.getmOpponent().getCurrentMana(), expectedOpponentMana);
        assertEquals(c1.getmOpponent().getCurrentManaCap(), expectedOpponentMana);

        //Make sure decks are loaded correctly:
        int expectedPlayerCards = 3;
        int expectedEnemyCards = 4;
        assertEquals(c1.getmPlayerDeck().getmCardHand().size(), expectedPlayerCards);
        assertEquals(c1.getmEnemyDeck().getmCardHand().size(), expectedEnemyCards);
    }

    @Test
    public void coinFlipResult_GeneratesStatsCorrectly_EnemyStarts() {
        CoinTossScreenForTesting c1 = new CoinTossScreenForTesting(mGame);

        c1.coinFlipResult(1); // Tails, i.e., enemy starts

        //Make sure 'yourTurn' values are set correctly:
        assertFalse(c1.getmPlayer().getYourTurn());
        assertTrue(c1.getmOpponent().getYourTurn());

        //Make sure mana values are set correctly, i.e. 5/5 for player:
        int expectedOpponentMana = 5;
        assertEquals(c1.getmPlayer().getCurrentMana(), expectedOpponentMana);
        assertEquals(c1.getmPlayer().getCurrentManaCap(), expectedOpponentMana);

        //Make sure decks are loaded correctly:
        int expectedPlayerCards = 4;
        int expectedEnemyCards = 3;
        assertEquals(c1.getmPlayerDeck().getmCardHand().size(), expectedPlayerCards);
        assertEquals(c1.getmEnemyDeck().getmCardHand().size(), expectedEnemyCards);
    }
}
