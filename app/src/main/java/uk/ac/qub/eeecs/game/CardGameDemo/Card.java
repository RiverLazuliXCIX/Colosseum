package uk.ac.qub.eeecs.game.CardGameDemo;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;


public class Card extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Width and height of the card, created to provide an appropriate overall
     * size and an appropriate width/height ratio.
     */
    private static final float CARD_WIDTH = 150.0f;
    private static final float CARD_HEIGHT = 205.0f;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a player controlled spaceship
     *
     * @param startX     x location of the player card
     * @param startY     y location of the player card
     * @param gameScreen Gamescreen to which card belongs
     * @param attack     attack power of this card
     * @param defence    defence power of this card
     * @param type       what type of card this is
     */
    public Card(float startX, float startY, GameScreen gameScreen, int attack, int defence, char type) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, gameScreen.getGame()
                .getAssetManager().getBitmap("CardFront"), gameScreen);
    }
}
