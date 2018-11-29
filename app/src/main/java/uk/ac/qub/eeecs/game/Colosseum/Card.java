package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

public class Card extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Width and height of the card, created to provide an appropriate overall
     * size and an appropriate width/height ratio.
     */

    private static final float CARD_WIDTH = 50.0f;
    private static final float CARD_HEIGHT = 70.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a card
     *
     * @param startX     x location of the player card
     * @param startY     y location of the player card
     * @param gameScreen Gamescreen to which card belongs
     * @param attack     attack power of this card
     * @param defence    defence power of this card
     * @param type       what type of card this is
     */
    public Card(float startX, float startY, GameScreen gameScreen, int attack, int defence, char type) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, gameScreen.getGame()
                .getAssetManager().getBitmap("CardFront"), gameScreen);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Drag and flip a card
     *
     * @param mCard                  card being touched
     * @param mDefaultScreenViewport default game screen viewport
     * @param mGameViewport          game screen viewport
     * @param mGame                  the game in question
     */
    public void cardDrag(Card mCard, ScreenViewport mDefaultScreenViewport,
                         LayerViewport mGameViewport, Game mGame) {
        Input mInput = mGame.getInput();

        for (int i = 0; i < mInput.getTouchEvents().size(); i++) {
            Vector2 touchLocation = new Vector2(0, 0);

            int touchType = mInput.getTouchEvents().get(i).type;
            ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, mInput.getTouchEvents().get(i).x,
                    mInput.getTouchEvents().get(i).y, mGameViewport, touchLocation);

            //Move the card - Story C1
            if (touchType == TouchEvent.TOUCH_DRAGGED
                    && mCard.getBound().contains(touchLocation.x, touchLocation.y))
                mCard.position = touchLocation;

            //Flip the card - Story C5
            if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                    && mCard.getBound().contains(touchLocation.x, touchLocation.y)) {
                Bitmap b = mCard.getBitmap();
                Bitmap front = mGame.getAssetManager().getBitmap("CardFront");
                Bitmap back = mGame.getAssetManager().getBitmap("CardBack");
                if (b == front) mCard.setBitmap(back);
                else if (b == back) mCard.setBitmap(front);
            }

            //Bound the card - Story C3
            float cardHalfWidth =  mCard.getBound().halfWidth, cardHalfHeight = mCard.getBound().halfHeight;
;
            if (mCard.getBound().getLeft() < 0)
                mCard.position.x = cardHalfWidth;
            if (mCard.getBound().getBottom() < 0)
                mCard.position.y = cardHalfHeight;
            if (mCard.getBound().getRight() > mGameViewport.getRight())
                mCard.position.x = mGameViewport.getRight() - cardHalfWidth;
            if (mCard.getBound().getTop() > mGameViewport.getTop())
                mCard.position.y = mGameViewport.getTop() - cardHalfHeight;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //TODO
    }
}