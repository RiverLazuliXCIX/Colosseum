package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

//FatigueScreen, coded by Dearbhaile
public class FatigueScreen extends GameScreen {

    //Variables required for the Fatigue Screen:
    final private long FATIGUE_TIMEOUT = 3000;
    private long mTimeOnCreate, mCurrentTime;
    private long mTimeRemaining;
    private int mDamageTaken;

    //Objects required for the appearance of the Fatigue Screen:
    private LayerViewport mFatigueScreenViewport;
    private TitleImage mOhNoText;
    private Paint mText;
    private Paint mTimerText;

    FPSCounter mFPSCounter;

    //Information needed to set Music/SFX/FPS Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
    private SharedPreferences.Editor mPrefEditor = mGetPreference.edit();

    public FatigueScreen(Game game, int damageToTake) {
        super("FatigueScreen", game);
        mTimeOnCreate = System.currentTimeMillis();
        this.mDamageTaken = damageToTake;

        setupViewports();
        setUpFatigueScreenObjects();
    }

    private void setUpFatigueScreenObjects() {
        mGame.getAssetManager().loadAssets("txt/assets/FatigueScreenAssets.JSON");

        // Spacing that will be used to position the Fatigue Screen Objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the title image
        mOhNoText = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.0f, spacingX*1.5f, spacingY/2.2f, "OhNoImg",this);

        //Set up the fps counter
        mFPSCounter = new FPSCounter( mFatigueScreenViewport.getWidth() * 0.50f, mFatigueScreenViewport.getHeight() * 0.20f , this) { }; //Story P1 Scott Barham

        //PAINT OBJECT:
        //Initialise Paint Objects I will use to draw text
        mText = new Paint();
        int screenHeight = mDefaultScreenViewport.height;
        float textHeight = screenHeight / 20.0f;
        mText.setTextSize(textHeight);
        mText.setColor(Color.BLACK);
        mText.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        mTimerText = new Paint();
        float smallTextHeight = screenHeight / 24.0f;
        mTimerText.setTextSize(smallTextHeight);
        mTimerText.setColor(Color.BLACK);
        mTimerText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mFatigueScreenViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        mCurrentTime = System.currentTimeMillis();
        mTimeRemaining = 3 - ((mCurrentTime - mTimeOnCreate)/1000);

        //Get current time and check for timeout
        mCurrentTime = System.currentTimeMillis();
        if (mCurrentTime - mTimeOnCreate >= FATIGUE_TIMEOUT) {
            mGame.getScreenManager().previousScreen();
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //Clear the screen
        graphics2D.clear(Color.WHITE);

        //Draw the title image
        mOhNoText.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Spacing that will be used to position the Paint object:
        float SCREEN_WIDTH = mGame.getScreenWidth();
        float SCREEN_HEIGHT = mGame.getScreenWidth();

        //Draw the text detailing fatigue function
        graphics2D.drawText("You have run out of cards. You take " + mDamageTaken + " damage this turn.", SCREEN_WIDTH * 0.2f, SCREEN_HEIGHT * 0.28f, mText);

        //Draw the text showing the timer
        graphics2D.drawText("Game will resume in " + mTimeRemaining + " seconds...", SCREEN_WIDTH * 0.46f, SCREEN_HEIGHT * 0.46f, mTimerText);

        if(mGetPreference.getBoolean("FPS", true)) {
            mFPSCounter.draw(elapsedTime, graphics2D);
        }
    }

    //Getters and setters:
    public int getmDamageTaken() { return this.mDamageTaken; }
}
