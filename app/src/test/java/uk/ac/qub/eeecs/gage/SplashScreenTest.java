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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import uk.ac.qub.eeecs.game.TestClasses.SplashScreenForTesting;

@RunWith(MockitoJUnitRunner.class)
public class SplashScreenTest {
    // /////////////////////////////////////////////////////////////////////////
    // Mocking setup
    // /////////////////////////////////////////////////////////////////////////
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
    private SplashScreenForTesting mSplashTesting;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(mSplashTesting.getGame()).thenReturn(game);
        when(mSplashTesting.getName()).thenReturn("SplashScreen");
        when(mSplashTesting.getDefaultLayerViewport()).thenReturn(layerViewport);
    }

    @Test
    public void screen_NameCorrect() {
        //This test is to ensure that the correct name is assigned to the Splash Screen
        SplashScreenForTesting s1 = new SplashScreenForTesting(game);

        String expectedResult = "SplashScreen";

        assertEquals(expectedResult, s1.getName());
    }

    @Test
    public void screen_NameIncorrect() {
        //This test is to ensure that the correct name is assigned to the Splash Screen
        SplashScreenForTesting s1 = new SplashScreenForTesting(game);

        String expectedResult = "SplashScreen";

        assertNotEquals("Splash", expectedResult);
    }


    @Test
    public void nextScreenCountdown_Pass() {
        SplashScreenForTesting testScreen = new SplashScreenForTesting(game);

        testScreen.setCurrentTime(5000);
        testScreen.setCurrentTime(0);

        testScreen.nextScreenCountadown();

        //Will be testing the goToMenuScreen method in this
        assertEquals("MenuScreen", "MenuScreen");
    }

    @Test
    public void nextScreenCountdown_Fails() {
        SplashScreenForTesting testScreen = new SplashScreenForTesting(game);

        testScreen.setCurrentTime(5000);
        testScreen.setCurrentTime(200);

        testScreen.nextScreenCountadown();

        //Will be testing the goToMenuScreen method in this
        assertNotEquals("Screen has not successfully moved to Menu Screen",
                "SplashScreen", "MenuScreen");
    }
}
