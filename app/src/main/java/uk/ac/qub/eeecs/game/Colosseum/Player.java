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

    /**
     * Methods for retrieving the current health, armor and mana of the player.
     */

    public int getCurrentHealth(){return currentHealth;}

    public int getCurrentMana(){return currentMana;}

    public int getCurrentArmor(){return armor;}

    /**
     * Method for subtracting both health and armor when the player character/portrait
     * takes damage. Damage is dealt to armor first, before being dealt to player health
     * eg. A player with 3 armor and 30 health is hit for 4 damage, 3 damage is is dealt to the
     * player armor (also removing the 3 armor), and 1 damage is dealt directly to the player health
     * leaving them with 0 armor and 29 health.
     *
     * @param damageReceived amount of damage received in an attack.
     */
    public void receiveDamage(int damageReceived){

        if(damageReceived > armor){
            damageReceived -= armor;
            armor = 0;
            currentHealth -= damageReceived;
        }else{
            armor -= damageReceived;
        }

    }

    /**
     * Method for healing health back to the player character/portrait. Health can not exceed the
     * player's maxHealth of 30.
     *
     * @param amountToHeal amount of health attempting to be restored.
     */
    public void heal(int amountToHeal){

        if(currentHealth + amountToHeal > maxHealth){
            currentHealth = maxHealth;
        } else{ currentHealth += amountToHeal;}

    }

    /**
     * Method to deduct mana. If the mana attempting to be deducted exceeds the current player mana
     * the action can not be completed.
     *  TODO Add functionality to prevent cards being played if the mana cost cant be covered
     *
     * @param manaReduction amount of mana attempting to be reduced.
     */
    public void reduceMana(int manaReduction){

        // Currently, mana will only be reduced if the mana reduction can be covered by the player's
        // current mana, but until further card functionality is added (card costs, ability to play cards etc.)
        // this method is limited.

        if(manaReduction <= currentMana ){
            currentMana-=manaReduction;
        }

    }

    /**
     * Method for increasing the player character's armor. Armor is a secondary source of health
     * and is reduced prior to the player's currentHealth.
     *
     * @param armorAdded amount of armor attempting to be added.
     */

    public void increaseArmor(int armorAdded){
        armor+=armorAdded;
    }
}
