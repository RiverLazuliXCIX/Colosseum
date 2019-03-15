package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.ui.FPSCounter; //Story P1
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class OptionsScreen extends GameScreen {

    private FPSCounter fpsCounter;
    private PushButton mBackButton;
    private List<PushButton> mButtons = new ArrayList<>();
    private ToggleButton tEdgeCaseButton;
    private List<ToggleButton> tButtons = new ArrayList<>();
    private GameObject mOptionBackground;
    private LayerViewport mGameViewport;

    //Initialise TitleImage for displaying 'Options' Title
    private TitleImage mOptionsTitle;

    //PushButtons used to toggle the music/SFX/FPS
    private PushButton mMusicOn, mMusicOff, mSFXon, mSFXoff, mFPSon, mFPSoff;

    //Information needed to set Music Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
    private SharedPreferences.Editor mPrefEditor = mGetPreference.edit();

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

        //Set up the FPS counter:
        fpsCounter = new FPSCounter( mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f , this) { };

        // Spacing that will be used to position the buttons:
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Spacing for title and buttons - they should be right in the middle of the screen:
        float mHalfWidth = mDefaultLayerViewport.getWidth() / 2.0f;
        float mButtonWidth = mDefaultLayerViewport.getWidth() / 4;
        float mButtonHeight = mDefaultLayerViewport.getHeight() / 5;

        //Set up the 'Options' Title Image:
        mOptionsTitle = new TitleImage(mHalfWidth, spacingY * 2.5f, mButtonWidth, spacingY/2.2f, "OptionsTitle",this);

        //Create the buttons necessary to set music preferences - Dearbhaile
        mMusicOn = new PushButton(
                mHalfWidth, spacingY * 1.95f, mButtonWidth, mButtonHeight, "OptButtonOn",this);
        mButtons.add(mMusicOn);
        mMusicOff = new PushButton(
                mHalfWidth, spacingY * 1.95f, mButtonWidth, mButtonHeight, "OptButtonOff",this);
        mButtons.add(mMusicOff);

        //Create the buttons necessary to set up SFX preferences - Dearbhaile
        mSFXon = new PushButton(
                mHalfWidth, spacingY * 1.30f, mButtonWidth, mButtonHeight, "SfxButtonOn", this);
        mButtons.add(mSFXon);
        mSFXoff = new PushButton(
                mHalfWidth, spacingY * 1.30f, mButtonWidth, mButtonHeight, "SfxButtonOff", this);
        mButtons.add(mSFXoff);

        //Create the buttons necessary to set up FPS preferences - Dearbhaile
        mFPSon = new PushButton(
                mHalfWidth, spacingY * 0.65f, mButtonWidth, mButtonHeight, "FpsButtonOn", this);
        mButtons.add(mFPSon);
        mFPSoff = new PushButton(
                mHalfWidth, spacingY * 0.65f, mButtonWidth, mButtonHeight, "FpsButtonOff", this);
        mButtons.add(mFPSoff);

        //Set up the remaining buttons:
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
    }

    //Method that can be used repeatedly when setting preferences in the Update method:
    public void setUpPreferences(PushButton pushButton, String dataType, boolean start, boolean finish) {
        if (pushButton.isPushTriggered()) {
            if (mGetPreference.getBoolean(dataType, start)) {
                mPrefEditor.putBoolean(dataType, finish);
                mPrefEditor.commit();
            } else {
                mPrefEditor.putBoolean(dataType, start);
                mPrefEditor.commit();
            }
        }
    }

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

            if (mBackButton.isPushTriggered()) { //Story O3, if the back button is pressed, go back to previous screen
                mGame.getScreenManager().previousScreen(); //Calls the "previousScreen" method to return to the screen listed below this in the stack (as this screen can be reached from different screens).
            }

            //Conditions for what happens when you click one of the 3 buttons.
            //Refactored using a method (above) to cut down ~40 lines of code. - Dearbhaile
            setUpPreferences(mMusicOn, "Music", false, true);
            setUpPreferences(mMusicOff, "Music", true, false);
            setUpPreferences(mSFXon, "SFX", false, true);
            setUpPreferences(mSFXoff, "SFX", true, false);
            setUpPreferences(mFPSon, "FPS", true, false);
            setUpPreferences(mFPSoff, "FPS", false, true);

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

    //Method that can be used repeatedly in the Draw method:
    public void drawPrefButtons(String prefType, PushButton buttonOne, PushButton buttonTwo, ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (mGetPreference.getBoolean(prefType, true)) {
            buttonOne.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        } else {
            buttonTwo.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the background first of all:
        mOptionBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        //Draw the Title 'Options' onscreen:
        mOptionsTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //If applicable, draw the FPS counter:
        if(mGetPreference.getBoolean("FPS", true)) {
            fpsCounter.draw(elapsedTime, graphics2D);
        }

        //Draw the back button (Push Button):
        mBackButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw the edge case button (Toggle Button):
        tEdgeCaseButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw preference buttons using refactored method - Dearbhaile
        drawPrefButtons("Music",  mMusicOff, mMusicOn, elapsedTime, graphics2D);
        drawPrefButtons("SFX", mSFXoff, mSFXon, elapsedTime, graphics2D);
        drawPrefButtons("FPS", mFPSoff, mFPSon, elapsedTime, graphics2D);
    }
}
