package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
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

    /**
     * Define the player's spaceship
     */
    private Card mCard;
    private Card mCard2;
    /**
     * Define the background board
     */
    private GameObject mGameBackground;

    //Array List to hold the PushButtons
    private List<PushButton> mButtons = new ArrayList<>();

    //Push button for ending player's turn
    private PushButton mEndTurnButton;

    //Paint item that will be used to tint the 'End Turn' button
    private Paint mButtonTint;

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

        mEndTurnButton = new PushButton(
                spacingX * 21.0f, spacingY * 7.0f , spacingX*2.5f, spacingY*2.0f,
                "EndTurn", this);
        mButtons.add(mEndTurnButton);

        Paint mButtonTint = new Paint();

        //Setting up demo cards:
        mCard = new Card(240, 120, this);
        mCard.setAttack(1);
        mCard.setDefence(4);
        mCard.setMana(5);
        mCard2 = new Card(280, 180, this);
        mCard2.setAttack(3);
        mCard2.setDefence(2);
        mCard2.setMana(4);
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
    }


    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        mCard.cardDrag(mCard, mDefaultScreenViewport, mGameViewport,  mGame);
        mCard2.cardDrag(mCard2, mDefaultScreenViewport, mGameViewport,  mGame);

        for (PushButton button : mButtons) {
            button.update(elapsedTime);
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

        //Draw PushButtons onscreen:
        for (PushButton button : mButtons) {
            button.draw(elapsedTime, graphics2D);
        }
    }
}
