package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by Kyle Corrigan. Class handles the player object along with its associated
 *
 * @author Kyle Corrigan
 */

public class Player extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    // Stores the character portrait height and width as well as the frame for the ability icons
    private static final float PORTRAIT_WIDTH = 35.0f;
    private static final float PORTRAIT_HEIGHT = 35.0f;
    private static final float ABILITY_FRAME_WIDTH = PORTRAIT_WIDTH - 15;
    private static final float ABILITY_FRAME_HEIGHT = PORTRAIT_HEIGHT - 15;

    private float portraitXPos = position.x;
    private float portraitYPos = position.y;

    private float abilityFrameXPos = portraitXPos + (PORTRAIT_WIDTH/2)+ (ABILITY_FRAME_WIDTH/2);
    private float abilityFrameYPos = portraitYPos - (PORTRAIT_HEIGHT/2) + (ABILITY_FRAME_HEIGHT/2);

    private PushButton heroAbility; // Push button handling the hero abilities

    private String hero; // Currently selected hero
    // Sets a cap on the maximum health and mana a player can have at once
    private static final int MAX_HEALTH = 30;
    private static final int MAX_MANA = 10;
    private static final int HERO_ABILITY_COST = 2; // All hero abilities have a mana cost of 2 by default

    private int currentHealth = MAX_HEALTH; // Current health at initialisation should be set to max.
    private int currentManaCap = 1;        // Initial mana cap should be set to one, and will increase by one each turn, until it reaches maxMana upon which it stops increasing (outside of potential card effects).
    private int currentMana = 1;           // Initial mana should be set to current mana cap on start
    private int armor = 0;                 // Current armor of player character (Provides additional 'Health' that is deducted prior to the player's health pool when taking damage)
    private int attack = 0;                // Current attack of player character (Through weapons, abilities etc.)
    private int weaponDurability = 0;      // Durability of player's equipped weapon, once it reaches zero weapon is destroyed, player can no longer use it to attack
    private Effect pEffect = Effect.NONE;
    private int eDuration = 0;

    private boolean weaponEquipped = false;// Stores whether the player has equipped a weapon or not (can be equipped through abilities or cards, default is false, equipping a new weapon destroys the currently equipped one)
    private boolean abilityUsedThisTurn = false; // Stores whether an ability has been used this turn, if true, ability cannot be played for the rest of the player's turn
    private boolean yourTurn = true; // Will be false or true depending on whether or not it is the player's turn

    // Add variables for storing player's active cards, deck, hand etc.

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param gameScreen       Gamescreen upon which the portrait will be displayed
     * @param hero             The player's hero, different heroes have different abilities etc.
     */

    // Create new game objects for the portrait when constructor called, used to be drawn to screen
    // call the draw methods of those objects within the draw call of the player class.
    // Add an additional screen to allow players to select a hero

    // Portrait drawn at midpoint on default layer viewport X axis to keep it centred on different screens
    public Player(GameScreen gameScreen, String hero){
        super(gameScreen.getDefaultLayerViewport().halfWidth, gameScreen.getDefaultLayerViewport().getBottom()+(PORTRAIT_HEIGHT/2)+(70.0f/1.5f),
                PORTRAIT_WIDTH, PORTRAIT_HEIGHT, gameScreen.getGame().getAssetManager().getBitmap("Hero"+hero), gameScreen);

        this.hero = hero;
        // Creates the relevant push buttons for the hero abilities
        createHeroAbilityButton();



    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

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
     *
     * @param manaReduction amount of mana attempting to be reduced.
     */
    public void reduceCurrentMana(int manaReduction){

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
     */
    public void increaseCurrentMana(){
        currentMana = getCurrentManaCap();
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
     *
     */
    // Will work regardless or a Player or AIOpponent attacking
    public void dealWeaponDamage(Player p){

        if(weaponEquipped) {
            // implement ability to target/interact with minions and heroes
            if (weaponDurability > 0) {
                // Deal damage to target and reduce weapon durability
                p.receiveDamage(getCurrentAttack());
                weaponDurability--;

                // If after dealing weapon damage, durability is 0, unequip weapon and set current
                // attack to 0.
                if (weaponDurability <= 0){
                    setCurrentAttack(0);
                    weaponEquipped = false;
                }

            }
            // Else if durability is zero or less destroy the currently equipped weapon if, after attacking,
            // weapon durability reaches 0 (Used in the event a card reduces weapon durability outside
            // of this method)
            else if (weaponDurability <= 0) { // in case durability is somehow less than 0
                setCurrentAttack(0);
                weaponEquipped = false;
            }
        }

    }

    public void dealWeaponDamage(MinionCard mc) {
        if(weaponEquipped) {
            if (weaponDurability > 0) {
                mc.takeDamage(getCurrentAttack());
                this.receiveDamage(mc.getAttack());

                mc.checkHealth();
                weaponDurability--;

                // If after dealing weapon damage, durability is 0, unequip weapon and set current
                // attack to 0.
                if (weaponDurability <= 0){
                    setCurrentAttack(0);
                    weaponEquipped = false;
                }

            } else if (weaponDurability <= 0) { // in case durability is somehow less than 0
                setCurrentAttack(0);
                weaponEquipped = false;
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Draw methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Draw elements associated with the player class, such as health, character portrait,
     * ability icon etc.
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     *
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport,
                     ScreenViewport screenViewport){
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport,
                screenViewport, drawSourceRect, drawScreenRect)) {

            graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);

        }

        drawStats(elapsedTime, graphics2D, layerViewport,screenViewport);
        drawAbilityFrame(elapsedTime, graphics2D, layerViewport,screenViewport);


    }
    /**
     * Method that handles drawing the hero's associated ability frame
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    public void drawAbilityFrame(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport,
                                 ScreenViewport screenViewport){
        GameObject abilityFrame = new GameObject(abilityFrameXPos,abilityFrameYPos,
                PORTRAIT_WIDTH-15, PORTRAIT_HEIGHT-15,
                getGameScreen().getGame().getAssetManager().getBitmap("AbilityFrame"),
                getGameScreen());

        abilityFrame.draw(elapsedTime,graphics2D, layerViewport,screenViewport);
        heroAbility.draw(elapsedTime,graphics2D, layerViewport,screenViewport);
    }

    /**
     * Changes the hero ability image based on what hero is being played
     */
    public void createHeroAbilityButton(){

        switch(hero){
            case "EmperorCommodus":
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"KnifeBelt","KnifeBeltPushed",getGameScreen());
                break;

            case "Mars":
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"Fortify","FortifyPushed",getGameScreen());
                break;

            case "Hircine":
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"Hunt","HuntPushed",getGameScreen());
                break;

            case "Meridia":
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"HolyHealing","HolyHealingPushed",getGameScreen());
                break;

            default:
                // Error handling / default hero hircine etc.
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"Hunt","HuntPushed",getGameScreen());
                break;
        }


    }

    /**
     * Method that handles drawing the hero's associated health and armour to the screen
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics instance
     * @param layerViewport  Game layer viewport
     * @param screenViewport Screen viewport
     */
    public void drawStats(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport,
                          ScreenViewport screenViewport){

        // Initialising properties for player health/armor stats text paint
        Paint textPaint = new Paint();
        textPaint.setTextSize(25);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Specifying icon location and dimensions (In relation to player hero portrait)
        float statIconWidth = PORTRAIT_WIDTH/3;
        float statIconHeight = PORTRAIT_HEIGHT/3;

        float healthIconXPos = portraitXPos-statIconWidth;
        float healthIconYPos = portraitYPos-statIconHeight;

        float armorIconXPos= portraitXPos+statIconWidth;
        float armorIconYPos= portraitYPos-statIconHeight;

        // Defining icon game objects
        GameObject healthIcon = new GameObject(healthIconXPos,healthIconYPos,
                statIconWidth, statIconHeight,
                getGameScreen().getGame().getAssetManager().getBitmap("HeroHealth"),
                getGameScreen());

        GameObject armorIcon = new GameObject(armorIconXPos,armorIconYPos,
                statIconWidth, statIconHeight,
                getGameScreen().getGame().getAssetManager().getBitmap("HeroArmor"),
                getGameScreen());

        // Drawing icons with associated health/armor text values
        healthIcon.draw(elapsedTime,graphics2D,layerViewport,screenViewport);
        graphics2D.drawText(currentHealth+"/"+MAX_HEALTH,healthIconXPos*4 , getGameScreen().getDefaultScreenViewport().bottom - healthIconYPos*4, textPaint);

        armorIcon.draw(elapsedTime,graphics2D,layerViewport,screenViewport);
        graphics2D.drawText(String.valueOf(armor),armorIconXPos*4 , getGameScreen().getDefaultScreenViewport().bottom - armorIconYPos*4, textPaint);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Update methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Updates elements associated with the player class
     *
     * @param elapsedTime    Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime){

        // Process any touch events occurring since the update
        Input playerInput;
        playerInput = getGameScreen().getGame().getInput();
        List<TouchEvent> touchEvents = playerInput.getTouchEvents();

        if(touchEvents.size()>0){

            // If ability has not been played this turn, allow hero ability to be tapped
            if(!abilityUsedThisTurn) {
                heroAbility.update(elapsedTime);

                if (heroAbility.isPushTriggered()) {
                    useHeroAbilities();
                }
            }

        }

    }

    /**
     *Determines what action is executed when user taps on their hero ability
     *
     * Heroes and associated abilities (Default 2 mana cost)
     * +-------------------------------------------------------------------------------------------
     * Emperor Commodus : Dagger Belt – Equip a 1 damage 2 durability weapon.
     * Mars : Fortify - Grants the hero +2 Armor.
     * Hircine : Hunt - Deals 2 damage to the opposing enemy hero.
     * Meridia : Holy Healing: Restore 2 Health to Player Hero
     * +-------------------------------------------------------------------------------------------
     */

    public void useHeroAbilities(){

        // If player has enough mana to cover ability cost, they haven't used an ability this turn,
        // and it is their turn to play, use the hero's ability
        // Note: reduce current mana and abilityUsedThisTurn are set within each case, as opposed to
        // after executing the switch, to ensure that only if the abilities have an effect, will
        // players need to cover the cost.
        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {

            switch(hero) {
                // Note: Equipping a new weapon replaces the player's currently equipped weapon
                case "EmperorCommodus":
                    setCurrentAttack(1);
                    setCurrentWeaponDurability(2);
                    weaponEquipped = true;
                    reduceCurrentMana(HERO_ABILITY_COST);
                    abilityUsedThisTurn = true;
                    break;

                case "Mars":
                    increaseArmor(2);
                    reduceCurrentMana(HERO_ABILITY_COST);
                    abilityUsedThisTurn = true;
                    break;

                case "Hircine":
                    //targetOpponent.receiveDamage(2);
                    //reduceCurrentMana(HERO_ABILITY_COST);
                    break;

                case "Meridia":
                    if(currentHealth<MAX_HEALTH) {
                        heal(2);
                        reduceCurrentMana(HERO_ABILITY_COST);
                        abilityUsedThisTurn = true;
                    }
                    break;

        }

        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and setter methods
    // /////////////////////////////////////////////////////////////////////////

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

    public float getPortraitXPos() {return portraitXPos;}
    public void setPortraitXPos(float portraitXPos) {this.portraitXPos = portraitXPos;}

    public float getPortraitYPos() {return portraitYPos;}
    public void setPortraitYPos(float portraitYPos) {
        this.portraitYPos = portraitYPos;
        position.y = portraitYPos;
    }

    public float getAbilityFrameXPos() {return abilityFrameXPos;}
    public void setAbilityFrameXPos(float abilityFrameXPos) {
        this.abilityFrameXPos = abilityFrameXPos;
        position.x = abilityFrameXPos;
    }

    public float getAbilityFrameYPos() {return abilityFrameYPos;}
    public void setAbilityFrameYPos(float abilityFrameYPos) {this.abilityFrameYPos = abilityFrameYPos;}

    public float getPortraitHeight() {return PORTRAIT_HEIGHT;}
    public float getPortraitWidth() {return PORTRAIT_WIDTH;}
    public float getAbilityFrameHeight(){return  ABILITY_FRAME_HEIGHT;}
    public float getAbilityFrameWidth(){return  ABILITY_FRAME_WIDTH;}

    public boolean isWeaponEquipped() { return weaponEquipped; }
    public void setWeaponEquipped(boolean weaponEquipped) { this.weaponEquipped = weaponEquipped; }

    public boolean isAbilityUsedThisTurn() { return abilityUsedThisTurn; }
    public void setAbilityUsedThisTurn(boolean abilityUsedThisTurn) { this.abilityUsedThisTurn = abilityUsedThisTurn; }

    public Effect getPEffect() { return this.pEffect; }
    public void setPEffect(Effect e) { this.pEffect = e; }

    public int getEDuration() { return this.eDuration; }
    public void setEDuration(int duration) { this.eDuration = duration; }

    public PushButton getHeroAbility() { return heroAbility; }
    public void setHeroAbility(PushButton heroAbility) { this.heroAbility = heroAbility; }

    public String getHero() { return hero; }
    public void setHero(String hero) { this.hero = hero; }

    public static int getMaxHealth() { return MAX_HEALTH; }
    public static int getMaxMana() { return MAX_MANA; }
    public static int getHeroAbilityCost() { return HERO_ABILITY_COST; }
}
