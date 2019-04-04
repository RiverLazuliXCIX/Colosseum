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

/* Tests for the screen 'PauseMenuScreen'
 Performed by @Diarmuid Toal Sprint 5. */

@RunWith(AndroidJUnit4.class) // Testing Pause Screen
public class PauseScreenAssetTest {

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

    //
    // Testing if the assets have successfully loaded
    //

    @Test
    public void loadAndAddPauseMenuBitmap_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Pause", assetPath = "img/PauseMenu.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddResumeButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Resume", assetPath = "img/Resume.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddResumeSelectedButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "ResumeSelected", assetPath = "img/ResumeSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddOptionsButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Options", assetPath = "img/PaOptions.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddOptionsSelectedButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "OptionsSelected", assetPath = "img/PaOptionsSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddMainMenuButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "MainMenu", assetPath = "img/MainMenu.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddMainMenuSelectedButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "MainMenuSelected", assetPath = "img/MainMenuSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddConcedeButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Concede", assetPath = "img/Concede.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddConcedeSelectedButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "ConcedeSelected", assetPath = "img/ConcedeSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddHowToPlayButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "HTPButton", assetPath = "img/HTPButton.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddHhowToPlaySelectedButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "HTPButtonSelected", assetPath = "img/HTPButtonSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddStatsButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "statsButton", assetPath = "img/statsButton.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddStatsSelectedButton_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "statsButtonSelected", assetPath = "img/statsButtonSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    //
    // Testing if the assets is already loaded in and unable to be added a second time
    //
    
    @Test
    public void loadAndAddPauseMenu_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "Pause", assetPath = "img/PauseMenu.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddResumeButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Resume", assetPath = "img/Resume.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddResumeSelectedButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "ResumeSelected", assetPath = "img/ResumeSelected.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddOptionsButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Options", assetPath = "img/PaOptions.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddOptionsSelectedButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "OptionsSelected", assetPath = "img/PaOptionsSelected.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddMainMenuButtonv() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "MainMenu", assetPath = "img/MainMenu.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddMainMenuSelectedButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "MainMenuSelected", assetPath = "img/MainMenuSelected.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddConcedeButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Concede", assetPath = "img/Concede.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddConcedeSelectedButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "ConcedeSelected", assetPath = "img/ConcedeSelected.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddHowToPlayButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "HTPButton", assetPath = "img/HTPButton.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddHhowToPlaySelectedButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "HTPButtonSelected", assetPath = "img/HTPButtonSelected.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddStatsButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "statsButton", assetPath = "img/statsButton.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddStatsSelectedButton_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "statsButtonSelected", assetPath = "img/statsButtonSelected.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }
}

