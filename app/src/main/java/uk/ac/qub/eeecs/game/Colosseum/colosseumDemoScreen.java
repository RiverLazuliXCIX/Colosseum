package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.icu.util.Output;
import android.os.storage.StorageManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.ToggleButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.game.PauseMenuScreen;

/**
 * Starter class for Card game stories
 *
 * @version 1.0
 */
public class colosseumDemoScreen extends GameScreen{

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private LayerViewport mGameViewport;
    private Input mInput;
    private static final Random RANDOM = new Random();

     //Define cards
    private List<Card> mCards = new ArrayList<Card>();

     //Define the background board
    private GameObject mGameBackground;

    //Check if card is being held
    private Card mCardHeld = null;

    //Array List to hold the Push Buttons
    private List<PushButton> mButtons = new ArrayList<>();

    //Push button for ending player's turn
    private PushButton mEndTurnButton;
    private PushButton mEndTurnButtonOff;

    //Push Button for pausing the game
    private PushButton mPauseButton;

    //Push Button for drawing a card
    private PushButton mDrawButton;

    //Array list to hold a deck of cards - Story 7 Sprint 4
    private List<Card> dCards = new ArrayList<>();

    //Define a test player
    private Player p2;

    //Define a Test Deck
    private CardDeck playerDeck, enemyDeck;

    // Define a test Opponent
    private AIOpponent opponent;

    protected int edgeCounter = 0; //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
    protected static boolean edgeCase = false;

    //Paint item that will be used to draw text
    private Paint mText;

    //Denarius
    //SINGLE COIN
    private GameObject pDenarius, eDenarius;

    //Test region
    BoardRegion playerRegion;
    BoardRegion opponentRegion;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the Card game screen
     *
     * @param game Game to which this screen belongs
     */
    public colosseumDemoScreen(Game game) {
        super("CardScreen", game);

        setupViewports();
        setUpGameObjects();
        setUpDecks();
        coinFlipStart();
        coinFlipResult();
    }

