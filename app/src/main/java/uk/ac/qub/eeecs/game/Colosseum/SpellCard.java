package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class SpellCard extends Card {

    private Effect effect;
    private int magnitude;

    // 'Default' Constructor
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

    // Copy Constructor
    public SpellCard(float x, float y, SpellCard sc) {
        super(x, y, sc.getGameScreen(), sc.getCoinCost(), sc.getIsEnemy(), sc.getCardName());
        setEffect(sc.getEffect());
        setMagnitude(sc.getMagnitude());
    }

    @Override
    public void useLogic(Card thisCard, GameObject other) {
        if (other instanceof Player)
            play((SpellCard)thisCard, (Player)other);
        else
            play((SpellCard)thisCard, (MinionCard)other);
    }

    // For attacking the Player/AI
    // TODO flesh out further effects for Players
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
                p.heal(getMagnitude());
                break;
            case DAMAGE:
                p.receiveDamage(getMagnitude());
                break;
            default:
                break;
        }
    }

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
