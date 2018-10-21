package uk.ac.qub.eeecs.game.spaceDemo;


import java.util.Random;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.particle.Emitter;
import uk.ac.qub.eeecs.gage.engine.particle.ParticleSystemManager;
import uk.ac.qub.eeecs.gage.util.MathsHelper;
import uk.ac.qub.eeecs.gage.util.SteeringBehaviours;
import uk.ac.qub.eeecs.gage.util.Vector2;

/* User Story 10: Creating an avoider spaceship [D3]
 * Based on the Seeker class, create a new type of spaceship
 * that will normally remain stationary (unmoving) unless the
 * player spaceship gets close, in which case it will attempt to flee from the player
 * [Unit test the flee behaviour].
 */
public class Avoider extends SpaceEntity {

    // ///////////////////////////////////////////////////
    // Properties
    // ///////////////////////////////////////////////////
    /**
     * Default size for the avoider ship
     */
    private static final float DEFAULT_RADIUS = 20;

    /**
     * Distance at which the Avoider spaceship should avoid other game objects
     */
    private float mSeparateThreshold = 100.0f;

    /**
     * Accumulators used to build up the net steering outcome
     */
    private Vector2 mAccAccumulator = new Vector2();
    private Vector2 mAccComponent = new Vector2();

    /**
     * Particle emitter providing a thrust trail for this spaceship
     */
    private Emitter movementEmitter;

    /**
     * Offset/location variables for the movement emitter so it appears to
     * exit from the back of the spaceship.
     */
    private Vector2 movementEmitterOffset;
    private Vector2 movementEmitterLocation;

    /*
     * User story 10: Stores whether the avoider is active, if the ship should flee the player, or if it is far enough away to deactivate/stop fleeing
     * This should make it a bit easier in the future to add additional features/behaviours based on whether the ship is fleeing or not (eg. different
     * sounds/particle effects)
     */
    private boolean activated = false;
    private float playerXPos;
    private float playerYPos;


    // ////////////////////////////////////////////////////
    // Constructors
    // ////////////////////////////////////////////////////

    /**
     * Create an AI controlled Avoider spaceship
     *
     * @param startX     x location of the AI spaceship
     * @param startY     y location of the AI spaceship
     * @param gameScreen Gamescreen to which AI belongs
     */
    public Avoider(float startX, float startY, SpaceshipDemoScreen gameScreen) {
        super(startX, startY, DEFAULT_RADIUS*2.0f, DEFAULT_RADIUS*2.0f, null, gameScreen);

        // Define movement variables for the spaceship
        maxAcceleration = 30.0f;
        maxVelocity = 50.0f;
        maxAngularVelocity = 150.0f;
        maxAngularAcceleration = 300.0f;

        mRadius = DEFAULT_RADIUS;
        mMass = 10.0f;

        // Define the appearance of the Avoider Spaceship
        // The avoider spaceship image has been set to Spaceship3 for the purpose of behaviour testing and can be changed to something different later with further implementations of user stories.
        mBitmap = gameScreen.getGame().getAssetManager().getBitmap("Spaceship3");

        //Sets a random width and height of the AvoiderSpaceship between a minimum of 25 and maximum of 50
        Random rand = new Random();
        int size = rand.nextInt(25)+25; //Generates random number between 0 and 25, then adds 25 to it. (min=25, max=50)
        setWidth(size);
        setHeight(size);

        // Create an offset for the movement emitter based on the size of the spaceship
        movementEmitterOffset = new Vector2(-DEFAULT_RADIUS, 0.0f);
        movementEmitterLocation = new Vector2(position);
        movementEmitterLocation.add(movementEmitterOffset);

        // Create and add a particle effect for the movement of the ship
        ParticleSystemManager particleSystemManager =
                ((SpaceshipDemoScreen) mGameScreen).getParticleSystemManager();
        movementEmitter = new Emitter(
                particleSystemManager, "txt/particle/ThrusterEmitter.JSON",
                movementEmitterLocation);
        particleSystemManager.addEmitter(movementEmitter);
    }