    private void setUpGameObjects() {
        // Load in the assets used by the steering demo
        mGame.getAssetManager().loadAssets("txt/assets/ColosseumAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");
        //mGame.getAssetManager().loadAssets("txt/assets/CardAssets.JSON");

        if (edgeCase) { //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
            edgeCaseTest();
        }

        // Spacing that will be used to position the objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the background
        Bitmap mBackgroundBitmap = getGame()
                .getAssetManager().getBitmap("ArenaFloor");

        mGameBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), mBackgroundBitmap, this);


        //PAINT OBJECT:
        //Initialise Paint object I will use to draw text
        mText = new Paint();
        int screenHeight = mDefaultScreenViewport.height;
        float textHeight = screenHeight / 24.0f;
        mText.setTextSize(textHeight);
        mText.setColor(Color.rgb(255,255,255));
        mText.setTypeface(Typeface.create("Arial",Typeface.BOLD));


        //Create the Push Buttons:
        //This button is created twice, where one has no effect when clicked, ie it is greyed out.
        mEndTurnButton = new PushButton (
                spacingX * 4.5f, spacingY * 1.5f, spacingX * 0.5f, spacingY * 0.5f,
                "EndTurn", this);

        //'Greyed out' version of the endTurnButton:
        mEndTurnButtonOff = new PushButton (
                spacingX * 4.5f, spacingY * 1.5f, spacingX * 0.5f, spacingY * 0.5f,
                "EndTurn2",  this);

        mPauseButton = new PushButton(
                spacingX*4.7f, spacingY*2.7f, spacingX*0.4f, spacingY*0.4f, "Cog", "CogSelected", this);
        mButtons.add(mPauseButton);

        //Button used to draw cards from deck:
        mDrawButton = new PushButton(
                spacingX*0.5f, spacingY*0.5f, spacingX*0.8f, spacingY*0.8f, "CardDeckImg", this);
        mButtons.add(mDrawButton);


        //Setting up demo cards:
        mCards.add(new MinionCard(100, 100,  this, 50, 11, 42));//, "CardFront"));
        //mCards.get(0).setAttack(11);
        //mCards.get(0).setDefence(42);
        //mCards.get(0).setMana(50);
        //mCards.get(0).setBitmap(getGame().getAssetManager().getBitmap("no1"));
        mCards.add(new MinionCard(200, 100, this, 4, 3, 2));//, "CardFront"));
        //mCards.get(1).setAttack(3);
        //mCards.get(1).setDefence(2);
        //mCards.get(1).setMana(4);
        //mCards.get(1).setBitmap(getGame().getAssetManager().getBitmap("no2"));

        //User Story 7, Sprint 4 - Scott
        dCards.add(generateRandomDeck()); //single random card, shows that it has random values and appears randomly.
        //for(int i = 0; i<30; i++) {   //creates a mass of cards for testing purposes, un-comment at your own demise.
        //    dCards.add(generateRandomDeck());
        //}

        //Setting up demo player:
        p2=new Player(this,"Meridia");
        opponent = new AIOpponent(this,"EmperorCommodus");

        //Create denarius objects
        Bitmap denarius = getGame()
                .getAssetManager().getBitmap("Denarius");
        //player
        p2.setCurrentMana(4);
        p2.setCurrentManaCap(4);

        opponent.setCurrentMana(4);
        opponent.setCurrentManaCap(4);

        //SINGLE COIN
        pDenarius = new GameObject(spacingX * 2.95f, spacingY * 0.38f, 30, 30, denarius, this);
        eDenarius = new GameObject(spacingX * 2.95f, spacingY * 2.78f, 30, 30, denarius, this);

        /* FOR MULTIPLE COINS
        createMultipleCoins(pDenarius, spacingX, spacingY, 0.38f, denarius);
        createMultipleCoins(eDenarius, spacingX, spacingY, 2.78f, denarius);
        */

        // Defining board region width and height
        float regionWidth= ViewportHelper.convertXDistanceFromLayerToScreen(mDefaultLayerViewport.getWidth(),mDefaultLayerViewport,mDefaultScreenViewport);
        float regionHeight = ViewportHelper.convertYDistanceFromLayerToScreen(mDefaultLayerViewport.halfHeight-(p2.getPortraitHeight()),mDefaultLayerViewport,mDefaultScreenViewport);

        // Creating board playable regions (+200)
        playerRegion = new BoardRegion(mDefaultScreenViewport.left,mDefaultScreenViewport.top+mDefaultScreenViewport.height/2, regionWidth,regionHeight,this);
        opponentRegion = new BoardRegion(mDefaultScreenViewport.left,mDefaultScreenViewport.top+p2.getPortraitHeight()+200 ,regionWidth ,regionHeight ,this);

    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public void endPlayerTurn() {
        //When player ends turn, a 'YourTurn' variable on the player should turn to false
        //This will prevent player from attempting to draw from their deck, or playing a card
        //It will also trigger the AI to make a move
        p2.setYourTurn(false);
    }

    public void setUpDecks() {
        //This method sets up the player and enemy decks, called when screen is loaded
        playerDeck = new CardDeck(1, "Basic Player Deck", this, false);
        enemyDeck = new CardDeck(2, "Basic Enemy Deck", this, true);
    }

    private Card generateRandomDeck() { //Scott Barham, Story 7 Sprint 4
        Card dCard;
        dCard = new MinionCard(RANDOM.nextInt((int)mDefaultLayerViewport.getWidth()-75)+35, //-75, +35 to prevent cards spawning outside the side regions of the screen.
                RANDOM.nextInt((int)mDefaultLayerViewport.getHeight()-75)+35, //-75, +35 to prevent cards spawning outside the top/bottom regions of the screen.
                this, RANDOM.nextInt(10), RANDOM.nextInt(10), RANDOM.nextInt(10));//, "CardFront");
        //dCard.setAttack(RANDOM.nextInt(10)); //limit of values 0-9
        //dCard.setDefence(RANDOM.nextInt(10)); //limit of values 0-9
        //dCard.setMana(RANDOM.nextInt(10)); //limit of values 0-9

        return dCard;
    }

    private int coinFlipStart() { //Scott, User Story 16, Sprint 4
        int flip = RANDOM.nextInt(6001);
        if(flip==6000) { //side of coin (1/6000 chance to auto-win)
            return 2;
        }else
        if(flip>=3000 && flip<6000){ //heads (ai starts)
            return 1;
        }else
        if(flip>=0 && flip<3000){ //tails (user starts)
            return 0;
        }
        return -1; //for error testing only
    }

    // Method for building hand (4 if heads, 3 if tails) - Dearbhaile

    private void coinFlipResult() {
        int result = coinFlipStart();
        switch (result) {
            case 0: // ie, player starts
                p2.setYourTurn(true);
                for (int i = 0; i < 3; i++) {
                    playerDeck.drawTopCard();
                }

                for (int i = 0; i < 4; i++) {
                    enemyDeck.drawTopCard();
                }
                break;
            case 1: // ie, ai starts
                p2.setYourTurn(false);
                for (int i = 0; i < 4; i++) {
                    playerDeck.drawTopCard();
                }

                for (int i = 0; i < 3; i++) {
                    enemyDeck.drawTopCard();
                }
                break;
            case 2: //edge of coin - set opponent health to 0, auto win game.

                break;
            default: //output an error as this should not be reached

                break;
        }
    }

    private void edgeCaseTest() { //Testing for the edge case scenario of the coin flip, User Story 18.1, Sprint 4 - Scott
        boolean i = true;
        while(i){
            edgeCounter++;
            switch(coinFlipStart()){
                case 0://tails - player starts
                    break;
                case 1: //heads - ai starts
                    break;
                case 2: //edge of coin - set opponent health to 0, auto win game.
                    i=false;
                    break;
                default: //output an error
                    break;
            }
        }
    }

    public boolean getEdgeCase() {
        return edgeCase;
    }
    public static void setEdgeCase(boolean edgeCaseInput) {
        edgeCase = edgeCaseInput;
    }

    public void createMultipleCoins(List<GameObject> denarius, float spacingX, float spacingY, float scaleVert, Bitmap b) {
        float scaleHor = 2.95f;

        for(int i = 0; i < p2.getCurrentMana(); i++) {
            denarius.add(new GameObject(spacingX * scaleHor, spacingY * scaleVert, 30, 30, b, this));
            scaleHor += 0.04f;
        }
    }

    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        mInput = mGame.getInput();

        p2.update(elapsedTime);
        opponent.update(elapsedTime);

        List<TouchEvent> touchEvents = mInput.getTouchEvents();
        if (touchEvents.size() > 0) {

            for (int i = 0; i < mCards.size(); i++){
                mCards.get(i).cardDrag(mCards, mDefaultScreenViewport, mGameViewport, mGame);

                // Updates both regions for all cards
                playerRegion.update(mCards.get(i));
                opponentRegion.update(mCards.get(i));
            }

            for (PushButton button : mButtons)
                button.update(elapsedTime);

            if(mPauseButton.isPushTriggered()){
                mGame.getScreenManager().changeScreenButton(new PauseMenuScreen(mGame));
            }

            mEndTurnButton.update(elapsedTime);
            mEndTurnButtonOff.update(elapsedTime);

            if (mEndTurnButton.isPushTriggered()) {
                endPlayerTurn();
            }


            //Still working on this - Dearbhaile
            //If player draws a card once their deck is at 0, they will be navigated to 'FatigueScreen'
            //On this screen, it will be displayed how much health they will lose (cumulative value)
            //Screen then disappears after 3 seconds.

            if (mDrawButton.isPushTriggered()) {
                if (!playerDeck.getDeck().isEmpty()) {
                    playerDeck.drawTopCard();
                } else {
                    mGame.getScreenManager().changeScreenButton(new FatigueScreen(mGame));
                }
            }

            for(Card deckOfCards: dCards) //Allows each card held within the "dCards" variable to be dragged, Story 29 Sprint 5 Scott
                deckOfCards.cardDrag(dCards, mDefaultScreenViewport, mGameViewport, mGame);


            //Eventually the cards from the hand should be displayed onscreen:
            for (Card cards : playerDeck.getmCardHand()) {
                cards.cardDrag(playerDeck.getmCardHand(), mDefaultScreenViewport, mDefaultLayerViewport, mGame);
            }

            for (Card cards : enemyDeck.getmCardHand()) {
                cards.cardDrag(enemyDeck.getmCardHand(), mDefaultScreenViewport, mDefaultLayerViewport, mGame);
            }

            //mCard2.cardDrag(mCard2, mDefaultScreenViewport, mGameViewport, mGame);

            /*
            for(Card deckOfCards: dCards){ //updates each card held within the "dCards" variable, Sprint 4 Story 7
                deckOfCards.cardDrag(deckOfCards, mDefaultScreenViewport, mGameViewport, mGame);
            }
            */
            /*
            for (int i = 0; i < mInput.getTouchEvents().size(); i++) {
                Vector2 touchLocation = new Vector2(0, 0);

                int touchType = mInput.getTouchEvents().get(i).type;
                ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, mInput.getTouchEvents().get(i).x,
                        mInput.getTouchEvents().get(i).y, mGameViewport, touchLocation);

                //Move the card - Story C1
                if (touchType == TouchEvent.TOUCH_DRAGGED
                        && mCardHeld == null) {
                    //Check which card was touched, if any
                    for (int j = 0; j < 2; j++) {
                        if (mCards[j].getBound().contains(touchLocation.x, touchLocation.y)) {
                            mCardHeld = mCards[j];
                        }
                    }
                }

                //if a card was touched, and the event was a drag, move it
                if (touchType == TouchEvent.TOUCH_DRAGGED
                        && mCardHeld != null)
                    mCardHeld.position = touchLocation;

                //release the card, meaning no card is now held
                if (touchType == TouchEvent.TOUCH_UP
                        && mCardHeld != null)
                    mCardHeld = null;

            }
            */
        }
    }

    /**
     * Draw the card demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */

    private Paint textPaint = new Paint();

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        // Clear the screen
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mGameBackground.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        // Draw the player portrait (Just for testing, still working on it)
        p2.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        opponent.draw(elapsedTime,graphics2D,mGameViewport,mDefaultScreenViewport);

        // Draw player board region (Just for testing again)
        //graphics2D.drawRect(playerRegion.getBoardRegionRect(),playerRegion.getBoardRegionPaint());
       // graphics2D.drawRect(opponentRegion.getBoardRegionRect(),opponentRegion.getBoardRegionPaint());

        //Draw initial 'End Turn' button onscreen, which toggles between pressable and not pressable image:
        if (p2.getYourTurn())
            mEndTurnButton.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        else
            mEndTurnButtonOff.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        //Draw the remainder of the buttons:
        for (PushButton buttons : mButtons) {
            buttons.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        //Draw the cards onscreen:
        /*for (int i = 0; i < mCards.size(); i++) //Fix to make sure everyone card introduced is drawn onscreen - Story 30 Sprint 5 Scott.
            mCards.get(i).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);*/

        if(edgeCase){ //To test for the edge case of the coin flip, User Story 18.1, Sprint 4 - Scott
            int screenHeight = graphics2D.getSurfaceHeight();
            float textHeight = screenHeight / 30.0f;
            textPaint.setTextSize(textHeight); //create a appropriate sizing of text
            graphics2D.drawText("Iterations to reach Edge Case:", 100.0f, 50.0f, textPaint); //draw the text "Iterations to reach Edge Case:"
            graphics2D.drawText(String.valueOf(edgeCounter), 100.0f, 100.0f, textPaint);
        }

        /*for(Card deckOfCards: dCards) //draws each card held within the "dCards" variable, Sprint 4 Story 7
            deckOfCards.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        */

        //Draw the two player's hands, user and enemy:
        for (int i = 0; i < playerDeck.getmCardHand().size(); i++) {
            playerDeck.getmCardHand().get(i).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        for (int i = 0; i < enemyDeck.getmCardHand().size(); i++) {
            enemyDeck.getmCardHand().get(i).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }


        //Spacing that will be used to position everything:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        //PLAYER STATS BEING DRAWN:
        //Draw player mana
        //SINGLE COIN
        pDenarius.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        /*MULTIPLE COINS
        for(int i = 0; i < p2.getCurrentMana(); i++)
            pDenarius.get(i).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        */

        graphics2D.drawText(p2.getCurrentMana()+ "/" + p2.getCurrentManaCap(), spacingX * 12.9f, spacingY * 12f, mText);

        //Draw player card stats
        int pCardsLeft = playerDeck.getDeck().size();
        int pCardsHand= playerDeck.getmCardHand().size();
        int pCardsDead = playerDeck.getmDiscardPile().size(); // All stats accurate - Dearbhaile
        graphics2D.drawText("Deck: " + pCardsLeft, spacingX * 7.0f, spacingY * 11.6f, mText);
        graphics2D.drawText("Hand: " + pCardsHand, spacingX * 7.0f, spacingY * 12.2f, mText);
        graphics2D.drawText("Graveyard: " + pCardsDead, spacingX * 7.0f, spacingY * 12.8f, mText);

        //Draw opponent mana
        //SINGLE COIN
        eDenarius.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        /*MULTIPLE COINS
        int eMana = 20;
        for(int i = 0; i < eMana; i++)
            eDenarius.get(i).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        */

        graphics2D.drawText(opponent.getCurrentMana()+"/"+opponent.getCurrentManaCap(), spacingX * 12.9f, spacingY * 1.2f, mText);

        //Draw opponent card stats
        int eCardsLeft = enemyDeck.getDeck().size();
        int eCardsHand = enemyDeck.getmCardHand().size();
        int eCardsDead = enemyDeck.getmDiscardPile().size(); // All stats accurate - Dearbhaile
        graphics2D.drawText("Deck: " + eCardsLeft, spacingX * 7.0f, spacingY * 0.6f, mText);
        graphics2D.drawText("Hand: " + eCardsHand, spacingX * 7.0f, spacingY * 1.2f, mText);
        graphics2D.drawText("Graveyard: " + eCardsDead, spacingX * 7.0f, spacingY * 1.8f, mText);
    }
}
