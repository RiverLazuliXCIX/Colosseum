package uk.ac.qub.eeecs.game.Colosseum;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class BoardRegion extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private Rect boardRegionRect;

    private Paint boardRegionPaint = new Paint();

    private int maxActiveCards = 5;
    private int numActiveCards = 0;

    private ArrayList<Card> activeCards = new ArrayList<>();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @param x X coordinate of left side of the region
     * @param y Y coordinate of the top of the region
     * @param width Width of the region
     * @param height Height of the region
     * @param gameScreen Game screen the region belongs to
     */

    public BoardRegion(float x, float y, float width, float height, GameScreen gameScreen){
        super(x, y, width, height, gameScreen);

        /**
         * To define the right side X coordinate of a rectangle, we add the desired width to the X
         * coordinate of the left side of the rectangle. Similarly, to define the bottom of a
         * rectangle, we add the desired height to the Y coordinate of the top of the rectangle.
         * Parameters cast as integer to be used by Rect.
         *
         * Illustrated: https://stackoverflow.com/a/26253377
         */
        boardRegionRect = new Rect((int)x,(int)y, (int)(x+width),(int)(y+height));

        // Sets region colour
//        boardRegionPaint.setColor(Color.parseColor("#00000000"));
        boardRegionPaint.setColor(Color.MAGENTA);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public Rect getBoardRegionRect() { return boardRegionRect; }
    public Paint getBoardRegionPaint() { return boardRegionPaint; }

    public boolean isBoardRegionFull(){
        if(numActiveCards == maxActiveCards) {
        return true;
    }
    return false;
    }

    /**
     * Checks if a specified card is present within the region.
     *
     * @param card Card being checked if within region.
     */
//boardRegionRect.bottom - boardRegionRect.top
    public boolean isInRegion(Card card){

        if(card.position.x <= boardRegionRect.right && card.position.x >= boardRegionRect.left
                && card.position.y >= position.y/4-getHeight()
                && card.position.y <= position.y/4){

            return true;
        }
        return false;
    }

    /**
     * Updates the position of the cards in the container, moving the card to the rightmost available
     * position. note: y position of 50 accounts for player portrait
     *
     * @param card Card which is having its position updated
     */

    public void updateCardPosition(Card card){

            if (numActiveCards == 0) {
                card.setPosition((position.x + (card.getWidth() / 2)) + (numActiveCards * card.getWidth()), position.y / 4 - (card.getHeight() / 2));
            } else {
                card.setPosition((position.x + (card.getWidth() / 2)) + (numActiveCards * card.getWidth() + 5), position.y / 4 - (card.getHeight() / 2));
            }
    }

    /**
     * If the board region is not full, update the card position
     *
     * @param card Card which is having its position updated
     */
    public void addCard (Card card){

        if(!isBoardRegionFull()){
            updateCardPosition(card);
            activeCards.add(card);
            card.setDraggable(false);
            numActiveCards++;
        }

    }

    public void removeCard (Card card){

            // Need additional minion/card functionality for when card is removed / destroyed
            activeCards.remove(card);
            numActiveCards--;

    }

    public void update(Card card){
        if (card.isCardDropped()&&isInRegion(card) && !isBoardRegionFull()&& card.isDraggable()){
            //updateCardPosition(card);
            addCard(card);
        }
    }

//    /**
//     * When a card is removed from the region, positions of all active cards are updated to reflect this
//     *
//     * @param activeCards array list of all cards that are currently active.
//     */
//
//    public void cardRemovedFromRegion(ArrayList<Card> activeCards){
//        ArrayList<Integer> temp = new ArrayList<>();
//
//        // For each active card add to a temporary array
//        for(int i = 0; i<activeCards.size();i++){
//                temp.add(i);
//        }
//        cardsInRegion.clear();
//
//        for(int i =0; i<temp.size();i++){
//           cardsInRegion.add(temp.get(i));
//           updateCardPosition(activeCards.get(temp.get(i)),i);
//        }
//
//    }

}
