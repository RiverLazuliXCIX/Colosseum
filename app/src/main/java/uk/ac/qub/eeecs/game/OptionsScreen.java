package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class OptionsScreen extends GameScreen {

    private PushButton mBackButton;
    private ToggleButton mColourToggle;
    private List<PushButton> mButtons = new ArrayList<>();
    private List<ToggleButton> mToggle = new ArrayList<>();
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

        //Load in a newly created "OptionScreenAssets.JSON" file which stores assets and their properties - Story O3 and O6 Scott Barham
        mGame.getAssetManager().loadAssets("txt/assets/OptionScreenAssets.JSON");

        // Load in the bitmap used for the back button, from a newly created "optionscreenassets.json" file - Story O6
        mBackButton = new PushButton(
                mDefaultLayerViewport.getWidth() * 0.88f, mDefaultLayerViewport.getHeight() * 0.10f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        mButtons.add(mBackButton);
        //Load in the background for the options menu Story O3
        mOptionBackground = new GameObject(mDefaultLayerViewport.getWidth()/ 2.0f,
                mDefaultLayerViewport.getHeight()/ 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("OptionsBackground"), this);
        //Load in the bitmap used for the Toggle Colour button
        mColourToggle = new ToggleButton(
                mDefaultLayerViewport.getWidth() * 0.85f, mDefaultLayerViewport.getHeight() * 0.85f,
                mDefaultLayerViewport.getWidth() * 0.075f, mDefaultLayerViewport.getHeight() * 0.10f,
                "ColourButton", "ColourButtonSelected", this);
        mToggle.add(mColourToggle);
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

        //Story O6
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {

            // Update each button
            for (PushButton button : mButtons)
                button.update(elapsedTime);

            for (ToggleButton button : mToggle)
                button.update(elapsedTime);

            if (mBackButton.isPushTriggered()) { //Story O6, if the back button is pressed, go back to previous screen (menu screen)
                mGame.getScreenManager().removeScreen(this);
            }

            if (mColourToggle.isToggledOn()) {
                ;
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

        // Draw the background first of all - Story O3
        mOptionBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        //Then draw the back button in (and any other buttons if added later) - Story O6
        for (PushButton button : mButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        for (ToggleButton button : mToggle)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }
}
