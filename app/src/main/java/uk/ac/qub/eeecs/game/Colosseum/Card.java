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

    // Was 50, 70
    private static final float CARD_WIDTH = 50.0f/1.5f;
    private static final float CARD_HEIGHT = 70.0f/1.5f;

    //Define the card digit images
    private Bitmap[] mCardDigits = new Bitmap[10];

    //Check if card is being held
    private Card mCardHeld = null;


    //Check card is flipped
    private Boolean mCardFlippedBack = false;  //initially the card is not flipped
    private Boolean draggable = true; // Card should not be draggable if it is locked in its region
    private Boolean cardDropped = false; // Used by region when a card is dropped to place (stops card insta-locking when dragged into region)
    //Define the attack and defence values
    //private int attack, defence, mana;
    private int coinCost;

    //Set offset and scale values for positioning

    private Vector2 mAttackOffset = new Vector2(-0.8f, -0.84f);
    private Vector2 mAttackScale = new Vector2(0.1f, 0.1f);
    private Vector2 mDefenceOffset = new Vector2(0.8f, -0.84f);
    private Vector2 mDefenceScale = new Vector2(0.1f, 0.1f);
    private Vector2 mManaOffset = new Vector2(0.72f, 0.8f);
    private Vector2 mManaScale = new Vector2(0.1f, 0.1f);

    /*ALT CARD
    private Vector2 mAttackOffset = new Vector2(-0.66f, -0.71f);
    private Vector2 mAttackScale = new Vector2(0.12f, 0.12f);
    private Vector2 mDefenceOffset = new Vector2(0.65f, -0.71f);
    private Vector2 mDefenceScale = new Vector2(0.12f, 0.12f);
    private Vector2 mManaOffset = new Vector2(0.69f, 0.78f);
    private Vector2 mManaScale = new Vector2(0.12f, 0.12f);
    */

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
    public Card(float startX, float startY, GameScreen gameScreen, int coinCost) {//, String cardName) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, gameScreen.getGame()
                .getAssetManager().getBitmap("CardFront"), gameScreen);

        setCoinCost(coinCost);
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
            cardDropped = false;

            //if a card was touched, and the event was a drag, move it
            if (touchType == TouchEvent.TOUCH_DRAGGED
                    && mCardHeld != null)
                mCardHeld.position = touchLocation;


            //Flip the card - Story C5
            if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                    && mCardHeld == null)
                checkCardTouched(mCards, touchLocation);

            if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                    && mCardHeld != null
                    && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)) {
                Bitmap b = mCardHeld.getBitmap();
                Bitmap front = mGame.getAssetManager().getBitmap("CardFront");
                Bitmap back = mGame.getAssetManager().getBitmap("CardBack");
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


            //Enlarge the card
            if (touchType == TouchEvent.TOUCH_LONG_PRESS
                    && mCardHeld == null)
                checkCardTouched(mCards, touchLocation);

            if (touchType == TouchEvent.TOUCH_LONG_PRESS
                    && mCardHeld != null
                    && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)) {
                //Enlarge the card
                mCardHeld.setHeight(CARD_HEIGHT * 2.0f);
                mCardHeld.setWidth(CARD_WIDTH * 2.0f);
            }

            //release the card, meaning no card is now held
            if (touchType == TouchEvent.TOUCH_UP
                    && mCardHeld != null) {
                cardDropped = true;
                mCardHeld.setHeight(CARD_HEIGHT);
                mCardHeld.setWidth(CARD_WIDTH);
                mCardHeld = null;
            }
        }
    }

    public void flipCard(Game mGame) {
        if(!mCardFlippedBack) {
            Bitmap b = this.getBitmap();
            Bitmap front = mGame.getAssetManager().getBitmap("CardFront");
            Bitmap back = mGame.getAssetManager().getBitmap("CardBack");
            if (b == front) {
                this.setBitmap(back);
                this.mCardFlippedBack = true;
            } //else if (b == back) {
                //this.setBitmap(front);
                //this.mCardFlippedBack = false;
            //}
        }
    }

    private void checkCardTouched(List<Card> mCards, Vector2 touchLocation) {
        //Check which card was touched, if any
        for (int j = 0; j < mCards.size(); j++) {
            if (mCards.get(j).getBound().contains(touchLocation.x, touchLocation.y)&&mCards.get(j).isDraggable()) {
                mCardHeld = mCards.get(j);
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        //Draw the base frame
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        if (this instanceof MinionCard) {
            if (!mCardFlippedBack) {
                MinionCard mc = (MinionCard) this;

                //ASSUMING all stats are 2 digits or less

                //Draw the attack on the card
                if (mc.getAttack() < 10)   //if the value is a single digit, just draw it
                    drawBitmap(mCardDigits[mc.getAttack()], mAttackOffset, mAttackScale, graphics2D, layerViewport, screenViewport);
                else {  //otherwise, draw the number divided by 10 (the tens) and the remainder (the units)
                    drawBitmap(mCardDigits[mc.getAttack() / 10], mAttackOffset.addReturn(-0.1f, 0), mAttackScale, graphics2D, layerViewport, screenViewport);
                    drawBitmap(mCardDigits[mc.getAttack() % 10], mAttackOffset.addReturn(0.2f, 0), mAttackScale, graphics2D, layerViewport, screenViewport);
                    mAttackOffset.add(-0.1f, 0);
                }

                //Draw the defence on the card
                if (mc.getHealth() < 10)
                    drawBitmap(mCardDigits[mc.getHealth()], mDefenceOffset, mDefenceScale, graphics2D, layerViewport, screenViewport);
                else {
                    drawBitmap(mCardDigits[mc.getHealth() / 10], mDefenceOffset.addReturn(-0.1f, 0), mDefenceScale, graphics2D, layerViewport, screenViewport);
                    drawBitmap(mCardDigits[mc.getHealth() % 10], mDefenceOffset.addReturn(0.2f, 0), mDefenceScale, graphics2D, layerViewport, screenViewport);
                    mDefenceOffset.add(-0.1f, 0);
                }

                //Draw the mana on the card
                if (mc.getCoinCost() < 10)
                    drawBitmap(mCardDigits[mc.getCoinCost()], mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);
                else {
                    drawBitmap(mCardDigits[mc.getCoinCost() / 10], mManaOffset.addReturn(-0.1f, 0), mManaScale, graphics2D, layerViewport, screenViewport);
                    drawBitmap(mCardDigits[mc.getCoinCost() % 10], mManaOffset.addReturn(0.2f, 0), mManaScale, graphics2D, layerViewport, screenViewport);
                    mManaOffset.add(-0.1f, 0);
                }
            }
        } else if (this instanceof WeaponCard) {
            WeaponCard wc = (WeaponCard) this;

            //Draw the attack on the card
            if (wc.getDamage() < 10)   //if the value is a single digit, just draw it
                drawBitmap(mCardDigits[wc.getDamage()], mAttackOffset, mAttackScale, graphics2D, layerViewport, screenViewport);
            else {  //otherwise, draw the number divided by 10 (the tens) and the remainder (the units)
                drawBitmap(mCardDigits[wc.getDamage() / 10], mAttackOffset.addReturn(-0.1f, 0), mAttackScale, graphics2D, layerViewport, screenViewport);
                drawBitmap(mCardDigits[wc.getDamage() % 10], mAttackOffset.addReturn(0.2f, 0), mAttackScale, graphics2D, layerViewport, screenViewport);
                mAttackOffset.add(-0.1f, 0);
            }

            //Draw the charges on the card
            if (wc.getCharges() < 10)
                drawBitmap(mCardDigits[wc.getCharges()], mDefenceOffset, mDefenceScale, graphics2D, layerViewport, screenViewport);
            else {
                drawBitmap(mCardDigits[wc.getCharges() / 10], mDefenceOffset.addReturn(-0.1f, 0), mDefenceScale, graphics2D, layerViewport, screenViewport);
                drawBitmap(mCardDigits[wc.getCharges() % 10], mDefenceOffset.addReturn(0.2f, 0), mDefenceScale, graphics2D, layerViewport, screenViewport);
                mDefenceOffset.add(-0.1f, 0);
            }

            //Draw the mana on the card
            if (wc.getCoinCost() < 10)
                drawBitmap(mCardDigits[wc.getCoinCost()], mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);
            else {
                drawBitmap(mCardDigits[wc.getCoinCost() / 10], mManaOffset.addReturn(-0.1f, 0), mManaScale, graphics2D, layerViewport, screenViewport);
                drawBitmap(mCardDigits[wc.getCoinCost() % 10], mManaOffset.addReturn(0.2f, 0), mManaScale, graphics2D, layerViewport, screenViewport);
                mManaOffset.add(-0.1f, 0);
            }

        } else if (this instanceof SpellCard) {
            SpellCard sc = (SpellCard) this;

            //Draw the mana on the card
            if (sc.getCoinCost() < 10)
                drawBitmap(mCardDigits[sc.getCoinCost()], mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);
            else {
                drawBitmap(mCardDigits[sc.getCoinCost() / 10], mManaOffset.addReturn(-0.1f, 0), mManaScale, graphics2D, layerViewport, screenViewport);
                drawBitmap(mCardDigits[sc.getCoinCost() % 10], mManaOffset.addReturn(0.2f, 0), mManaScale, graphics2D, layerViewport, screenViewport);
                mManaOffset.add(-0.1f, 0);
            }
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

    /*
    public void setAttack(int attack){
        this.attack = attack;
    }

    public void setDefence(int defence){
        this.defence = defence;
    }

    public void setMana(int mana){
        this.mana = mana;
    }
    */

    public void setDraggable(boolean draggable){
        this.draggable = draggable;
    }

    public void setCardDropped(boolean cardDropped) { this.cardDropped = cardDropped;}

    /*
    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }

    public int getMana() {
        return mana;
    }
    */

    public boolean isDraggable(){
        return draggable;
    }

    public boolean isCardDropped(){return cardDropped;}

    public int getCoinCost() { return this.coinCost; }
    public void setCoinCost(int coinCost) { this.coinCost = coinCost; }

    public Card getCard(int i) { return this; }
}