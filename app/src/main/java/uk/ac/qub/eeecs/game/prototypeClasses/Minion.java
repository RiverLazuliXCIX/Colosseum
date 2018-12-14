package uk.ac.qub.eeecs.game.prototypeClasses;

/**
 * Created by Matthew, 05/12/2018
 */
public class Minion extends Card {

    private int attack;
    private int maxHealth;
    private int health;
    private Effect mEffect;

    public Minion() {
        super();
        setAttack(0);
        setMaxHealth(1);
        setHealth(1);
        mEffect = Effect.NONE;
    }

    public Minion(int coinCost, int attack, int health, Effect mEffect) {
        super(coinCost);
        setAttack(attack);
        setMaxHealth(health);
        setHealth(health);
        setEffect(mEffect);
    }

    public Minion(int coinCost, int attack, int maxHealth, int health, Effect mEffect) {
        super(coinCost);
        setAttack(attack);
        setMaxHealth(maxHealth);
        setHealth(health);
        setEffect(mEffect);
    }

    public void attackEnemy(Minion eMinion) {
        // add a check for any enemy minions on the board with taunts

        eMinion.takeDamage(this.attack);
        takeDamage(eMinion.getAttack());

        // check health after attacks so the enemy object can still exist if its health falls below 0
        eMinion.checkHealth();
        checkHealth();
    }

    // Another attack method will be required for attacking the enemy hero
    // Check for taunts on the board
    // Enemy hero will take the minions attack as damage
    // Minion will take damage for any weapon the enemy has equipped
    // Check both healths
    // public void attackEnemy([Enemy/Player/Hero etc.] [enemy/player/hero]) {}

    public void takeDamage(int damage) { this.health -= damage; }

    public void heal(int healAmount) {
        if (health + healAmount > maxHealth) health = maxHealth;
        else health += healAmount;
    }

    public void checkHealth() {
        if (health <= 0) {
            // remove card from board and add to the player's graveyard
            // player.deck.addToGraveyard(this);
        }
    }

    public int getAttack() {
        return this.attack;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getMaxHealth() { return this.maxHealth; }
    public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; }

    public int getHealth() { return this.health; }
    public void setHealth(int health) { this.health = health; }

    public Effect getEffect() { return this.mEffect; }
    public void setEffect(Effect mEffect) { this.mEffect = mEffect; }

}