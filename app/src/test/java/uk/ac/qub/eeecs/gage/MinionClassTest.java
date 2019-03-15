package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.Effect;
import uk.ac.qub.eeecs.game.Colosseum.MinionCard;
import uk.ac.qub.eeecs.game.colosseumDemoScreen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MinionClassTest {

    @Mock
    private colosseumDemoScreen mDemoScreen;

    @Mock
    private GameScreen gameScreen;

    @Before
    public void setUp() {


    }

    @Test
    public void setAttackTest() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setAttack(9);

        assertEquals(newCard.getAttack(), 9);
    }

    @Test
    public void setMaxHealthTest() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setMaxHealth(9);

        assertEquals(newCard.getMaxHealth(), 9);
    }

    @Test
    public void setHealthTest() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setHealth(9);

        assertEquals(newCard.getHealth(), 9);
    }

    @Test
    public void setEffectTest() {
        MinionCard newCard = new MinionCard(gameScreen);
        Effect e = Effect.HEAL;
        newCard.setEffect(e);

        assertEquals(newCard.getEffect(), e);
    }

    @Test
    public void healTest_MaxHealth() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setHealth(30);

        newCard.heal(5);

        assertEquals(newCard.getHealth(), 30);
    }

    @Test
    public void healTest_NearMaxHealth() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setHealth(29);

        newCard.heal(5);

        assertEquals(newCard.getHealth(), 30);
    }

    @Test
    public void healTest_Health() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setHealth(10);

        newCard.heal(5);

        assertEquals(newCard.getHealth(), 15);
    }

    @Test
    public void takeDamageTest() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setHealth(10);

        newCard.takeDamage(5);

        assertEquals(newCard.getHealth(), 5);
    }

    @Test
    public void takeDamageTest_Negative() {
        MinionCard newCard = new MinionCard(gameScreen);
        newCard.setHealth(10);

        newCard.takeDamage(15);

        assertEquals(newCard.getHealth(), -5);
    }
}