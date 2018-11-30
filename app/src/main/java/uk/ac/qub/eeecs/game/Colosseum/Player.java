package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class Player extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    // Stores the character portrait height and width
    private static final float PORTRAIT_WIDTH = 50.0f;
    private static final float PORTRAIT_HEIGHT = 100.0f;

    // Sets a cap on the maximum health and mana a player can have at once
    private final int maxHealth = 30;
    private final int maxMana = 10;

    private int currentHealth = maxHealth; // Current health at initialisation should be set to max.
    private int currentManaCap = 1;        // Initial mana cap should be set to one, and will increase by one each turn, until it reaches maxMana upon which it stops increasing (outside of potential card effects).
    private int currentMana = 1;           // Initial mana should be set to current mana cap on start
    private int armor = 0;                 // Current armor of player character (Provides additional 'Health' that is deducted prior to the player's health pool when taking damage)
    private int attack = 0;                // Current attack of player character (Through weapons, abilities etc.)
    private int weaponDurability = 0;      // Durability of player's equipped weapon, once it reaches zero weapon is destroyed, player can no longer use it to attack


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param startX           Starting x coordinate of the player character portrait
     * @param startY           Starting y coordinate of the player character portrait
     * @param gameScreen       Gamescreen upon which the portrait will be displayed
     * @param hero             The player's hero, different heroes have different abilities etc.
     * @param portraitImage    Bitmap of the player's chosen character to be displayed.
     */

    public Player(float startX, float startY, GameScreen gameScreen, Bitmap portraitImage, char hero){
        super(startX, startY, PORTRAIT_WIDTH, PORTRAIT_HEIGHT, portraitImage, gameScreen);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    // TODO Implement methods that handle damage being dealt, healed, status effects etc.

}
