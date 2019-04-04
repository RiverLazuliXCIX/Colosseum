
package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.Effect;
import uk.ac.qub.eeecs.game.Colosseum.MinionCard;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.HeroSelectScreen;
import uk.ac.qub.eeecs.game.TestClasses.HeroSelectScreenForTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Kyle Corrigan
 */

@RunWith(MockitoJUnitRunner.class)
public class HeroSelectScreenTest {

    // /////////////////////////////////////////////////////////////////////////
    // Mocking setup
    // /////////////////////////////////////////////////////////////////////////
    @Mock
    private Game game;
    @Mock
    private HeroSelectScreenForTesting heroSelectScreen;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private Input input;
    @Mock
    private LayerViewport layerViewport;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(heroSelectScreen.getGame()).thenReturn(game);
        when(heroSelectScreen.getName()).thenReturn("HeroSelectScreen");
        when(heroSelectScreen.getDefaultLayerViewport()).thenReturn(layerViewport);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing heroSelectScreen constructor
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Test to ensure that the hero select screen constructor creates a hero select screen
     */
    @Test
    public void heroSelectScreen_ConstructorTest() {

        heroSelectScreen = new HeroSelectScreenForTesting(game);
        assertNotNull(heroSelectScreen);

    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing heroSelectScreen getters and setters
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Test to ensure that the hero select screen constructor creates a hero select screen
     */
    @Test
    public void heroSelectScreen_GettersSettersTest() {

        // Initialising hero select screen objects
        heroSelectScreen = new HeroSelectScreenForTesting(game);
        GameObject gameBackground = new GameObject(heroSelectScreen);
        ArrayList<PushButton> heroButtons = new ArrayList<>();
        PushButton testPushButton = new PushButton(0.0f,0.0f,0.0f,0.0f,"TestBitmap",heroSelectScreen);
        ArrayList<PushButton> uiButtons = new ArrayList<>();
        Map<PushButton,String> heroButtonTriggers = new HashMap<>();
        Map<PushButton, String> uiButtonTriggers = new HashMap<>();

        // Setting values using the setters from the player class
        heroSelectScreen.setGameBackground(gameBackground);
        heroSelectScreen.setHeroSelectViewport(layerViewport);
        heroSelectScreen.setHeroButtons(heroButtons);
        heroSelectScreen.setSelectedPlayerHero("Test");
        heroSelectScreen.setSelectedOpponentHero("Test");
        heroSelectScreen.setPlayerHeroChosen(true);
        heroSelectScreen.setOpponentHeroChosen(true);
        heroSelectScreen.setTitleText("TestTitle");
        heroSelectScreen.setDescriptionText("TestDescription");
        heroSelectScreen.setUiButtons(uiButtons);
        heroSelectScreen.setHeroButtonTriggers(heroButtonTriggers);
        heroSelectScreen.setUiButtonTriggers(uiButtonTriggers);;

        // Specifying the expected values for each of the properties
        GameObject expectedGameBackground = gameBackground;
        LayerViewport expectedHeroViewport = layerViewport;
        ArrayList<PushButton> expectedHeroButtons = heroButtons;
        String expectedSelectedPlayerHero = "Test";
        String expectedSelectedOpponentHero = "Test";
        boolean expectedPlayerHeroChosen = true;
        boolean expectedOpponentHeroChosen = true;
        String expectedTitleText = "TestTitle";
        String expectedDescriptionText = "TestDescription";
        ArrayList<PushButton> expectedUiButtons = uiButtons;
        Map<PushButton,String> expectedHeroButtonTriggers = heroButtonTriggers;
        Map<PushButton,String> expectedUiButtonTriggers = uiButtonTriggers;

        // retrieving actual values using the getters
        assertEquals(expectedGameBackground,heroSelectScreen.getGameBackground());
        assertEquals(expectedHeroViewport,heroSelectScreen.getHeroSelectViewport());
        assertEquals(expectedHeroButtons,heroSelectScreen.getHeroButtons());
        assertEquals(expectedSelectedPlayerHero,heroSelectScreen.getSelectedPlayerHero());
        assertEquals(expectedSelectedOpponentHero,heroSelectScreen.getSelectedOpponentHero());
        assertEquals(expectedPlayerHeroChosen,heroSelectScreen.getPlayerHeroChosen());
        assertEquals(expectedOpponentHeroChosen,heroSelectScreen.getOpponentHeroChosen());
        assertEquals(expectedTitleText,heroSelectScreen.getTitleText());
        assertEquals(expectedDescriptionText,heroSelectScreen.getDescriptionText());
        assertEquals(expectedUiButtons,heroSelectScreen.getUiButtons());
        assertEquals(expectedHeroButtonTriggers,heroSelectScreen.getHeroButtonTriggers());
        assertEquals(expectedUiButtonTriggers,heroSelectScreen.getUiButtonTriggers());

    }

}