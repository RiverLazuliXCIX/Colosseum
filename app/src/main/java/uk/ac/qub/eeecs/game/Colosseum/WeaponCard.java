package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameScreen;

public class WeaponCard extends Card {

    private int damage, charges;

    // 'Default' constructor
    public WeaponCard(GameScreen gs) {
        super(0, 0, gs, 1, false);
    }

    public WeaponCard(float x, float y, GameScreen gs, int coinCost, int damage, int charges) {
        super(x, y, gs, coinCost, false);
        setDamage(damage);
        setCharges(charges);
    }

    // Copy Constructor
    public WeaponCard(float x, float y, WeaponCard wc) {
        super(x, y, wc.getGameScreen(), wc.getCoinCost(), wc.getIsEnemy());
        setDamage(wc.getDamage());
        setCharges(wc.getCharges());
    }

    // Will work for Player and AI
    public void play(Player p) {
        p.setWeaponEquipped(true);
        p.setCurrentAttack(getDamage());
        p.setCurrentWeaponDurability(getCharges());
    }

    public int getDamage() { return this.damage; }
    public void setDamage(int damage) { this.damage =  damage; }

    public int getCharges() { return this.charges; }
    public void setCharges(int charges) { this.charges = charges; }

}
