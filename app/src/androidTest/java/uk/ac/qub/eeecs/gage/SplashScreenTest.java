package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.DemoGame;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

 /* Tests for the screen 'SplashScreen'
  Performed by @Diarmuid Toal Sprint 5. */

@RunWith(AndroidJUnit4.class) // Testing Splash Screen
public class SplashScreenTest {
    private Context context;
    private DemoGame game;
    AssetManager assetManager;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        game = new DemoGame();
        assetManager = new AssetManager(game);
        game.mFileIO = new FileIO(context);
    }

    @Test
    public void loadAndAddSplashScreenBackground_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "SplashScreenBackground", assetPath = "img/ColosseumSplashScreen.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddSplashScreenBackground_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "SplashScreenBackground", assetPath = "img/ColosseumSplashScreen.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddSplashScreenTitle_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "SplashScreenTitle", assetPath = "img/Title.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
    }

    @Test
    public void loadAndAddSplashScreenTitle_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "SplashScreenTitle", assetPath = "img/Title.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddTouchToContinue_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "TouchToContinue", assetPath = "img/TouchToContinue.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
    }

    @Test
    public void loadAndAddTouchToContinue_AlreadyLoadedAsset_TestErrorReport()  {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "TouchToContinue", assetPath = "img/TouchToContinue.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddTeamLogo_ValidData()  {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "TeamLogo", assetPath = "img/red-panda-face.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
    }

    @Test
    public void loadAndAddTeamLogo_AlreadyLoadedAsset_TestErrorReport()  {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "TeamLogo", assetPath = "img/red-panda-face.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
    @Test
    public void goToMenuScreen() {
    }
}