    // //////////////////////////////////////////////////
    // Methods
    // //////////////////////////////////////////////////

    /**
     * Update the AI Spaceship
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime) {

        /*
         * On update retrieves the x and y position of the player spaceship and assigns them
         * to associated variables, this helps to improve the readability of the code particularly
         * within the if statement conditions that follow.
         */
        playerXPos = ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship().position.x;
        playerYPos = ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship().position.y;

        /*
         * The activated boolean variable stores the current state of the avoider, with the ship
         * becoming active when the player gets within the separate threshold, otherwise,
         * being set to false.
         */

        if( (playerXPos <= this.position.x - this.mSeparateThreshold || playerXPos >= this.position.x + this.mSeparateThreshold)
                || (playerYPos <=  this.position.y - this.mSeparateThreshold || playerYPos >= this.position.y + this.mSeparateThreshold))
        {
            activated = false;
        } else{
            activated = true;
        }

        /*
         * If the Avoider is active, the ship will flee, and make an effort to avoid the other space objects
         * in addition to the player, until it is set to inactive, upon which, the avoider ship will
         * decelerate until it stops, and will not try to avoid other space entities whilst inactive
         * (this is just to prevent unnecessary and constant movement when the game is full of entities).
         */
        if(activated){
            avoiderFlee(this,
                    ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship().position,
                    acceleration);

            // Try to avoid a collision with the player ship
            SteeringBehaviours.separate(this,
                    ((SpaceshipDemoScreen) mGameScreen).getPlayerSpaceship(),
                    mSeparateThreshold, 1.0f, mAccComponent);
            mAccAccumulator.set(mAccComponent);

            // Try to avoid a collision with the other space entities
            SteeringBehaviours.separate(this,
                    ((SpaceshipDemoScreen) mGameScreen).getSpaceEntities(),
                    mSeparateThreshold, 1.0f, mAccComponent);
            mAccAccumulator.add(mAccComponent);

            // If we are trying to avoid a collision then combine
            // it with the seek behaviour, placing more emphasis on
            // avoiding a collision.
            if (!mAccAccumulator.isZero()) {
                acceleration.x = 0.1f * acceleration.x + 0.9f * mAccAccumulator.x;
                acceleration.y = 0.1f * acceleration.y + 0.9f * mAccAccumulator.y;
            }

        }else{
            // Dampen the linear and angular acceleration and velocity leading to the fleeing ship
            // slowing to a stop, once it is out of range of the separate threshold.
            this.acceleration.set(Vector2.Zero);

            this.angularAcceleration *= 0.95f;
            this.angularVelocity *= 0.75f;
            this.acceleration.multiply(0.75f);
            this.velocity.multiply(0.95f);
        }

        // Make sure we point in the direction of travel.
        angularAcceleration = SteeringBehaviours.alignWithMovement(this);

        // Call the sprite's superclass to apply the determined accelerations
        super.update(elapsedTime);

        // Update the particle emitter associated with this ship to rhe new position,
        // calculating an offset so the particles emerge from the rear of the ship
        MathsHelper.rotateOffsetAboutCentre(
                position, movementEmitterOffset, orientation, movementEmitterLocation);
        movementEmitter.setPosition(movementEmitterLocation.x, movementEmitterLocation.y);
    }

    public static void avoiderFlee(Avoider fleeingShip, Vector2 targetPosition, Vector2 acceleration) {
            /*
             * User Story 10: Determine the fleeing direction
             * Subtracting the target's coordinates from the fleeing ship's corresponding coordinates results in
             * the acceleration being set to the opposite direction of the player, causing a fleeing behaviour.
             */
            acceleration.set(fleeingShip.position.x - targetPosition.x, fleeingShip.position.y - targetPosition.y);
            acceleration.normalise();
            acceleration.multiply(fleeingShip.maxAcceleration);
    }

}
