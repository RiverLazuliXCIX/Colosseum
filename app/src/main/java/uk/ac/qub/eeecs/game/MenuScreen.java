package uk.ac.qub.eeecs.game;

import android.graphics.Color;

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
import uk.ac.qub.eeecs.game.Colosseum.TitleImage;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

public class MenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////
    private GameObject mMenuBackground;
    private LayerViewport mMenuViewport;
    private TitleImage mMenuTitle;
    private PushButton mPlayGameButton;

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
        mGame.getAssetManager().loadAssets("txt/assets/MenuScreenAssets.JSON");

        // Create the background
        mMenuBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("ColMenuScreen"), this);

        // Create the title image
        mMenuTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "MenuText",this);

        //Create the push buttons
        mPlayGameButton = new PushButton(
                spacingX * 4, spacingY * 8.5f, spacingX*7.5f, spacingY*7.5f,
                "PlayButton", "PlayButton",this);
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

            mPlayGameButton.update(elapsedTime);

            if (mPlayGameButton.isPushTriggered()) {
                mGame.getScreenManager().removeScreen(this.getName());
                mGame.getScreenManager().addScreen(new colosseumDemoScreen(mGame));
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

        //Draw the buttons
        mPlayGameButton.draw(elapsedTime, graphics2D);
    }

}
