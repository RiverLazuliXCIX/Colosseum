package uk.ac.qub.eeecs.game.BoardGameDemo;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;

public class SplashScreen extends GameScreen {


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the 'Splash Screen' screen
     *
     * @param game Game to which this screen belongs
     */

    public SplashScreen(Game game) {
        super("SplashScreen", game);
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
    }

    /**
     * Draw the 'Splah Screen' screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
    }
}
