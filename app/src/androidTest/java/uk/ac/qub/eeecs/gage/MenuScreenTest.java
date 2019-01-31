package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;
import uk.ac.qub.eeecs.game.DemoGame;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.OptionsScreen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the screen 'MenuScreen' - Dearbhaile, Sprint 4.
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class) // Testing Framework
public class MenuScreenTest {
    private Context context;
    private DemoGame game;
    AssetManager assetManager;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        game = new DemoGame();
        assetManager = new AssetManager(game);
    }

    //**Tests on my 'playBackgroundMusic' method**
    @Test
    public void playBackgroundMusic_ValidData() {
        // This test is to ensure that if valid data is submitted, the music will play:
        assetManager.loadAndAddMusic("Menu-Music", "sound/Benedictus.mp3");

        //Attempt to play music, and then assert that "Music Playing" flag is true:
        assertTrue(assetManager.getMusic("Menu-Music").isPlaying());
    }

    @Test(expected = RuntimeException.class)
    public void playBackgroundMusic_InvalidData() {
        //This test is to ensure that when invalid data is submitted, when my program
        //is instructed to play said data, it throws up a Runtime exception instead:
        String assetName = "DoesNotExist", assetPath = "sound/ThisMusicIsFake.mp3";
        assetManager.loadAndAddMusic(assetName, assetPath);

        //Attempt to play music, and expect a Runtime exception:
        assetManager.getMusic(assetName).play();
    }

    //**Test on my stopBackgroundMusic method**
    @Test
    public void stopBackgroundMusic_StopCheck() {
        //This test is to ensure that when I call the 'stopBackgroundMusic'
        //method, the music does actually stop playing:
        String assetName = "Menu-Music", assetPath = "sound/Benedictus.mp3";
        assetManager.loadAndAddMusic(assetName, assetPath);

        //Method body:
        if (assetManager.getMusic(assetName).isPlaying()) {
            assetManager.getMusic("Menu-Music").stop();
        }

        //Check has music stopped:
        assertFalse(assetManager.getMusic(assetName).isPlaying());
    }
}