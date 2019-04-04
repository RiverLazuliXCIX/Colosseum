package uk.ac.qub.eeecs.game.TestClasses;

import java.util.ArrayList;
import uk.ac.qub.eeecs.game.Colosseum.Effect;

public class LogicPlayer {

    private int maxHealth;
    private int health;
    private Effect effect;
    private int magnitude;
    private boolean weaponEquipped;
    private int attack;
    private int charges;

    public LogicPlayer() {
        maxHealth = 30;
        health = 30;
        effect = Effect.NONE;
        magnitude = 0;
        weaponEquipped = false;
        attack = 0;
        charges = 0;
    }

    public boolean hasTaunts(ArrayList<LogicMinion> enemyMinions) {
        for (LogicMinion mc : enemyMinions) {
            if (mc.getEffect() == Effect.TAUNT) return true;
        }

        return false;
    }

    public void attackEnemy(LogicPlayer enemyPlayer, ArrayList<LogicMinion> enemyMinions) {
        if (hasTaunts(enemyMinions)) return;
        if (!weaponEquipped && charges < 1) return;

        if (effect == Effect.VENOMOUS) enemyPlayer.setHealth(0);
        else enemyPlayer.receiveDamage(this.getAttack());
        if (effect == Effect.LIFESTEAL)
            heal(attack);

        if (enemyPlayer.weaponEquipped && enemyPlayer.getPEffect() == Effect.VENOMOUS)
            this.setHealth(0);
        else this.receiveDamage(enemyPlayer.getAttack());
        if (enemyPlayer.weaponEquipped && enemyPlayer.getPEffect() == Effect.LIFESTEAL)
            enemyPlayer.heal(enemyPlayer.attack);

        charges--;
        if (charges == 0) {
            weaponEquipped = false;
            attack = 0;
        }
    }

    public void attackEnemy(LogicMinion enemyMinion, ArrayList<LogicMinion> enemyMinions) {
        if (hasTaunts(enemyMinions)) return;
        if (!weaponEquipped && charges < 1) return;

        enemyMinion.takeDamage(this.attack);
        this.receiveDamage(enemyMinion.getAttack());

        charges--;
    }

    public void receiveDamage(int damage) { health -= damage; checkHealth(); }

    public void heal(int amount) {
        if (amount + health > maxHealth) health = maxHealth;
        else health += amount;
    }

    public boolean checkHealth() { return health > 0; }

    public int getHealth() { return this.health; }
    public void setHealth(int health) { this.health = health; }

    public Effect getPEffect() { return this.effect; }
    public void setPEffect(Effect effect) { this.effect = effect; }

    public int getMagnitude() { return this.magnitude; }
    public void setMagnitude(int magnitude) { this.magnitude = magnitude; }

    public boolean isWeaponEquipped() { return weaponEquipped; }
    public void setWeaponEquipped(boolean weaponEquipped) { this.weaponEquipped = weaponEquipped; }

    public int getAttack() { return this.attack; }
    public void setAttack(int attack) { this.attack = attack; }

    public int getCharges() { return this.charges; }
    public void setCharges(int charges) { this.charges = charges; }

}
