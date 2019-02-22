package uk.ac.qub.eeecs.game.Colosseum.Regions;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.Colosseum.Card;

/**
 * Class used to define regions of the game board, which may be used as playable regions, hand regions
 * deck regions etc.
 */

public class GameRegion {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private float regionXPosLeft;
    private float regionXPosRight;
    private float regionYPosTop;
    private float regionYPosBottom;
    private float regionWidth;
    private float regionHeight;

    private int numCardsInRegion;
    private int maxNumCardsInRegion;

    private ArrayList <Card> cardsInRegion;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public GameRegion (float xLeftEdge,float xRightEdge, float yTopEdge, float yBottomEdge){

        // Initialising playable region dimensions
        this.regionXPosLeft = xLeftEdge;
        this.regionXPosRight = xRightEdge;
        this.regionYPosTop = yTopEdge;
        this.regionYPosBottom = yBottomEdge;
        this.regionWidth = xRightEdge-xLeftEdge;
        this.regionHeight = yTopEdge - yBottomEdge;

        // Sets default values related to cards within the region
        this.numCardsInRegion = 0;
        this.maxNumCardsInRegion = 10;
        this.cardsInRegion = new ArrayList<>();

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a specified card is present within the region.
     *
     * @param card Card to be checked if within region.
     */

    public boolean isInRegion(Card card){

        if (card.position.x < regionXPosRight && card.position.x > regionXPosLeft
            && card.position.y < regionYPosTop && card.position.y > regionYPosBottom){

            return true;
        }

        return false;
    }

    /**
     * Checks if the active region is full
     */

    public boolean isRegionFull(){
        if(numCardsInRegion == maxNumCardsInRegion) {
            return true;
        }
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and Setter Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Getter and setter methods for each of the appropriate private variables
     */

    public float getRegionXPosLeft() { return regionXPosLeft; }
    public void setRegionXPosLeft(float regionXPosLeft) { this.regionXPosLeft = regionXPosLeft; }

    public float getRegionXPosRight() { return regionXPosRight; }
    public void setRegionXPosRight(float regionXPosRight) { this.regionXPosRight = regionXPosRight; }

    public float getRegionYPosTop() { return regionYPosTop; }
    public void setRegionYPosTop(float regionYPosTop) { this.regionYPosTop = regionYPosTop; }

    public float getRegionYPosBottom() { return regionYPosBottom; }
    public void setRegionYPosBottom(float regionYPosBottom) { this.regionYPosBottom = regionYPosBottom; }

    public float getRegionWidth() { return regionWidth; }
    public void setRegionWidth(float regionWidth) { this.regionWidth = regionWidth; }

    public float getRegionHeight() { return regionHeight; }
    public void setRegionHeight(float regionHeight) { this.regionHeight = regionHeight; }

    public int getNumCardsInRegion() { return numCardsInRegion; }
    public void setNumCardsInRegion(int numCardsInRegion) { this.numCardsInRegion = numCardsInRegion; }

    public ArrayList<Card> getCardsInRegion() { return cardsInRegion; }
    public void setCardsInRegion(ArrayList<Card> cardsInRegion) { this.cardsInRegion = cardsInRegion; }

    public int getMaxNumCardsInRegion() { return maxNumCardsInRegion; }
    public void setMaxNumCardsInRegion(int maxNumCardsInRegion) { this.maxNumCardsInRegion = maxNumCardsInRegion; }
}
