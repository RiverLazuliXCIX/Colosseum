package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/*
 * An extension of the player class, with the addition of AI behaviors
 */

public class AIOpponent extends Player {

    private float opponentPortraitXPos = getPortraitXPos(); // Centers the player portrait x coordinate to the center of the screen, same as the player portrait
    private float opponentPortraitYPos = getGameScreen().getDefaultLayerViewport().getTop()-(getPortraitHeight()/2); // Displays the portrait at the top of the screen
    private float abilityFrameXPos = getAbilityFrameXPos();
    private float abilityFrameYPos = opponentPortraitYPos - (getPortraitHeight()/2) + (getAbilityFrameHeight()/2);

    public AIOpponent(GameScreen gameScreen, String hero){
        super(gameScreen, hero);

        setPortraitYPos(opponentPortraitYPos);
        setAbilityFrameYPos(abilityFrameYPos);

        createHeroAbilityButton(hero);

    }

    // AIOpponent will use Identical methods to the player class, with the exception of some new
    // additional methods relating to behaviors and the AI in general
    //TODO Additional methods/properties unique to AI opponent (Next sprint possibly)

}
