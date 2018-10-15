package uk.ac.qub.eeecs.game.spaceDemo;

import java.util.Random;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Simple asteroid
 *
 * Note: See the course documentation for extension/refactoring stories
 * for this class.
 *
 * @version 1.0
 */
public class Asteroid extends SpaceEntity {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Default size for the asteroid
     */
    private static final float DEFAULT_RADIUS = 20;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create an asteroid
     *
     * @param startX     x location of the asteroid
     * @param startY     y location of the asteroid
     * @param gameScreen Gamescreen to which asteroid belongs
     */
    public Asteroid(float startX, float startY, GameScreen gameScreen) {
        super(startX, startY, DEFAULT_RADIUS*2.0f, DEFAULT_RADIUS*2.0f, null, gameScreen);

        //User Story 2 Asteroid Size Variety [D1]
        Random random = new Random();
        float aMin = 25; //Sets a minimum size of the asteroid
        float aMax = 50; //Sets a maximum size of the asteroid
        setWidth(random.nextFloat()*(aMax-aMin)+aMin); //Sets a width that will be randomly between the max and min sizes
        setHeight(random.nextFloat()*(aMax-aMin)+aMin); //Sets a height that will be randomly between the max and min sizes
        //Allows for different width and heights of an asteroid that is randomly assigned, not a perfect "square" each time.

        mBitmap = gameScreen.getGame().getAssetManager()
                .getBitmap("Asteroid" + Integer.toString(random.nextInt(4) + 1));

        mRadius = DEFAULT_RADIUS;
        mMass = 1000.0f;

        angularVelocity = random.nextFloat() * 240.0f - 20.0f;

    }
}
