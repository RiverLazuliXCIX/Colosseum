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
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.Turn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TurnClassTest {

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
    // This is a test on the constructor, to see when it is called, is an object created:
    //

    @Test
    public void setUpTurnConstructor() {
        Turn newTurn = new Turn();

        assertNotNull(newTurn);
    }

    //
    // This test is to see, when the constructor is called, is the mTurnNum variable
    // initially set to 1, as it is supposed to:
    //

    @Test
    public void initialTurn_SetCorrectly() {
        Turn newTurn = new Turn();

        int expectedResult = 1;

        assertEquals(newTurn.getmTurnNum(), expectedResult);
    }

    //
    // This test is on the 'incrementTurnNum()' method to see does it work as expected:
    //

    @Test
    public void incrementTurnNum() {
        Turn newTurn = new Turn();

        newTurn.incrementTurnNum();

        int expectedResult = 2;

        assertEquals(newTurn.getmTurnNum(), expectedResult);
    }

    //
    // This test is to check that when the 'newTurnFunc()' method is called, and the current player mana
    // is lower than the mana cap, ie less than 10, do the mana & mana cap values increase by 1:
    //

    @Test
    public void newTurnFunc_Success() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");

        newPlayer.setCurrentMana(5);
        newPlayer.setCurrentManaCap(5);

        newTurn.newTurnFunc(newPlayer);

        int expectedResult = 6;

        assertEquals(newPlayer.getCurrentMana(), expectedResult);
        assertEquals(newPlayer.getCurrentManaCap(), expectedResult);
    }

    //
    // In this test, the current mana/mana cap values are set to 10, ie the mana cap value.
    // The mana cap, upon increase, should not go up, and thus the values should still be 10:
    //

    @Test
    public void newTurnFunc_ValueOverManaCap() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");

        newPlayer.setCurrentMana(10);
        newPlayer.setCurrentManaCap(10);

        newTurn.newTurnFunc(newPlayer);

        int expectedResult = 10;

        assertEquals(newPlayer.getCurrentMana(), expectedResult);
        assertEquals(newPlayer.getCurrentManaCap(), expectedResult);
    }

    //
    // The following test is on the 'setUpStats_PlayerStarts()' method, which is supposed to
    // flip which player's turn it is. This test is to see does it change the appropriate values correctly:
    //
    @Test
    public void setUpStats_PlayerStarts_CheckTurnsCorrect() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);
        AIOpponent enemyPlayer = new AIOpponent(mGameScreen, "Sagira");
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);

        newTurn.setUpStats_PlayerStarts(newPlayer, playerDeck, enemyPlayer, enemyDeck);

        assertTrue(newPlayer.getYourTurn());
        assertFalse(enemyPlayer.getYourTurn());
    }

    //
    // The following test is on the 'setUpStats_PlayerStarts()' method, which is supposed to
    // give an extra mana (ie totalling 5 mana points) to whoever does not start first.
    // This test is to see does it change the appropriate values correctly:
    //
    @Test
    public void setUpStats_PlayerStarts_CheckManaCorrect() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);
        AIOpponent enemyPlayer = new AIOpponent(mGameScreen, "Sagira");
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);

        newTurn.setUpStats_PlayerStarts(newPlayer, playerDeck, enemyPlayer, enemyDeck);

        int expectedEnemyMana = 5;
        int expectedEnemyManaCap = 5;

        assertEquals(enemyPlayer.getCurrentMana(), expectedEnemyMana);
        assertEquals(enemyPlayer.getCurrentManaCap(), expectedEnemyManaCap);
    }

    //
    // The following test is on the 'setUpStats_PlayerStarts()' method, which is supposed to
    // give an extra card (ie, totalling 4 cards) to whomever starts first, and give the other
    // player just 3 cards. This test is to see does it change the appropriate values correctly:
    //
    @Test
    public void setUpStats_PlayerStarts_CheckCardNumsCorrect() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);
        AIOpponent enemyPlayer = new AIOpponent(mGameScreen, "Sagira");
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);

        newTurn.setUpStats_PlayerStarts(newPlayer, playerDeck, enemyPlayer, enemyDeck);

        int expectedPlayerCardNums = 3;
        int expectedEnemyCardNums = 4;

        assertEquals(playerDeck.getmCardHand().size(), expectedPlayerCardNums);
        assertEquals(enemyDeck.getmCardHand().size(), expectedEnemyCardNums);
    }

    //
    // The following test is on the 'setUpStats_EnemyStarts()' method, which is supposed to
    // flip which player's turn it is. This test is to see does it change the appropriate values correctly:
    //
    @Test
    public void setUpStats_EnemyStarts_CheckTurnsCorrect() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        HandRegion handRegion = new HandRegion(10,20,20,10);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);
        AIOpponent enemyPlayer = new AIOpponent(mGameScreen, "Sagira");
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);

        newTurn.setUpStats_EnemyStarts(newPlayer, playerDeck, enemyPlayer, enemyDeck);

        assertTrue(enemyPlayer.getYourTurn());
        assertFalse(newPlayer.getYourTurn());
    }

    //
    // The following test is on the 'setUpStats_EnemyStarts()' method, which is supposed to
    // give an extra mana (ie totalling 5 mana points) to whoever does not start first.
    // This test is to see does it change the appropriate values correctly:
    //
    @Test
    public void setUpStats_EnemyStarts_CheckManaCorrect() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);
        AIOpponent enemyPlayer = new AIOpponent(mGameScreen, "Sagira");
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);

        newTurn.setUpStats_EnemyStarts(newPlayer, playerDeck, enemyPlayer, enemyDeck);

        int expectedPlayerMana = 5;
        int expectedPlayerManaCap = 5;

        assertEquals(newPlayer.getCurrentMana(), expectedPlayerMana);
        assertEquals(newPlayer.getCurrentManaCap(), expectedPlayerManaCap);
    }

    //
    // The following test is on the 'setUpStats_EnemyStarts()' method, which is supposed to
    // give an extra card (ie, totalling 4 cards) to whomever starts first, and give the other
    // player just 3 cards. This test is to see does it change the appropriate values correctly:
    //
    @Test
    public void setUpStats_EnemyStarts_CheckCardNumsCorrect() {
        Turn newTurn = new Turn();
        Player newPlayer = new Player(mGameScreen, "Brutalus");
        HandRegion handRegion = new HandRegion(10, 20, 20, 10);
        CardDeck playerDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);
        AIOpponent enemyPlayer = new AIOpponent(mGameScreen, "Sagira");
        CardDeck enemyDeck = new CardDeck(1, "aCardDeck", mGameScreen, false, handRegion);

        newTurn.setUpStats_EnemyStarts(newPlayer, playerDeck, enemyPlayer, enemyDeck);

        int expectedPlayerCardNums = 4;
        int expectedEnemyCardNums = 3;

        assertEquals(enemyDeck.getmCardHand().size(), expectedEnemyCardNums);
        assertEquals(playerDeck.getmCardHand().size(), expectedPlayerCardNums);
    }

    //
    // This test is on the getter 'getmTurnNum()' to ensure it works:
    //
    @Test
    public void getmTurnNumber_WorksCorrectly() {
        Turn newTurn = new Turn();

        for (int i = 0; i < 5; i++) {
            newTurn.incrementTurnNum();
        }

        int expectedValue = 6;

        assertEquals(newTurn.getmTurnNum(), expectedValue);
    }
}