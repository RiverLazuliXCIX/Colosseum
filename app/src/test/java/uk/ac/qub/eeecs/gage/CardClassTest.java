package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.colosseumDemoScreen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CardClassTest {

    @Mock
    private colosseumDemoScreen mDemoScreen;

    @Mock
    private GameScreen gameScreen;

    @Before
    public void setUp() {


    }

    @Test
    public void setCardAttack() {
        Card newCard = new Card(100, 100, gameScreen);
        newCard.setAttack(5);

        assertEquals(newCard.getAttack(), 5);
    }

    @Test
    public void setCardDefence() {
        Card newCard = new Card(100, 100, gameScreen);
        newCard.setDefence(2);

        assertEquals(newCard.getDefence(), 2);
    }

    @Test
    public void setCardMana() {
        Card newCard = new Card(100, 100, gameScreen);
        newCard.setMana(9);

        assertEquals(newCard.getMana(), 9);
    }

    @Test
    public void checkCardTouchedTest() {
        Vector2 touchLocation = new Vector2(100, 100);
        Card newCard = new Card(100, 100, gameScreen);
        Boolean ok = false;

        if (newCard.getBound().contains(touchLocation.x, touchLocation.y))
            ok = true;

        assertTrue(ok);
    }
}