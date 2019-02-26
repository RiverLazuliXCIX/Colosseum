package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

public class CoinTossScreen extends GameScreen {

    // Properties
    //Different objects required for this screen to function
    private LayerViewport mCTSViewPort;
    private GameObject mCTSBackground;
    private LayerViewport mGameViewport;
    private TitleImage mCoinTossTitle;

    //Variables required for the time delay on this screen:
    final private long mCOINTOSS_TIMEOUT = 10000;
    private long mTimeOnCreate, mCurrentTime;

    //Variables required for the message (lines 1 and 2) to display properly
    private int mCoinTossResult = 0;
    private String mCoinTossMsg1 = "";
    private String mCoinTossMsg2 = "";


    // Constructor
    //Create the 'CoinTossScreen' screen
    public CoinTossScreen(Game game, int coinToss) {
        super("CoinTossScreen", game);
        mTimeOnCreate = System.currentTimeMillis();
        setupViewports();
        setUpCTSObjects();
        this.mCoinTossResult = coinToss;
        chooseTextToDisplay();
    }

    // Methods
    public void setUpCTSObjects() {
        mGame.getAssetManager().loadAssets("txt/assets/CoinTossAssets.JSON");

        mCTSBackground = new GameObject(mGameViewport.getWidth()/ 2.0f,
                mGameViewport.getHeight()/ 2.0f, mGameViewport.getWidth(),
                mGameViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("CTSBackground"), this);

        // Spacing that will be used to position the Coin Toss Screen Objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the title image
        mCoinTossTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "CTSTitle",this);

    }

    public void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    public void chooseTextToDisplay() {
        if (mCoinTossResult == 0) {
            mCoinTossMsg1 = "The coin landed on heads!";
            mCoinTossMsg2 = "You start first. The other player gains a bonus card and 1 mana.";
        }
        else if (mCoinTossResult == 1) {
            mCoinTossMsg1 = "The coin landed on tails!";
            mCoinTossMsg2 = "The enemy starts first. You gain a bonus card and 1 mana.";
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        mCurrentTime = System.currentTimeMillis();
        if (mCurrentTime - mTimeOnCreate >= mCOINTOSS_TIMEOUT) {
            mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mGame));
        }

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {


        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen
        graphics2D.clear(Color.WHITE);

        //Draw the background
        mCTSBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        if (mCurrentTime - mTimeOnCreate >= 3000) {

        }

        //Draw the title image
        mCoinTossTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }
}
