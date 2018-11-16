package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import uk.ac.qub.eeecs.gage.ui.FPSCounter; //Story P5
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;

public class PerformanceScreen extends GameScreen {

    private int rectanglesPerUpdate = 10; // User story P3 Kyle Corrigan: Number of textured rectangles drawn to screen per update.
    boolean rectangleCapReached=false; // P3: When true, rectangles will no longer be added to the list.
    private List<GameObject> rectangleList; // P3: saves 10 rectangles to a list, to be drawn again at random sizes and positions

    Random rand = new Random();
    float min=5.0f;
    float maxX = mGame.getScreenWidth();
    float maxY =getGame().getScreenHeight();

    private FPSCounter fpsCounter; //Story P5

    /**
     * Create the "PerformanceScreen" screen
     *
     * @param game Game to which this screen belongs
     */
    public PerformanceScreen(Game game) {
        super("PerformanceScreen", game);
        fpsCounter = new FPSCounter( 0.0f,  2.0f , this) { }; //Story P5 Scott Barham

        mGame.getAssetManager().loadAndAddBitmap("TextureGrass","img/TextureGrass.png");
        rectangleList = new ArrayList<>(rectanglesPerUpdate); //P3: Stores rectangle objects up to the amount needed per draw call

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

        //P3: adds rectangles to be drawn each update, up to a specified "rectanglesPerUpdate" cap.
        if (!rectangleCapReached) {
            for (int i = 0; i < rectanglesPerUpdate; i++) {
                rectangleList.add(new GameObject(rand.nextFloat() * (maxX) + min, rand.nextFloat() * (maxY) + min, getGame().getAssetManager().getBitmap("TextureGrass"), this));
            }
            rectangleCapReached=true;
        }

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

        // P3: Will draw all rectangles within the rectangle list, setting random sizes and positions.
        // the same rectangles are drawn and redrawn on screen at different sizes, meaning they will not
        // stay on screen.
        for (GameObject texturedRectangles : rectangleList) {
            texturedRectangles.setHeight(rand.nextFloat()*(maxY));
            texturedRectangles.setWidth(rand.nextFloat()*(maxX));
            texturedRectangles.setPosition(rand.nextFloat() * (maxX) + min, rand.nextFloat() * (maxY) + min);
            texturedRectangles.draw(elapsedTime, graphics2D);
        }

        fpsCounter.draw(elapsedTime,graphics2D); //Story P5

    }

}
