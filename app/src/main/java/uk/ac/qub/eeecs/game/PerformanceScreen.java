package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import uk.ac.qub.eeecs.gage.ui.FPSCounter; //Story P5
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

public class PerformanceScreen extends GameScreen {
    private FPSCounter fpsCounter; //Story P5
    /**
     * Create the "PerformanceScreen" screen
     *
     * @param game Game to which this screen belongs
     */
    public PerformanceScreen(Game game) {
        super("PerformanceScreen", game);
        fpsCounter = new FPSCounter( 0.0f,  2.0f , this) { }; //Story P5 Scott Barham
    }

    /**
     * Update the performance screen
     *
     * @param elapsedTime Elapsed time information
     */

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        fpsCounter.update(elapsedTime); //Story P5
    }

    /**
     * Draw the Performance screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        fpsCounter.draw(elapsedTime,graphics2D); //Story P5
    }

}
