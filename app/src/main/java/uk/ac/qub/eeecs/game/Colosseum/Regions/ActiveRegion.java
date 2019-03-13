package uk.ac.qub.eeecs.game.Colosseum.Regions;

import uk.ac.qub.eeecs.game.Colosseum.Card;

/**
 * Class used to define the region of the game board, within which, cards can be played
 */

public class ActiveRegion extends GameRegion {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private float spacingXCards = 20.0f;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public ActiveRegion (float xLeftEdge,float xRightEdge, float yTopEdge, float yBottomEdge){
        super (xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        setMaxNumCardsInRegion(11);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

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

            getCardsInRegion().add(card);
            setCardPosition(card);
            card.setDraggable(false);

        }
        else{
            // Region full, do something
        }

    }

    /**
     * Update function for the active. If a card is present within a region when a held card is dropped
     * or, a card is dropped into a region, adds the card to the selected region. Currently uses
     * addCard method to do this
     *
     * @param card Card being updated
     */
    public void update(Card card){

        if (card.isCardDropped()&& isInRegion(card) && !isRegionFull()&& card.isDraggable()){

            addCard(card);

        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and Setter Methods
    // /////////////////////////////////////////////////////////////////////////

    public float getSpacingXCards() { return spacingXCards; }
    public void setSpacingXCards(float spacingXCards) { this.spacingXCards = spacingXCards; }

}
