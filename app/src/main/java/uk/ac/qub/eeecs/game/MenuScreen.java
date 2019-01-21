package uk.ac.qub.eeecs.game;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

//Menu Screen implemented by Dearbhaile.

public class MenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //Different objects required for this screen to function
    private GameObject mMenuBackground;
    private LayerViewport mMenuViewport;
    private TitleImage mMenuTitle;

    //Array List to hold the PushButtons
    private List<PushButton> mButtons = new ArrayList<>();
    //Push buttons for accessing different screens
    private PushButton mPlayGameButton;
    private PushButton mOptionsButton;
    private PushButton mQuitButton;

    //Asset manager, where necessary assets are stored
    AssetManager assetManager = mGame.getAssetManager();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the 'Menu Screen' screen
     *
     * @param game Game to which this screen belongs
     */

    public MenuScreen(Game game) {
        super("MenuScreen", game);

        // Load in the required assets from an external .JSON file
        assetManager.loadAssets("txt/assets/MenuScreenAssets.JSON");

        setupViewports();
        playBackgroundMusic();

        // Define the spacing that will be used to position the buttons
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the background
        mMenuBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), assetManager.getBitmap("ColMenuScreen"), this);

        // Create the title image
        mMenuTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "MenuText",this);

        //PUSH BUTTONS:
        //Create the Play Game button
        mPlayGameButton = new PushButton(
                spacingX * 1.0f, spacingY * 1.5f , spacingX*1.5f, spacingY*1.5f,
                "PlayButton", "PlayButton-Select",this);
        mButtons.add(mPlayGameButton);
        //Create the Options button
        mOptionsButton = new PushButton(
                spacingX * 2.5f, spacingY * 1.5f , spacingX*1.4f, spacingY*1.4f,
                "cog2", "Options-Select",this);
        mButtons.add(mOptionsButton);
        //Create the Quit button
        mQuitButton = new PushButton(
                spacingX * 4.0f, spacingY * 1.5f , spacingX*1.5f, spacingY*1.5f,
                "QuitBtn", "Quit-Select",this);
        mButtons.add(mQuitButton);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setupViewports() {
        // Setup the screen viewport to use the full screen:
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mMenuViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    //Method that controls background music
    private void playBackgroundMusic() {
        while (!assetManager.getMusic("Menu-Music").isPlaying()) {
            assetManager.getMusic("Menu-Music").play();
        }
        
    }

    //Method that controls button sounds
    private void playButtonSound() {
        mGame.getAssetManager().getMusic("ButtonPress").play();
        if (!mGame.getAssetManager().getMusic("ButtonPress").isPlaying()) {
            mGame.getAssetManager().getMusic("ButtonPress").stop();
        }
    }

    /**
     * Update the menu screen
     *
     * @param elapsedTime Elapsed time information
     */

    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            for (PushButton button : mButtons)
                button.update(elapsedTime);

            if (mPlayGameButton.isPushTriggered()) {
                playButtonSound();
                mGame.getScreenManager().addScreen(new colosseumDemoScreen(mGame));
            } else if (mOptionsButton.isPushTriggered()) {
                playButtonSound();
                mGame.getScreenManager().addScreen(new OptionsScreen(mGame));
            } else if (mQuitButton.isPushTriggered()) {
                playButtonSound();
                System.exit(0);
            }
        }
    }

    /**
     * Draw the menu screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
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

        //Draw the buttons using enhanced for loop
        for (PushButton button : mButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }
}