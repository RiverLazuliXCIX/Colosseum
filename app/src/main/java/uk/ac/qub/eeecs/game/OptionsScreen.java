package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
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

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the 'Options Screen' screen
     *
     * @param game Game to which this screen belongs
     */

    public OptionsScreen(Game game) {
        super("OptionScreen", game);

        setupViewports();
        setUpGameObjects();
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

    private void setUpGameObjects() {
        //Load in a newly created "OptionScreenAssets.JSON" file which stores assets and their properties - Story O1 and O2 Scott Barham
        mGame.getAssetManager().loadAssets("txt/assets/OptionScreenAssets.JSON");

        fpsCounter = new FPSCounter( mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f , this) { }; //Story P1 Scott Barham
        // Load in the bitmap used for the back button, from a newly created "optionscreenassets.json" file - Story O3
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

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();
        fpsCounter.update(elapsedTime); //Story P1
        //Story O3
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button
            for (PushButton button : mButtons)
                button.update(elapsedTime);

            if (mBackButton.isPushTriggered()) { //Story O3, if the back button is pressed, go back to previous screen (menu screen)
                if(mGame.getScreenManager().previousScreen("PauseScreen")){
                    mGame.getScreenManager().changeScreenButton(new PauseMenuScreen(mGame));
                } else {
                    mGame.getScreenManager().changeScreenButton(new MenuScreen(mGame)); //Refactored return button
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

    /**
     * Draw the 'Options Screen' screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the background first of all - Story O2
        mOptionBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        fpsCounter.draw(elapsedTime,graphics2D); //Story P1

        //Then draw the back button in (and any other buttons if added later) - Story O3
        for (PushButton button : mButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        for (ToggleButton tbutton : tButtons)
            tbutton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}
