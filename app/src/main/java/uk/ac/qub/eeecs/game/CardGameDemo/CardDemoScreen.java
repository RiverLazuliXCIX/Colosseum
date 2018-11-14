package uk.ac.qub.eeecs.game.CardGameDemo;

import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.CardGameDemo.Card;

/**
 * Starter class for Card game stories
 *
 * @version 1.0
 */
public class CardDemoScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties: Table Related
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define a viewport for the game objects (cards etc)
     */
    private LayerViewport mTableLayerViewport;

    /**
     * Define the player's card
     */
    private Card mCard;

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
        mTableLayerViewport = new LayerViewport(240, 160, 240, 160);

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
