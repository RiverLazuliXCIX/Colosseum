package uk.ac.qub.eeecs.gage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.io.FileIO;
import uk.ac.qub.eeecs.game.CardGameDemo.CardDemoScreen;
import uk.ac.qub.eeecs.game.DemoGame;


import static junit.framework.Assert.assertTrue;

/**
 * Example instrumentation tests, which will execute on an Anpdroid device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CardGameDemoTest {

    // /////////////////////////////////////////////////////////////////////////
    // Example Instrumented Test
    // /////////////////////////////////////////////////////////////////////////

    private Context context;
    private DemoGame game;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        setupGameManager();
    }

    private void setupGameManager() {
        // This method should be called to setup/reset a basic game instance

        game = new DemoGame();

        // The demo game isn't properly constructed until the onCreate and
        // onCreateView methods are called. We are going to partially constrct
        // the class using a bit of this code to add a functional file IO and
        // asset manager which game objects can use to load assets.
        game.mFileIO = new FileIO(context);
        AssetManager assetManager = new AssetManager(game);
        game.mAssetManager = assetManager;

        // We can also construct and add a ScreenManager
        game.mScreenManager = new ScreenManager(game);
    }

    //@Mock

    @Test
    public void test_checkCardNotOutOfBounds_InBound(){
        //Setup
        CardDemoScreen cardDemoScreen = new CardDemoScreen(game);
        ElapsedTime elapsedTime = new ElapsedTime();

        //Execute
        cardDemoScreen.checkCardNotOutOfBounds(elapsedTime);

        //Check outcome
        //Assert.assertTrue();

    }
}
