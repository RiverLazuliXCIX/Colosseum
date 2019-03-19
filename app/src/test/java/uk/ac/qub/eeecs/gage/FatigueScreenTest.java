package uk.ac.qub.eeecs.gage;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.game.TestClasses.FatigueScreenForTesting;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FatigueScreenTest {

    @Mock
    private FatigueScreenForTesting mFatigueScreen;
    @Mock
    private Game mGame;

    @Before
    public void setUp() {
        when(mFatigueScreen.getGame()).thenReturn(mGame);
        when(mFatigueScreen.getName()).thenReturn("FatigueScreen");
    }

    //The following tests are to test the FatigueScreen constructor:
    @Test
    public void screen_SetUpFully() {
        //This test is to ensure that the constructor successfully creates an instance of FatigueScreen
        FatigueScreenForTesting f1 = new FatigueScreenForTesting(mGame, 5);

        assertNotNull(f1);
    }

    @Test
    public void screen_NameCorrect() {
        //This test is to ensure that the correct name is assigned to the new FatigueScreen
        FatigueScreenForTesting f1 = new FatigueScreenForTesting(mGame, 5);

        String expectedResult = "FatigueScreen";

        assertEquals(expectedResult, f1.getName());
    }

    @Test
    public void damageTakenSetCorrectly() {
        //This test is to ensure that whatever damage is passed into the class, is assigned to
        //the mDamageTaken variable within the FatigueScreen class
        FatigueScreenForTesting f1 = new FatigueScreenForTesting(mGame, 5);

        int expectedResult = 5;

        assertEquals(expectedResult, f1.getmDamageTaken());
    }
}
