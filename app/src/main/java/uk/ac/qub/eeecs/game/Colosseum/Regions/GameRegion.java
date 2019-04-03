package uk.ac.qub.eeecs.game.Colosseum.Regions;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Card;

/**
 * Created by Kyle Corrigan
 *
 * Class used to define regions of the game board, which may be used as playable regions, hand regions
 * deck regions etc.
 *
 * @author Kyle Corrigan
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
        this.maxNumCardsInRegion = 10;
        this.cardsInRegion = new ArrayList<>();

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Sets the card position to the leftmost available position, dependant on the number of
     * cards already in the region. Determines xco-ordinate by multiplying the card's index, in the
     * region array list, by the width of the card.
     *
     */

    public void setCardPosition(Card card){

        card.setPosition(getRegionXPosLeft() + (card.getWidth()/2) +
                        (getCardsInRegion().indexOf(card) * card.getWidth()),
                getRegionYPosBottom() + (card.getHeight()/2));

    }

    /**
     * For each card still stored in the array list, repositions the card depending on its index.
     * Use
     *
     */

    public void updateCardPositions(){

        for (int i = 0; i < getCardsInRegion().size(); i++) {

            setCardPosition(getCardsInRegion().get(i));

        }

    }

    /**
     * Removes card from active cards and updates the card positions within that region
     *
     * @param card Card being removed
     */
    public void removeCard (Card card){

        getCardsInRegion().remove(card);
        updateCardPositions();

    }

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
        if(cardsInRegion.size() >= maxNumCardsInRegion) {
            return true;
        }
        return false;
    }

    /**
     * Draws a white border around the game region
     *
     * @param graphics2D Graphics instance
     * @param gameScreen Gamescreen to which this object is drawn
     */
    public void drawRegion(IGraphics2D graphics2D, GameScreen gameScreen){

        // Setting up region paint
        // White border without a fill
        Paint regionPaint = new Paint();
        regionPaint.setStyle(Paint.Style.STROKE);
        regionPaint.setStrokeWidth(10);
        regionPaint.setColor(Color.rgb(255, 255, 255));

        // Note: To add a fill to the region, a second rectangle should be drawn, using a different paint type
        // with style of fill without stroke

        // Scaling positions to the display using viewport helper
        float regionBorderLeft = ViewportHelper.convertXDistanceFromLayerToScreen(regionXPosLeft, gameScreen.getDefaultLayerViewport(),gameScreen.getDefaultScreenViewport());
        float regionBorderTop = ViewportHelper.convertYDistanceFromLayerToScreen(regionYPosTop, gameScreen.getDefaultLayerViewport(),gameScreen.getDefaultScreenViewport());
        float regionBorderWidth = ViewportHelper.convertXDistanceFromLayerToScreen(regionWidth, gameScreen.getDefaultLayerViewport(),gameScreen.getDefaultScreenViewport());
        float regionBorderHeight= ViewportHelper.convertYDistanceFromLayerToScreen(regionHeight, gameScreen.getDefaultLayerViewport(),gameScreen.getDefaultScreenViewport());

        graphics2D.drawRect(regionBorderLeft,regionBorderTop, regionBorderLeft+regionBorderWidth,regionBorderTop-regionBorderHeight, regionPaint);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and Setter Methods
    // /////////////////////////////////////////////////////////////////////////

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

    public ArrayList<Card> getCardsInRegion() { return cardsInRegion; }
    public void setCardsInRegion(ArrayList<Card> cardsInRegion) { this.cardsInRegion = cardsInRegion; }

    public int getMaxNumCardsInRegion() { return maxNumCardsInRegion; }
    public void setMaxNumCardsInRegion(int maxNumCardsInRegion) { this.maxNumCardsInRegion = maxNumCardsInRegion; }
}