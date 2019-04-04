package uk.ac.qub.eeecs.game.TestClasses;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;


public class PauseMenuForTesting extends GameScreen {

    private LayerViewport mMenuViewport;

    public PauseMenuForTesting(Game game) {
        super("PauseMenuScreen", game);
        //Setting up  viewports method
        setupViewports();
        //Setting up pause menu objects
        SetUpPauseMenuOpbjects();
    }


    public void setupViewports() {
        // Setup the screen viewport to use the full screen:
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        /* Calculate the layer height that will preserved the screen aspect ratio
         given an assume 480 layer width.*/
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mMenuViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }


    private void SetUpPauseMenuOpbjects() {
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

    }

    public void addScreen(String gameScreenToAdd) {
        try {
            GameScreen gameScreen =
                    (GameScreen) Class.forName("uk.ac.qub.eeecs.game." + gameScreenToAdd)
                            .getConstructor(Game.class).newInstance(mGame);
            mGame.getScreenManager().changeScreenButton(gameScreen);

        } catch (ClassNotFoundException | NoSuchMethodException
                | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(
                    "PauseMenuScreen.addScreen: Error creating [" + gameScreenToAdd + " " + e.getMessage() + "]");
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() > 0) {

        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }
}
