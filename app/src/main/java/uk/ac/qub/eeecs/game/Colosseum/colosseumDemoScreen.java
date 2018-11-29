package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;
import android.graphics.Color;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
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

    /**
     * Define the background board
     */
    private GameObject mGameBackground;

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

        mCard = new Card(240, 120, this, 1, 1, 'A');
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


    /**
     * Update the card demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        cardDrag();
    }

    public void cardDrag() {
        mInput = this.getGame().getInput();

        for (int i = 0; i < mInput.getTouchEvents().size(); i++) {
            Vector2 touchLocation = new Vector2(0, 0);

            int touchType = mInput.getTouchEvents().get(i).type;
            ViewportHelper.convertScreenPosIntoLayer(mDefaultScreenViewport, mInput.getTouchEvents().get(i).x,
                    mInput.getTouchEvents().get(i).y, mGameViewport, touchLocation);

            //Move the card
            if (touchType == TouchEvent.TOUCH_DRAGGED
                    && mCard.getBound().contains(touchLocation.x, touchLocation.y)) mCard.position = touchLocation;
            //Flip the card - C5
            if (touchType == TouchEvent.TOUCH_SINGLE_TAP
                    && mCard.getBound().contains(touchLocation.x, touchLocation.y)) {
                Bitmap b = mCard.getBitmap();
                Bitmap front = mGame.getAssetManager().getBitmap("CardFront");
                Bitmap back = mGame.getAssetManager().getBitmap("CardBack");
                if (b == front) mCard.setBitmap(back);
                else if (b == back) mCard.setBitmap(front);
            }

            //Bound the card - Story C3
            if (mCard.getBound().getLeft() < 0)
                mCard.position.x = mCard.getBound().halfWidth;
            if (mCard.getBound().getBottom() < 0)
                mCard.position.y = mCard.getBound().halfHeight;
            if (mCard.getBound().getRight() > mGameViewport.getRight())
                mCard.position.x = mGameViewport.getRight() - mCard.getBound().halfWidth;
            if (mCard.getBound().getTop() > mGameViewport.getTop())
                mCard.position.y = mGameViewport.getTop() - mCard.getBound().halfHeight;
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

        mCard.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
    }
}
