package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
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

    public static Bitmap front = null;
    public static Bitmap back = null;
    public static Bitmap selected = null;

    // Was 50, 70
    private static final float CARD_WIDTH = 50.0f/1.5f;
    private static final float CARD_HEIGHT = 70.0f/1.5f;

    //Define the card digit images
    private Bitmap[] mCardDigits = new Bitmap[10];

    //Check if card is being held
    private Card mCardHeld = null;
    //Check if card is selected to attack with
    private Card mAttackerSelected = null;

    //Check card is flipped
    private Boolean mCardFlippedBack = false;  //initially the card is not flipped
    private Boolean draggable = true; // Card should not be draggable if it is locked in its region
    private Boolean selectable = false;
    private Boolean cardDropped = false; // Used by region when a card is dropped to place (stops card insta-locking when dragged into region)
    private Boolean isEnemy;
    //Define the attack and defence values
    //private int attack, defence, mana;
    private int coinCost;

    private Bitmap mCardPortrait;
    private String mCardName;
    private String currentRegion; // Stores the current region the card is currently located


    //Set offset and scale values for positioning
    private Vector2 mAttackOffset = new Vector2(-0.8f, -0.84f);
    private Vector2 mAttackScale = new Vector2(0.1f, 0.1f);
    private Vector2 mDefenceOffset = new Vector2(0.8f, -0.84f);
    private Vector2 mDefenceScale = new Vector2(0.1f, 0.1f);
    private Vector2 mManaOffset = new Vector2(0.72f, 0.8f);
    private Vector2 mManaScale = new Vector2(0.1f, 0.1f);

    private Vector2 mPortraitOffset = new Vector2(0f, 0f);
    private Vector2 mPortraitScale = new Vector2(1f, 1f);

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
    public Card(float startX, float startY, GameScreen gameScreen, int coinCost, Boolean isEnemy, String mCardName) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, gameScreen.getGame()
                .getAssetManager().getBitmap("CardFront"), gameScreen);

        front = gameScreen.getGame().getAssetManager().getBitmap("CardFront");
        back = gameScreen.getGame().getAssetManager().getBitmap("CardBack");
        selected = gameScreen.getGame().getAssetManager().getBitmap("CardFrontSelected");

        this.isEnemy = isEnemy;

        //temp
        mCardPortrait = gameScreen.getGame().getAssetManager().getBitmap(mCardName);

        if(this.isEnemy)
            flipCard();

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
     *  @param mCards                 card being touched
     * @param mDefaultScreenViewport default game screen viewport
     * @param mGameViewport          game screen viewport
     * @param mGame                  the game in question
     */
    public void cardEvents(List<Card> mCards, ScreenViewport mDefaultScreenViewport,
                           LayerViewport mGameViewport, Game mGame) {
        Input mInput = mGame.getInput();

        for (int i = 0; i < mInput.getTouchEvents().size(); i++) {
            Vector2 touchLocation = new Vector2(0, 0);

            int touchType = mInput.getTouchEvents().get(i).type;
            ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, mInput.getTouchEvents().get(i).x,
                    mInput.getTouchEvents().get(i).y, mGameViewport, touchLocation);

            moveCard(touchType, mCards, touchLocation);
            selectCard(touchType, mCards, touchLocation, mGame);
            boundCard(mCards, mGameViewport);
            enlargeCard(touchType, mCards, touchLocation);
            releaseCard(touchType);
        }
    }

    public void moveCard(int touchType, List<Card> mCards, Vector2 touchLocation){
        //Move the card - Story C1
        checkCardTouched(touchType, TouchEvent.TOUCH_DRAGGED, mCards, touchLocation);
        cardDropped = false;

        //if a card was touched, and the event was a drag, move it
        if (touchType == TouchEvent.TOUCH_DRAGGED
                && mCardHeld != null
                && mCardHeld.getDraggable())
            mCardHeld.position = touchLocation.addReturn(0f, 5.0f);
    }

    public void selectCard(int touchType, List<Card> mCards, Vector2 touchLocation, Game mGame){
        //Edited: select card
        //initial selection, change card frame
        checkCardTouched(touchType, TouchEvent.TOUCH_SINGLE_TAP, mCards, touchLocation);

        //deselect
        if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                && mCardHeld != null
                && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)
                && getmAttackerSelected() != null
                && getmAttackerSelected() != mCardHeld
                && getmAttackerSelected().getBitmap() == selected
                && mCardHeld.getSelectable()) {
            getmAttackerSelected().setBitmap(front);
            setmAttackerSelected(null);
        }
        //select
        if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                && mCardHeld != null
                && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)
                && !mCardHeld.getIsEnemy()
                && mCardHeld.getBitmap() == front
                && mCardHeld.getSelectable()) {
            setmAttackerSelected(mCardHeld);
            getmAttackerSelected().setBitmap(selected);
        }
    }

    public void useCard(int touchType, Vector2 touchLocation) {
        if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                && mCardHeld != null
                && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)
                && mCardHeld.getIsEnemy()
                && getmAttackerSelected() != null
                && !getmAttackerSelected().getIsEnemy()
                && mCardHeld.getSelectable()) {
            //attack logic
        }
    }

    public void boundCard(List<Card> mCards, LayerViewport mGameViewport){
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

    public void enlargeCard(int touchType, List<Card> mCards, Vector2 touchLocation){
        //Enlarge the card
        checkCardTouched(touchType, TouchEvent.TOUCH_LONG_PRESS, mCards, touchLocation);

        if (touchType == TouchEvent.TOUCH_LONG_PRESS
                && mCardHeld != null
                && mCardHeld.getBound().contains(touchLocation.x, touchLocation.y)) {
            //Enlarge the card
            mCardHeld.setHeight(CARD_HEIGHT * 2.0f);
            mCardHeld.setWidth(CARD_WIDTH * 2.0f);
        }
    }

    public void releaseCard(int touchType){
        //release the card, meaning no card is now held
        if (touchType == TouchEvent.TOUCH_UP
                && mCardHeld != null) {
            cardDropped = true;
            mCardHeld.setHeight(CARD_HEIGHT);
            mCardHeld.setWidth(CARD_WIDTH);
            mCardHeld = null;
        }
    }

    public void flipCard() {
        if(!mCardFlippedBack) {
            this.setBitmap(back);
            this.mCardFlippedBack = true;
        }
        else {
            this.setBitmap(front);
            this.mCardFlippedBack = false;
        }

    }

    private void checkCardTouched(int touchType, int touchEvent, List<Card> mCards, Vector2 touchLocation) {
        if (touchType == touchEvent
                && mCardHeld == null) {
            for (int j = 0; j < mCards.size(); j++) {
                if (mCards.get(j).getBound().contains(touchLocation.x, touchLocation.y)){
                    mCardHeld = mCards.get(j);
                }
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Draw the portrait
        drawBitmap(mCardPortrait, mPortraitOffset, mPortraitScale,
                graphics2D, layerViewport, screenViewport);

        //Draw the base frame
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        if(!mCardFlippedBack) {

            if (this instanceof MinionCard) {

                MinionCard mc = (MinionCard) this;
                //Draw the attack on the card
                drawStat(mc.getAttack(), mAttackOffset, mAttackScale, graphics2D, layerViewport, screenViewport);
                //Draw the defence on the card
                drawStat(mc.getHealth(), mDefenceOffset, mDefenceScale, graphics2D, layerViewport, screenViewport);
                //Draw the mana on the card
                drawStat(mc.getCoinCost(), mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);

            } else if (this instanceof WeaponCard) {

                WeaponCard wc = (WeaponCard) this;
                //Draw the attack on the card
                drawStat(wc.getDamage(), mAttackOffset, mAttackScale, graphics2D, layerViewport, screenViewport);
                //Draw the charges on the card
                drawStat(wc.getCharges(), mDefenceOffset, mDefenceScale, graphics2D, layerViewport, screenViewport);
                //Draw the mana on the card
                drawStat(wc.getCoinCost(), mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);

            } else if (this instanceof SpellCard) {

                SpellCard sc = (SpellCard) this;
                //Draw the mana on the card
                drawStat(sc.getCoinCost(), mManaOffset, mManaScale, graphics2D, layerViewport, screenViewport);
            }
        }
    }

    public void drawStat(int stat, Vector2 offset, Vector2 scale, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {

        //ASSUMING all stats are 2 digits or less
        if (stat < 10)   //if the value is a single digit, just draw it
            drawBitmap(mCardDigits[stat], offset, scale, graphics2D, layerViewport, screenViewport);
        else {  //otherwise, draw the number divided by 10 (the tens) and the remainder (the units)
            drawBitmap(mCardDigits[stat / 10], offset.addReturn(-0.1f, 0), scale, graphics2D, layerViewport, screenViewport);
            drawBitmap(mCardDigits[stat % 10], offset.addReturn(0.2f, 0), scale, graphics2D, layerViewport, screenViewport);
            offset.add(-0.1f, 0);
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


    //Getters and Setters
    public boolean getSelectable(){ return selectable; }
    public void setSelectable(boolean selectable){ this.selectable = selectable; }

    public boolean getDraggable(){ return draggable; }
    public void setDraggable(boolean draggable){ this.draggable = draggable; }

    public boolean getCardDropped(){ return cardDropped; }
    public void setCardDropped(boolean cardDropped) { this.cardDropped = cardDropped;}

    public int getCoinCost() { return this.coinCost; }
    public void setCoinCost(int coinCost) { this.coinCost = coinCost; }

    public boolean getIsEnemy() { return this.isEnemy; }
    public void setIsEnemy(Boolean isEnemy) { this.isEnemy = isEnemy; }

    public Card getCard(int i) { return this; }

    public Card getmAttackerSelected() { return mAttackerSelected; }
    public void setmAttackerSelected(Card attackerSelected) { this.mAttackerSelected = attackerSelected; }

    public String getmCardName() { return mCardName; }
    public void setmCardName(String mCardName) { this.mCardName = mCardName; }

    public Bitmap getmCardPortrait() { return mCardPortrait; }
    public void setmCardPortrait(Bitmap cardPortrait) { this.mCardPortrait = cardPortrait; }

    public String getCurrentRegion() { return currentRegion; }
    public void setCurrentRegion(String currentRegion) { this.currentRegion = currentRegion; }
}