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

        // Sets region colour Currently used for testing, displaying region hitboxes
        // boardRegionPaint.setColor(Color.parseColor("#00000000"));
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
     * /4 converts rectangle positions to their relevant positions on screen. Will need to refactor
     * this to rid us of this magic number.
     *
     * @param card Card being checked if within region.
     */

    public boolean isInRegion(Card card){

        if(card.position.x <= boardRegionRect.right/4 && card.position.x >= boardRegionRect.left/4
                && card.position.y >= boardRegionRect.top/4
                && card.position.y <= boardRegionRect.bottom/4){

            return true;
        }
        return false;
    }

    /**
     * Updates the position of the cards in the container, moving the card to the rightmost available
     * position. Using /4 to convert the positions to their relevant viewport positions
     *
     * @param card Card which is having its position updated
     */

    public void updateCardPosition(Card card){

            if (numActiveCards == 0) {
                card.setPosition(position.x/4 + (card.getWidth()/2),position.y/4 + (card.getHeight()/2));
            } else {
                card.setPosition((position.x/4 + (card.getWidth() / 2)) + (numActiveCards * card.getWidth() + 5), position.y / 4 + (card.getHeight() / 2));
            }
    }

    /**
     * If the board region is not full, update the card position, add card to that region's active
     * cards array set cards to not be draggable, should not be movable once they are in play.
     *
     * TODO Add checks for card type, minions should appear in slots, but weapons/spells cards should be consumed once dropped and appear in the graveyard etc.
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


    /**
     * Removes card from active cards and decrements the number of cards on the board.
     *
     * @param card Card being removed
     */
    public void removeCard (Card card){

            // Need additional minion/card functionality for when card is removed / destroyed
            activeCards.remove(card);
            numActiveCards--;

    }


    /**
     * Update function for regions. If a card is present within a region when a held card is dropped
     * or, a card is dropped into a region, adds the card to the selected region. Currently uses
     * addCard method to do this
     *
     * @param card Card being updated
     */
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
