package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameScreen;

public class SpellCard extends Card {

    private Effect effect;
    private int magnitude;

    // 'Default' Constructor
    public SpellCard(GameScreen gs) {
        super(0, 0, gs, 1);
        setEffect(Effect.DAMAGE);
        setMagnitude(0);
    }

    public SpellCard(float x, float y, GameScreen gs, int coinCost, Effect effect) {
        super(x, y, gs, coinCost);
        setEffect(effect);
        setMagnitude(0);
    }

    public SpellCard(float x, float y, GameScreen gs, int coinCost, Effect effect, int magnitude) {
        super(x, y, gs, coinCost);
        setEffect(effect);
        setMagnitude(magnitude);
    }

    // Copy Constructor
    public SpellCard(float x, float y, SpellCard sc) {
        super(x, y, sc.getGameScreen(), sc.getCoinCost());
        setEffect(sc.getEffect());
        setMagnitude(sc.getMagnitude());
    }

    // For attacking the Player/AI
    // TODO flesh out further effects for Players
    public void play(Player p) {
        switch (effect) {
            case NONE:
                break;
            case STEALTH:
                // give the player stealth for x turns
                break;
            case COMBINATION:
                break;
            case VENOMOUS:
                if (p.isWeaponEquipped()) {
                    // set effect to venomous
                }
                break;
            case LIFESTEAL:
                if (p.isWeaponEquipped()) {
                    // set effect to lifesteal
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

    public void play(MinionCard mc) {
        switch (effect) {
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
                break;
            default:
                break;
        }
    }

    public Effect getEffect() { return this.effect; }
    public void setEffect(Effect effect) { this.effect = effect; }

    public int getMagnitude() { return this.magnitude; }
    public void setMagnitude(int magnitude) { this.magnitude = magnitude; }

}
