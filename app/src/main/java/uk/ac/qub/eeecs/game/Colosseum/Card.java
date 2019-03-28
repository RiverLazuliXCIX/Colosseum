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

    private static Bitmap front = null;
    private static Bitmap back = null;
    private static Bitmap selected = null;
    private static Bitmap attacked = null;
    private static Bitmap discarded = null;

    // Was 50, 70
    private static final float CARD_WIDTH = 50.0f/1.5f;
    private static final float CARD_HEIGHT = 70.0f/1.5f;

    //Define the card digit images
    private static Bitmap[] cardDigits = new Bitmap[10];

    //Check if card is being held
    private Card cardTouched = null;
    private AIOpponent opponentTouched = null;
    //Check if card is selected to attack with
    private static Card attackerSelected;
    private boolean isSelected;

    //Check card is flipped
    private Boolean flippedBack = false;  //initially the card is not flipped
    private Boolean draggable = true; // Card should not be draggable if it is locked in its region
    private Boolean selectable = false;
    private Boolean cardDropped = false; // Used by region when a card is dropped to place (stops card insta-locking when dragged into region)
    private Boolean isEnemy;
    private Boolean toBeDiscarded;

    //Define the attack and defence values
    //private int attack, defence, mana;
    private int coinCost;

    private Bitmap cardPortrait;
    private String cardName;
    private String currentRegion; // Stores the current region the card is currently located

    //Set offset and scale values for positioning
    private static Vector2 attackOffset = new Vector2(-0.8f, -0.84f);
    private static Vector2 attackScale = new Vector2(0.1f, 0.1f);
    private static Vector2 defenceOffset = new Vector2(0.8f, -0.84f);
    private static Vector2 defenceScale = new Vector2(0.1f, 0.1f);
    private static Vector2 manaOffset = new Vector2(0.72f, 0.8f);
    private static Vector2 manaScale = new Vector2(0.1f, 0.1f);

    private static Vector2 portraitOffset = new Vector2(0f, 0f);
    private static Vector2 portraitScale = new Vector2(1f, 1f);

    /*ALT CARD
    private Vector2 attackOffset = new Vector2(-0.66f, -0.71f);
    private Vector2 attackScale = new Vector2(0.12f, 0.12f);
    private Vector2 defenceOffset = new Vector2(0.65f, -0.71f);
    private Vector2 defenceScale = new Vector2(0.12f, 0.12f);
    private Vector2 manaOffset = new Vector2(0.69f, 0.78f);
    private Vector2 manaScale = new Vector2(0.12f, 0.12f);
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
    public Card(float startX, float startY, GameScreen gameScreen, int coinCost, Boolean isEnemy, String cardName) {
        super(startX, startY, CARD_WIDTH, CARD_HEIGHT, gameScreen.getGame()
                .getAssetManager().getBitmap("CardFront"), gameScreen);

        this.toBeDiscarded = false;

        front = gameScreen.getGame().getAssetManager().getBitmap("CardFront");
        back = gameScreen.getGame().getAssetManager().getBitmap("CardBack");
        selected = gameScreen.getGame().getAssetManager().getBitmap("CardFrontSelected");
        attacked = gameScreen.getGame().getAssetManager().getBitmap("CardFrontAttacked");
        discarded = gameScreen.getGame().getAssetManager().getBitmap("Card_Discarded");

        this.isEnemy = isEnemy;

        //temp
        cardPortrait = gameScreen.getGame().getAssetManager().getBitmap(cardName);

        if(this.isEnemy)
            flipCard();

        setCoinCost(coinCost);
        // Store each of the damage/health digits
        for(int digit = 0; digit <= 9; digit++)
            cardDigits[digit] = gameScreen.getGame().getAssetManager().getBitmap("no" + Integer.toString(digit));
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Drag and flip a card
     *  @param cards                 card being touched
     * @param defaultScreenViewport default game screen viewport
     * @param gameViewport          game screen viewport
     * @param game                  the game in question
     */
    public void cardEvents(List<Card> cards, AIOpponent opponent, ScreenViewport defaultScreenViewport,
                           LayerViewport gameViewport, Game game, boolean isOpponent) {
        Input input = game.getInput();

        for (int i = 0; i < input.getTouchEvents().size(); i++) {
            Vector2 touchLocation = new Vector2(0, 0);

            int touchType = input.getTouchEvents().get(i).type;
            ViewportHelper.convertScreenPosIntoLayer(defaultScreenViewport, input.getTouchEvents().get(i).x,
                    input.getTouchEvents().get(i).y, gameViewport, touchLocation);

            //List of all touch methods
            if(!isOpponent)
                playerTouchMethods(touchType, cards, opponent, touchLocation, game, gameViewport);
            else
                opponentTouchMethods(touchType, cards, opponent, touchLocation, game, gameViewport);
        }
    }

    public void playerTouchMethods(int touchType, List<Card> cards, AIOpponent opponent, Vector2 touchLocation, Game mGame, LayerViewport mGameViewport) {
        moveCard(touchType, cards, touchLocation);
        selectCard(touchType, cards, touchLocation, mGame);
        useCard(touchType, cards, opponent, touchLocation);
        boundCard(cards, mGameViewport);
        enlargeCard(touchType, cards, touchLocation);
        releaseCard(touchType);
    }

    public void opponentTouchMethods(int touchType, List<Card> cards, AIOpponent opponent, Vector2 touchLocation, Game mGame, LayerViewport mGameViewport) {
        //moveCard(touchType, cards, touchLocation);
        useCard(touchType, cards, opponent, touchLocation);
        enlargeCard(touchType, cards, touchLocation);
        releaseCard(touchType);
    }

    public void moveCard(int touchType, List<Card> cards, Vector2 touchLocation){
        //Move the card - Story C1
        if (touchType == TouchEvent.TOUCH_DRAGGED) {
            checkCardTouched(touchType, TouchEvent.TOUCH_DRAGGED, cards, touchLocation);
            cardDropped = false;

            //if a card was touched, and the event was a drag, move it
            if (cardTouched != null
                    && cardTouched.getDraggable())
                cardTouched.position = touchLocation.addReturn(0f, 5.0f);
        }
    }

    public void selectCard(int touchType, List<Card> cards, Vector2 touchLocation, Game mGame) {
        //Edited: select card
        //initial selection, change card frame
        if(touchType == TouchEvent.TOUCH_SINGLE_TAP) {
            checkCardTouched(touchType, TouchEvent.TOUCH_SINGLE_TAP, cards, touchLocation);

            //deselect
            if (cardTouched != null
                    && cardTouched.getBound().contains(touchLocation.x, touchLocation.y)
                    && getAttackerSelected() != null
                    && getAttackerSelected() != cardTouched
                    && getAttackerSelected().getBitmap() == selected
                    && cardTouched.getSelectable()) {
                getAttackerSelected().setBitmap(front);
                //setmAttackerSelected(null);
            }
            //select
            else if (cardTouched != null
                        && cardTouched.getBound().contains(touchLocation.x, touchLocation.y)
                        && !cardTouched.getIsEnemy()
                        && cardTouched.getBitmap() == front
                        && cardTouched.getSelectable()) {
                cardTouched.setSelected(true);
                setAttackerSelected(cardTouched);
                getAttackerSelected().setBitmap(selected);
            }
        }
    }

    public void useLogic(Card thisCard, GameObject other) {
        //do nothing
        //method is overridden in child classes (Minion/Weapon/Spell)
    }

    public void useCard(int touchType, List<Card> cards, AIOpponent opponent, Vector2 touchLocation) {
        if (touchType == TouchEvent.TOUCH_SINGLE_TAP) {
            checkCardTouched(touchType, TouchEvent.TOUCH_SINGLE_TAP, cards, touchLocation);
            checkOpponentTouched(touchType, TouchEvent.TOUCH_SINGLE_TAP, opponent, touchLocation);

            if (cardTouched != null
                    && cardTouched.getBound().contains(touchLocation.x, touchLocation.y)
                    && cardTouched.getIsEnemy()
                    && getAttackerSelected() != null
                    && !getAttackerSelected().getIsEnemy()
                    && cardTouched.getSelectable()) {
                cardTouched.setBitmap(attacked);
                useLogic(getAttackerSelected(), cardTouched);
            }
            else if (opponentTouched != null
                        && opponentTouched.getBound().contains(touchLocation.x, touchLocation.y)
                        && getAttackerSelected() != null
                        && !getAttackerSelected().getIsEnemy()) {
                useLogic(getAttackerSelected(), opponentTouched);
            }
        }
    }

    public void discardCard(Card card) { // - Dearbhaile
        //Set Discarded:
        if (    !card.getIsEnemy()
                && getCardTouched()!= null
                && getCardTouched().getSelectable());
        {
            card.setBitmap(discarded);
            card.setToBeDiscarded(true);
        }
    }

    public void boundCard(List<Card> cards, LayerViewport mGameViewport){
        //Bound the card - Story C3
        for (int j = 0; j < cards.size(); j++) {
            float cardHalfWidth = cards.get(j).getBound().halfWidth, cardHalfHeight = cards.get(j).getBound().halfHeight;

            if (cards.get(j).getBound().getLeft() < 0)
                cards.get(j).position.x = cardHalfWidth;
            if (cards.get(j).getBound().getBottom() < 0)
                cards.get(j).position.y = cardHalfHeight;
            if (cards.get(j).getBound().getRight() > mGameViewport.getRight())
                cards.get(j).position.x = mGameViewport.getRight() - cardHalfWidth;
            if (cards.get(j).getBound().getTop() > mGameViewport.getTop())
                cards.get(j).position.y = mGameViewport.getTop() - cardHalfHeight;
        }
    }

    public void enlargeCard(int touchType, List<Card> cards, Vector2 touchLocation){
        //Enlarge the card
        if(touchType == TouchEvent.TOUCH_LONG_PRESS) {
            checkCardTouched(touchType, TouchEvent.TOUCH_LONG_PRESS, cards, touchLocation);

            if (cardTouched != null
                    && cardTouched.getBound().contains(touchLocation.x, touchLocation.y)) {
                //Enlarge the card
                cardTouched.setHeight(CARD_HEIGHT * 2.0f);
                cardTouched.setWidth(CARD_WIDTH * 2.0f);
            }
        }
    }

    public void releaseCard(int touchType){
        //release the card, meaning no card is now held
        if (touchType == TouchEvent.TOUCH_UP
                && cardTouched != null) {
            cardDropped = true;
            cardTouched.setHeight(CARD_HEIGHT);
            cardTouched.setWidth(CARD_WIDTH);
            cardTouched = null;
        }
    }

    public void flipCard() {
        if(!flippedBack) {
            this.setBitmap(back);
            this.flippedBack = true;
        }
        else {
            this.setBitmap(front);
            this.flippedBack = false;
        }
    }

    private void checkCardTouched(int touchType, int touchEvent, List<Card> cards, Vector2 touchLocation) {
        if (touchType == touchEvent
                && cardTouched == null) {
            for (int j = 0; j < cards.size(); j++) {
                if (cards.get(j).getBound().contains(touchLocation.x, touchLocation.y)){
                    cardTouched = cards.get(j);
                }
            }
        }
    }

    private void checkOpponentTouched(int touchType, int touchEvent, AIOpponent opponent, Vector2 touchLocation) {
        if (touchType == touchEvent
                && opponentTouched == null) {
            if (opponent.getBound().contains(touchLocation.x, touchLocation.y)){
                    opponentTouched = opponent;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Draw the portrait
        drawBitmap(cardPortrait, portraitOffset, portraitScale,
                graphics2D, layerViewport, screenViewport);

        //Draw the base frame
        super.draw(elapsedTime, graphics2D, layerViewport, screenViewport);

        if(!flippedBack) {

            if (this instanceof MinionCard) {

                MinionCard mc = (MinionCard) this;
                //Draw the attack on the card
                drawStat(mc.getAttack(), attackOffset, attackScale, graphics2D, layerViewport, screenViewport);
                //Draw the defence on the card
                drawStat(mc.getHealth(), defenceOffset, defenceScale, graphics2D, layerViewport, screenViewport);
                //Draw the mana on the card
                drawStat(mc.getCoinCost(), manaOffset, manaScale, graphics2D, layerViewport, screenViewport);

            } else if (this instanceof WeaponCard) {

                WeaponCard wc = (WeaponCard) this;
                //Draw the attack on the card
                drawStat(wc.getDamage(), attackOffset, attackScale, graphics2D, layerViewport, screenViewport);
                //Draw the charges on the card
                drawStat(wc.getCharges(), defenceOffset, defenceScale, graphics2D, layerViewport, screenViewport);
                //Draw the mana on the card
                drawStat(wc.getCoinCost(), manaOffset, manaScale, graphics2D, layerViewport, screenViewport);

            } else if (this instanceof SpellCard) {

                SpellCard sc = (SpellCard) this;
                //Draw the mana on the card
                drawStat(sc.getCoinCost(), manaOffset, manaScale, graphics2D, layerViewport, screenViewport);
            }
        }
    }

    public void drawStat(int stat, Vector2 offset, Vector2 scale, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {

        //ASSUMING all stats are 2 digits or less
        if (stat < 10)   //if the value is a single digit, just draw it
            drawBitmap(cardDigits[stat], offset, scale, graphics2D, layerViewport, screenViewport);
        else {  //otherwise, draw the number divided by 10 (the tens) and the remainder (the units)
            drawBitmap(cardDigits[stat / 10], offset.addReturn(-0.1f, 0), scale, graphics2D, layerViewport, screenViewport);
            drawBitmap(cardDigits[stat % 10], offset.addReturn(0.2f, 0), scale, graphics2D, layerViewport, screenViewport);
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

    public Card getAttackerSelected() { return attackerSelected; }
    public void setAttackerSelected(Card attackerSelected) { this.attackerSelected = attackerSelected; }

    public Card getCardTouched() { return cardTouched; }
    public void setCardTouched(Card cardTouched) { this.cardTouched = cardTouched; }

    public boolean gettoBeDiscarded() {return toBeDiscarded; }
    public void setToBeDiscarded(boolean newDiscardVal) { this.toBeDiscarded = newDiscardVal; }

    public boolean getSelected() { return isSelected; }
    public void setSelected(boolean newIsSelected) { this.isSelected = newIsSelected; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public Bitmap getCardPortrait() { return cardPortrait; }
    public void setCardPortrait(Bitmap cardPortrait) { this.cardPortrait = cardPortrait; }

    public String getCurrentRegion() { return currentRegion; }
    public void setCurrentRegion(String currentRegion) { this.currentRegion = currentRegion; }
}