package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Stack;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScreenManagerTest {

    String menuScreenName = "Menu Screen";
    String gameScreenName = "Game Screen";
    Stack<GameScreen> mGameScreens;
    GameScreen searchedScreen; //Used to temporarily store the "searched for" screen, to be moved to the top of the stack.

    @Mock
    Game game;
    @Mock
    GameScreen menuScreen = Mockito.mock(GameScreen.class);
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);


//Example code from Lectures/Emails
    @Before
    public void setUp() {
        when(menuScreen.getName()).thenReturn(menuScreenName);
        when(gameScreen.getName()).thenReturn(gameScreenName);
    }

    @Test
    public void addScreen_ValidData_TestSuccess() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen);
        assertEquals(menuScreen , manager.getCurrentScreen());
    }

    @Test
    public void getCurrentScreen_DefaultScreen_TestSuccess() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen );
        manager.addScreen(gameScreen);
        assertEquals(gameScreen, manager.getCurrentScreen());
    }

    @Test
    public void getScreen_ValidData_TestSuccess() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen );
        manager.addScreen(gameScreen);
        assertEquals(menuScreen , manager.getScreen(menuScreenName));
    }

    @Test
    public void removeScreen_ValidData_TestSuccess() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen );
        manager.addScreen(gameScreen);
        assertTrue(manager.removeScreen(menuScreenName));
    }

    @Test
    public void removeScreen_NotFound_TestSuccess() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen );
        manager.addScreen(gameScreen);
        manager.removeScreen(menuScreenName);
        assertFalse(manager.removeScreen(menuScreenName));
    }


    //Scott's unit tests below

    @Test
    public void findScreen_ValidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen);
        manager.addScreen(gameScreen);
        assertEquals(menuScreen, manager.findScreen(menuScreen));
    }

    @Test
    public void findScreen_InvalidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(gameScreen);
        assertNotEquals(menuScreen, manager.findScreen(menuScreen));
    }

    @Test
    public void findScreenIterator_ValidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen);
        manager.addScreen(gameScreen);
        int screenLocation = manager.findScreenIterator(menuScreenName);
        assertEquals(screenLocation, 0);
    }

    @Test
    public void findScreenIterator_InvalidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(gameScreen);
        int screenLocation = manager.findScreenIterator(menuScreenName);
        assertEquals(screenLocation, -1); //If screen cant be found it returns -1
    }

    @Test
    public void gotoScreen_ValidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen);
        manager.addScreen(gameScreen);
        manager.gotoScreen(manager.findScreenIterator(menuScreenName)); //Resume the screen that was already open
        assertEquals(menuScreen , manager.getCurrentScreen());
    }

    @Test
    public void gotoScreen_InvalidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(gameScreen);
        manager.gotoScreen(manager.findScreenIterator(menuScreenName)); //Resume the screen that was already open
        assertNotEquals(menuScreen , manager.getCurrentScreen());
    }

    @Test
    public void previousScreen_ValidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen);
        manager.addScreen(gameScreen);
        manager.previousScreen();
        assertEquals(menuScreen , manager.getCurrentScreen());
    }

    @Test
    public void previousScreen_InvalidData() throws Exception {
        ScreenManager manager = new ScreenManager(game);
        manager.addScreen(menuScreen);
        manager.addScreen(gameScreen);
        manager.previousScreen();
        assertNotEquals(gameScreen , manager.getCurrentScreen());
    }

}