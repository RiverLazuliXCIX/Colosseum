package uk.ac.qub.eeecs.game.Colosseum;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.icu.text.CollationKey;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.GraphicsHelper;
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

    //Define the card digit images
    private Bitmap[] mCardDigits = new Bitmap[10];

    //Check if card is being held
    private Card mCardHeld = null;

    //Check card is flipped
    private Boolean mCardFlippedBack = false;  //initially the card is not flipped

    //Define the attack and defence values
    private int attack, defence, mana;

    //Set offset and scale values for positioning
    private Vector2 mAttackOffset = new Vector2(-0.8f, -0.84f);
    private Vector2 mAttackScale = new Vector2(0.1f, 0.1f);
    private Vector2 mDefenceOffset = new Vector2(0.8f, -0.84f);
    private Vector2 mDefenceScale = new Vector2(0.1f, 0.1f);
    private Vector2 mManaOffset = new Vector2(0.72f, 0.8f);
    private Vector2 mManaScale = new Vector2(0.1f, 0.1f);

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a card
     *
     * @param startX     x location of the player card
     * @param startY     y location of the player card
     * @param gameScreen Gamescreen to which card belongs
     */
    public Card(float startX, float startY, GameScreen gameScreen) {//, String cardName) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, gameScreen.getGame()
                .getAssetManager().getBitmap("CardFront"), gameScreen);
        // Store each of the damage/health digits
        for(int digit = 0; digit <= 9; digit++)
            mCardDigits[digit] = gameScreen.getGame().getAssetManager().getBitmap("no" + Integer.toString(digit));
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Drag and flip a card
     *
     * @param mCards                 card being touched
     * @param mDefaultScreenViewport default game screen viewport
     * @param mGameViewport          game screen viewport
     * @param mGame                  the game in question
     */
    public void cardDrag(List<Card> mCards, ScreenViewport mDefaultScreenViewport,
                         LayerViewport mGameViewport, Game mGame) {
        Input mInput = mGame.getInput();

        for (int i = 0; i < mInput.getTouchEvents().size(); i++) {
            Vector2 touchLocation = new Vector2(0, 0);

            int touchType = mInput.getTouchEvents().get(i).type;
            ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, mInput.getTouchEvents().get(i).x,
                    mInput.getTouchEvents().get(i).y, mGameViewport, touchLocation);

            //Move the card - Story C1
            if (touchType == TouchEvent.TOUCH_DRAGGED
                    && mCardHeld == null)
                checkCardTouched(mCards, touchLocation);

            //if a card was touched, and the event was a drag, move it
            if (touchType == TouchEvent.TOUCH_DRAGGED
                    && mCardHeld != null)
                mCardHeld.position = touchLocation;

            //release the card, meaning no card is now held
            if (touchType == TouchEvent.TOUCH_UP
                    && mCardHeld != null)
                mCardHeld = null;


            //Flip the card - Story C5
            if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                    && mCardHeld == null)
                checkCardTouched(mCards, touchLocation);

            if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                    && mCardHeld != null
                    && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)) {
                Bitmap b = mCardHeld.getBitmap();
                Bitmap front = mGame.getAssetManager().getBitmap("no1");
                Bitmap back = mGame.getAssetManager().getBitmap("no9");
                if (b == front) {
                    mCardHeld.setBitmap(back);
                    mCardHeld.mCardFlippedBack = true;
                }
                else if (b == back) {
                    mCardHeld.setBitmap(front);
                    mCardHeld.mCardFlippedBack = false;
                }
            }

            //Bound the card - Story C3
            for (int j = 0; j < mCards.size(); j++) {
                float cardHalfWidth = mCards.get(j).getBound().halfWidth, cardHalfHeight = mCards.get(j).getBound().halfHeight;

                if (mCards.get(j).getBound().getLeft() < 0)
                    mCards.get(j).position.x = cardHalfWidth;
                if (mCards.get(j).getBound().getBottom() < 0)
                    mCards.get(j).position.y = cardHalfHeight;
                if (mCards.get(j).getBound().getRight() > mGameViewport.getRight())
                    mCards.get(j).position.x = mGameViewport.getRight() - cardHalfWidth;
                if (mCards.get(j).getBound().getTop() > mGameViewport.getTop())
                    mCards.get(j).position.y = mGameViewport.getTop() - cardHalfHeight;
            }
        }
    }

    private void checkCardTouched(List<Card> mCards, Vector2 touchLocation) {
        //Check which card was touched, if any
        for (int j = 0; j < mCards.size(); j++) {
            if (mCards.get(j).getBound().contains(touchLocation.x, touchLocation.y)) {
                mCardHeld = mCards.get(j);
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        //Draw the base frame
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        if (!mCardFlippedBack) {
            //Draw the attack on the card
            drawBitmap(mCardDigits[getAttack()], mAttackOffset, mAttackScale, graphics2D, layerViewport, screenViewport);

            //Draw the defence on the card
            drawBitmap(mCardDigits[getDefence()], mDefenceOffset, mDefenceScale, graphics2D, layerViewport, screenViewport);

            //Draw the mana on the card
            drawBitmap(mCardDigits[getMana()], mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);
        }
    }


    private BoundingBox bound;
    private void drawBitmap(Bitmap bitmap, Vector2 offset, Vector2 scale, IGraphics2D graphics2D,
                            LayerViewport layerViewport, ScreenViewport screenViewport) {

        bound = new BoundingBox(position.x + mBound.halfWidth * offset.x,
                                position.y + mBound.halfHeight * offset.y,
                                mBound.halfWidth * scale.x,
                                mBound.halfHeight * scale.y);

        if (GraphicsHelper.getSourceAndScreenRect(
                bound, bitmap, layerViewport, screenViewport, drawSourceRect, drawScreenRect)) {

            Matrix drawMatrix = new Matrix();

            // Build an appropriate transformation matrix
            drawMatrix.reset();

            float scaleX = (float) drawScreenRect.width() / (float) drawSourceRect.width();
            float scaleY = (float) drawScreenRect.height() / (float) drawSourceRect.height();
            drawMatrix.postScale(scaleX, scaleY);

            drawMatrix.postTranslate(drawScreenRect.left, drawScreenRect.top);

            // Draw the bitmap
            graphics2D.drawBitmap(bitmap, drawMatrix, null);
        }
    }

    public void setAttack(int attack){
        this.attack = attack;
    }

    public void setDefence(int defence){
        this.defence = defence;
    }

    public void setMana(int mana){
        this.mana = mana;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getMana() {
        return mana;
    }

    public Card getCard(int i) {return this; }
}