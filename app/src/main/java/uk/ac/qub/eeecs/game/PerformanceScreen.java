package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

public class PerformanceScreen extends GameScreen {

    /**
     * Create the "PerformanceScreen" screen
     *
     * @param game Game to which this screen belongs
     */
    public PerformanceScreen(Game game) {
        super("PerformanceScreen", game);
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
    }

}
