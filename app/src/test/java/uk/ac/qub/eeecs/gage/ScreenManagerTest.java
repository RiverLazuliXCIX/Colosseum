package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScreenManagerTest {

    String menuScreenName = "Menu Screen";
    String gameScreenName = "Game Screen";

    @Mock
    Game game;
    @Mock
    GameScreen menuScreen = Mockito.mock(GameScreen.class);
    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);

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
}