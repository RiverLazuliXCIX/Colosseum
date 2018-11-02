package uk.ac.qub.eeecs.game.platformDemo;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.engine.AssetManager;

/**
 * A simple platform-style demo that generates a number of platforms and
 * provides a player controlled entity that can move about the images.
 * <p>
 * Illustrates button based user input, animations and collision handling.
 * <p>
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class PlatformDemoScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Define the width and height of the game world
     */
    private final float LEVEL_WIDTH = 2000.0f;
    private final float LEVEL_HEIGHT = 320.0f;

    /**
     * Define the layer viewport used to display the platforms
     */
    private LayerViewport mPlatformLayerViewport;
    // US26
    private final float IVP_WIDTH = 240; // initial viewport width
    private final float IVP_HEIGHT = 160; // initial viewport height
    private final float MAXVP_WIDTH = 480; // maximum viewport width
    private final float MAXVP_HEIGHT = 320; // maximum viewport height

    /**
     * Create three simple touch controls for player input
     * Create a powerUp button for User Story 21
     */
    private PushButton moveLeft, moveRight, jumpUp, powerUp;
    private List<PushButton> mControls;

    /**
     * Define an array of sprites to populate the game world
     */
    private ArrayList<Platform> mPlatforms;

    /**
     * Define the player
     */
    private Player mPlayer;
    private boolean isSoundPlaying;

    private AudioManager am;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a simple platform game level
     *
     * @param game Game to which this screen belongs
     */
    public PlatformDemoScreen(Game game) {
        super("PlatformDemoScreen", game);

        // Load in the assets used by this layer
        mGame.getAssetManager().loadAssets("txt/assets/PlatformDemoScreenAssets.JSON");

        //User Story 24: Loads JumpSound into AssetManager, and creates a value to show that JUMPSOUND is not playing
        mGame.getAssetManager().loadAndAddSound("JumpSound", "sound/JumpSound.wav");
        isSoundPlaying = false;

        // US23: Loads background music to the asset manager and creates an audio manager to play/pause/resume/stop music
        mGame.getAssetManager().loadAndAddMusic("BckgrndMsc", "sound/PlatformBckgrndMsc.mp3");
        am = getGame().getAudioManager();

        // Create the layer viewport used to display the platforms (and other game
        // objects). The default, inherited, layer viewport will be used to display
        // movement controls and the default, inherited, screen viewport will be used
        // to define the drawable region on the screen.
        mPlatformLayerViewport = new LayerViewport(240, 160, IVP_WIDTH, IVP_HEIGHT); // US26, update arguments with constants

        // Create and position the touch controls (relative to the default layer viewport)

        // Determine the layer size to correctly position the touch buttons
        float layerWidth = mDefaultLayerViewport.halfWidth * 2.0f;

        // Create and position the touch buttons
        mControls = new ArrayList<>();
        moveLeft = new PushButton(35.0f, 30.0f, 50.0f, 50.0f,
                "LeftArrow", "LeftArrowSelected", this);
        mControls.add(moveLeft);
        moveRight = new PushButton(100.0f, 30.0f, 50.0f, 50.0f,
                "RightArrow", "RightArrowSelected", this);
        mControls.add(moveRight);
        jumpUp = new PushButton((layerWidth - 35.0f), 30.0f, 50.0f, 50.0f,
                "UpArrow", "UpArrowSelected", this);
        mControls.add(jumpUp);
        //User story 21 - Scott
        powerUp = new PushButton((layerWidth - 100.0f), 30.0f, 50.0f, 50.0f,
                "PowerUp", "PowerUpSelected", this);
        mControls.add(powerUp);
        // Create and position the game objects (relative to the platform viewport)

        // Create the player
        mPlayer = new Player(100.0f, 100.0f, this);

        // Create the platforms
        mPlatforms = new ArrayList<>();

        // Add a wide platform for the ground tile
        int groundTileWidth = 64, groundTileHeight = 35, groundTiles = 50;
        mPlatforms.add(
                new Platform(groundTileWidth * groundTiles / 2, groundTileHeight / 2,
                        groundTileWidth * groundTiles, groundTileHeight,
                        "Ground", groundTiles, 1, this));

        // Add a number of randomly positioned platforms. They are not added in
        // the first 200 units of the level to avoid overlap with the player.
        // A simple (but not that useful) approach is used to position the platforms
        // to avoid overlapping.
        //User story 16 - Random number generator to generate which platform will be used - Diarmuid
        // Integer.toString(randPlatform) used to add the number from the random number genorator
        // to the end of the name to used that image for the game instance
        Random random = new Random();
        int randPlatform = random.nextInt(3) + 1;
        int numPlatforms = 30, platformOffset = 200;
        float platformWidth = 70, platformHeight = 70, platformX, platformY = platformHeight;
        for (int idx = 0; idx < numPlatforms; idx++) {
            platformX = platformOffset;
            if (random.nextFloat() > 0.33f)
                platformY = (random.nextFloat() * (LEVEL_HEIGHT - platformHeight));
            mPlatforms.add(new Platform(platformX, platformY, platformWidth, platformHeight,
                    "Platform" + Integer.toString(randPlatform), this));
            platformOffset += (random.nextFloat() > 0.5f ?
                    platformWidth : platformWidth + random.nextFloat() * platformWidth);
        }
    }

    //User Story 18: Detecting Overlapping Platforms - Dearbhaile.
    public boolean checkOverLapping() {
        boolean isOverlapping = false;
        for (int i = 0; i < mPlatforms.size(); i++) {
            if (CollisionDetector.isCollision(mPlatforms.get(i).getBound(), mPlatforms.get(mPlatforms.size() - 1).getBound())) {
                isOverlapping = true;
                break;
            }
        }
        return isOverlapping;
    }

    // US23: Checks if the music is already playing before playing it
    // I believe that something happens to the game's audio manager (destroyed/disposed) whenever
    // the song finishes playing. The song ending will then cause the game to crash without
    // some exception handling. Until a fix is found, this is in place.
    public void playBckgrndMsc() {
        try {
            if (!am.isMusicPlaying()) {
                am.playMusic(getGame().getAssetManager().getMusic("BckgrndMsc"));
            }
        } catch (Exception ex) {
        }
    }


    // /////////////////////////////////////////////////////////////////////////
    // Update and Draw
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the platform demo screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        // US23
        playBckgrndMsc();

        // Update the touch buttons checking for player input
        for (PushButton control : mControls)
            control.update(elapsedTime, mDefaultLayerViewport, mDefaultScreenViewport);

        // Update the player - User story 21 Added powerUp button - Scott
        mPlayer.update(elapsedTime, moveLeft.isPushed(),
                moveRight.isPushed(), jumpUp.isPushed(), mPlatforms, powerUp.isPushed());

        //If player jumps, play sound
        if (mPlayer.velocity.y > 20 && (!isSoundPlaying)) {
            mGame.getAssetManager().getSound("JumpSound").play();
            isSoundPlaying = true;
        }

        if (mPlayer.velocity.y < 20) {
            isSoundPlaying = false;
        }

        // Ensure the player cannot leave the confines of the world
        BoundingBox playerBound = mPlayer.getBound();
        if (playerBound.getLeft() < 0)
            mPlayer.position.x -= playerBound.getLeft();
        else if (playerBound.getRight() > LEVEL_WIDTH)
            mPlayer.position.x -= (playerBound.getRight() - LEVEL_WIDTH);

        if (playerBound.getBottom() < 0)
            mPlayer.position.y -= playerBound.getBottom();
        else if (playerBound.getTop() > LEVEL_HEIGHT)
            mPlayer.position.y -= (playerBound.getTop() - LEVEL_HEIGHT);

        checkOverLapping();

        // US26
        if (Math.abs(mPlayer.velocity.x) <= 20) { // set the viewport to the initial settings
            mPlatformLayerViewport.set(mPlatformLayerViewport.x, mPlatformLayerViewport.y, IVP_WIDTH, IVP_HEIGHT);
        } else if (Math.abs(mPlayer.velocity.x) >= 400) { // set the viewport to the max settings
            mPlatformLayerViewport.set(mPlatformLayerViewport.x, mPlatformLayerViewport.y, MAXVP_WIDTH, MAXVP_HEIGHT);
        } else {
            float ratio = (Math.abs(mPlayer.velocity.x) - 20) / 380; // work out the ratio of current to max speed.
            float newVPWidth = ratio * (MAXVP_WIDTH - IVP_WIDTH) + IVP_WIDTH; // calculate new width
            float newVPHeight = ratio * (MAXVP_HEIGHT - IVP_HEIGHT) + IVP_HEIGHT; // calculate new height
            mPlatformLayerViewport.set(mPlatformLayerViewport.x, mPlatformLayerViewport.y, newVPWidth, newVPHeight); // set the viewport accordingly
        }

        // Focus the layer viewport on the player's x location
        mPlatformLayerViewport.x = mPlayer.position.x;

        // Ensure the viewport cannot leave the confines of the world
        if (mPlatformLayerViewport.getLeft() < 0)
            mPlatformLayerViewport.x -= mPlatformLayerViewport.getLeft();
        else if (mPlatformLayerViewport.getRight() > LEVEL_WIDTH)
            mPlatformLayerViewport.x -= (mPlatformLayerViewport.getRight() - LEVEL_WIDTH);

        // US26 comment these lines out to stop viewport being restrained in y axis and causing camera problems
        //if (mPlatformLayerViewport.getBottom() < 0)
        //    mPlatformLayerViewport.y -= mPlatformLayerViewport.getBottom();
        //else if (mPlatformLayerViewport.getTop() > LEVEL_HEIGHT)
        //    mPlatformLayerViewport.y -= (mPlatformLayerViewport.getTop() - LEVEL_HEIGHT);
    }

    /**
     * Draw the platform demo screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        graphics2D.clear(Color.WHITE);

        // Draw the player
        mPlayer.draw(elapsedTime, graphics2D, mPlatformLayerViewport, mDefaultScreenViewport);

        // Draw each of the platforms
        for (Platform platform : mPlatforms)
            platform.draw(elapsedTime, graphics2D, mPlatformLayerViewport, mDefaultScreenViewport);

        // Draw the controls last of all
        for (PushButton control : mControls)
            control.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }


    // US23 Pause, Resume, and Stop music when required
    @Override
    public void pause() {
        try {
            am.pauseMusic();
        } catch (Exception ex) {
        }
    }

    @Override
    public void resume() {
        try {
            am.resumeMusic();
        } catch (Exception ex) {
        }
    }

    @Override
    public void dispose() {
        try {
            am.stopMusic();
        } catch (Exception ex) {
        }
    }

}
