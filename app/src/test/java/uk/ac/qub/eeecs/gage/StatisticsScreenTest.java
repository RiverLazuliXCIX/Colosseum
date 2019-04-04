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
import uk.ac.qub.eeecs.game.TestClasses.StatisticsScreenForTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsScreenTest {

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
    public void Screen_ConstructorTest() {

        StatisticsScreenForTesting statsScreen = new StatisticsScreenForTesting(mGame);
        assertNotNull(statsScreen);

    }

    @Test
    public void TestGamesPlayedStats() {

        StatisticsScreenForTesting statsScreen = new StatisticsScreenForTesting(mGame);

        statsScreen.setTotalLosses(1.0);
        statsScreen.setTotalWins(2.0);
        statsScreen.gamePlayed();

        assertEquals(statsScreen.getTotalGamesPlayed(), 3.0, 0);
        assertEquals(statsScreen.getWinLossRatio(), 0.6,0.1);
        assertEquals(statsScreen.getWinPercent(),  66.6,0.1);

    }

    @Test
    public void TestResetPlayedStats() {

        StatisticsScreenForTesting statsScreen = new StatisticsScreenForTesting(mGame);

        statsScreen.setTotalLosses(1.0);
        statsScreen.setTotalWins(2.0);
        statsScreen.gamePlayed();

        statsScreen.resetStats();

        assertEquals(statsScreen.getTotalGamesPlayed(), 0.0, 0);
        assertEquals(statsScreen.getWinLossRatio(), 0.0,0);
        assertEquals(statsScreen.getWinPercent(), 0.0,0);

    }

    @Test
    public void TestGameStreakWinStats() {

        StatisticsScreenForTesting statsScreen = new StatisticsScreenForTesting(mGame);

        statsScreen.setMostRecentResult("win");
        statsScreen.setMostRecentResult("win");

        assertEquals(statsScreen.getGameStreak(), 2.0, 0);

    }

    @Test
    public void TestGameStreakLossWinStats() {

        StatisticsScreenForTesting statsScreen = new StatisticsScreenForTesting(mGame);

        statsScreen.setMostRecentResult("loss");
        statsScreen.setMostRecentResult("loss");
        statsScreen.setMostRecentResult("loss");
        statsScreen.setMostRecentResult("win");

        assertEquals(statsScreen.getGameStreak(), 1.0, 0);

    }

}
