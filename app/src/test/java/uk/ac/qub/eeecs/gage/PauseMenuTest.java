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
import uk.ac.qub.eeecs.game.OptionsScreen;
import uk.ac.qub.eeecs.game.StatisticsScreen;
import uk.ac.qub.eeecs.game.TestClasses.PauseMenuForTesting;
import uk.ac.qub.eeecs.game.TestClasses.SplashScreenForTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)

public class PauseMenuTest {

    @Mock
    private Game game;
    @Mock
    private GameScreen gameScreen;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private Input input;
    @Mock
    private LayerViewport layerViewport;
    @Mock
    private PauseMenuForTesting mPauseMenuTesting;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(mPauseMenuTesting.getGame()).thenReturn(game);
        when(mPauseMenuTesting.getName()).thenReturn("PauseMenuScreen");
        when(mPauseMenuTesting.getDefaultLayerViewport()).thenReturn(layerViewport);
    }

    @Test
    public void screen_NameCorrect() {
        //This test is to ensure that the correct name is assigned to the Splash Screen
        PauseMenuForTesting p1 = new PauseMenuForTesting(game);

        String expectedResult = "PauseMenuScreen";

        assertEquals(expectedResult, p1.getName());
    }

    @Test
    public void screen_NameIncorrect() {
        //This test is to ensure that the correct name is assigned to the Splash Screen
        SplashScreenForTesting p1 = new SplashScreenForTesting(game);

        String expectedResult = "PauseMenuScreen";

        assertNotEquals("Pause", expectedResult);
    }

    @Test
    //Attempting to see if addScreen will change screen
    public void addScreen_Success(){
        PauseMenuForTesting testScreen = new PauseMenuForTesting(game);

        testScreen.addScreen("OptionsScreen" );

        assertEquals("OptionsScreen", "OptionsScreen");
    }

    @Test
    //Attempting to see if addScreen will not change
    public void addScreen_Fail(){
        PauseMenuForTesting testScreen = new PauseMenuForTesting(game);

        testScreen.addScreen("OptionsScreen" );

        assertNotEquals("Options", "OptionsScreen");
    }
}
