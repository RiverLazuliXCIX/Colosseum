package uk.ac.qub.eeecs.game;

import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.world.LayerViewport;


public class SplashScreen extends GameScreen {

    // Properties
    final private long SPLASH_TIMEOUT = 8000;
    private long timeOnCreate, currentTime;
    private GameObject mSplashBackground;
    private LayerViewport mSplashLayerViewport;
    private TitleImage mMenuTitle;
    private int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
    private int spacingY = (int) mDefaultLayerViewport.getHeight() / 4;

    /**
     * Creating the 'Splash Screen' screen
     * @param game Game to which this screen belongs
     */

    public SplashScreen(Game game) {
        super("SplashScreen", game);

        timeOnCreate = System.currentTimeMillis();
        setupViewports();
        setUpSplashScreeneObjects();
    }

    //Methods
    private void setUpSplashScreeneObjects() {
        // Load in the assets used by the steering demo
        mGame.getAssetManager().loadAssets("txt/assets/SplashScreen.JSON");

        // Create the background
        mSplashBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("SplashScreenBackground"), this);

        // Create the title image for the splash screens
        mMenuTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "SplashScreenTitle",this);
    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mSplashLayerViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    /**
     * Update the splash screen
     * @param elapsedTime Elapsed time information
     */
    @Override

    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        //Get current time and check for timeout
        currentTime = System.currentTimeMillis();
        if (currentTime - timeOnCreate >= SPLASH_TIMEOUT) {
            goToMenuScreen();
        }
        // Process any touch events occurring since the update
        Input touchInput = mGame.getInput();
        List<TouchEvent> touchEvents = touchInput.getTouchEvents();
        if (touchEvents.size() > 0) {
            goToMenuScreen();
        }
    }

    public void goToMenuScreen() {
        mGame.getScreenManager().removeScreen(this.getName());
        mGame.getScreenManager().addScreen(new MenuScreen(mGame));
    }

    /**
     * Draw the 'Splash Screen' screen
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mSplashBackground.draw(elapsedTime, graphics2D, mSplashLayerViewport,
                mDefaultScreenViewport);

        mMenuTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

    }

}
