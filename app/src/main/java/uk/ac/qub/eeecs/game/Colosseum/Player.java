package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.game.prototypeClasses.Deck;

public class Player extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    // Stores the character portrait height and width
    // TODO Portrait width stores temporary values, needs to make it scale
    private static final float PORTRAIT_WIDTH = 50.0f;
    private static final float PORTRAIT_HEIGHT = 50.0f;

    // Sets a cap on the maximum health and mana a player can have at once
    private static final int MAX_HEALTH = 30;
    private static final int MAX_MANA = 10;
    private static final int HERO_ABILITY_COST = 2; // All hero abilities have a mana cost of 2 by default

    // Stores numbers used by hero stats (health, mana, durability, armor etc.)
    //private Bitmap[] heroStatNumbers = new Bitmap[10];

    private int currentHealth = MAX_HEALTH; // Current health at initialisation should be set to max.
    private int currentManaCap = 1;        // Initial mana cap should be set to one, and will increase by one each turn, until it reaches maxMana upon which it stops increasing (outside of potential card effects).
    private int currentMana = 1;           // Initial mana should be set to current mana cap on start
    private int armor = 0;                 // Current armor of player character (Provides additional 'Health' that is deducted prior to the player's health pool when taking damage)
    private int attack = 0;                // Current attack of player character (Through weapons, abilities etc.)
    private int weaponDurability = 0;      // Durability of player's equipped weapon, once it reaches zero weapon is destroyed, player can no longer use it to attack

    private boolean weaponEquipped = false;// Stores whether the player has equipped a weapon or not (can be equipped through abilities or cards, default is false, equipping a new weapon destroys the currently equipped one)
    private boolean abilityUsedThisTurn = false; // Stores whether an ability has been used this turn, if true, ability cannot be played for the rest of the player's turn
    private boolean yourTurn = true; // Will be false or true depending on whether or not it is the player's turn

    // Add variables for storing player's active cards, deck, hand etc.

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     //* @param startX           Starting x coordinate of the player character portrait
     //* @param startY           Starting y coordinate of the player character portrait
     * @param gameScreen       Gamescreen upon which the portrait will be displayed
     * @param hero             The player's hero, different heroes have different abilities etc.
     //* @param portraitImage    Bitmap of the player's chosen character to be displayed.
     */

    // Create new game objects for the portrait when constructor called, used to be drawn to screen
    // call the draw methods of those objects within the draw call of the player class.
    // Add an additional screen to allow players to select a hero

    // Portrait drawn at midpoint on default layer viewport X axis to ~Hopefully~ keep it centred on different screens
    public Player(GameScreen gameScreen, String hero){
        super(gameScreen.getDefaultLayerViewport().halfWidth, gameScreen.getDefaultLayerViewport().getBottom()+(PORTRAIT_HEIGHT/2),
                PORTRAIT_WIDTH, PORTRAIT_HEIGHT, gameScreen.getGame().getAssetManager().getBitmap("Hero"+hero), gameScreen);

//        for(int number = 0; number <= 9; number++) {
//            heroStatNumbers[number] = gameScreen.getGame().getAssetManager().getBitmap("no" + Integer.toString(number));
//        }

        setUpHero(hero);

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

        if(currentHealth + amountToHeal > MAX_HEALTH){
            currentHealth = MAX_HEALTH;
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

        if(currentManaCap < MAX_MANA) {
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
    // 2 methods needed, deal damage to player/hero, deal damage to minion
    public void dealWeaponDamage(){

        if(weaponEquipped) {
            // implement ability to target/interact with minions and heroes
            if (weaponDurability > 0) {
                // Deal damage to target
                weaponDurability -= 1;
            }
            // Else if zero display message or some sort of feedback to the player
            // Destroys the currently equipped weapon if, after attacking, weapon durability reaches 0
            if (weaponDurability == 0) {
                setCurrentAttack(0);
                weaponEquipped = false;
            }
        }

    }

    // TODO Overridden draw method required to include ability icons etc

//    @Override
//    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport,
//                     ScreenViewport screenViewport){
//        // Draw elements associated with the player class, such as health, character portrait,
//        // ability icon etc.
//
//
//
//    }

    /**
     * Method displays a different portrait based on the hero string that has been passed, and
     * defines the hero abilities.
     *
     * Heroes and associated abilities (Default 2 mana cost)
     * +-------------------------------------------------------------------------------------------
     * Emperor Commodus : Dagger Belt – Equip a 1 damage 2 durability weapon.
     * Mars : Fortify - Grants the hero +2 Armor.
     * Brutalus : Engage – Gain +1 attack this turn and +1 armour.
     * Sagira : Eyes Up – Able to give any minion +1 health.
     * Hircine : Hunt - Deals 2 damage to the opposing enemy hero.
     * Meridia : Holy Healing: Restore 2 Health to Player Hero
     * +-------------------------------------------------------------------------------------------
     */

    public void setUpHero(String hero){

        switch(hero){
            case "Commodus":

                // I'll stick some ability related stuff in these sections

                break;

            case "Mars":

                // I'll stick some ability related stuff in these sections

                break;

            case "Brutalus":

                // I'll stick some ability related stuff in these sections

                break;

            case "Sagira":

                // I'll stick some ability related stuff in these sections

                break;

            case "Hircine":

                // I'll stick some ability related stuff in these sections

                break;

            case "Meridia":

                // I'll stick some ability related stuff in these sections

                break;

            default:
                // Error handling / default hero hircine etc.
                // I'll stick some ability related stuff in these sections

        }


    }

    // /////////////////////////////////////////////////////////////////////////
    // Hero Ability methods
    // /////////////////////////////////////////////////////////////////////////
    // All abilites have a mana cost of 2 and can be activated once per turn,
    // provided the mana cost can be covered.
    // Note: Current implementation regarding abilities targeting enemy heroes, accounts for use against
    // an AIOpponent.

    // Note: Equipping a new weapon replaces the player's currently equipped weapon
    public void commodusAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);
            setCurrentAttack(1);
            setCurrentWeaponDurability(2);
            weaponEquipped = true;
            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void marsAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);
            increaseArmor(3);
            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void brutalusAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            // TODO end turn handler for temporary stat boosts (Could apply to both cards and heroes)
            increaseAttack(1);
            increaseArmor(1);

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void sagiraAbilityUsed(MinionCard targetMinion){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            // If healing the minion by 1 results in a health value greater than or equal to max health, set current health to max health
            if(targetMinion.getHealth()+1>=targetMinion.getMaxHealth()){
                targetMinion.setHealth(targetMinion.getMaxHealth());
            }else{
                // else increase minion health by 1
                targetMinion.setHealth(targetMinion.getHealth()+1);
            }

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void hircineAbilityUsed(AIOpponent targetOpponent){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            targetOpponent.receiveDamage(2);

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void meridiaAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            heal(2);

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

}
