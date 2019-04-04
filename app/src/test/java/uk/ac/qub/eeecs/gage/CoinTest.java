package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.TestClasses.CoinScreenForTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoinTest {

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

    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);

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
    public void Coin_ConstructorTest() {
        String coinTossResult = "Heads";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        assertNotNull(coin1);
    }

    @Test
    public void Coin_SetupTest() {
        String coinTossResult = "Heads";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        assertEquals(250f, coin1.position.x, 0);
        assertEquals(250f, coin1.position.y, 0);
        assertEquals(100f, coin1.getBound().getWidth(), 0);
        assertEquals(100f, coin1.getBound().getHeight(), 0);
    }

    @Test
    public void Coin_AnimationHeadsSetupTest() {
        String coinTossResult = "Heads";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        coin1.setupCoinAnimation(coinTossResult);
        assertEquals(coin1.getFramesLeft(), 96);
    }

    @Test
    public void Coin_AnimationTailsSetupTest() {
        String coinTossResult = "Tails";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        coin1.setupCoinAnimation(coinTossResult);
        assertEquals(coin1.getFramesLeft(), 80);
    }

    @Test
    public void Coin_AnimationEdgeSetupTest() {
        String coinTossResult = "Edge";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        coin1.setupCoinAnimation(coinTossResult);
        assertEquals(coin1.getFramesLeft(), 88);
    }


    @Test
    public void Coin_AnimationSetupInvalidTest() {
        String coinTossResult = "Invalid";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        coin1.setupCoinAnimation(coinTossResult);
        assertEquals(coin1.getFramesLeft(), 0);
    }

    @Test
    public void Coin_ProgressingFramesTest() {
        String coinTossResult = "Heads";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        coin1.setupCoinAnimation(coinTossResult);
        coin1.coinAnimation(); //One frame has been completed, decrease value

        assertEquals(coin1.getFramesLeft(), 95);
        assertEquals(coin1.getCurrentCoinFrame(),2 );
    }

    @Test
    public void Coin_FinishedAnimationFramesTest() {
        String coinTossResult = "Heads";
        CoinScreenForTesting coin1 = new CoinScreenForTesting(250.0f,250.0f,100.0f,100.0f, gameScreen, coinTossResult);
        coin1.setupCoinAnimation(coinTossResult);
       for(int i =0; i <96; i++) {
           coin1.coinAnimation(); //One frame has been completed, decrease value
       }
        assertEquals(coin1.getFramesLeft(), 0); //If we completed the animation
        assertEquals(coin1.getCurrentCoinFrame(),1 ); //If it landed on the heads frame

    }


}

