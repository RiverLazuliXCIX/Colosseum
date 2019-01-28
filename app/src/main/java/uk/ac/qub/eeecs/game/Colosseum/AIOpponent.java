package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/*
 * An extension of the player class, with the addition of AI behaviors
 */

public class AIOpponent extends Player {

    public AIOpponent(float startX, float startY, GameScreen gameScreen, Bitmap portraitImage, String hero){
        super(gameScreen, hero);
        // Create new game objects for the portrait when constructor called, used to be drawn to screen
        // call the draw methods of those objects within the draw call of the opponent class.
    }

    // AIOpponent will use Identical methods to the player class, with the exception of some new
    // additional methods relating to behaviors and the AI in general
    //TODO Additional methods/properties unique to AI opponent (Next sprint possibly)

}
