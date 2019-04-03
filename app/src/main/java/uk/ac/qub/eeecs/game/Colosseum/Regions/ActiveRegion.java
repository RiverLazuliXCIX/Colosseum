package uk.ac.qub.eeecs.game.Colosseum.Regions;

import uk.ac.qub.eeecs.game.CoinTossScreen;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.WeaponCard;

/**
 * Created by Kyle Corrigan
 *
 * Class used to define the region of the game board, within which, cards can be played
 *
 * @author Kyle Corrigan
 */

public class ActiveRegion extends GameRegion {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private float spacingXCards = 20.0f; // Defines the amount of spacing between each card in the region

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
     * @param card Card which is having its position updated
     */
    public void addCard (Card card){

        if(!isRegionFull()){

            getCardsInRegion().add(card);
            setCardPosition(card);
            card.setDraggable(false);
            card.setSelectable(true);
            card.setCurrentRegion("Active");

            if(card.getIsEnemy())
                card.flipCard();

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

        if (card.getCardDropped()&& isInRegion(card) && !isRegionFull()&& card.getDraggable()){

            int cost = card.getCoinCost();
            CoinTossScreen cts = (CoinTossScreen) card.getGameScreen();

            Player p;
            if (!card.getIsEnemy()) p = cts.getCds().getmPlayer();
            else p = cts.getCds().getmOpponent();

            if (p.getCurrentMana() >= cost) {
                p.reduceCurrentMana(cost);

                if (card instanceof WeaponCard) {
                    WeaponCard wc = (WeaponCard) card;
                    wc.play(wc, p);
                } else {
                    addCard(card);
                }
            }

        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and Setter Methods
    // /////////////////////////////////////////////////////////////////////////

    public float getSpacingXCards() { return spacingXCards; }
    public void setSpacingXCards(float spacingXCards) { this.spacingXCards = spacingXCards; }

}