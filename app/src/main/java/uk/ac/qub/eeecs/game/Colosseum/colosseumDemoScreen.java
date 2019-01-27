package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

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
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

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
    private Card mCard, mCard2;

    /**
     * Define the background board
     */
    private GameObject mGameBackground;

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

        // Create the background
        Bitmap mBackgroundBitmap = getGame()
                .getAssetManager().getBitmap("ArenaFloor");

        mGameBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mBackgroundBitmap.getHeight(), mBackgroundBitmap, this);

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
        mCard = new Card(240, 120, this);
        mCard.setAttack(1);
        mCard.setDefence(4);
        mCard.setMana(5);
        mCard2 = new Card(280, 180, this);
        mCard2.setAttack(3);
        mCard2.setDefence(2);
        mCard2.setMana(4);

        //User Story 7, Sprint 4 - Scott
        dCards.add(generateRandomDeck()); //single random card, shows that it has random values and appears randomly.
        //for(int i = 0; i<30; i++) {   //creates a mass of cards for testing purposes, un-comment at your own demise.
        //    dCards.add(generateRandomDeck());
        //}

        //Setting up demo player:
        Bitmap p2bit = mGame.getAssetManager().getBitmap("Test");
        Player p2 = new Player(spacingX * 5.0f, spacingY * 5.0f, this, p2bit, 'a');
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
        dCard.setAttack(RANDOM.nextInt(9)); //limit of values 0-9
        dCard.setDefence(RANDOM.nextInt(9)); //limit of values 0-9
        dCard.setMana(RANDOM.nextInt(9)); //limit of values 0-9

        return dCard;
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

            mCard.cardDrag(mCard, mDefaultScreenViewport, mGameViewport, mGame);
            mCard2.cardDrag(mCard2, mDefaultScreenViewport, mGameViewport, mGame);

            for(Card deckOfCards: dCards){ //updates each card held within the "dCards" variable, Sprint 4 Story 7
                deckOfCards.cardDrag(deckOfCards, mDefaultScreenViewport, mGameViewport, mGame);
            }

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
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mGameBackground.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        //Draw the cards onscreen
        mCard.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        mCard2.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        for(Card deckOfCards: dCards){ //draws each card held within the "dCards" variable, Sprint 4 Story 7
            deckOfCards.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        //Draw PushButtons onscreen:
        for (ToggleButton button : mButtons) {
            button.draw(elapsedTime, graphics2D);
        }
    }
}
