package uk.ac.qub.eeecs.game.TestClasses;

import uk.ac.qub.eeecs.game.Colosseum.Effect;

public class LogicSpell extends LogicCard {

    private Effect effect;
    private int magnitude;

    public LogicSpell(int coinCost, Effect effect, int magnitude) {
        super(coinCost);
        setEffect(effect);
        setMagnitude(magnitude);
    }

    public void play(LogicPlayer p) {
        switch (this.effect) {
            case NONE:
                break;
            case STEALTH:
                p.setPEffect(Effect.STEALTH);
                p.setMagnitude(magnitude);
                break;
            case COMBINATION:
                break;
            case VENOMOUS:
                if (p.isWeaponEquipped()) {
                    p.setPEffect(Effect.VENOMOUS);
                }
                break;
            case LIFESTEAL:
                if (p.isWeaponEquipped()) {
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
                p.heal(getMagnitude());
                break;
            case DAMAGE:
                p.receiveDamage(getMagnitude());
                break;
            default:
                break;
        }
    }

    public void play(LogicMinion mc) {
        switch (this.effect) {
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
    }

    /////////////////////////////////////////////////////////////////////
    // ACCESSOR AND MUTATOR METHODS
    /////////////////////////////////////////////////////////////////////

    public Effect getEffect() { return this.effect; }
    public void setEffect(Effect effect) { this.effect = effect; }

    public int getMagnitude() { return this.magnitude; }
    public void setMagnitude(int magnitude) { this.magnitude = magnitude; }

}
