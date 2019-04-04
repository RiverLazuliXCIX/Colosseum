package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.CoinTossScreen;
import uk.ac.qub.eeecs.game.colosseumDemoScreen;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;

/**
 * MinionCard class used to represent minions placed on the board to attack against the opponent
 * @author Matthew, 05/12/2018
 */
public class MinionCard extends Card {

    private int attack; // The damage dealt to enemy minions or opponents
    private int maxHealth; // The maximum amount of health a minion can have
    private int health; // The current health a minion has
    private Effect mEffect; // Any effect a minion has
    private boolean canAttack; // This will be set to false when played and after attacks

    /**
     * 'Default' Constructor
     * while not strictly default, it is as close as it can be
     *
     * @param gs The gamescreen the card is on
     */
    public MinionCard(GameScreen gs) {
        super(0, 0, gs, 1, false, "");
        setAttack(1);
        setMaxHealth(1);
        setHealth(1);
        mEffect = Effect.NONE;
        setCanAttack(false);
    }

    public MinionCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, int attack, int health) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setAttack(attack);
        setMaxHealth(health);
        setHealth(health);
        setEffect(Effect.NONE);
        setCanAttack(false);
    }

    public MinionCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, int attack, int health, Effect mEffect) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setAttack(attack);
        setMaxHealth(health);
        setHealth(health);
        setEffect(mEffect);
        setCanAttack(mEffect == Effect.RUSH);
    }

    public MinionCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, int attack, int maxHealth, int health, Effect mEffect) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setAttack(attack);
        setMaxHealth(maxHealth);
        setHealth(health);
        setEffect(mEffect);
        setCanAttack(mEffect == Effect.RUSH);
    }

    /**
     * Copy Constructor
     *
     * @param x x coordinate of the card
     * @param y y coordinate of the card
     * @param mc card to copy
     */
    public MinionCard(float x, float y, MinionCard mc) {
        super(x, y, mc.getGameScreen(), mc.getCoinCost(), mc.getIsEnemy(), mc.getCardName());
        setAttack(mc.getAttack());
        setMaxHealth(mc.getMaxHealth());
        setHealth(mc.getHealth());
        setEffect(mc.getEffect());
        setCanAttack(mc.getCanAttack());
    }

    @Override
    public void useLogic(Card thisCard, GameObject other) {
        if(thisCard instanceof MinionCard && other instanceof MinionCard)
            attackEnemy((MinionCard) thisCard, (MinionCard) other);
        else if (thisCard instanceof MinionCard && other instanceof Player)
            attackEnemy((MinionCard) thisCard, (Player)other);
    }

    /**
     * This method will be used to determine if there are any enemy taunts on the board to allow for
     * attacks to be prevented on minions without the taunt effect
     *
     * @return boolean: whether or not there are enemy taunts on the board
     */
    public boolean hasTaunts() {
        // Get the game screen to access the correct regions
        CoinTossScreen cts = (CoinTossScreen) mGameScreen;
        ActiveRegion ar;

        // if the current card is a friendly card get the opponent's region
        if (!getIsEnemy()) ar = cts.getCds().getOpponentActiveRegion();
        // if the card is an enemy card get the player's region
        else ar = cts.getCds().getPlayerActiveRegion();

        MinionCard mc;
        // search the region for taunts
        for (Card c : ar.getCardsInRegion()) {
            // only check taunts on minion cards
            if (c instanceof MinionCard) {
                mc = (MinionCard) c;
                // if a taunt is found, return true
                if (mc.getEffect() == Effect.TAUNT) return true;
            }
        }

        // otherwise return false
        return false;
    }

    /**
     * This method will be used to allow for a minion to attack another minion
     *
     * @param thisCard Attacking card
     * @param eMinionCard Defending card
     */
    public void attackEnemy(MinionCard thisCard, MinionCard eMinionCard) {
        // if the card cannot attack, do not let it
        if (!thisCard.getCanAttack()) return;
        // add a check for any enemy minions on the board with taunts
        // if there are any taunts on the board and the minion being attacked doesnt have a taunt, return
        if (thisCard.hasTaunts() && eMinionCard.getEffect() != Effect.TAUNT) return;

        if (eMinionCard.getEffect() == Effect.PARRY) eMinionCard.setEffect(Effect.NONE);
        else if (thisCard.getEffect() == Effect.VENOMOUS) eMinionCard.setHealth(0);
        else eMinionCard.takeDamage(thisCard.attack);

        if (thisCard.getEffect() == Effect.PARRY) thisCard.setEffect(Effect.NONE);
        else if (eMinionCard.getEffect() == Effect.VENOMOUS) thisCard.setHealth(0);
        else thisCard.takeDamage(eMinionCard.getAttack());

        // once the card has attacked, don't let it attack again
        thisCard.setCanAttack(false);

        // check health after attacks so the enemy object can still exist if its health falls below 0
        eMinionCard.checkHealth();
        thisCard.checkHealth();
    }

    /* Another attack method will be required for attacking the enemy hero
       Check for taunts on the board
       Enemy hero will take the minions attack as damage
       MinionCard will take damage for any weapon the enemy has equipped
       Check both healths
       public void attackEnemy([Enemy/Player/Hero etc.] [enemy/player/hero]) {} */

    /**
     * This method will allow for minions to be able to attack the enemy hero
     *
     * @param hero Hero to be attacked (allows for Player and AIOpponent)
     */
    public void attackEnemy(MinionCard thisCard, Player hero) {
        // If the minion has already attacked, don't let it attack again
        if (!thisCard.getCanAttack()) return;
        // if there are any taunts on the board, do not attack
        if (hasTaunts()) return;
        hero.receiveDamage(thisCard.attack);
        thisCard.setCanAttack(false);
    }

    /**
     * This will allow the minion to take damage from a variety of sources
     * HEALTH IS NOT CHECKED HERE, this is to ensure that both cards will still be available to attack
     * HEALTH SHOULD BE CHECKED IN INDIVIDUAL DAMAGING METHODS
     *
     * @param damage amount of damage to be taken
     */
    public void takeDamage(int damage) {
        this.health -= damage;
    }

    /**
     * This method will heal a minion but it will not exceed its maximum health
     *
     * @param healAmount the amount the minion should be healed by
     */
    public void heal(int healAmount) {
        // do not exceed max health
        if (health + healAmount > maxHealth) health = maxHealth;
        else health += healAmount;
    }

    /**
     * This method will check a minions health to see if it should be removed from play
     */
    public void checkHealth(){
        if (health <= 0) {
            // remove card from board and add to the player's graveyard
            // player.deck.addToGraveyard(this);

            setHealth(0);
            if (this.getIsEnemy());
                this.flipCard();
            removeCard();
        }
    }

    /////////////////////////////////////////////////////////////////////
    // ACCESSOR AND MUTATOR METHODS
    /////////////////////////////////////////////////////////////////////

    public int getAttack() { return this.attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getMaxHealth() { return this.maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getHealth() { return this.health; }
    public void setHealth(int health) { this.health = health; }

    public Effect getEffect() { return this.mEffect; }
    public void setEffect(Effect mEffect) { this.mEffect = mEffect; }

    public boolean getCanAttack() { return this.canAttack; }
    public void setCanAttack(boolean canAttack) { this.canAttack = canAttack; }

}