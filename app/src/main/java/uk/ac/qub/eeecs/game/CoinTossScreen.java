package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

//CoinTossScreen, coded by Dearbhaile Walsh
public class CoinTossScreen extends GameScreen {

    // Properties
    //Different objects required for this screen to function
    private GameObject mCTSBackground;
    private LayerViewport mGameViewport;
    private TitleImage mCoinTossTitle;
    private FPSCounter fpsCounter;

    //Variables required for the time delay on this screen:
    private long mCoinToss_Timeout = 10000;
    private long mTimeOnCreate, mCurrentTime;
    private long mTimeRemaining;

    //Create PushButton necessary to skip animation
    PushButton mSkipButton;

    //Variables required for the message (lines 1 and 2) to display properly
    private int mCoinTossResult = 0;
    private String mCoinTossMsg1 = "";
    private String mCoinTossMsg2 = "";

    //Paint items that will be used to draw text
    private Paint mMessageText;
    private Paint mTimerText;

    //Information needed to set Music/SFX/FPS Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);

    // Constructor
    //Create the 'CoinTossScreen' screen
    public CoinTossScreen(Game game, int coinToss) {
        super("CoinTossScreen", game);
        mTimeOnCreate = System.currentTimeMillis();
        setupViewports();
        setUpCTSObjects();

        //This next line assigns the coinToss result from the colosseumDemoScreen
        //and assigns it for use in this screen to determine which message is displayed.
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

        //Set up the fps counter - Scott Barham
        fpsCounter = new FPSCounter( mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f , this) {};

        // Spacing that will be used to position the Coin Toss Screen Objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the title image
        mCoinTossTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "CTSTitle",this);

        //Create the Skip button
        mSkipButton = new PushButton(spacingX * 3.9f, spacingY * 2.5f, spacingX*0.8f, spacingY*0.8f,
                "SkipArrow", this);

        //PAINT OBJECTS:
        //Initialise Paint Objects I will use to draw text
        mMessageText = new Paint();
        int screenHeight = mDefaultScreenViewport.height;
        float textHeight = screenHeight / 20.0f;
        mMessageText.setTextSize(textHeight);
        mMessageText.setColor(Color.BLACK);
        mMessageText.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        mTimerText = new Paint();
        float smallTextHeight = screenHeight / 24.0f;
        mTimerText.setTextSize(smallTextHeight);
        mTimerText.setColor(Color.BLACK);
        mTimerText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
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
            mCoinTossMsg1 = "The coin landed on heads! You get to play first.";
            mCoinTossMsg2 = "The other player draws 4 cards, and gets 1 additional mana.";
        }
        else if (mCoinTossResult == 1) {
            mCoinTossMsg1 = "The coin landed on tails! The enemy plays first.";
            mCoinTossMsg2 = "You draw an extra card and additional mana for your troubles.";
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        mCurrentTime = System.currentTimeMillis();
        mTimeRemaining = 10 - ((mCurrentTime - mTimeOnCreate)/1000);

        if (mCurrentTime - mTimeOnCreate >= mCoinToss_Timeout) {
            mGame.getScreenManager().getCurrentScreen().dispose();
            mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mGame));
        }

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            mSkipButton.update(elapsedTime);

            //If the 'skip animation' button is pressed, then go straight to game:
            if (mSkipButton.isPushTriggered()) {
                mCoinToss_Timeout = 0;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen
        graphics2D.clear(Color.WHITE);

        //Draw the background
        mCTSBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        //Draw the title image
        mCoinTossTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw the skip button
        mSkipButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Spacing that will be used to position the Paint object:
        float SCREEN_WIDTH = mGame.getScreenWidth();
        float SCREEN_HEIGHT = mGame.getScreenWidth();

        if (mCurrentTime - mTimeOnCreate >= 3000) {
            graphics2D.drawText(mCoinTossMsg1, SCREEN_WIDTH * 0.24f, SCREEN_HEIGHT * 0.32f, mMessageText);
            graphics2D.drawText(mCoinTossMsg2, SCREEN_WIDTH * 0.18f, SCREEN_HEIGHT * 0.38f, mMessageText);

            graphics2D.drawText("Game will begin in " + mTimeRemaining + " seconds...", SCREEN_WIDTH * 0.46f, SCREEN_HEIGHT * 0.46f, mTimerText);
        }

        if(mGetPreference.getBoolean("FPS", true)) {
            fpsCounter.draw(elapsedTime, graphics2D);
        }
    }

    //Getters and setters:
    public int getmCoinTossResult() { return this.mCoinTossResult; }
    public String getmCoinTossMsg1() { return this.mCoinTossMsg1; }
    public String getmCoinTossMsg2() { return this.mCoinTossMsg2; }
}
