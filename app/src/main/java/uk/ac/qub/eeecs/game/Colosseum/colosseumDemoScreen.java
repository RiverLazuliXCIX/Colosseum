package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.icu.util.Output;

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
    /**
     * Define cards
     **/
    private List<Card> mCards = new ArrayList<Card>();

    /**
     * Define the background board
     */
    private GameObject mGameBackground;

    //Check if card is being held
    private Card mCardHeld = null;

    //Array List to hold the Toggle Button ('End Turn')
    private List<ToggleButton> mButtons = new ArrayList<>();

    //Array list to hold a deck of cards - Story 7 Sprint 4
    private List<Card> dCards = new ArrayList<>();

    //Push button for ending player's turn
    private ToggleButton mEndTurnButton;

    //Paint item that will be used to tint the 'End Turn' button
    private Paint mButtonTint;

    //Define a TEST player
    private Player p2;
    private Bitmap p2bit;

    protected int edgeCounter = 0; //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
    protected boolean edgeCase = false;

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
    }

    private void setUpGameObjects() {
        // Load in the assets used by the steering demo
        mGame.getAssetManager().loadAssets("txt/assets/ColosseumAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");


        // Create the background
        Bitmap mBackgroundBitmap = getGame()
                .getAssetManager().getBitmap("ArenaFloor");

        mGameBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mBackgroundBitmap.getHeight(), mBackgroundBitmap, this);

        //Deciding who starts first, Story 16 Sprint 4 - Scott
        if (edgeCase) { //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
            edgeCaseTest();
        } else {
        switch (coinFlipStart()) { //Start of Story 16, Sprint 4.
            case 0: //dCards.add(generateRandomDeck());  //used the random card generator to test if each case could be entered
                // tails - player starts
                break;
            case 1: //dCards.add(generateRandomDeck()); //heads - ai starts
                //dCards.add(generateRandomDeck());
                break;
            case 2:
                //dCards.add(generateRandomDeck());
                //dCards.add(generateRandomDeck());
                //dCards.add(generateRandomDeck());//edge of coin - set opponent health to 0, auto win game.
                break;
            default: //output an error
                break;
        }
    }
        
        //Create the Push Buttons:
        // Spacing that will be used to position the buttons:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        mEndTurnButton = new ToggleButton(
                spacingX * 21.0f, spacingY * 7.0f , spacingX*2.5f, spacingY*2.0f,
                "EndTurn", "EndTurn2", this);
        mButtons.add(mEndTurnButton);

        //Paint mButtonTint = new Paint();

        //Setting up demo cards:
        mCards.add(new Card(100, 100, this));//) = new Card(100, 100, this);
        mCards.get(0).setAttack(1);//[0].setAttack(1);
        mCards.get(0).setDefence(4);
        mCards.get(0).setMana(5);
        mCards.add(new Card(200, 100, this));
        mCards.get(1).setAttack(3);
        mCards.get(1).setDefence(2);
        mCards.get(1).setMana(4);

        //User Story 7, Sprint 4 - Scott
        dCards.add(generateRandomDeck()); //single random card, shows that it has random values and appears randomly.
        //for(int i = 0; i<30; i++) {   //creates a mass of cards for testing purposes, un-comment at your own demise.
        //    dCards.add(generateRandomDeck());
        //}

        //Setting up demo player:
        //Bitmap p2bit = mGame.getAssetManager().getBitmap("Test");
        //Player p2 = new Player(spacingX * 5.0f, spacingY * 5.0f, this, p2bit, "Hircine");
        p2=new Player(this,"Hircine");
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

    protected void endPlayerTurn() {
        //p2.setYourTurn(false);

        //When player ends turn, a 'YourTurn' variable on the player should turn to false
        //This will prevent player from attempting to draw from their deck, or playing a card
        //It will also trigger the AI to make a move
    }

    private Card generateRandomDeck() { //Scott Barham, Story 7 Sprint 4
        Card dCard;
        dCard = new Card(RANDOM.nextInt(
                (int)mDefaultLayerViewport.getWidth()-75)+35, //-75, +35 to prevent cards spawning outside the side regions of the screen.
                RANDOM.nextInt((int)mDefaultLayerViewport.getHeight()-75)+35, //-75, +35 to prevent cards spawning outside the top/bottom regions of the screen.
                this);
        dCard.setAttack(RANDOM.nextInt(10)); //limit of values 0-9
        dCard.setDefence(RANDOM.nextInt(10)); //limit of values 0-9
        dCard.setMana(RANDOM.nextInt(10)); //limit of values 0-9

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

    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        mInput = mGame.getInput();

        List<TouchEvent> touchEvents = mInput.getTouchEvents();
        if (touchEvents.size() > 0) {

            for (int i = 0; i < 2; i++)
                mCards.get(i).cardDrag(mCards, mDefaultScreenViewport, mGameViewport, mGame);
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

            mEndTurnButton.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);

            if (mEndTurnButton.isToggledOn()) {
                endPlayerTurn();
            }
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

        //Draw the cards onscreen
        mCards.get(0).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        mCards.get(1).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        if(edgeCase){ //To test for the edge case of the coin flip, User Story 18.1, Sprint 4 - Scott
            int screenHeight = graphics2D.getSurfaceHeight();
            float textHeight = screenHeight / 30.0f;
            textPaint.setTextSize(textHeight); //create a appropriate sizing of text
            graphics2D.drawText("Iterations to reach Edge Case:", 100.0f, 50.0f, textPaint); //draw the text "Iterations to reach Edge Case:"
            graphics2D.drawText(String.valueOf(edgeCounter), 100.0f, 100.0f, textPaint);
        }

        for(Card deckOfCards: dCards){ //draws each card held within the "dCards" variable, Sprint 4 Story 7
            deckOfCards.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        //Draw PushButtons onscreen:
        for (ToggleButton button : mButtons) {
            button.draw(elapsedTime, graphics2D);
        }
    }
}
