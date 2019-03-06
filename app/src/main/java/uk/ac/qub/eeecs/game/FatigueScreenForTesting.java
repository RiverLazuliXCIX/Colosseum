package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

public class FatigueScreenForTesting extends GameScreen {

    //Variables required for the Fatigue Screen:
    final private long FATIGUE_TIMEOUT = 3000;
    private long mTimeOnCreate, mCurrentTime;
    private long mTimeRemaining;
    private int mDamageTaken;

    //Objects required for the appearance of the Fatigue Screen:
    private LayerViewport mFatigueScreenViewport;


    public FatigueScreenForTesting(Game game, int damageToTake) {
        super("FatigueScreen", game);
        mTimeOnCreate = System.currentTimeMillis();
        this.mDamageTaken = damageToTake;

        setupViewports();
        setUpFatigueScreenObjects();
    }

    //Methods
    private void setUpFatigueScreenObjects() {

    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mFatigueScreenViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the last update
        Input input = mGame.getInput();

        mCurrentTime = System.currentTimeMillis();
        mTimeRemaining = 3 - ((mCurrentTime - mTimeOnCreate)/1000);

        //Get current time and check for timeout
        mCurrentTime = System.currentTimeMillis();
        if (mCurrentTime - mTimeOnCreate >= FATIGUE_TIMEOUT) {
            mGame.getScreenManager().getCurrentScreen().dispose();
            mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mGame));
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }

    //Getters and setters:
    public int getmDamageTaken() { return this.mDamageTaken; }

}
