package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.Regions.GameRegion;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameRegionTest {

    // /////////////////////////////////////////////////////////////////////////
    // Mocking setup
    // /////////////////////////////////////////////////////////////////////////
    @Mock
    private Game game;
    @Mock
    private GameScreen gameScreen;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private Input input;
    @Mock
    private LayerViewport layerViewport;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(gameScreen.getGame()).thenReturn(game);
        when(gameScreen.getName()).thenReturn("colosseumDemoScreen");
        when(gameScreen.getDefaultLayerViewport()).thenReturn(layerViewport);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing setCardPosition()
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Should set the card's x and y position to the next available position within the region.
     * The x position that the card is set to, is dependant on the number of cards currently in
     * the region (the card's index in the region array), multiplied by the width of a card + the x
     * position of the left edge of the region + half a card width. The y position is the same as the
     * y position of the bottom of the region + half a card width.
     *
     * In this instance, the only card in the region will be the one being tested.
     */
    @Test
    public void GameRegion_SetCardPositionOnlyCardInRegion() {

        // Create instance of card
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Create a new GameRegion instance
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Adds card to region array list
       gameRegion.getCardsInRegion().add(card);

        // Define expected properties
        float expectedCardXPos = xLeftEdge+card.getWidth()/2;
        float expectedCardYPos = yBottomEdge+card.getHeight()/2;

        // SetCard position executed
        gameRegion.setCardPosition(card);

        // Ensure that after set card position, the cards are set at the correct position
        assertEquals(expectedCardXPos, card.position.x,0.0f);
        assertEquals(expectedCardYPos, card.position.y,0.0f);
    }

    /*
     * Should set the card's x and y position to the next available position within the region.
     * The x position that the card is set to, is dependant on the number of cards currently in
     * the region (the card's index in the region array), multiplied by the width of a card + the x
     * position of the left edge of the region + half a card width. The y position is the same as the
     * y position of the bottom of the region + half a card width.
     *
     * In this instance, there is one card already in the region before setting the position.
     */
    @Test
    public void GameRegion_SetCardPositionOneOtherCardInRegion() {

        // Create 2 instances of card
        Card card1 = new Card(0,0,gameScreen,5,false,"Name");
        Card card2 = new Card(0,0,gameScreen,5,false,"Name");

        // Create a new GameRegion instance
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Adds card to region array list
        gameRegion.getCardsInRegion().add(card1);
        gameRegion.getCardsInRegion().add(card2);

        // Define expected properties
        float expectedCardXPos = xLeftEdge+card1.getWidth()/2+(gameRegion.getCardsInRegion().indexOf(card2)*card1.getWidth());
        float expectedCardYPos = yBottomEdge+card1.getHeight()/2;

        // SetCard position executed
        gameRegion.setCardPosition(card2);

        // Ensure that after set card position, the cards are set at the correct position
        assertEquals(expectedCardXPos, card2.position.x,0.0f);
        assertEquals(expectedCardYPos, card2.position.y,0.0f);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing updateCardPosition()
    // /////////////////////////////////////////////////////////////////////////

}