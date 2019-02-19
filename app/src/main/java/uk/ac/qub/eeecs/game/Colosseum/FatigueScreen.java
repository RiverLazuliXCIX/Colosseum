package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Color;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class FatigueScreen extends GameScreen {

    //Variables required for the Fatigue Screen:
    final private long FATIGUE_TIMEOUT = 3000;
    private long timeOnCreate, currentTime;
    private LayerViewport mFatigueScreenViewport;


    public FatigueScreen(Game game) {
        super("FatigueScreen", game);
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
        mFatigueScreenViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        //Get current time and check for timeout
        currentTime = System.currentTimeMillis();
        if (currentTime - timeOnCreate >= FATIGUE_TIMEOUT) {
            mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mGame));
        }

        // Process any touch events occurring since the update
        Input touchInput = mGame.getInput();
        List<TouchEvent> touchEvents = touchInput.getTouchEvents();
        if (touchEvents.size() > 0) {

        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLUE);

    }
}
