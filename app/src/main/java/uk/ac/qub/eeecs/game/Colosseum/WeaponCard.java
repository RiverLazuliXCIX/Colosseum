package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class WeaponCard extends Card {

    private int damage, charges;

    // 'Default' constructor
    public WeaponCard(GameScreen gs) {
        super(0, 0, gs, 1, false, "");
    }

    public WeaponCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, int damage, int charges) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setDamage(damage);
        setCharges(charges);
    }

    // Copy Constructor
    public WeaponCard(float x, float y, WeaponCard wc) {
        super(x, y, wc.getGameScreen(), wc.getCoinCost(), wc.getIsEnemy(), wc.getCardName());
        setDamage(wc.getDamage());
        setCharges(wc.getCharges());
    }

    @Override
    public void useLogic(Card thisCard, GameObject other) {
        play((WeaponCard)thisCard, (Player)other);
    }

    // Will work for Player and AI
    public void play(WeaponCard thisCard, Player p) {
        p.setWeaponEquipped(true);
        p.setCurrentAttack(thisCard.getDamage());
        p.setCurrentWeaponDurability(thisCard.getCharges());
    }

    public int getDamage() { return this.damage; }
    public void setDamage(int damage) { this.damage =  damage; }

    public int getCharges() { return this.charges; }
    public void setCharges(int charges) { this.charges = charges; }

}
