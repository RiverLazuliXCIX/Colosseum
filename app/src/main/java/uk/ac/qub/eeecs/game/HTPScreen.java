package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import android.graphics.Paint;

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
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class HTPScreen extends GameScreen {

    private PushButton backButton; // Back button to return from this screen
    private List<PushButton> pushButtons = new ArrayList<>(); // List for all push buttons to update all in a loop
    private GameObject mOptionBackground; // Add in the options background so that the screen isn't bland
    private LayerViewport mGameViewport;

    /**
     * Constructor for the How To Play screen
     *
     * @param g The game object

     */
    public HTPScreen(Game g) {
        // Set up the screen and load the assets
        super("HTPScreen", g);
        mGame.getAssetManager().loadAssets("txt/assets/OptionScreenAssets.JSON");

        // Take the x and y coordinates
        this.x = x;
        this.y = y;

        setupViewports();

        // Create the back button
        backButton = new PushButton(
                mGameViewport.getWidth() * 0.88f, mGameViewport.getHeight() * 0.10f,
                mGameViewport.getWidth() * 0.075f, mGameViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        pushButtons.add(backButton);

        // Load the background for the screen
        mOptionBackground = new GameObject(mGameViewport.getWidth()/ 2.0f,
                mGameViewport.getHeight()/ 2.0f, mGameViewport.getWidth(),
                mGameViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("OptionsBackground"), this);
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

    /**
     * Update method allowing for events on the screen to be handled
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Get the any events
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        // If there are events
        if (touchEvents.size() > 0) {
            // Update each of the push buttons (in case any more are added in future)
            for (PushButton button : pushButtons)
                button.update(elapsedTime);

            // If the back button is pressed, return to the options menu
            if (backButton.isPushTriggered()) {
                mGame.getScreenManager().changeScreenButton(new OptionsScreen(mGame));
            }
        }
    }

    private Paint textPaint = new Paint();
    private float x, y;

    /**
     * Draw method to allow for objects to be drawn onto the screen
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the background onto the screen
        mOptionBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        // Draw the back button
        for (PushButton button : pushButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Draw How to Play text onto the screen
        int screenHeight = graphics2D.getSurfaceHeight();
        float textHeight = screenHeight / 30.0f;
        textPaint.setTextSize(textHeight);
        // Placeholder text
        graphics2D.drawText("Colosseum: How to Play", mGameViewport.getWidth()*0.55f, mGameViewport.getHeight()*0.6f, textPaint);
    }
}
