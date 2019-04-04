package uk.ac.qub.eeecs.game.TestClasses;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.MenuScreen;

public class SplashScreenForTesting extends GameScreen {


    // Properties
    final private long SPLASH_TIMEOUT = 5000;
    private long timeOnCreate, currentTime;

    public SplashScreenForTesting(Game game) {
        super("SplashScreen", game);

        timeOnCreate = System.currentTimeMillis();
        setupViewports();
    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }


    public void nextScreenCountadown() {
        //Get current time and check for timeout
        //Based upon the splash screen from Ragnarok past example
        currentTime = System.currentTimeMillis();
        if (currentTime - timeOnCreate >= SPLASH_TIMEOUT) {
            goToMenuScreen();
        }
    }


    public void update(ElapsedTime elapsedTime) {
        nextScreenCountadown();

        // Process any touch events occurring since the update
        Input touchInput = mGame.getInput();
        List<TouchEvent> touchEvents = touchInput.getTouchEvents();
        if (touchEvents.size() > 0) {
             goToMenuScreen();
        }
    }

    public void goToMenuScreen() {
        mGame.getScreenManager().changeScreenButton(new MenuScreen(mGame));
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }


    public long getTimeOnCreate() {
        return timeOnCreate;
    }

    public void setTimeOnCreate(long timeOnCreate) {
        this.timeOnCreate = timeOnCreate;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
