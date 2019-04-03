package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.colosseumDemoScreen;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardClassTest {

    @Mock
    private colosseumDemoScreen mDemoScreen;
    @Mock
    private AssetManager mAssetManager;
    @Mock
    private Bitmap mBitmap;
    @Mock
    private Game mGame;
    @Mock
    private GameScreen gameScreen;
    @Mock
    private Input mInput;
    @Mock
    private LayerViewport mLayerViewport;
    @Mock
    private ScreenManager mScreenManager;

    @Before
    public void setUp() {
        when(mGame.getAssetManager()).thenReturn(mAssetManager);
        when(mAssetManager.getBitmap(any(String.class))).thenReturn(mBitmap);
        when(mGame.getScreenManager()).thenReturn(mScreenManager);
        when(mGame.getInput()).thenReturn(mInput);
        when(mDemoScreen.getGame()).thenReturn(mGame);
        when(mDemoScreen.getName()).thenReturn("colosseumDemoScreen");
        when(mDemoScreen.getDefaultLayerViewport()).thenReturn(mLayerViewport);
    }

    @Test
    public void cardCreate_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        assertNotNull(newCard.getCard(0));
    }

    @Test
    public void enlargeCard_LongPress_Test() {
        int touchType = TouchEvent.TOUCH_LONG_PRESS;
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card cardTouched = newCard;

        //cardTouched.enlargeCard(touchType, );

        assertNull(cardTouched);
    }

    @Test
    public void releaseCard_Released_Test() {
        int touchType = TouchEvent.TOUCH_UP;
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card cardTouched = newCard;

        cardTouched.releaseCard(touchType);

        assertNull(cardTouched);
    }

    @Test
    public void releaseCard_NotReleased_Test() {
        int touchType = TouchEvent.TOUCH_DOWN;
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card cardTouched = newCard;

        cardTouched.releaseCard(touchType);

        assertNotNull(cardTouched);
    }

    @Test
    public void checkCardTouched_Test() {
        Vector2 touchLocation = new Vector2(100, 100);
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Boolean ok = false;

        if (newCard.getBound().contains(touchLocation.x, touchLocation.y))
            ok = true;

        assertTrue(ok);
    }

    @Test
    public void flipCard_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Bitmap b = newCard.getBitmap();
        newCard.flipCard();

        assertTrue(b != newCard.getBitmap());
    }

    @Test
    public void setSelectable_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setSelectable(true);

        assertTrue(newCard.getSelectable());
    }

    @Test
    public void setDraggable_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setDraggable(true);

        assertTrue(newCard.getDraggable());
    }

    @Test
    public void setCoinCost_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCoinCost(9);

        assertEquals(newCard.getCoinCost(), 9);
    }

    @Test
    public void setIsEnemy_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setIsEnemy(true);

        assertTrue(newCard.getIsEnemy());
    }

    @Test
    public void setAttackerSelected_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card otherCard = new Card(150, 150, gameScreen, 1, false, "asdfg");
        newCard.setAttackerSelected(otherCard);

        assertEquals(newCard.getAttackerSelected(), otherCard);
    }

    @Test
    public void setCardTouched_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Card otherCard = new Card(150, 150, gameScreen, 1, false, "asdfg");
        newCard.setCardTouched(otherCard);

        assertEquals(newCard.getCardTouched(), otherCard);
    }

    @Test
    public void setCardName_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCardName("yes");

        assertEquals(newCard.getCardName(), "yes");
    }

    @Test
    public void setCardPortrait_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        Bitmap b = null;
        newCard.setCardPortrait(b);

        assertEquals(newCard.getCardPortrait(), b);
    }

    @Test
    public void setCurrentRegion_Test() {
        Card newCard = new Card(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCurrentRegion("yes");

        assertEquals(newCard.getCurrentRegion(), "yes");
    }
}