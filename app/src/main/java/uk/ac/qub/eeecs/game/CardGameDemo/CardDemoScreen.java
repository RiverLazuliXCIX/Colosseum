package uk.ac.qub.eeecs.game.CardGameDemo;

import android.graphics.Color;
import android.text.method.Touch;
import android.view.GestureDetector;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.CardGameDemo.Card;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;

/**
 * Starter class for Card game stories
 *
 * @version 1.0
 */
public class CardDemoScreen extends GameScreen{

    // /////////////////////////////////////////////////////////////////////////
    // Properties: Table Related
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Width and height of the level
     */
    private final float TABLE_WIDTH = 240.0f;
    private final float TABLE_HEIGHT = 160.0f;

    /**
     * Define a viewport for the game objects (cards etc)
     */
    private LayerViewport mTableLayerViewport;

    /**
     * Define the player's card
     */
    private Card mCard;


    /**
     * Define storage of touch points. Up to 5 simultaneous touch
     * events are tested (an arbitrary value that displays well on ]
     * screen). An array of booleans is used to determine if a given
     * touch point exists, alongside this a corresponding 2D array of
     * x/y points is maintained to hold the location of touch points
     */
    private static final int mTouchIdToDisplay = 5;
    private boolean[] mTouchIdExists = new boolean[mTouchIdToDisplay];
    private float[][] mTouchLocation = new float[mTouchIdExists.length][2];



    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public CardDemoScreen(Game game) {
        super("CardDemoScreen", game);

        //Viewports!
        /// Setup the screen viewport to use the full screen.
        mTableLayerViewport = new LayerViewport(TABLE_WIDTH, TABLE_HEIGHT, TABLE_WIDTH, TABLE_HEIGHT);

        //Objects!
        // Load in the assets used by the steering demo
        mGame.getAssetManager().loadAssets("txt/assets/CardDemoAssets.JSON");

        // Create the player card
        mCard = new Card(240, 160, this, 1, 1, 'A');
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

        // Store touch point information.
        for (int pointerId = 0; pointerId < mTouchIdExists.length; pointerId++) {
            mTouchIdExists[pointerId] = input.existsTouch(pointerId);
            if (mTouchIdExists[pointerId]) {
                mTouchLocation[pointerId][0] = input.getTouchX(0);
                mTouchLocation[pointerId][1] = input.getTouchY(0);
            }
            mCard.setPosition(mTouchLocation[pointerId][0], mTouchLocation[pointerId][1]);
        }

        checkCardNotOutOfBounds(elapsedTime);
    }

    public void checkCardNotOutOfBounds(ElapsedTime elapsedTime) {
        // Ensure the card cannot leave the confines of the table
        BoundingBox cardBound = mCard.getBound();
        if (cardBound.getLeft() < 0)
            mCard.position.x -= cardBound.getLeft();
        else if (cardBound.getRight() > TABLE_WIDTH)
            mCard.position.x -= (cardBound.getRight() - TABLE_WIDTH);

        if (cardBound.getBottom() < 0)
            mCard.position.y -= cardBound.getBottom();
        else if (cardBound.getTop() > TABLE_HEIGHT)
            mCard.position.y -= (cardBound.getTop() - TABLE_HEIGHT);
    }

    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Create the screen to black and define a clip based on the viewport
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the player
        mCard.draw(elapsedTime, graphics2D, mTableLayerViewport, mDefaultScreenViewport);
    }
}
