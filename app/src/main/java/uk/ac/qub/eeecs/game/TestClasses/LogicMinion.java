package uk.ac.qub.eeecs.game.TestClasses;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.Colosseum.Effect;

public class LogicMinion extends LogicCard {

    private int attack;
    private int maxHealth;
    private int health;
    private Effect mEffect;

    public LogicMinion(int coinCost, int attack, int health, Effect effect) {
        super(coinCost);
        setAttack(attack);
        setMaxHealth(health);
        setHealth(health);
        setEffect(effect);
    }

    public boolean hasTaunts(ArrayList<LogicMinion> enemyMinions) {
        for (LogicMinion mc : enemyMinions) {
            if (mc.getEffect() == Effect.TAUNT) return true;
        }

        return false;
    }

    public void attackEnemy(LogicMinion eMinionCard, ArrayList<LogicMinion> enemyMinions) {
        if (this.hasTaunts(enemyMinions) && eMinionCard.getEffect() != Effect.TAUNT) return;

        if (eMinionCard.getEffect() == Effect.PARRY) eMinionCard.setEffect(Effect.NONE);
        else if (this.getEffect() == Effect.VENOMOUS) eMinionCard.setHealth(0);
        else eMinionCard.takeDamage(this.attack);

        if (this.getEffect() == Effect.PARRY) this.setEffect(Effect.NONE);
        else if (eMinionCard.getEffect() == Effect.VENOMOUS) this.setHealth(0);
        else this.takeDamage(eMinionCard.getAttack());
    }

    public void attackEnemy(LogicPlayer hero, ArrayList<LogicMinion> enemyMinions) {
        if (hasTaunts(enemyMinions)) return;

        hero.receiveDamage(this.getAttack());
    }

    public void takeDamage(int damage) {
        this.health -= damage;
    }

    public void heal(int healAmount) {
        if (health + healAmount > maxHealth) health = maxHealth;
        else health += healAmount;
    }

    public boolean checkHealth() { return health > 0; }

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

}
