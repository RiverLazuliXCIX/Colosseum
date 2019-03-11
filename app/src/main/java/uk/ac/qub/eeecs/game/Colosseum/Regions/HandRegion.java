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
     * Handles what happens when a card is removed from hand, and dropped into an accessible region,
     * excluding opponent regions, and the player's hand.
     *
     * @param card Card which is having its position updated
     */

    public void removeCard(Card card){



    }

    /**
     * If the board region is not full, update the card position, add card to that region's card array
     * called when drawing from deck.
     *
     * TODO Add checks for card type, minions should appear in slots, but weapons/spells cards should be consumed once dropped and appear in the graveyard etc.
     *
     * @param card Card which is having its position updated
     */

    public void addCard (Card card) {

        if (!isRegionFull()) {

            getCardsInRegion().add(card);
            setNumCardsInRegion(getNumCardsInRegion() + 1);

            if (getNumCardsInRegion() == 0) {
                card.setPosition(getRegionXPosLeft() + (card.getWidth()/2),getRegionYPosBottom() + (card.getHeight()/2) );
            } else {
                card.setPosition(getRegionXPosLeft() + (card.getWidth()/2) + (getNumCardsInRegion() * card.getWidth()), getRegionYPosBottom() + (card.getHeight()/2));
            }

        }else{

            // Hand is full, do something, eg. card shuffled back into deck etc.

        }

    }

    /**
     * Update function for the hand. If a card is present within a region when a held card is dropped
     * or, a card is dropped into a region, adds the card to the selected region. Currently uses
     * addCard method to do this
     *
     * @param card Card being updated
     */

    public void update(Card card){

    }

}
