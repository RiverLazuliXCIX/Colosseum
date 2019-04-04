package uk.ac.qub.eeecs.game.TestClasses;

public class LogicWeapon extends LogicCard {

    private int damage, charges;

    public LogicWeapon(int coinCost, int damage, int charges) {
        super(coinCost);
        setDamage(damage);
        setCharges(charges);
    }

    public void play(LogicPlayer p) {
        p.setWeaponEquipped(true);
        p.setAttack(getDamage());
        p.setCharges(getCharges());
    }

    /////////////////////////////////////////////////////////////////////
    // ACCESSOR AND MUTATOR METHODS
    /////////////////////////////////////////////////////////////////////

    public int getDamage() { return this.damage; }
    public void setDamage(int damage) { this.damage =  damage; }

    public int getCharges() { return this.charges; }
    public void setCharges(int charges) { this.charges = charges; }

}
