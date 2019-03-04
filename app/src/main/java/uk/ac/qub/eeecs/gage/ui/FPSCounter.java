package uk.ac.qub.eeecs.gage.ui;

import android.graphics.Paint;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * FPS counter base class. Provides an fps counter and average fps counter
 *
 * @version 1.0
 */
public abstract class FPSCounter extends GameObject {
    //Story P1 Measuring Performance -Scott Barham

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////


    private static int totalFrames = 0; //create a total frames property to calculate the average fps
    private static int recentFrames = 0; //create a recentFrames to calculate current fps
    private static int FPScurrent = 0; //holder for current fps
    private static int FPSaverage = 0; //holder for average fps
    private static long sinceLastUpdate = 0; //holds the system time since the last update of fps
    private static int updateAmount = 0; //used to count how many seconds has passed since the start to calculate an average
    final private static long ONE_SECOND = 1000000000L; //1,000x1,000,000, 1second = 1000MS, 1MS = 1,000,000NS. So 1,000MSx1,000,000NS = 1 Second

    private float x,y;
    private GameScreen gameScreen;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Setup base FPS counter properties
     *
     * @param x                   Centre x location of the counter
     * @param y                   Centre y location of the counter
     * @param gameScreen          Gamescreen to which this control belongs
     */
    public FPSCounter(float x, float y, GameScreen gameScreen) {
        super(gameScreen);
        this.x = x;
        this.y = y;
        this.gameScreen = gameScreen;
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////


    /**
     * Update the FPS counter, calculating new fps.
     *
     * @param elapsedTime Elapsed time information
     */
    public void update(ElapsedTime elapsedTime) {

        recentFrames++; //count the number of frames that passes each update

        if(ONE_SECOND < System.nanoTime() - sinceLastUpdate) { //If one second passes
            //Story P1
            sinceLastUpdate = System.nanoTime(); //update the sinceLastUpdate variable with the new time
            updateAmount++; //update the amount of iterations(seconds) that this has been going
            FPScurrent = recentFrames; //make the current fps update to the amount of recent frames
            //Story P2
            totalFrames += recentFrames; //make the total frames equal to total frames + recent frames
            recentFrames = 0; //reset recent frames back to 0
            FPSaverage = totalFrames / updateAmount; //calculate an average using total frames/total amount of times iterated (seconds).
        }
    }

    private Paint textPaint = new Paint();

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        int screenHeight = graphics2D.getSurfaceHeight();
        float textHeight = screenHeight / 30.0f;
        textPaint.setTextSize(textHeight); //create a appropriate sizing of text
        //Draw fps on screen
        //Story P1
        graphics2D.drawText("Current FPS = ", x, y+80, textPaint); //draw the text "Current FPS = "
        graphics2D.drawText(String.valueOf(FPScurrent), x+250, y +80, textPaint); //draw the text of the current fps value
        //Story P2
        graphics2D.drawText("Average FPS = ", x, y+110, textPaint); //draw the text "Average FPS = "
        graphics2D.drawText(String.valueOf(FPSaverage), x+250, y +110, textPaint); //draw the text of the average fps value
    }
}
