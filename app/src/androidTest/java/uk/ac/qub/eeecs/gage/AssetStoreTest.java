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
 * Example AssetStore Unit Tests.
 * <p>
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AssetStoreTest {

    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {

        context = InstrumentationRegistry.getTargetContext();
        game = new DemoGame();
        game.mFileIO = new FileIO(context);
    }

    @Test
    public void loadAndAddBitmap_ValidData_TestIsSuccess() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for successful load
        String assetName = "Spaceship1", assetPath = "img/Spaceship1.png";
        assertTrue(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test(expected = RuntimeException.class)
    public void loadAndAddBitmap_InvalidData_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Check for an unsuccessful load
        String assetName = "Spaceship1", assetPath = "img/Doesnotexist.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
    }

    @Test
    public void loadAndAddBitmap_AlreadyLoadedAsset_TestErrorReport() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it cannot be added a second time
        String assetName = "Spaceship1", assetPath = "img/Spaceship1.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertFalse(assetManager.loadAndAddBitmap(assetName, assetPath));
    }

    @Test
    public void getBitmap_ValidGet_TestIsSuccess() {
        // Build asset store
        AssetManager assetManager = new AssetManager(game);
        // Load a bitmap and test it can be extracted
        String assetName = "Spaceship1", assetPath = "img/Spaceship1.png";
        assetManager.loadAndAddBitmap(assetName, assetPath);
        assertNotNull(assetManager.getBitmap(assetName));
    }
}
