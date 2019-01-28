package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerClassTest {

    // /////////////////////////////////////////////////////////////////////////
    // Mocking setup
    // /////////////////////////////////////////////////////////////////////////
    @Mock
    private Game game;
    @Mock
    private GameScreen gameScreen;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private Input input;


    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(gameScreen.getGame()).thenReturn(game);
        when(gameScreen.getName()).thenReturn("colosseumDemoScreen");
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing receiveDamage();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Damages the player by 6 from the default health value of 30 with 0 armor
     * Expecting 24 health and 0 Armor to remain.
     */
    @Test
    public void player_damageCurrentHealthNoArmor() {
        // Define expected properties
        int expectedHealth = 24;
        int expectedArmor = 0;
        float startX = 10.0f;
        float startY = 10.0f;
        char hero = 'c';

        // Create a new player instance
        Player player = new Player(startX,startY,gameScreen,bitmap,hero);

        // Damage the player character, from default health of 30
        player.receiveDamage(6);

        // Test that after taking damage health is decreased to the correct amount
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    /*
     * Damages the player by 6 from the default health value of 30 and added 3 armor
     * Expecting 27 health and 0 Armor to remain.
     */
    @Test
    public void player_damageCurrentHealthWithArmor() {
        // Define expected properties
        int expectedHealth = 27;
        int expectedArmor = 0;
        float startX = 10.0f;
        float startY = 10.0f;
        char hero = 'c';

        // Create a new player instance
        Player player = new Player(startX,startY,gameScreen,bitmap,hero);

        // Increases player armor by 3 from it's default value of 0
        player.increaseArmor(3);
        // Damage the player character, from default health of 30
        player.receiveDamage(6);

        // Test that after taking damage health is decreased to the correct amount and armor is zero
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }


    /*
     * Damages the player by 3 from the default health value of 30 with added 3 armor
     * Expecting 30 health and 0 Armor to remain.
     */
    @Test
    public void player_damageReducesArmorToZero() {
        // Define expected properties
        int expectedHealth = 30;
        int expectedArmor = 0;
        float startX = 10.0f;
        float startY = 10.0f;
        char hero = 'c';

        // Create a new player instance
        Player player = new Player(startX,startY,gameScreen,bitmap,hero);

        // Increases player armor by 3 from it's default value of 0
        player.increaseArmor(3);
        // Damage the player character, from default health of 30
        player.receiveDamage(3);

        // Test that after taking damage health is not decreased, and armor is reduced to zero
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    /*
     * Damages the player by 1 from the default health value of 30 with 3 armor
     * Expecting 30 health and 2 Armor to remain.
     */
    @Test
    public void player_damageReducesArmorToAboveZero() {
        // Define expected properties
        int expectedHealth = 30;
        int expectedArmor = 2;
        float startX = 10.0f;
        float startY = 10.0f;
        char hero = 'c';

        // Create a new player instance
        Player player = new Player(startX,startY,gameScreen,bitmap,hero);

        // Increases player armor by 3 from it's default value of 0
        player.increaseArmor(3);
        // Damage the player character, from default health of 30
        player.receiveDamage(1);

        // Test that after taking damage health is not decreased, and armor is reduced to zero
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    /*
     * Placeholder test for opponent method **TEMP: next sprint add new test class for opponent **
     */
    @Test
    public void OppArmorTest() {
        // Define expected properties
        int expectedHealth = 30;
        int expectedArmor = 1;
        float startX = 10.0f;
        float startY = 10.0f;
        char hero = 'c';


        AIOpponent opponent = new AIOpponent(startX,startY,gameScreen,bitmap,hero);

        // Increases player armor by 3 from it's default value of 0
        opponent.increaseArmor(5);
        // Damage the player character, from default health of 30
        opponent.receiveDamage(4);

        // Test that after taking damage health is not decreased, and armor is reduced to zero
        assertEquals(expectedHealth,opponent.getCurrentHealth());
        assertEquals(expectedArmor,opponent.getCurrentArmor());
    }

}
