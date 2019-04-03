package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * SpellCard class used to represent a spell with a variety of effects to choose from
 * @author Matthew, 05/12/2018
 */
public class SpellCard extends Card {

    private Effect effect; // Effect the spell has
    private int magnitude; // The size of whatever effect is being applied, i.e. Damage will be the damage to be taken by a target

    /**
     * 'Default' Constructor
     * while not strictly default, it is as close as it can be
     *
     * @param gs The gamescreen the card is on
     */
    public SpellCard(GameScreen gs) {
        super(0, 0, gs, 1, false, "");
        setEffect(Effect.DAMAGE);
        setMagnitude(0);
    }

    public SpellCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, Effect effect) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setEffect(effect);
        setMagnitude(0);
    }

    public SpellCard(float x, float y, GameScreen gs, int coinCost, boolean isEnemy, String cardName, Effect effect, int magnitude) {
        super(x, y, gs, coinCost, isEnemy, cardName);
        setEffect(effect);
        setMagnitude(magnitude);
    }

    /**
     * Copy Constructor
     *
     * @param x x coordinate of the card
     * @param y y coordinate of the card
     * @param sc card to copy
     */
    public SpellCard(float x, float y, SpellCard sc) {
        super(x, y, sc.getGameScreen(), sc.getCoinCost(), sc.getIsEnemy(), sc.getCardName());
        setEffect(sc.getEffect());
        setMagnitude(sc.getMagnitude());
    }

    @Override
    public void useLogic(Card thisCard, GameObject other) {
        if (thisCard instanceof SpellCard && other instanceof Player)
            play((SpellCard)thisCard, (Player)other);
        else if (thisCard instanceof SpellCard && other instanceof MinionCard)
            play((SpellCard)thisCard, (MinionCard)other);
    }

    // For attacking the Player/AI
    // TODO flesh out further effects for Players

    /**
     * This play method will allow for a spell effect to be used on a player or ai
     *
     * @param thisCard Card being used
     * @param p Player/AI to affect
     */
    public void play(SpellCard thisCard, Player p) {
        switch (thisCard.effect) {
            case NONE:
                break;
            case STEALTH:
                // give the player stealth for x turns
                p.setPEffect(Effect.STEALTH);
                p.setEDuration(magnitude);
                break;
            case COMBINATION:
                break;
            case VENOMOUS:
                if (p.isWeaponEquipped()) {
                    // set effect to venomous
                    p.setPEffect(Effect.VENOMOUS);
                }
                break;
            case LIFESTEAL:
                if (p.isWeaponEquipped()) {
                    // set effect to lifesteal
                    p.setPEffect(Effect.LIFESTEAL);
                }
                break;
            case DISABLE:
                break;
            case CHOOSEONE:
                break;
            case SILENCE:
                break;
            case SPELLDAMAGE:
                break;
            case DOUBLESTRIKE:
                break;
            case HEAL:
                // heal the player
                p.heal(getMagnitude());
                break;
            case DAMAGE:
                // damage the player
                p.receiveDamage(getMagnitude());
                break;
            default:
                break;
        }

        removeCard();
    }

    /**
     * This play method will allow for a spell effect to be used on a minion
     *
     * @param thisCard Card being used
     * @param mc minion to be affected
     */
    public void play(SpellCard thisCard, MinionCard mc) {
        switch (thisCard.effect) {
            case NONE:
                break;
            case TAUNT:
                mc.setEffect(Effect.TAUNT);
                break;
            case DYINGBREATH:
                mc.setEffect(Effect.DYINGBREATH);
                break;
            case PARRY:
                mc.setEffect(Effect.PARRY);
                break;
            case STEALTH:
                mc.setEffect(Effect.STEALTH);
                break;
            case COMBINATION:
                break;
            case VENOMOUS:
                mc.setEffect(Effect.VENOMOUS);
                break;
            case LIFESTEAL:
                mc.setEffect(Effect.LIFESTEAL);
                break;
            case DISABLE:
                break;
            case CHOOSEONE:
                break;
            case SILENCE:
                mc.setEffect(Effect.NONE);
                break;
            case SPELLDAMAGE:
                mc.setEffect(Effect.SPELLDAMAGE);
                break;
            case DOUBLESTRIKE:
                mc.setEffect(Effect.DOUBLESTRIKE);
                break;
            case HEAL:
                mc.heal(getMagnitude());
                break;
            case DAMAGE:
                mc.takeDamage(getMagnitude());
                mc.checkHealth();
                break;
            default:
                break;
        }

        removeCard();
    }

    /////////////////////////////////////////////////////////////////////
    // ACCESSOR AND MUTATOR METHODS
    /////////////////////////////////////////////////////////////////////

    public Effect getEffect() { return this.effect; }
    public void setEffect(Effect effect) { this.effect = effect; }

    public int getMagnitude() { return this.magnitude; }
    public void setMagnitude(int magnitude) { this.magnitude = magnitude; }

}
