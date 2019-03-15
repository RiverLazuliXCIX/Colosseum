package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

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
    public void setSelectableTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setSelectable(true);

        assertTrue(newCard.getSelectable());
    }

    @Test
    public void setDraggableTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setDraggable(true);

        assertTrue(newCard.getDraggable());
    }

    @Test
    public void setCoinCostTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCoinCost(9);

        assertEquals(newCard.getCoinCost(), 9);
    }

    @Test
    public void setIsEnemyTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setIsEnemy(true);

        assertTrue(newCard.getIsEnemy());
    }

    @Test
    public void setmAttackerSelectedTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card otherCard = new Card(150, 150, gameScreen, 1, false, "asdfg");
        newCard.setmAttackerSelected(otherCard);

        assertEquals(newCard.getmAttackerSelected(), otherCard);
    }

    @Test
    public void setmCardTouchedTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card otherCard = new Card(150, 150, gameScreen, 1, false, "asdfg");
        newCard.setmCardTouched(otherCard);

        assertEquals(newCard.getmCardTouched(), otherCard);
    }

    @Test
    public void setmCardNameTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setmCardName("yes");

        assertEquals(newCard.getmCardName(), "yes");
    }

    @Test
    public void setmCardPortraitTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Bitmap b = null;
        newCard.setmCardPortrait(b);

        assertEquals(newCard.getmCardPortrait(), b);
    }

    @Test
    public void setCurrentRegionTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCurrentRegion("yes");

        assertEquals(newCard.getCurrentRegion(), "yes");
    }

    @Test
    public void checkCardTouchedTest() {
        Vector2 touchLocation = new Vector2(100, 100);
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Boolean ok = false;

        if (newCard.getBound().contains(touchLocation.x, touchLocation.y))
            ok = true;

        assertTrue(ok);
    }

    @Test
    public void flipCardTest() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Bitmap b = newCard.getBitmap();
        newCard.flipCard();

        assertTrue(b != newCard.getBitmap());
    }
}