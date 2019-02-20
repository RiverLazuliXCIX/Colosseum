package uk.ac.qub.eeecs.game.Colosseum.Regions;

import uk.ac.qub.eeecs.game.Colosseum.Card;

/**
 * Class used to define the region of the game board, within which, cards can be played
 */

public class ActiveRegion extends GameRegion {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private static final int MAX_PLAYABLE_CARDS = 8;

    private float spacingXCards = 5.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public ActiveRegion (float xLeftEdge,float xRightEdge, float yTopEdge, float yBottomEdge){
        super (xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Checks if the active region is full
     */

    public boolean isRegionFull(){
        if(getNumCardsInRegion() == getMaxPlayableCards()) {
            return true;
        }
        return false;
    }

    /**
     * Updates the position of the cards in the container, moving the card to the leftmost available
     * position.
     *
     * @param card Card which is having its position updated
     */

    public void updateCardPosition(Card card){

        if (getNumCardsInRegion() == 0) {
            card.setPosition(getRegionXPosLeft() + (card.getWidth()/2),getRegionYPosBottom() + (card.getHeight()/2) );
        } else {
            card.setPosition(getRegionXPosLeft() + (card.getWidth()/2) + (getNumCardsInRegion() * card.getWidth() + spacingXCards), getRegionYPosBottom() + (card.getHeight()/2));
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

        if(!isRegionFull()){
            updateCardPosition(card);
            getCardsInRegion().add(card);
            card.setDraggable(false);
            setNumCardsInRegion(getNumCardsInRegion()+1);
        }

    }

    /**
     * Removes card from active cards and decrements the number of cards on the board.
     *
     * @param card Card being removed
     */
    public void removeCard (Card card){

        getCardsInRegion().remove(card);
        setNumCardsInRegion(getNumCardsInRegion()-1);

    }

    /**
     * Update function for the active. If a card is present within a region when a held card is dropped
     * or, a card is dropped into a region, adds the card to the selected region. Currently uses
     * addCard method to do this
     *
     * @param card Card being updated
     */
    public void update(Card card){
        if (card.isCardDropped()&&isInRegion(card) && !isRegionFull()&& card.isDraggable()){
            //updateCardPosition(card);
            addCard(card);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and Setter Methods
    // /////////////////////////////////////////////////////////////////////////

    public static int getMaxPlayableCards() { return MAX_PLAYABLE_CARDS; }

    public float getSpacingXCards() { return spacingXCards; }
    public void setSpacingXCards(float spacingXCards) { this.spacingXCards = spacingXCards; }
}
