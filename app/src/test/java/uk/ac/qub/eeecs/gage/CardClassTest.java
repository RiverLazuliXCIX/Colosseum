package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.TestClasses.AIOpponentForTesting;
import uk.ac.qub.eeecs.game.TestClasses.CardClassForTesting;
import uk.ac.qub.eeecs.game.colosseumDemoScreen;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
        when(gameScreen.getDefaultLayerViewport()).thenReturn(mLayerViewport);
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
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "CardFront");
        assertNotNull(newCard.getCard(0));
    }

    @Test
    public void moveCard_Drag_Test() {
        int touchType  = TouchEvent.TOUCH_DRAGGED;
        Vector2 touchLocation = new Vector2(105, 105);
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 400; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        CardClassForTesting cardTouched = cards.get(0);

        cardTouched.moveCard(touchType, cards, touchLocation);

        assertTrue(cardTouched.position.equals(touchLocation));
    }

    @Test
    public void moveCard_WrongInput_Test() {
        int touchType  = TouchEvent.TOUCH_SINGLE_TAP;
        Vector2 touchLocation = new Vector2(105, 105);
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 400; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        CardClassForTesting cardTouched = cards.get(0);

        cardTouched.moveCard(touchType, cards, touchLocation);

        assertFalse(cardTouched.position.equals(touchLocation));
    }

    @Test
    public void moveCard_WrongLocation_Test() {
        int touchType  = TouchEvent.TOUCH_DRAGGED;
        Vector2 touchLocation = new Vector2(200, 200);
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 400; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        CardClassForTesting cardTouched = cards.get(0);

        cardTouched.moveCard(touchType, cards, touchLocation);

        assertFalse(cardTouched.position.equals(touchLocation));
    }

    @Test
    public void selectCard_Select_Test() {
        int touchType = TouchEvent.TOUCH_SINGLE_TAP;
        Vector2 touchLocation = new Vector2(100, 100);

        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 400; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        cards.get(1).selectCard(touchType, cards, touchLocation, mGame);

        assertTrue(cards.get(1).getSelected());
    }

    @Test
    public void selectCard_Deselect_Test() {
        int touchType = TouchEvent.TOUCH_SINGLE_TAP;
        Vector2 touchLocation = new Vector2(100, 100);

        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 400; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        cards.get(1).selectCard(touchType, cards, touchLocation, mGame);

        touchLocation = new Vector2(200, 100);
        cards.get(2).selectCard(touchType, cards, touchLocation, mGame);

        assertFalse(cards.get(1).getSelected());
    }

    //Needs modification
    @Test
    public void useCard_AttackEnemyCard_Test() {
        int touchType = TouchEvent.TOUCH_SINGLE_TAP;
        Vector2 touchLocation = new Vector2(0, 100);

        AIOpponentForTesting enemy = new AIOpponentForTesting(gameScreen, "asdf");

        CardClassForTesting playerCard = new CardClassForTesting(0, 0, gameScreen, 1, false, "asdf");
        CardClassForTesting enemyCard = new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf");
        CardClassForTesting cardTouched = enemyCard;
        cardTouched.setAttackerSelected(playerCard);

        List<CardClassForTesting> cards = new ArrayList<>();
        cards.add(new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf"));


        int ans = cardTouched.useCard(touchType, cards, enemy, touchLocation);

        assertEquals((float)ans, 1.0f);
    }

    //Needs modification
    @Test
    public void useCard_WrongInput_Test() {
        int touchType = TouchEvent.TOUCH_UP;
        Vector2 touchLocation = new Vector2(0, 100);

        AIOpponentForTesting enemy = new AIOpponentForTesting(gameScreen, "asdf");

        CardClassForTesting playerCard = new CardClassForTesting(0, 0, gameScreen, 1, false, "asdf");
        CardClassForTesting enemyCard = new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf");
        CardClassForTesting cardTouched = enemyCard;
        cardTouched.setAttackerSelected(playerCard);

        List<CardClassForTesting> cards = new ArrayList<>();
        cards.add(new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf"));


        int ans = cardTouched.useCard(touchType, cards, enemy, touchLocation);

        assertEquals(ans, 0);
    }

    //Needs modification
    @Test
    public void useCard_WrongLocation_Test() {
        int touchType = TouchEvent.TOUCH_SINGLE_TAP;
        Vector2 touchLocation = new Vector2(200, 100);

        AIOpponentForTesting enemy = new AIOpponentForTesting(gameScreen, "asdf");

        CardClassForTesting playerCard = new CardClassForTesting(0, 0, gameScreen, 1, false, "asdf");
        CardClassForTesting enemyCard = new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf");
        CardClassForTesting cardTouched = enemyCard;
        cardTouched.setAttackerSelected(playerCard);

        List<CardClassForTesting> cards = new ArrayList<>();
        cards.add(new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf"));


        int ans = cardTouched.useCard(touchType, cards, enemy, touchLocation);

        assertEquals(ans, 0);
    }

    //Needs modification
    @Test
    public void useCard_AttackAIOpponent_Test() {
        int touchType = TouchEvent.TOUCH_SINGLE_TAP;

        AIOpponentForTesting enemy = new AIOpponentForTesting(gameScreen, "asdf");

        Vector2 touchLocation = new Vector2(enemy.position);

        CardClassForTesting playerCard = new CardClassForTesting(0, 0, gameScreen, 1, false, "asdf");
        CardClassForTesting enemyCard = new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf");
        CardClassForTesting cardTouched = enemyCard;
        cardTouched.setAttackerSelected(playerCard);

        List<CardClassForTesting> cards = new ArrayList<>();
        cards.add(new CardClassForTesting(100, 100, gameScreen, 1, true, "asdf"));


        int ans = cardTouched.useCard(touchType, cards, enemy, touchLocation);

        assertEquals(ans, 2);
    }

    @Test
    public void boundCard_Bounded_Test() {
        Vector2 lowest = new Vector2(0, 0);
        Vector2 highest = new Vector2(mLayerViewport.getRight(), mLayerViewport.getTop());
        Boolean ok = false;
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 500; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        cards.get(1).boundCard(cards, mLayerViewport);

        if(cards.get(1).position.x <= highest.x
                && cards.get(1).position.y <= highest.y
                && cards.get(1).position.x >= lowest.x
                && cards.get(1).position.x >= lowest.x)
            ok = true;

        assertTrue(ok);
    }

    @Test
    public void boundCard_OOB_Test() {
        Vector2 lowest = new Vector2(0, 0);
        Vector2 highest = new Vector2(mLayerViewport.getRight(), mLayerViewport.getTop());
        Boolean ok = false;
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = -100; i < 400; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        cards.get(0).boundCard(cards, mLayerViewport);

        if(cards.get(0).position.x <= highest.x
                && cards.get(0).position.y <= highest.y
                && cards.get(0).position.x >= lowest.x
                && cards.get(0).position.x >= lowest.x)
            ok = true;

        assertTrue(ok);
    }

    @Test
    public void enlargeCard_LongPress_Test() {
        int touchType = TouchEvent.TOUCH_LONG_PRESS;
        Vector2 touchLocation = new Vector2(0, 100);
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 500; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));
        CardClassForTesting cardTouched = cards.get(0);
        Boolean ok = false;

        float ogWidth = cardTouched.getWidth(), ogHeight = cardTouched.getHeight();

        cardTouched.enlargeCard(touchType, cards, touchLocation);

        assertEquals(cardTouched.getWidth(), ogWidth);
        assertEquals(cardTouched.getHeight(), ogHeight);
    }

    @Test
    public void enlargeCard_WrongTouch_Test() {
        int touchType = TouchEvent.TOUCH_SINGLE_TAP;
        Vector2 touchLocation = new Vector2(0, 100);
        List<CardClassForTesting> cards = new ArrayList<>();
        for (int i = 0; i < 500; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));
        CardClassForTesting cardTouched = cards.get(0);
        Boolean ok = false;

        float ogWidth = cardTouched.getWidth(), ogHeight = cardTouched.getHeight();

        cardTouched.enlargeCard(touchType, cards, touchLocation);

        assertEquals(cardTouched.getWidth(), ogWidth);
        assertEquals(cardTouched.getHeight(), ogHeight);
    }

    @Test
    public void enlargeCard_LongPress_WrongLocation_Test() {
        int touchType = TouchEvent.TOUCH_LONG_PRESS;
        Vector2 touchLocation = new Vector2(100, 100);
        List<CardClassForTesting> cards = new ArrayList<>();
        for(int i = 0; i < 500; i += 100)
            cards.add(new CardClassForTesting(i, 100, gameScreen, 1, false, "asdf"));

        CardClassForTesting cardTouched = cards.get(0);
        Boolean ok = false;

        float ogWidth = cardTouched.getWidth(), ogHeight = cardTouched.getHeight();

        cardTouched.enlargeCard(touchType, cards, touchLocation);

        assertEquals(cardTouched.getWidth(), ogWidth);
        assertEquals(cardTouched.getHeight(), ogHeight);
    }

    @Test
    public void releaseCard_Released_Test() {
        int touchType = TouchEvent.TOUCH_UP;
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        CardClassForTesting cardTouched = newCard;

        cardTouched.releaseCard(touchType);

        assertNull(cardTouched);
    }

    @Test
    public void releaseCard_NotReleased_Test() {
        int touchType = TouchEvent.TOUCH_DOWN;
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        CardClassForTesting cardTouched = newCard;

        cardTouched.releaseCard(touchType);

        assertNotNull(cardTouched);
    }

    @Test
    public void checkCardTouched_Test() {
        Vector2 touchLocation = new Vector2(100, 100);
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        Boolean ok = false;

        if (newCard.getBound().contains(touchLocation.x, touchLocation.y))
            ok = true;

        assertTrue(ok);
    }

    @Test
    public void flipCard_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        Bitmap b = newCard.getBitmap();
        newCard.flipCard();

        assertTrue(b != newCard.getBitmap());
    }

    @Test
    public void setSelectable_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        newCard.setSelectable(true);

        assertTrue(newCard.getSelectable());
    }

    @Test
    public void setDraggable_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        newCard.setDraggable(true);

        assertTrue(newCard.getDraggable());
    }

    @Test
    public void setCoinCost_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCoinCost(9);

        assertEquals(newCard.getCoinCost(), 9);
    }

    @Test
    public void setIsEnemy_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        newCard.setIsEnemy(true);

        assertTrue(newCard.getIsEnemy());
    }

    @Test
    public void setAttackerSelected_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        CardClassForTesting otherCard = new CardClassForTesting(150, 150, gameScreen, 1, false, "asdfg");
        newCard.setAttackerSelected(otherCard);

        assertEquals(newCard.getAttackerSelected(), otherCard);
    }

    @Test
    public void setCardTouched_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        CardClassForTesting otherCard = new CardClassForTesting(150, 150, gameScreen, 1, false, "asdfg");
        newCard.setCardTouched(otherCard);

        assertEquals(newCard.getCardTouched(), otherCard);
    }

    @Test
    public void setCardName_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCardName("yes");

        assertEquals(newCard.getCardName(), "yes");
    }

    @Test
    public void setCardPortrait_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        Bitmap b = null;
        newCard.setCardPortrait(b);

        assertEquals(newCard.getCardPortrait(), b);
    }

    @Test
    public void setCurrentRegion_Test() {
        CardClassForTesting newCard = new CardClassForTesting(100, 100, gameScreen, 1, false, "asdf");
        newCard.setCurrentRegion("yes");

        assertEquals(newCard.getCurrentRegion(), "yes");
    }
}