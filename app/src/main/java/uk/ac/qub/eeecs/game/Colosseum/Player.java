package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

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

    private Input playerInput; // Input for the player class

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

    // Portrait drawn at midpoint on default layer viewport X axis to ~Hopefully~ keep it centred on different screens
    public Player(GameScreen gameScreen, String hero){
        super(gameScreen.getDefaultLayerViewport().halfWidth, gameScreen.getDefaultLayerViewport().getBottom()+(PORTRAIT_HEIGHT/2)+(70.0f/1.5f),
                PORTRAIT_WIDTH, PORTRAIT_HEIGHT, gameScreen.getGame().getAssetManager().getBitmap("Hero"+hero), gameScreen);

        // Creates the relevant push buttons for the hero abilities
        createHeroAbilityButton(hero);

        this.hero = hero;

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
                weaponDurability--;
            }
            // Else if zero display message or some sort of feedback to the player
            // Destroys the currently equipped weapon if, after attacking, weapon durability reaches 0
            if (weaponDurability == 0) {
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
        // Drawing ability frame
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

    /** Changes the hero ability image based on what hero is being played
     *
     * @param hero The hero that the player has selected to play as
     */
    public void createHeroAbilityButton(String hero){

        switch(hero){
            case "EmperorCommodus":
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"KnifeBelt","KnifeBeltPushed",getGameScreen());

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
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"Hunt","HuntPushed",getGameScreen());

                break;

            case "Meridia":
                heroAbility = new PushButton(abilityFrameXPos,abilityFrameYPos,ABILITY_FRAME_WIDTH-10,ABILITY_FRAME_HEIGHT-10,"HolyHealing","HolyHealingPushed",getGameScreen());

                break;

            default:
                // Error handling / default hero hircine etc.
                // I'll stick some ability related stuff in these sections

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
        playerInput = getGameScreen().getGame().getInput();
        List<TouchEvent> touchEvents = playerInput.getTouchEvents();

        if(touchEvents.size()>0){

            // If ability has not been played this turn, allow hero ability to be tapped
            if(!abilityUsedThisTurn) {
                heroAbility.update(elapsedTime);

                if (heroAbility.isPushTriggered()) {
                    updateHeroAbilities();
                }
            }

        }

    }

    /**
     *Determines what action is executed when user taps on their hero ability
     */
    public void updateHeroAbilities(){

        if(hero.equals("EmperorCommodus")){
            commodusAbilityUsed();
        }

        if(hero.equals("Mars")){
            marsAbilityUsed();
        }

        if(hero.equals("Brutalus")){
            // TODO add temporary attack boost field
        }

        if(hero.equals("Sagira")){
            // Updated when playing cards are developed further
        }

        if(hero.equals("Hircine")){
            // TODO obtain player's opponent to use as target
        }

        if(hero.equals("Meridia")){
            meridiaAbilityUsed();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Hero Ability methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Methods that define the different hero abilities
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

    // Note: Equipping a new weapon replaces the player's currently equipped weapon
    public void commodusAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);
            setCurrentAttack(1);
            setCurrentWeaponDurability(2);
            weaponEquipped = true;
            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void marsAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);
            increaseArmor(2);
            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void brutalusAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            // When turn end button pressed, return attack to what it was previously
            increaseAttack(1);
            increaseArmor(1);

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void sagiraAbilityUsed(MinionCard targetMinion){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {
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

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            targetOpponent.receiveDamage(2);

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    public void meridiaAbilityUsed(){

        if(currentMana >= HERO_ABILITY_COST && !abilityUsedThisTurn && yourTurn) {
            reduceCurrentMana(HERO_ABILITY_COST);

            heal(2);

            abilityUsedThisTurn = true;
        }
        // Action can't be performed, provide feedback to player (Message box, some form of prompt)
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and setter methods
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

    // As these elements are "final", setters are not currently required, however, other elements of
    // the game may use their dimensions for alignment and positioning, (ie. board regions etc.)
    public float getPortraitHeight() {return PORTRAIT_HEIGHT;}
    public float getPortraitWidth() {return PORTRAIT_WIDTH;}
    public float getAbilityFrameHeight(){return  ABILITY_FRAME_HEIGHT;}
    public float getAbilityFrameWidth(){return  ABILITY_FRAME_WIDTH;}

    public boolean isWeaponEquipped() { return weaponEquipped; }
    public void setWeaponEquipped(boolean weaponEquipped) { this.weaponEquipped = weaponEquipped; }

    public boolean isAbilityUsedThisTurn() { return abilityUsedThisTurn; }
    public void setAbilityUsedThisTurn(boolean abilityUsedThisTurn) { this.abilityUsedThisTurn = abilityUsedThisTurn; }

}
