package uk.ac.qub.eeecs.game.platformDemo;

import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.animation.Animation;
import uk.ac.qub.eeecs.gage.engine.animation.AnimationManager;
import uk.ac.qub.eeecs.gage.engine.audio.AudioManager;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.util.CollisionDetector.CollisionType;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Simple controllable player sprite.
 * <p>
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Player extends Sprite {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Strength of gravity to apply along the y-axis
     */
    private final float GRAVITY = -800.0f;

    /**
     * Acceleration with which the player can move along the x-axis
     */
    private final float RUN_ACCELERATION = 150.0f;
    //User Story 20 variable for boosted running movement
    private final float RUN_ACCELERATION_BOOST = 300.0f;

    /**
     * Maximum velocity of the player along the x-axis
     */
    private final float MAX_X_VELOCITY = 200.0f;
    //User Story 20 variable for boosted running movement
    private final float MAX_X_VELOCITY_BOOST = 400.0f;

    /**
     * Scale factor that is applied to the x-velocity when the player is not
     * moving left or right
     */
    private final float RUN_DECAY = 0.85f;

    /**
     * Instantaneous y velocity with which the player jumps up alongside
     * the scale factor applied to the x velocity when the player jumps
     */
    private final float JUMP_Y_VELOCITY = 450.0f;
    //User Story 20 variable for boosted jumping movement
    private final float JUMP_Y_VELOCITY_BOOST = 600.0f;
    private final float JUMP_X_MULTIPLIER = 10.0f;

    /**
     * Trigger downwards velocity under which a jump will be permitted.
     * Used to reflect the fact that gravity provides a small download
     * acceleration each frame.
     */
    private final float JUMP_VELOCITY_THRESHOLD = 25.0f;

    /**
     * Define the velocity below which the player can be considered to be stopped
     */
    private final float MAX_STANDING_VELOCITY = 20.0f;

    /**
     * Width and height of the player, created to provide an appropriate overall
     * size and an appropriate width/height ratio.
     */
    private static final float PLAYER_WIDTH = 50.0f;
    private static final float PLAYER_HEIGHT = 75.0f;

    /**
     * Animation manager used by the player to control the animated playback
     */
    private AnimationManager mAnimationManager;


    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create the player
     *
     * @param startX     x location of the player
     * @param startY     y location of the player
     * @param gameScreen Gamescreen to which the player belongs
     */
    public Player(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, PLAYER_WIDTH, PLAYER_HEIGHT, null, gameScreen);

        // Create an animation manager and add animations used by the player
        mAnimationManager = new AnimationManager(this);
        mAnimationManager.addAnimation("txt/animation/AdventurerIdle.JSON");
        mAnimationManager.addAnimation("txt/animation/AdventurerRunning.JSON");
        mAnimationManager.addAnimation("txt/animation/AdventurerJumping.JSON");
        mAnimationManager.setCurrentAnimation("AdventurerIdle");

        mGameScreen.getGame().getAssetManager().loadAndAddSound("CollisionSound", "sound/CollisionSound.mp3");
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Update the player
     *
     * @param elapsedTime Elapsed time information
     * @param moveLeft    True if the player should move left
     * @param moveRight   True if the player should move right
     * @param jumpUp      True if the player should consider jumping
     * @param platforms   Array of platforms in the world
     */
    public void update(ElapsedTime elapsedTime, boolean moveLeft,
                       boolean moveRight, boolean jumpUp, List<Platform> platforms, boolean powerUpStatus) {

        // Apply gravity to the y-axis acceleration
        acceleration.y = GRAVITY;

        //User Story 21 - Scott - powerUpStatus should be defaulted false until pressed
        //When the powerUp button is held down, the powerUpStatus is true until the player lets go
        //Calling method for User Story 20 - Scott
        powerUpMovement(powerUpStatus, moveLeft, moveRight);
        //Calling method for User Story 20 -  Scott
        powerUpJump(powerUpStatus, jumpUp, elapsedTime);

        // Call the sprite's update method to apply the defined accelerations
        // and velocities to provide a new position.
        super.update(elapsedTime);

        //Calling method for User Story 20
        powerUpMaxVelocity(powerUpStatus);

        // Check that our new position has not collided with any of
        // the defined platforms. If so, then remove any overlap and
        // ensure a valid velocity.
        checkForAndResolveCollisions(platforms);

        // Ensure we select a suitable animation based on the movement.
        // If currently jumping then let the jump animation complete.
        Animation currentAnimation = mAnimationManager.getCurrentAnimation();
        if (!(currentAnimation.getName().equals("AdventurerJumping") &&
                currentAnimation.isPlaying())) {
            // If not jumping, then pick the animation based on the movement speed
            if (velocity.x < -MAX_STANDING_VELOCITY) {
                // Play a moving left animation
                mAnimationManager.play("AdventurerRunning", elapsedTime);
                mAnimationManager.setFacing(Animation.Facing.Left);
            } else if (velocity.x > MAX_STANDING_VELOCITY) {
                // Play a moving right animation
                mAnimationManager.play("AdventurerRunning", elapsedTime);
                mAnimationManager.setFacing(Animation.Facing.Right);
            } else {
                // Play an idle animation
                mAnimationManager.play("AdventurerIdle", elapsedTime);
            }
        }

        // Update the current animation
        mAnimationManager.update(elapsedTime);
    }


    //User Story 20 method 'powerUpMovement'
    private void powerUpMovement(boolean powerUpStatus, boolean moveLeft, boolean moveRight){
        // Depending upon the left and right movement touch controls set an
        // appropriate x-acceleration. If the user does not want to move left or
        // right, then the x-acceleration is zero and the velocity decays towards zero.

        //If the power up is active, then run accelaration is boosted, else it is normal values.
        if(powerUpStatus) {
            if (moveLeft && !moveRight) {
                acceleration.x = -RUN_ACCELERATION_BOOST;
            } else if (moveRight && !moveLeft) {
                acceleration.x = RUN_ACCELERATION_BOOST;
            } else {
                acceleration.x = 0.0f;
                velocity.x *= RUN_DECAY;
            }
        } else if(!powerUpStatus) {
            if (moveLeft && !moveRight) {
                acceleration.x = -RUN_ACCELERATION;
            } else if (moveRight && !moveLeft) {
                acceleration.x = RUN_ACCELERATION;
            } else {
                acceleration.x = 0.0f;
                velocity.x *= RUN_DECAY;
            }
        }
    }

    //User Story 20 method 'powerUpJump'
    private void powerUpJump(boolean powerUpStatus, boolean jumpUp, ElapsedTime elapsedTime){
        // Check if the user wants to and can jump
        if (jumpUp && (velocity.y > -JUMP_VELOCITY_THRESHOLD && velocity.y < JUMP_VELOCITY_THRESHOLD)) {
            // Provide a suitable velocity boost
            //If the power up is active, then jump velocity is boosted, else it is normal values.
            if(powerUpStatus){
                velocity.y = JUMP_Y_VELOCITY_BOOST;
            } else if(!powerUpStatus){
                velocity.y = JUMP_Y_VELOCITY;
            }
            velocity.x *= JUMP_X_MULTIPLIER;

            // Play the jump animation
            mAnimationManager.play("AdventurerJumping", elapsedTime);
        }
    }

    //User Story 20 method 'powerUpMaxVelocity'
    private void powerUpMaxVelocity(boolean powerUpStatus){
        // The player is constrained by a max x-velocity, test this is not exceeded.
        //If the power up is active, the max velocity is boosted, else it is normal values.
        if(powerUpStatus)
        {
            if (Math.abs(velocity.x) > MAX_X_VELOCITY_BOOST) {
                velocity.x = Math.signum(velocity.x) * MAX_X_VELOCITY_BOOST;
            }
        } else if(!powerUpStatus){
            if (Math.abs(velocity.x) > MAX_X_VELOCITY) {
                velocity.x = Math.signum(velocity.x) * MAX_X_VELOCITY;
            }
        }
    }


    /**
     * Check for and then resolve any collision between the player and the
     * platforms.
     *
     * @param platforms Array of platforms to test for collision against
     */
    private void checkForAndResolveCollisions(List<Platform> platforms) {

        CollisionType collisionType;

        // Consider each platform for collision
        for (Platform platform : platforms) {
            collisionType =
                    CollisionDetector.determineAndResolveCollision(this, platform);

            // Current the player doesn't 'bounce' following any collision - they just stop
            switch (collisionType) {
                case Top:
                    if (velocity.y > 20)
                        mGameScreen.getGame().getAssetManager().getSound("CollisionSound").play();

                    velocity.y = -0.0f * velocity.y;
                    break;
                case Bottom:
                    velocity.y = -0.0f * velocity.y;
                    break;
                case Left:
                    if (Math.abs(velocity.x) > 20)
                        mGameScreen.getGame().getAssetManager().getSound("CollisionSound").play();

                    velocity.x = -0.0f * velocity.x;
                    break;
                case Right:
                    if (Math.abs(velocity.x) > 20)
                        mGameScreen.getGame().getAssetManager().getSound("CollisionSound").play();

                    velocity.x = -0.0f * velocity.x;
                    break;
                case None:
                    break;
            }
        }
    }

    /**
     * Draw the player to the screen
     *
     * @param elapsedTime    Elapsed time information
     * @param graphics2D     Graphics2D instance to draw using
     * @param layerViewport  Layer viewport
     * @param screenViewport Screen viewport
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {
        // Get the animation manager to draw the current animation
        mAnimationManager.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
    }
}
