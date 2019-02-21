package uk.ac.qub.eeecs.game.Colosseum.Regions;

import uk.ac.qub.eeecs.game.Colosseum.Card;

/**
 * Class used to define the hand region of the game board, within which, cards are drawn to
 * TODO Method to return card to hand if chosen position is invalid
 */

public class HandRegion extends GameRegion {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////



    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public HandRegion (float xLeftEdge,float xRightEdge, float yTopEdge, float yBottomEdge){
        super(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        setMaxNumCardsInRegion(8);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Updates the position of the cards in the container, moving the card to the leftmost available
     * position. This method will be different depending on the type of region.
     *
     * @param card Card which is having its position updated
     */

    public void updateCardPosition(Card card){

        if (getNumCardsInRegion() == 0) {
            card.setPosition(getRegionXPosLeft() + (card.getWidth()/2),getRegionYPosBottom() + (card.getHeight()/2) );
        } else {
            card.setPosition(getRegionXPosLeft() + (card.getWidth()/2) + (getNumCardsInRegion() * card.getWidth()), getRegionYPosBottom() + (card.getHeight()/2));
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

    public void addCard (Card card) {

        if (!isRegionFull()) {
            updateCardPosition(card);
            getCardsInRegion().add(card);
            setNumCardsInRegion(getNumCardsInRegion() + 1);
        }

    }

//    public void removeCard (Card card) {
//
//            getCardsInRegion().remove(card);
//            setNumCardsInRegion(getNumCardsInRegion() - 1);
//
//
//
//    }

    /**
     * Update function for the hand. If a card is present within a region when a held card is dropped
     * or, a card is dropped into a region, adds the card to the selected region. Currently uses
     * addCard method to do this
     *
     * @param card Card being updated
     */

    public void update(Card card){

        // If card is dropped and is in the region, and not currently stored in the region, add it to the region
        if (card.isCardDropped()&& isInRegion(card) && !getCardsInRegion().contains(card)){
            addCard(card);
        }

//        // If card was moved out of the region, remove it from the region
//        if(card.isCardDropped()&& !isInRegion(card) && getCardsInRegion().contains(card)){
//            removeCard(card);
//        }

    }

}
