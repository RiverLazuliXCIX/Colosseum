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
    private static final int maxHealth = 30;
    private static final int maxMana = 10;

    private int currentHealth = maxHealth; // Current health at initialisation should be set to max.
    private int currentManaCap = 1;        // Initial mana cap should be set to one, and will increase by one each turn, until it reaches maxMana upon which it stops increasing (outside of potential card effects).
    private int currentMana = 1;           // Initial mana should be set to current mana cap on start
    private int armor = 0;                 // Current armor of player character (Provides additional 'Health' that is deducted prior to the player's health pool when taking damage)
    private int attack = 0;                // Current attack of player character (Through weapons, abilities etc.)
    private int weaponDurability = 0;      // Durability of player's equipped weapon, once it reaches zero weapon is destroyed, player can no longer use it to attack

    private boolean yourTurn; // Will be false or true depending on whether or not it is the player's turn

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
     * Getter and setter methods for each of the appropriate private variables
     */

    public int getCurrentHealth(){return currentHealth;}
    public void setCurrentHealth(int currentHealth){this.currentHealth = currentHealth;}

    public int getCurrentManaCap(){return currentManaCap;}
    public void setCurrentManaCap(int currentManaCap){this.currentManaCap = currentManaCap;}

    public int getCurrentMana(){return currentMana;}
    public void setCurrentMana(int currentMana){this.currentMana = currentMana;}

    public int getCurrentArmor(){return armor;}
    public void setCurrentArmor(int armor){this.armor = armor;}

    public int getCurrentAttack() {return attack;}
    public void setCurrentAttack(int attack){this.attack = attack;}

    public int getCurrentWeaponDurability() {return weaponDurability;}
    public void setCurrentWeaponDurability(int weaponDurability) {this.weaponDurability = weaponDurability;}

    public boolean getYourTurn() {return yourTurn;}
    public void setYourTurn(boolean newTurnValue) {this.yourTurn = newTurnValue;}

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
    public void reduceCurrentMana(int manaReduction){

        // Currently, mana will only be reduced if the mana reduction can be covered by the player's
        // current mana, but until further card functionality is added (card costs, ability to play cards etc.)
        // this method is limited.

        if(manaReduction <= currentMana ){
            currentMana-=manaReduction;
        }

    }

    /**
     * Method to increase current mana. This function could be called based on specific card effects
     * or abilities etc. (such as the coin card) Current implementation allows the mana to exceed
     * the current mana cap, which would be common with the use of the coin card, allowing players to
     * play more expensive cards earlier, or play more cards in a turn provided their current mana can
     * cover the cost.
     *
     * @param manaAdded amount of mana attempting to be increased.
     */
    public void increaseCurrentMana(int manaAdded){
        currentMana+=manaAdded;
    }

    /**
     * Method for increasing the player character's current mana cap by 1 until the max mana cap has
     * been reached. Method would be called when the turn ends.
     *
     */
    public void increaseCurrentManaCap(){

        if(currentManaCap < maxMana) {
            currentManaCap += 1;
        }

    }

    /**
     * Method for increasing the player character's armor. Armor is a secondary source of health
     * and is reduced prior to the player's currentHealth, serving as a method to somewhat increase
     * the player's health cap.
     *
     * @param armorAdded amount of armor attempting to be added.
     */
    public void increaseArmor(int armorAdded){
        armor+=armorAdded;
    }

    /**
     * Method for reducing the player character's attack. If the amount being reduced exceeds the
     * current hero attack value, set attack to 0, to prevent negative attack values, else reduce
     * the player attack by the amount expected.
     *
     * @param attackReduction amount of attack attempting to be reduced
     */
    public void reduceAttack(int attackReduction){

        if(attackReduction>attack){
            attack=0;
        } else{
            attack-=attackReduction;
        }

    }

    /**
     * Method for increasing the player character's attack. This could potentially be done through
     * card abilities or weapons being equipped etc.
     *
     * @param attackAdded amount of attack being added to the player hero
     */
    public void increaseAttack(int attackAdded){
        attack+=attackAdded;
    }

    /**
     * Method for the player hero dealing weapon damage. Performs a check to ensure that the weapon
     * durability is above 0, if true, enemy hero should receive damage and the hero's weapon
     * durability should decrease by one.
     * TODO Targeting enemy hero or minions when cards and enemy class has been implemented
     * (current implementation only decreases durability)
     *
     */
    public void dealWeaponDamage(){
        // implement ability to target/interact with minions and heroes
        if(weaponDurability >0) {
            weaponDurability -= 1;
        }
        // Else if zero display message or some sort of feedback to the player
    }
}
