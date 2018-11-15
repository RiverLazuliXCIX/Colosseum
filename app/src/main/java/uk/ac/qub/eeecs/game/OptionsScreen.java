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
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class OptionsScreen extends GameScreen {

    private PushButton mBackButton;
    private List<PushButton> mButtons = new ArrayList<>();
    private GameObject mOptionBackground;
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

        // Load in the bitmap used for the back button, this could later be moved to a seperate "optionscreenassets.json" file
        //if it is needed for other options menu assets
        mGame.getAssetManager().loadAssets("txt/assets/OptionScreenAssets.JSON");

        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.88f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mButtons.add(mBackButton);

        mOptionBackground = new GameObject(mDefaultLayerViewport.getWidth()/ 2.0f,
                mDefaultLayerViewport.getHeight()/ 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), getGame()
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

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button
            for (PushButton button : mButtons)
                button.update(elapsedTime);

            if (mBackButton.isPushTriggered()) {
                mGame.getScreenManager().removeScreen(this);
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

        // Draw the background first of all
        mOptionBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        for (PushButton button : mButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}
