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
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

//Menu Screen implemented by Dearbhaile.

public class MenuScreen extends GameScreen {

    // Properties
    //Different objects required for this screen to function
    private GameObject mMenuBackground;
    private LayerViewport mMenuViewport;
    private TitleImage mMenuTitle;

    //Array List to hold the PushButtons
    private List<PushButton> mButtons = new ArrayList<>();

    //Push buttons for accessing different screens
    private PushButton mPlayGameButton, mOptionsButton, mQuitButton, mStatsButton, mHTPButton;

    //Asset manager, where necessary assets are stored:
    AssetManager mAssetManager = mGame.getAssetManager();

    //Screen manager, used throughout the MenuScreen Class:
    ScreenManager mScreenManager = mGame.getScreenManager();

    //Shared preferences for music:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    private SharedPreferences.Editor mPrefEditor = mGetPreferences.edit();

    //Declares the background music
    private Music mBgMusicMenu;

    // Constructors
    //Create the 'Menu Screen' screen
    public MenuScreen(Game game) {
        super("MenuScreen", game);

        //Set up viewports:
        setupViewports();

        //Set up Menu Screen Objects:
        setUpMenuScreenObjects();
    }


    public void setUpMenuScreenObjects() {
        // Load in the required assets from an external .JSON file
        mAssetManager.loadAssets("txt/assets/MenuScreenAssets.JSON");
        mBgMusicMenu = mAssetManager.getMusic("Menu-Music");

        // Spacing that will be used to position the Menu Screen Objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the background
        mMenuBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), mAssetManager.getBitmap("ColMenuScreen"), this);

        // Create the title image
        mMenuTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "MenuText",this);

        //PUSH BUTTONS:
        //Create the Play Game button
        mPlayGameButton = new PushButton(
                spacingX * 1.0f, spacingY * 1.5f, spacingX*1.5f, spacingY*1.5f,
                "PlayButton", "PlayButton-Select",this);
        mButtons.add(mPlayGameButton);
        //Create the Options button
        mOptionsButton = new PushButton(
                spacingX * 2.5f, spacingY * 1.5f, spacingX*1.4f, spacingY*1.4f,
                "cog2", "Options-Select",this);
        mButtons.add(mOptionsButton);
        //Create the Quit button
        mQuitButton = new PushButton(
                spacingX * 4.0f, spacingY * 1.55f, spacingX*1.5f, spacingY*1.5f, //Aligning buttons slightly
                "QuitBtn", "Quit-Select",this);
        mButtons.add(mQuitButton);
        mStatsButton = new PushButton(
                spacingX * 1.6f, spacingY * 0.4f, spacingX*0.7f, spacingY*0.6f,
                "statsButton", "statsButtonSelected",this);
        mButtons.add(mStatsButton);
        //Create the Quit button
        mHTPButton = new PushButton(
                spacingX * 3.2f, spacingY * 0.4f, spacingX*0.8f, spacingY*0.5f,
                "HTPButton", "HTPButtonSelected",this);
        mButtons.add(mHTPButton);
    }

    // Methods
    public void setupViewports() {
        // Setup the screen viewport to use the full screen:
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mMenuViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    public void stopBackgroundMusic() {
        while (mBgMusicMenu.isPlaying()) {
            mBgMusicMenu.stop();
        }
    }

    //'New Screen' button functions:
    public void newScreenButtonPress(GameScreen screen) {
        if(mGetPreferences.getBoolean("SFX", true)) {
            mAssetManager.getSound("ButtonPress").play();
        }

        mScreenManager.changeScreenButton(screen);

        if (mGetPreferences.getBoolean("Music", true)) {
            stopBackgroundMusic();
        }
    }

    //Method to update MenuScreen:
    @Override
    public void update(ElapsedTime elapsedTime) {
        //Check if music preferences are set to 'True', music plays. Otherwise, don't.
        mGetPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        if(mGetPreferences.getBoolean("Music", true)) {
            mBgMusicMenu.play();
        } else if (mGetPreferences.getBoolean("Music", false)) {
            stopBackgroundMusic();
        }

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            //Update Push Buttons, and carry out appropriate action
            for (PushButton button : mButtons)
                button.update(elapsedTime);

            if (mPlayGameButton.isPushTriggered()) {
                newScreenButtonPress(new colosseumDemoScreen(mGame));
            } else if (mOptionsButton.isPushTriggered()) {
                newScreenButtonPress(new OptionsScreen(mGame));
            } else if (mQuitButton.isPushTriggered()) {
                System.exit(0);
            } else if (mStatsButton.isPushTriggered()) {
                newScreenButtonPress(new StatisticsScreen(mGame));
            } else if (mHTPButton.isPushTriggered()) {
                newScreenButtonPress(new HTPScreen(mGame));
            }
        }
    }

    //Method to draw the MenuScreen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mMenuBackground.draw(elapsedTime, graphics2D, mMenuViewport,
                mDefaultScreenViewport);

        //Draw the title image
        mMenuTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw the buttons using enhanced for loops
        for (PushButton button : mButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}