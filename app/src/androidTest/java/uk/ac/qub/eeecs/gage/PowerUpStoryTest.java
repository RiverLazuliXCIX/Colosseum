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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * PowerUp AssetStore Unit Tests.
 * <p>
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PowerUpStoryTest {

    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {

        context = InstrumentationRegistry.getTargetContext();
        game = new DemoGame();
        game.mFileIO = new FileIO(context);
    }

    //Using example tests given with my "PowerUp" bitmaps
    @Test
    public void loadAndAddPowerUp_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "PowerUp", assetPath = "img/PowerUp.PNG";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void loadAndAddPowerUpSelected_ValidData() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "PowerUpSelected", assetPath = "img/PowerUpSelected.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test(expected = RuntimeException.class)
    public void loadAndAddBitmap_InvalidData_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for an unsuccessful load
        String assetName = "PowerUp3", assetPath = "img/NoPowerUp.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
    }

    @Test
    public void loadAndAddPowerUp_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "PowerUp", assetPath = "img/PowerUp.PNG";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void getPowerUp_ValidGet_TestIsSuccess() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it can be extracted
        String assetName = "PowerUp", assetPath = "img/PowerUp.PNG";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertNotNull(assetManager.getBitmap(assetName));
    }
}
