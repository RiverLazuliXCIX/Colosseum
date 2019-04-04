package uk.ac.qub.eeecs.gage;

import android.util.Log;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.Colosseum.Effect;
import uk.ac.qub.eeecs.game.TestClasses.*;

import org.junit.*;

import static org.junit.Assert.*;

public class CardLogicTests {

    @Test
    public void createMinion() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        assertNotNull(m);
    }

    @Test
    public void getCoinCostTest() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        assertEquals(2, m.getCoinCost());
    }

    @Test
    public void setCoinCostTest() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);
        m.setCoinCost(3);

        assertEquals(3, m.getCoinCost());
    }

    @Test
    public void checkTauntsT() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em2 = new LogicMinion(2, 1, 4, Effect.TAUNT);

        ArrayList<LogicMinion> ems = new ArrayList<>();
        ems.add(em);
        ems.add(em2);

        assertEquals(true, m.hasTaunts(ems));
    }

    @Test
    public void checkTauntsF() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em2 = new LogicMinion(2, 1, 4, Effect.NONE);

        ArrayList<LogicMinion> ems = new ArrayList<>();
        ems.add(em);
        ems.add(em2);

        assertEquals(false, m.hasTaunts(ems));
    }

    @Test
    public void takeDamageTest() {
        LogicMinion m = new LogicMinion(2, 2, 5, Effect.NONE);
        m.takeDamage(3);

        assertEquals(true, m.checkHealth());
        assertEquals(2, m.getHealth());
    }

    @Test
    public void takeFatalDamageTest() {
        LogicMinion m = new LogicMinion(2, 2, 5, Effect.NONE);
        m.takeDamage(7);

        assertEquals(false, m.checkHealth());
        assertEquals(-2, m.getHealth());
    }

    @Test
    public void minionAttack() {
        LogicMinion m = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em = new LogicMinion(2, 1, 4, Effect.NONE);

        m.attackEnemy(em, new ArrayList<LogicMinion>());

        assertEquals(true, m.checkHealth());
        assertEquals(1, m.getHealth());

        assertEquals(true, em.checkHealth());
        assertEquals(2, em.getHealth());
    }

    @Test
    public void minionParryAttack() {
        LogicMinion m = new LogicMinion(2, 2, 2, Effect.PARRY);
        LogicMinion em = new LogicMinion(2, 1, 4, Effect.NONE);

        assertEquals(Effect.PARRY, m.getEffect());

        m.attackEnemy(em, new ArrayList<LogicMinion>());

        assertEquals(Effect.NONE, m.getEffect());
    }

    @Test
    public void minionVenomAttack() {
        LogicMinion m = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em = new LogicMinion(2, 1, 4, Effect.VENOMOUS);

        m.attackEnemy(em, new ArrayList<LogicMinion>());

        assertEquals(false, m.checkHealth());
        assertEquals(0, m.getHealth());
    }

    @Test
    public void minionNotTauntAttack() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em2 = new LogicMinion(2, 1, 4, Effect.TAUNT);

        ArrayList<LogicMinion> ems = new ArrayList<>();
        ems.add(em);
        ems.add(em2);

        m.attackEnemy(em, ems);

        assertEquals(true, m.checkHealth());
        assertEquals(2, m.getHealth());

        assertEquals(true, em.checkHealth());
        assertEquals(2, em.getHealth());
    }

    @Test
    public void minionTauntAttack() {
        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em2 = new LogicMinion(2, 1, 4, Effect.TAUNT);

        ArrayList<LogicMinion> ems = new ArrayList<>();
        ems.add(em);
        ems.add(em2);

        m.attackEnemy(em2, ems);

        assertEquals(true, m.checkHealth());
        assertEquals(1, m.getHealth());

        assertEquals(true, em2.checkHealth());
        assertEquals(1, em2.getHealth());
    }

    @Test
    public void minionPlayerAttackTaunts() {
        LogicPlayer p = new LogicPlayer();


        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em2 = new LogicMinion(2, 1, 4, Effect.TAUNT);

        ArrayList<LogicMinion> ems = new ArrayList<>();
        ems.add(em);
        ems.add(em2);

        m.attackEnemy(p, ems);

        assertEquals(true, p.checkHealth());
        assertEquals(30, p.getHealth());
    }

    @Test
    public void minionPlayerAttackNoTaunts() {
        LogicPlayer p = new LogicPlayer();
        p.setHealth(2);

        LogicMinion m = new LogicMinion(2, 3, 2, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);
        LogicMinion em2 = new LogicMinion(2, 1, 4, Effect.NONE);

        ArrayList<LogicMinion> ems = new ArrayList<>();
        ems.add(em);
        ems.add(em2);

        m.attackEnemy(p, ems);

        assertEquals(false, p.checkHealth());
        assertEquals(-1, p.getHealth());
    }

    @Test
    public void minionKillAttack() {
        LogicMinion m = new LogicMinion(2, 5, 3, Effect.NONE);

        LogicMinion em = new LogicMinion(2, 2, 2, Effect.NONE);

        m.attackEnemy(em, new ArrayList<LogicMinion>());

        assertEquals(1, m.getHealth());

        assertEquals(false, em.checkHealth());
        assertEquals(-3, em.getHealth());
    }

    @Test
    public void minionHeal() {
        LogicMinion m = new LogicMinion(2, 5, 10, Effect.NONE);

        m.setHealth(3);
        m.heal(5);

        assertEquals(8, m.getHealth());
    }

    @Test
    public void minionOverHeal() {
        LogicMinion m = new LogicMinion(2, 5, 10, Effect.NONE);

        m.setHealth(3);
        m.heal(20);

        assertEquals(10, m.getHealth());
    }

    @Test
    public void createWeapon() {
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        assertNotNull(w);
    }

    @Test
    public void equipWeapon() {
        LogicPlayer p = new LogicPlayer();
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        w.play(p);

        assertEquals(3, p.getAttack());
        assertEquals(2, p.getCharges());
    }

    @Test
    public void playerAttackTaunts() {
        LogicPlayer p = new LogicPlayer();
        LogicPlayer eP = new LogicPlayer();
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        LogicMinion eM = new LogicMinion(2, 1, 3, Effect.TAUNT);
        ArrayList<LogicMinion> eMs = new ArrayList<>();
        eMs.add(eM);

        w.play(p);
        p.attackEnemy(eP, eMs);

        assertEquals(true, eP.checkHealth());
        assertEquals(30, eP.getHealth());
    }

    @Test
    public void playerAttackNoTaunts() {
        LogicPlayer p = new LogicPlayer();
        LogicPlayer eP = new LogicPlayer();
        eP.setHealth(5);
        LogicWeapon w = new LogicWeapon(2, 5, 1);

        w.play(p);
        p.attackEnemy(eP, new ArrayList<LogicMinion>());

        assertEquals(false, eP.checkHealth());
        assertEquals(0, eP.getHealth());
    }

    @Test
    public void playerAttackMinionTaunts() {
        LogicPlayer p = new LogicPlayer();
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        LogicMinion eM = new LogicMinion(2, 1, 3, Effect.TAUNT);
        LogicMinion eM2 = new LogicMinion(2, 3, 1, Effect.NONE);
        ArrayList<LogicMinion> eMs = new ArrayList<>();
        eMs.add(eM);
        eMs.add(eM2);

        w.play(p);
        p.attackEnemy(eM2, eMs);

        assertEquals(true, eM2.checkHealth());
        assertEquals(1, eM2.getHealth());
    }

    @Test
    public void playerAttackMinionNoTaunts() {
        LogicPlayer p = new LogicPlayer();
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        LogicMinion eM = new LogicMinion(2, 3, 1, Effect.NONE);
        ArrayList<LogicMinion> eMs = new ArrayList<>();
        eMs.add(eM);

        w.play(p);
        p.attackEnemy(eM, eMs);

        assertEquals(false, eM.checkHealth());
        assertEquals(-2, eM.getHealth());
    }

    @Test
    public void createSpell() {
        LogicSpell s = new LogicSpell(2, Effect.DAMAGE, 4);

        assertNotNull(s);
    }

    @Test
    public void spellDamageMinion() {
        LogicSpell s = new LogicSpell(2, Effect.DAMAGE, 4);
        LogicMinion m = new LogicMinion(1, 1, 1, Effect.NONE);

        s.play(m);

        assertEquals(false, m.checkHealth());
        assertEquals(-3, m.getHealth());
    }

    @Test
    public void spellDamagePlayer() {
        LogicSpell s = new LogicSpell(2, Effect.DAMAGE, 4);
        LogicPlayer p = new LogicPlayer();

        s.play(p);

        assertEquals(true, p.checkHealth());
        assertEquals(26, p.getHealth());
    }

    @Test
    public void spellHealMinion() {
        LogicSpell s = new LogicSpell(1, Effect.HEAL, 3);
        LogicMinion m = new LogicMinion(1, 1, 4, Effect.NONE);
        m.setHealth(2);

        s.play(m);

        assertEquals(true, m.checkHealth());
        assertEquals(4, m.getHealth());
    }

    @Test
    public void spellHealPlayer() {
        LogicSpell s = new LogicSpell(1, Effect.HEAL, 3);
        LogicPlayer p = new LogicPlayer();
        p.setHealth(12);

        s.play(p);

        assertEquals(true, p.checkHealth());
        assertEquals(15, p.getHealth());
    }

    @Test
    public void spellMakeMinionTaunt() {
        LogicSpell s = new LogicSpell(1, Effect.TAUNT, 0);
        LogicMinion m = new LogicMinion(1, 1, 4, Effect.NONE);

        s.play(m);

        assertEquals(Effect.TAUNT, m.getEffect());
    }

    @Test
    public void spellMakeMinionParry() {
        LogicSpell s = new LogicSpell(1, Effect.PARRY, 0);
        LogicMinion m = new LogicMinion(1, 1, 4, Effect.NONE);

        s.play(m);

        assertEquals(Effect.PARRY, m.getEffect());
    }

    @Test
    public void spellMakeMinionVenomous() {
        LogicSpell s = new LogicSpell(1, Effect.VENOMOUS, 0);
        LogicMinion m = new LogicMinion(1, 1, 4, Effect.NONE);

        s.play(m);

        assertEquals(Effect.VENOMOUS, m.getEffect());
    }

    @Test
    public void spellMakePlayerVenomousNoWeapon() {
        LogicPlayer p = new LogicPlayer();
        LogicSpell s = new LogicSpell(3, Effect.VENOMOUS, 0);

        s.play(p);

        assertEquals(Effect.NONE, p.getPEffect());
    }

    @Test
    public void spellMakePlayerVenomousWeapon() {
        LogicPlayer p = new LogicPlayer();
        LogicSpell s = new LogicSpell(3, Effect.VENOMOUS, 0);
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        w.play(p);
        s.play(p);

        assertEquals(Effect.VENOMOUS, p.getPEffect());
    }

    @Test
    public void spellMakePlayerLifestealWeapon() {
        LogicPlayer p = new LogicPlayer();
        LogicSpell s = new LogicSpell(3, Effect.LIFESTEAL, 0);
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        w.play(p);
        s.play(p);

        assertEquals(Effect.LIFESTEAL, p.getPEffect());
    }

    @Test
    public void playerVenomousAttack() {
        LogicPlayer p = new LogicPlayer();
        LogicPlayer p2 = new LogicPlayer();
        LogicSpell s = new LogicSpell(3, Effect.VENOMOUS, 0);
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        w.play(p);
        s.play(p);

        p.attackEnemy(p2, new ArrayList<LogicMinion>());

        assertEquals(true, p.checkHealth());
        assertEquals(30, p.getHealth());
        assertEquals(true, p2.checkHealth());
        assertEquals(27, p2.getHealth());
    }

    @Test
    public void playerLifestealAttack() {
        LogicPlayer p = new LogicPlayer();
        LogicPlayer p2 = new LogicPlayer();
        LogicSpell s = new LogicSpell(3, Effect.LIFESTEAL, 0);
        LogicWeapon w = new LogicWeapon(2, 3, 2);

        p.setHealth(20);
        w.play(p);
        s.play(p);

        p.attackEnemy(p2, new ArrayList<LogicMinion>());

        assertEquals(true, p.checkHealth());
        assertEquals(23, p.getHealth());
        assertEquals(true, p2.checkHealth());
        assertEquals(27, p2.getHealth());
    }

}