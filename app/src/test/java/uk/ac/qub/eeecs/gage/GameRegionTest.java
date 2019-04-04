package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.GameRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    // Testing GameRegion, ActiveRegion and HandRegion constructors
    // /////////////////////////////////////////////////////////////////////////
    /*
     * Test to ensure that the region constructors do create a game region
     */
    @Test
    public void Regions_TestConstructors() {

        // Create a new region instances
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);
        HandRegion handRegion = new HandRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);
        ActiveRegion activeRegion = new ActiveRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Checking to ensure that the objects have been created
        assertNotNull(gameRegion);
        assertNotNull(handRegion);
        assertNotNull(activeRegion);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing GameRegion, ActiveRegion and HandRegion getters and setters
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Test to ensure that the region getters and setters modify values appropriately
     * Getters and setters from the GameRegion Class are shared to both HandRegion and ActiveRegion,
     */
    @Test
    public void Regions_TestGettersSetters() {

        ArrayList<Card>testCardsInRegion = new ArrayList<>();

        // Create a new region instances
        float xLeftEdge = 100;
        float xRightEdge = 500;
        float yTopEdge = 500;
        float yBottomEdge = 100;
        GameRegion gameRegion = new GameRegion(xLeftEdge, xRightEdge, yTopEdge, yBottomEdge);
        HandRegion handRegion = new HandRegion(xLeftEdge, xRightEdge, yTopEdge, yBottomEdge);
        ActiveRegion activeRegion = new ActiveRegion(xLeftEdge, xRightEdge, yTopEdge, yBottomEdge);

        // Test GameRegion getters and setters
        gameRegion.setMaxNumCardsInRegion(1);
        gameRegion.setCardsInRegion(testCardsInRegion);
        gameRegion.setRegionHeight(2.0f);
        gameRegion.setRegionWidth(3.0f);
        gameRegion.setRegionXPosLeft(4.0f);
        gameRegion.setRegionXPosRight(5.0f);
        gameRegion.setRegionYPosBottom(6.0f);
        gameRegion.setRegionYPosTop(7.0f);

        // Define expected GameRegion values
        int expectedMaxNumCardsInRegion = 1;
        ArrayList<Card> expectedCardsInRegion = testCardsInRegion;
        float expectedRegionHeight = 2.0f;
        float expectedRegionWidth = 3.0f;
        float expectedRegionXPosLeft = 4.0f;
        float expectedRegionXPosRight = 5.0f;
        float expectedRegionYPosBottom = 6.0f;
        float expectedRegionYPosTop = 7.0f;

        // ensuring that GameRegion getters and setters work
        assertEquals(expectedMaxNumCardsInRegion,gameRegion.getMaxNumCardsInRegion());
        assertEquals(expectedCardsInRegion, gameRegion.getCardsInRegion());
        assertEquals(expectedRegionHeight, gameRegion.getRegionHeight(),0.0f);
        assertEquals(expectedRegionWidth, gameRegion.getRegionWidth(), 0.0f);
        assertEquals(expectedRegionXPosLeft,gameRegion.getRegionXPosLeft(),0.0f);
        assertEquals(expectedRegionXPosRight,gameRegion.getRegionXPosRight(),0.0f);
        assertEquals(expectedRegionYPosBottom,gameRegion.getRegionYPosBottom(),0.0f);
        assertEquals(expectedRegionYPosTop,gameRegion.getRegionYPosTop(),0.0f);

        // Set values using active region setters
        activeRegion.setSpacingXCards(2.0f);
        // Define expected active region values
        float expectedSpacingXCards = 2.0f;
        // ensuring ActiveRegion getters and setters work
        assertEquals(expectedSpacingXCards, activeRegion.getSpacingXCards(),0.0f);

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

     /*
     * Sets cards to the correct position on update. In this test if we initially have 2 cards in a
     * region, and then delete the first card added to the array list, we expect the cards following
     * the deleted card to move back a position to fill the newly available position.
     */
    @Test
    public void GameRegion_UpdateCardPositions1CardAfterRemovedCard(){
        // Create 2 card instances to be added to the region array list
        Card card1 = new Card(0,0,gameScreen,5,false,"Name");
        Card card2 = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Adds card to region array list
        gameRegion.getCardsInRegion().add(card1);
        gameRegion.getCardsInRegion().add(card2);

        // Define expected properties
        float expectedCardXPos = xLeftEdge+card1.getWidth()/2+(gameRegion.getCardsInRegion().indexOf(card1)*card1.getWidth());
        float expectedCardYPos = yBottomEdge+card1.getHeight()/2;

        // Remove the first card from region array list
        gameRegion.getCardsInRegion().remove(0);

        // update card positions executed
        gameRegion.updateCardPositions();

        // Ensure that after executing the method, the card positions are set to the correct values
        assertEquals(expectedCardXPos,card2.position.x,0.0f);
        assertEquals(expectedCardYPos,card2.position.y,0.0f);

    }

    /*
     * Sets cards to the correct position on update. In this test if we initially have 3 cards in a
     * region, and then delete the first card added to the array list, we expect the cards following
     * the deleted card to move back a position to fill the newly available position.
     */
    @Test
    public void GameRegion_UpdateCardPositionsMoreThan1CardAfterRemovedCard(){
        // Create 3 card instances to be added to the region array list
        Card card1 = new Card(0,0,gameScreen,5,false,"Name");
        Card card2 = new Card(0,0,gameScreen,5,false,"Name");
        Card card3 = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Adds card to region array list
        gameRegion.getCardsInRegion().add(card1);
        gameRegion.getCardsInRegion().add(card2);
        gameRegion.getCardsInRegion().add(card3);

        // Define expected properties
        float expectedCard2XPos = xLeftEdge+card1.getWidth()/2+(gameRegion.getCardsInRegion().indexOf(card1)*card1.getWidth());
        float expectedCard2YPos = yBottomEdge+card1.getHeight()/2;
        float expectedCard3XPos = xLeftEdge+card2.getWidth()/2+(gameRegion.getCardsInRegion().indexOf(card2)*card2.getWidth());
        float expectedCard3YPos = yBottomEdge+card2.getHeight()/2;

        // Remove the first card from region array list
        gameRegion.getCardsInRegion().remove(0);

        // update card positions executed
        gameRegion.updateCardPositions();

        // Ensure that after executing the method, the card positions are set to the correct values
        assertEquals(expectedCard2XPos,card2.position.x,0.0f);
        assertEquals(expectedCard2YPos,card2.position.y,0.0f);
        assertEquals(expectedCard3XPos,card3.position.x,0.0f);
        assertEquals(expectedCard3YPos,card3.position.y,0.0f);

    }

    /*
     * Sets cards to the correct position on update. In this test if we initially have 3 cards in a
     * region, and then delete the second card added to the array list, we expect the card following
     * the deleted card to move back a position to fill the newly available position, and the first
     * card to stay in its original position.
     */
    @Test
    public void GameRegion_UpdateCardPositionsCardsBeforeAndAfterRemovedCard(){
        // Create 3 card instances to be added to the region array list
        Card card1 = new Card(0,0,gameScreen,5,false,"Name");
        Card card2 = new Card(0,0,gameScreen,5,false,"Name");
        Card card3 = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Adds card to region array list
        gameRegion.getCardsInRegion().add(card1);
        gameRegion.getCardsInRegion().add(card2);
        gameRegion.getCardsInRegion().add(card3);

        // Define expected properties
        float expectedCard1XPos = xLeftEdge+card1.getWidth()/2+(gameRegion.getCardsInRegion().indexOf(card1)*card1.getWidth());
        float expectedCard1YPos = yBottomEdge+card1.getHeight()/2;
        float expectedCard3XPos = xLeftEdge+card2.getWidth()/2+(gameRegion.getCardsInRegion().indexOf(card2)*card2.getWidth());
        float expectedCard3YPos = yBottomEdge+card2.getHeight()/2;

        // Remove the second card from region array list
        gameRegion.getCardsInRegion().remove(1);

        // update card positions executed
        gameRegion.updateCardPositions();

        // Ensure that after executing the method, the card positions are set to the correct values
        assertEquals(expectedCard1XPos,card1.position.x,0.0f);
        assertEquals(expectedCard1YPos,card1.position.y,0.0f);
        assertEquals(expectedCard3XPos,card3.position.x,0.0f);
        assertEquals(expectedCard3YPos,card3.position.y,0.0f);

    }
    
    // /////////////////////////////////////////////////////////////////////////
    // Testing isInRegion()
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Checks if a specified card is within a given region. In this test, the card is within the region
     * boundaries, and therefore should return true
     */
    @Test
    public void GameRegion_isInRegionWithinRegion(){
        // Create card instance to be tested
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        card.setPosition(200,300); // sets the card position to be within the region

        // Checks if card is within the region's boundary
        // Ensure that after executing the method, the card is registered as being within the region
        assertTrue( gameRegion.isInRegion(card));
    }

    /*
     * Checks if a specified card is within a given region. In this test, the card is not within the region
     * boundaries, and therefore should return false
     */
    @Test
    public void GameRegion_isInRegionNotInRegion(){
        // Create card instance to be tested
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        card.setPosition(0,0); // sets the card position to not be within the region


        // Checks if card is within the region's boundary
        // Ensure that after executing the method, the card is registered as not being within the region
        assertFalse( gameRegion.isInRegion(card));
    }

    /*
     * Checks if a specified card is within a given region. In this test, the card is on region
     * boundaries, but should return false.
     */
    @Test
    public void GameRegion_isInRegionOnLowestRegionBoundary(){
        // Create card instance to be tested
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // lowest x bound lowest y bound
        card.setPosition(100,100); // sets the card position to be on the game region boundary


        // Checks if card is within the region's boundary
        // Ensure that after executing the method, the card is registered as not being within the region
        assertFalse( gameRegion.isInRegion(card));
    }

    /*
     * Checks if a specified card is within a given region. In this test, the card is on region
     * boundaries, but should return false.
     */
    @Test
    public void GameRegion_isInRegionOnHighestRegionBoundary(){
        // Create card instance to be tested
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // highest x bound highest y bound
        card.setPosition(500,500); // sets the card position to be on the game region boundary


        // Checks if card is within the region's boundary
        // Ensure that after executing the method, the card is registered as not being within the region
        assertFalse( gameRegion.isInRegion(card));
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing isRegionFull()
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Checks if the region is full, returning true if true, and false if not. This test adds
     * the maximum number of cards to the region, and as such, should return true
     */
    @Test
    public void GameRegion_isRegionFullTrue(){

        // Create card instance to be tested
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        gameRegion.setMaxNumCardsInRegion(11);
        // add max number of cards to region
        for(int i= 0;i<gameRegion.getMaxNumCardsInRegion();i++){
            gameRegion.getCardsInRegion().add(card);
        }

        // Checks if region is full
        // Ensure that after executing the method, the region is counted as full
        assertTrue( gameRegion.isRegionFull());
    }

    /*
     * Checks if the region is full, returning true if true, and false if not. This test clears all
      * cards from the region, and as such, should return false
     */
    @Test
    public void GameRegion_isRegionFullFalse(){

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        gameRegion.setMaxNumCardsInRegion(11);

        // remove all cards in the region
        gameRegion.getCardsInRegion().clear();

        // Checks if region is full
        // Ensure that after executing the method, the region is not counted as full
        assertFalse( gameRegion.isRegionFull());
    }

    /*
     * Checks if the region is full, returning true if true, and false if not. This test adds more than
     * the maximum number of cards to the region. Should return true (Methods within active and hand region
     *  addCard, prevents this from happening in normal circumstances)
     */
    @Test
    public void GameRegion_isRegionFullTrueExceedsBoundary(){

        // Create card instance to be tested
        Card card = new Card(0,0,gameScreen,5,false,"Name");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        GameRegion gameRegion = new GameRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        gameRegion.setMaxNumCardsInRegion(11);

        // add more than max number of cards to region
        for(int i= 0;i<gameRegion.getMaxNumCardsInRegion()+3;i++){
            gameRegion.getCardsInRegion().add(card);
        }

        // Checks if region is full
        // Ensure that after executing the method, the region is counted as full
        assertTrue( gameRegion.isRegionFull());
    }


    // /////////////////////////////////////////////////////////////////////////
    // Testing addCard() method
    // /////////////////////////////////////////////////////////////////////////

    // Hand region addCard();
    /*
     * If hand region is not full adds card to the hand region array list, sets card's region to hand
     * and sets the added card to the next available position. Since the region is full, the card should not
     * be added to the region
     */
    @Test
    public void HandRegion_addCardHandFull(){

        // Create card instance to be tested
        Card initialCard = new Card(0,0,gameScreen,5,false,"Name");
        Card addedCard = new Card(0,0,gameScreen,5,false,"Name");
        addedCard.setCurrentRegion("TestDefault");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        HandRegion handRegion = new HandRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        handRegion.setMaxNumCardsInRegion(1);
        // card region is now full
        handRegion.getCardsInRegion().add(initialCard);

        // Call the addCardMethod
        handRegion.addCard(addedCard);

        // Should stay at initial values
        float expectedCardX = 0;
        float expectedCardY = 0;
        String expectedCardRegion = "TestDefault";

        // Check to ensure that variables are at their expected values
        assertEquals(expectedCardX, addedCard.position.x, 0.0f);
        assertEquals(expectedCardY, addedCard.position.y, 0.0f);
        assertEquals(expectedCardRegion, addedCard.getCurrentRegion());

    }

    /*
     * If hand region is not full adds card to the hand region array list, sets card's region to hand
     * and sets the added card to the next available position. Since the region is not full, the card should
     * be added to the region
     */
    @Test
    public void HandRegion_addCardHandEmpty(){

        // Create card instance to be tested
        Card addedCard = new Card(0,0,gameScreen,5,false,"Name");
        addedCard.setCurrentRegion("TestDefault");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        HandRegion handRegion = new HandRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        handRegion.setMaxNumCardsInRegion(11);
        // card region is empty
        handRegion.getCardsInRegion().clear();

        // Call the addCardMethod
        handRegion.addCard(addedCard);

        // Should update values to the next available position
        float expectedCardX = xLeftEdge+addedCard.getWidth()/2+(handRegion.getCardsInRegion().indexOf(addedCard)*addedCard.getWidth());
        float expectedCardY = yBottomEdge+addedCard.getHeight()/2;
        String expectedCardRegion = "Hand";

        // Check to ensure that variables are at their expected values
        assertEquals(expectedCardX, addedCard.position.x, 0.0f);
        assertEquals(expectedCardY, addedCard.position.y, 0.0f);
        assertEquals(expectedCardRegion, addedCard.getCurrentRegion());

    }

    /*
     * If hand region is not full adds card to the hand region array list, sets card's region to hand
     * and sets the added card to the next available position. Since the region is not full, the card should
     * be added to the region
     */
    @Test
    public void HandRegion_addCardHandWithCardsPresent(){

        // Create card instance to be tested
        Card initialCard = new Card(0,0,gameScreen,5,false,"Name");
        Card addedCard = new Card(0,0,gameScreen,5,false,"Name");
        addedCard.setCurrentRegion("TestDefault");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        HandRegion handRegion = new HandRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        handRegion.setMaxNumCardsInRegion(11);
        // card region is empty
        handRegion.getCardsInRegion().clear();
        // Add initial card to region
        handRegion.addCard(initialCard);

        // Call the addCardMethod on new card
        handRegion.addCard(addedCard);

        // Should update values to the next available position
        float expectedCardX = xLeftEdge+addedCard.getWidth()/2+(handRegion.getCardsInRegion().indexOf(addedCard)*addedCard.getWidth());
        float expectedCardY = yBottomEdge+addedCard.getHeight()/2;
        String expectedCardRegion = "Hand";

        // Check to ensure that variables are at their expected values
        assertEquals(expectedCardX, addedCard.position.x, 0.0f);
        assertEquals(expectedCardY, addedCard.position.y, 0.0f);
        assertEquals(expectedCardRegion, addedCard.getCurrentRegion());

    }

    /*
     * If active region is not full adds card to the active region array list, sets card's region to active
     * and sets the added card to the next available position. Since the region is full, the card should not
     * be added to the region. Cards successfully added to active region should not be draggable, but should be
     * selectable
     */
    @Test
    public void ActiveRegion_addEnemyCardActiveFull(){

        // Create card instance to be tested
        Card initialCard = new Card(0,0,gameScreen,5,false,"Name");
        Card addedCard = new Card(0,0,gameScreen,5,false,"Name");
        addedCard.setCurrentRegion("TestDefault");
        addedCard.setDraggable(true);
        addedCard.setSelectable(false);

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        ActiveRegion activeRegion = new ActiveRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        activeRegion.setMaxNumCardsInRegion(1);
        // card region is now full
        activeRegion.getCardsInRegion().add(initialCard);

        // Call the addCardMethod
        activeRegion.addCard(addedCard);

        // Should stay at initial values
        float expectedCardX = 0;
        float expectedCardY = 0;
        String expectedCardRegion = "TestDefault";
        boolean expectedDraggable = true;
        boolean expectedSelectable = false;

        // Check to ensure that variables are at their expected values
        assertEquals(expectedCardX, addedCard.position.x, 0.0f);
        assertEquals(expectedCardY, addedCard.position.y, 0.0f);
        assertEquals(expectedCardRegion, addedCard.getCurrentRegion());
        assertTrue(expectedDraggable);
        assertFalse(expectedSelectable);

    }

    /*
     * If active region is not full adds card to the active region array list, sets card's region to active
     * and sets the added card to the next available position. Since the region is not full, the card should
     * be added to the region. Cards successfully added to active region should not be draggable, but should be
     * selectable
     */
    @Test
    public void ActiveRegion_addCardActiveEmpty(){

        // Create card instance to be tested
        Card addedCard = new Card(0,0,gameScreen,5,false,"Name");
        addedCard.setCurrentRegion("TestDefault");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        ActiveRegion activeRegion = new ActiveRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        activeRegion.setMaxNumCardsInRegion(11);
        // card region is empty
        activeRegion.getCardsInRegion().clear();

        // Call the addCardMethod
        activeRegion.addCard(addedCard);

        // Should update values to the next available position
        float expectedCardX = xLeftEdge+addedCard.getWidth()/2+(activeRegion.getCardsInRegion().indexOf(addedCard)*addedCard.getWidth());
        float expectedCardY = yBottomEdge+addedCard.getHeight()/2;
        String expectedCardRegion = "Active";

        // Check to ensure that variables are at their expected values
        assertEquals(expectedCardX, addedCard.position.x, 0.0f);
        assertEquals(expectedCardY, addedCard.position.y, 0.0f);
        assertEquals(expectedCardRegion, addedCard.getCurrentRegion());

    }

    /*
     * If hand region is not full adds card to the hand region array list, sets card's region to hand
     * and sets the added card to the next available position. Since the region is not full, the card should
     * be added to the region
     */
    @Test
    public void ActiveRegion_addCardActiveWithCardsPresent(){

        // Create card instance to be tested
        Card initialCard = new Card(0,0,gameScreen,5,false,"Name");
        Card addedCard = new Card(0,0,gameScreen,5,false,"Name");
        addedCard.setCurrentRegion("TestDefault");

        // Initialise test game region
        float xLeftEdge =100;
        float xRightEdge =500;
        float yTopEdge =500;
        float yBottomEdge =100;
        ActiveRegion activeRegion = new ActiveRegion(xLeftEdge,xRightEdge,yTopEdge,yBottomEdge);

        // Set max region capacity
        activeRegion.setMaxNumCardsInRegion(11);
        // card region is empty
        activeRegion.getCardsInRegion().clear();
        // Add initial card to region
        activeRegion.addCard(initialCard);

        // Call the addCardMethod on new card
        activeRegion.addCard(addedCard);

        // Should update values to the next available position
        float expectedCardX = xLeftEdge+addedCard.getWidth()/2+(activeRegion.getCardsInRegion().indexOf(addedCard)*addedCard.getWidth());
        float expectedCardY = yBottomEdge+addedCard.getHeight()/2;
        String expectedCardRegion = "Active";

        // Check to ensure that variables are at their expected values
        assertEquals(expectedCardX, addedCard.position.x, 0.0f);
        assertEquals(expectedCardY, addedCard.position.y, 0.0f);
        assertEquals(expectedCardRegion, addedCard.getCurrentRegion());

    }
}