package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * WeaponCard class used to represent a weapon with damage and a number of charges
 * @author Matthew, 05/12/2018
 */
public class WeaponCard extends Card {

    private int damage, charges; // Damage a weapon deals and number of charges it has

    /**
     * 'Default' Constructor
     * while not strictly default, it is as close as it can be
     *
     * @param gs The gamescreen the card is on
     */
    public WeaponCard(GameScreen gs) {
        super(0, 0, gs, 1, false, "");
    }

    public WeaponCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, int damage, int charges) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setDamage(damage);
        setCharges(charges);
    }

    /**
     * Copy Constructor
     *
     * @param x x coordinate of the card
     * @param y y coordinate of the card
     * @param wc card to copy
     */
    public WeaponCard(float x, float y, WeaponCard wc) {
        super(x, y, wc.getGameScreen(), wc.getCoinCost(), wc.getIsEnemy(), wc.getCardName());
        setDamage(wc.getDamage());
        setCharges(wc.getCharges());
    }

    @Override
    public void useLogic(Card thisCard, GameObject other) {
        if (thisCard instanceof WeaponCard && other instanceof Player)
            play((WeaponCard)thisCard, (Player)other);
    }

    /**
     * This will equip a weapon for the player to attack enemy minions/players with
     *
     * @param thisCard
     * @param p
     */
    public void play(WeaponCard thisCard, Player p) {
        p.setWeaponEquipped(true);
        p.setCurrentAttack(thisCard.getDamage());
        p.setCurrentWeaponDurability(thisCard.getCharges());

        removeCard();
    }

    /////////////////////////////////////////////////////////////////////
    // ACCESSOR AND MUTATOR METHODS
    /////////////////////////////////////////////////////////////////////

    public int getDamage() { return this.damage; }
    public void setDamage(int damage) { this.damage =  damage; }

    public int getCharges() { return this.charges; }
    public void setCharges(int charges) { this.charges = charges; }

}
