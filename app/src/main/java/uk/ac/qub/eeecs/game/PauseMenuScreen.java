package uk.ac.qub.eeecs.game;

import android.app.AlertDialog;
import android.app.Notification;
import android.graphics.Color;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.MainActivity;
import uk.ac.qub.eeecs.gage.R;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class PauseMenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private List<PushButton> mButtons = new ArrayList<>();

    private PushButton mMenuScreen, mResume, mOptions, mMainMenu;
    private LayerViewport mMenuViewport;

    public PauseMenuScreen(Game game)
    {
        super("PauseScreen",game);
        //Setting up  viewports method
        setupViewports();
        //Settign up pause menu objects
        SetUpPauseMenuOpbjects();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public void setupViewports() {
        // Setup the screen viewport to use the full screen:
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        /* Calculate the layer height that will preserved the screen aspect ratio
         given an assume 480 layer width.*/
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mMenuViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }


    private void SetUpPauseMenuOpbjects()
    {
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        //getting relevant images for this screen
        mGame.getAssetManager().loadAssets("txt/assets/PauseMenuAssets.JSON");

        //Creating the screen
        mMenuScreen = new PushButton(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(),"Pause", this);
        //Creating resume button
        mResume = new PushButton(spacingX * 2.4f, spacingY * 1.9f,
                spacingX * 1.0f,spacingY * 0.5f,"Resume",
                "ResumeSelected",this );
        mButtons.add(mResume);
        //Creating the options button
        mOptions = new PushButton(spacingX * 2.4f, spacingY * 1.3f,
                spacingX * 1.0f,spacingY * 0.5f,"Options",
                "OptionsSelected",this );
        mButtons.add(mOptions);
        //Creating the 'main menu' button
        mMainMenu = new PushButton(spacingX * 2.4f, spacingY * 0.7f,
                spacingX * 1.0f,spacingY * 0.5f,"MainMenu",
                "MainMenuSelected",this );
        mButtons.add(mMainMenu);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() > 0) {
            for (PushButton button : mButtons)
                button.update(elapsedTime);

            if(mResume.isPushTriggered())
                mGame.getScreenManager().removeScreen(this);
            else if(mOptions.isPushTriggered())
                mGame.getScreenManager().addScreen((new OptionsScreen(mGame)));
            else if (mMainMenu.isPushTriggered())
            {
                mGame.getScreenManager().removeScreen(this);
                mGame.getScreenManager().removeScreen("colosseumDemoScreen");
                mGame.getScreenManager().addScreen(new MenuScreen(mGame));
            }
        }
    }
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mMenuScreen.draw(elapsedTime, graphics2D, mMenuViewport,
                mDefaultScreenViewport);

        mResume.draw(elapsedTime, graphics2D, mMenuViewport, mDefaultScreenViewport);

        mOptions.draw(elapsedTime, graphics2D, mMenuViewport, mDefaultScreenViewport);

        mMainMenu.draw(elapsedTime, graphics2D, mMenuViewport, mDefaultScreenViewport);
    }
}