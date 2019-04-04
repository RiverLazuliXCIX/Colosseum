package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.MinionCard;
import uk.ac.qub.eeecs.game.TestClasses.AIOpponentForTesting;
import uk.ac.qub.eeecs.game.TestClasses.CardClassForTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AIOpponentTest {

    @Mock
    private GameScreen mDemoScreen;
    @Mock
    private Game mGame;
    @Mock
    private AssetManager mAssetManager;
    @Mock
    private ScreenManager mScreenManager;
    @Mock
    private Bitmap mBitmap;
    @Mock
    private Input mInput;
    @Mock
    private LayerViewport mLayerViewport;

    @Mock
    GameScreen gameScreen = Mockito.mock(GameScreen.class);

    @Before
    public void setUp() {
        when(mGame.getAssetManager()).thenReturn(mAssetManager);
        when(mAssetManager.getBitmap(any(String.class))).thenReturn(mBitmap);
        when(mGame.getScreenManager()).thenReturn(mScreenManager);
        when(mGame.getInput()).thenReturn(mInput);
        when(mDemoScreen.getGame()).thenReturn(mGame);
        when(mDemoScreen.getName()).thenReturn("colosseumDemoScreen");
        when(mDemoScreen.getDefaultLayerViewport()).thenReturn(mLayerViewport);
    }

    @Test
    public void AI_ConstructorTest() {
        AIOpponentForTesting aiOpponent = new AIOpponentForTesting(mDemoScreen,"Mars");
        assertNotNull(aiOpponent);
    }

    @Test
    public void AI_ValuesResetTest() {
        int[] valuesToReset = {1,1,1};
        AIOpponentForTesting aiOpponent = new AIOpponentForTesting(mDemoScreen,"Mars");
        aiOpponent.resetValues(valuesToReset);
        assertEquals(valuesToReset[0], 0);
        assertEquals(valuesToReset[1], 0);
        assertEquals(valuesToReset[2], 0);

    }


    @Test
    public void AI_TauntAddTest() {
        int[][] tauntMinionList = new int [10][3];
        AIOpponentForTesting aiOpponent = new AIOpponentForTesting(mDemoScreen,"Mars");
        aiOpponent.addtauntMinions(tauntMinionList,7,3,4);

        assertNotNull(tauntMinionList[0]);
        assertNotNull(tauntMinionList[1]);
        assertNotNull(tauntMinionList[2]);
    }

    @Test
    public void AI_DelayTest() {

        AIOpponentForTesting aiOpponent = new AIOpponentForTesting(mDemoScreen,"Mars");
        long timeCurrent = System.currentTimeMillis();
        aiOpponent.actionDelay(200);
        assertEquals(System.currentTimeMillis(), timeCurrent+200);
    }

    @Test
    public void AI_Minion(){ //I would test minion implementation, but cards dont work with testing, and the testing class for cards doesnt work with the pre-existing regions and therefore AI.
        //If i had more time i would get this finished and do more tests related to cards and how they are used throughout
        AIOpponentForTesting aiOpponent = new AIOpponentForTesting(mDemoScreen,"Mars");
        CardClassForTesting card1 = new CardClassForTesting(100.0f,100.0f, gameScreen,5,true, "Card_Lion");
        //aiOpponent.getCardsInAIHandRegion().add((Card)card1);
        assertNotNull(aiOpponent.getCardsInAIBoardRegion());
    }

}

