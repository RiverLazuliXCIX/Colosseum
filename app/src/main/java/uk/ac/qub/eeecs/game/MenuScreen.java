package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

public class MenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////
    private GameObject mMenuBackground;
    private LayerViewport mMenuViewport;
    private TitleImage mMenuTitle;

    //Buttons for accessing different screens
    private List<PushButton> mButtons = new ArrayList<>();
    private PushButton mPlayGameButton;
    private PushButton mOptionsButton;
    private PushButton mQuitButton;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public MenuScreen(Game game) {
        super("MenuScreen", game);

        setupViewports();

        // Define the spacing that will be used to position the buttons
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Load in the assets e.g. background image
        AssetManager assetManager = mGame.getAssetManager();
        assetManager.loadAssets("txt/assets/MenuScreenAssets.JSON");
        assetManager.loadAndAddSound("ButtonPress", "sound/ButtonSword.mp3");

        // Create the background
        mMenuBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), assetManager.getBitmap("ColMenuScreen"), this);

        // Create the title image
        mMenuTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "MenuText",this);

        //Create the push buttons
        //Create the Play Game button
        mPlayGameButton = new PushButton(
                spacingX * 1.0f, spacingY * 1.5f , spacingX*1.5f, spacingY*1.5f,
                "PlayButton", "PlayButton",this);
        mButtons.add(mPlayGameButton);
        //Create the Option button
        mOptionsButton = new PushButton(
                spacingX * 2.5f, spacingY * 1.5f , spacingX*1.2f, spacingY*1.2f,
                "cog2", "cog2selected",this);
        mButtons.add(mOptionsButton);
        //Create the Quit button
        mQuitButton = new PushButton(
                spacingX * 4.0f, spacingY * 1.5f , spacingX*1.4f, spacingY*1.4f,
                "QuitBtn", "QuitBtn",this);
        mButtons.add(mQuitButton);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mMenuViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
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
                mGame.getAssetManager().getSound("ButtonPress").play();
                mGame.getScreenManager().addScreen(new colosseumDemoScreen(mGame));
            } else if (mOptionsButton.isPushTriggered()) {
                mGame.getAssetManager().getSound("ButtonPress").play();
                mGame.getScreenManager().addScreen(new OptionsScreen(mGame));
            } else if (mQuitButton.isPushTriggered()) {
                mGame.getAssetManager().getSound("ButtonPress").play();

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
