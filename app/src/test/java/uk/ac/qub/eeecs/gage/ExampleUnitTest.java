package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.CollisionDetector;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.game.MenuScreen;
import uk.ac.qub.eeecs.game.spaceDemo.Turret;
import uk.ac.qub.eeecs.game.spaceDemo.SpaceshipDemoScreen;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Example local unit tests, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    // /////////////////////////////////////////////////////////////////////////
    // Simple Unit Test
    // /////////////////////////////////////////////////////////////////////////

    @Test
    public void addition_isCorrect() throws Exception {
        // As basic as it gets!
        assertEquals(4, 2 + 2);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Example of mocking
    // /////////////////////////////////////////////////////////////////////////

    @Mock
    GameObject object1, object2;

    @Test
    public void determineCollisionType_CorrectDetection_TestIsSuccess()
    {
        BoundingBox bound1 = new BoundingBox(100, 100, 50, 50);
        Vector2 position1 = new Vector2(100,100);
        when(object1.getBound()).thenReturn(bound1);
        object1.position = position1;

        BoundingBox bound2 = new BoundingBox(180, 180, 50, 50);
        Vector2 position2 = new Vector2(180,180);
        when(object2.getBound()).thenReturn(bound2);
        object2.position = position2;

        CollisionDetector.CollisionType collisionType =
                CollisionDetector.determineAndResolveCollision(object1, object2);

        assert(collisionType == CollisionDetector.CollisionType.Bottom);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Example of mocking setup
    // /////////////////////////////////////////////////////////////////////////

    @Mock
    private Game game;
    @Mock
    private Input input;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private SpaceshipDemoScreen spaceshipDemoScreen;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(spaceshipDemoScreen.getGame()).thenReturn(game);
        when(spaceshipDemoScreen.getName()).thenReturn("SpaceshipDemoScreen");
    }

    // /////////////////////////////////////////////////////////////////////////
    // Example of mocking SpaceshipDemoScreen construction
    // /////////////////////////////////////////////////////////////////////////

    @Test
    public void aiSpaceShip_TurretConstruction_ExpectedProperties() {
        // Define expected properties
        float expectedXPosition = 100.0f;
        float expectedYPosition = 50.0f;
        float expectedMaxAcceleration = 0.0f;
        float expectedMaxVelocity = 0.0f;
        float expectedMaxAngularVelocity = 50.0f;
        float expectedMaxAngularAcceleration = 50.0f;

        // Create a new aiSpaceship turret instance
        Turret turret = new Turret(expectedXPosition, expectedYPosition, spaceshipDemoScreen);

        // Test that the constructed values are as expected
        assertTrue(turret.position.x == expectedXPosition);
        assertTrue(turret.position.y == expectedYPosition);
        assertTrue(turret.maxAcceleration == expectedMaxAcceleration);
        assertTrue(turret.maxVelocity == expectedMaxVelocity);
        assertTrue(turret.maxAngularVelocity == expectedMaxAngularVelocity);
        assertTrue(turret.maxAngularAcceleration == expectedMaxAngularAcceleration);
        assertEquals(turret.getBitmap(), bitmap);
    }
}





