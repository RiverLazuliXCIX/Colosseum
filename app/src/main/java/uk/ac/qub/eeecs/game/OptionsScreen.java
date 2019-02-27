package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.ui.FPSCounter; //Story P1
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

public class OptionsScreen extends GameScreen {

    private FPSCounter fpsCounter;
    private PushButton mBackButton, htpButton;
    private List<PushButton> mButtons = new ArrayList<>();
    private ToggleButton tEdgeCaseButton;
    private List<ToggleButton> tButtons = new ArrayList<>();
    private GameObject mOptionBackground;
    private LayerViewport mGameViewport;

    //Initialise TitleImage for displaying 'Options' Title
    private TitleImage mOptionsTitle;

    //PushButtons used to toggle the music on and off:
    private PushButton mMusicOn, mMusicOff;

    //PushButtons used to toggle sound effects on and off:
    private PushButton mSFXon, mSFXoff;

    //PushButtons used to toggle on and off the FPS counter:
    private PushButton mFPSon, mFPSoff;

    // Spacing that will be used to position the buttons:
    float SCREEN_WIDTH = mGame.getScreenWidth();
    float SCREEN_HEIGHT = mGame.getScreenWidth();

    //Information needed to set Music Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
    private SharedPreferences.Editor mPrefEditor = mGetPreference.edit();

    //Paint item that will be used to draw text
    private Paint mSettingsText;

    //Constructor
    public OptionsScreen(Game game) {
        super("OptionScreen", game);

        setupViewports();
        setUpOptionScrnObjects();
    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    private void setUpOptionScrnObjects() {
        //Load in a newly created "OptionScreenAssets.JSON" file which stores assets and their properties - Story O1 and O2 Scott Barham
        mGame.getAssetManager().loadAssets("txt/assets/OptionScreenAssets.JSON");

        // Spacing that will be used to position the buttons:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        //Set up the 'Options' Title Image:
        mOptionsTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "OptionsTitle",this);

        //Create the buttons necessary to set music preferences - Dearbhaile
        mMusicOn = new PushButton(
                spacingX * 3.55f, spacingY * 1.95f, spacingX*0.4f, spacingY*0.3f, "OptButtonOff",this);
        mMusicOff = new PushButton(
                spacingX * 3.55f, spacingY * 1.95f, spacingX*0.4f, spacingY*0.3f, "OptButtonOn",this);

        //Set up the fps counter - Scott Barham
        fpsCounter = new FPSCounter( mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f , this) { }; //Story P1 Scott Barham

        //Set up the remaining buttons - Scott Barham
        mBackButton = new PushButton(
                mGameViewport.getWidth() * 0.88f, mGameViewport.getHeight() * 0.10f,
                mGameViewport.getWidth() * 0.075f, mGameViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mButtons.add(mBackButton);

        tEdgeCaseButton = new ToggleButton(
                mGameViewport.getWidth() * 0.15f, mGameViewport.getHeight()*0.70f,
                mGameViewport.getWidth() * 0.095f, mGameViewport.getHeight() * 0.15f,
                "CoinFlip", "CoinFlipSelected", this);
        tButtons.add(tEdgeCaseButton);

        //Load in the background for the options menu Story O2
        mOptionBackground = new GameObject(mGameViewport.getWidth()/ 2.0f,
                mGameViewport.getHeight()/ 2.0f, mGameViewport.getWidth(),
                mGameViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("OptionsBackground"), this);

        //PAINT OBJECT:
        //Initialise Paint Objects I will use to draw text
        mSettingsText = new Paint();
        int screenHeight = mDefaultScreenViewport.height;
        float textHeight = screenHeight / 16.0f;
        mSettingsText.setTextSize(textHeight);
        mSettingsText.setColor(Color.BLACK);
        mSettingsText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
    }

    // Methods
    @Override
    public void update(ElapsedTime elapsedTime) {

        mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);

        // Process any touch events occurring since the last update
        Input input = mGame.getInput();
        fpsCounter.update(elapsedTime);
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button
            for (PushButton button : mButtons)
                button.update(elapsedTime);

            mMusicOff.update(elapsedTime);
            mMusicOn.update(elapsedTime);

            if (mBackButton.isPushTriggered()) { //Story O3, if the back button is pressed, go back to previous screen (menu screen)
                if(mGame.getScreenManager().previousScreen("PauseScreen")){
                    mGame.getScreenManager().changeScreenButton(new PauseMenuScreen(mGame));
                } else {
                    mGame.getScreenManager().changeScreenButton(new MenuScreen(mGame)); //Refactored return button
                }
            }

            if (mMusicOn.isPushTriggered()) {
                if (mGetPreference.getBoolean("Music", false)) {
                    mPrefEditor.putBoolean("Music", true);
                    mPrefEditor.commit();
                } else {
                    mPrefEditor.putBoolean("Music", false);
                    mPrefEditor.commit();
                }
            }

            if (mMusicOff.isPushTriggered()) {
                if (mGetPreference.getBoolean("Music", true)) {
                    mPrefEditor.putBoolean("Music", false);
                    mPrefEditor.commit();
                } else {
                    mPrefEditor.putBoolean("Music", true);
                    mPrefEditor.commit();
                }
            }

            //Testing a toggle button input to enable the edgecase coinflip
            for(ToggleButton button : tButtons)
                button.update(elapsedTime);

            if(tEdgeCaseButton.isToggledOn()) {
                colosseumDemoScreen.setEdgeCase(true);
            } else {
                colosseumDemoScreen.setEdgeCase(false);
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the background first of all - Story O2
        mOptionBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        mOptionsTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        fpsCounter.draw(elapsedTime,graphics2D); //Story P1

        //Then draw the back button in (and any other buttons if added later) - Story O3
        for (PushButton button : mButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        for (ToggleButton tbutton : tButtons)
            tbutton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        if(mGetPreference.getBoolean("Music", true)) {
            mMusicOff.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        } else {
            mMusicOn.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        }

        graphics2D.drawText("Background Music Playing:", SCREEN_WIDTH * 0.20f, SCREEN_HEIGHT * 0.18f, mSettingsText);
    }
}
