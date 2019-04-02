package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

public class PauseMenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // ///////////////////////////////t//////////////////////////////////////////

    private List<PushButton> mButtons = new ArrayList<>();

    /**
     * Provide a list of game screen names that each button will trigger
     */
    private Map<PushButton, String> mScreenChanges = new HashMap<>();

    private LayerViewport mMenuViewport;

    private PushButton mMenuScreen, mConcede, mResume;

    //Information needed to set Music/SFX/FPS Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);

    FPSCounter fpsCounter;

    public PauseMenuScreen(Game game) {
        super("PauseScreen", game);
        //Setting up  viewports method
        setupViewports();
        //Setting up pause menu objects
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


    private void SetUpPauseMenuOpbjects() {
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        //getting relevant images for this screen
        mGame.getAssetManager().loadAssets("txt/assets/PauseMenuAssets.JSON");

        // Construct the menu buttons
        constructButtons("txt/layout/PauseMenuScreenButtonLayout.JSON", mButtons);

        //Creating the screen
        mMenuScreen = new PushButton(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), "Pause", this);

        /*Creating the 'concede' button
         * Due to how the concede button works, if was not possible to add it to the
         * PauseMenuScreenButtonLayout.JSON file as it was not able to call @Scott Barham's
         * Code which would add a loss and update the statistics screen when it has been clicked
         */
        mConcede = new PushButton(spacingX * 2.5f, spacingY * 0.45f,
                spacingX * 0.8f, spacingY * 0.39f, "Concede",
                "ConcedeSelected", this);

        /* Creating the 'resume' button
         * Due to the changes made by in commit 2a45e034032d75b055064c8a62b366a0747ad96e
         * The resume button would no longer work with its current design as there are parameters in
         * the colosseumDemoScreen class declaration that is not in other classes. This alternative
         * has been made as a solution.
         */
        mResume = new PushButton(spacingX * 2.5f, spacingY * 2.0f,
                spacingX * 0.80f, spacingY * 0.39f, "Resume",
                "ResumeSelected", this);

        mButtons.add(mConcede);
        mButtons.add(mResume);
        fpsCounter = new FPSCounter(mMenuViewport.getWidth() * 0.50f, mMenuViewport.getHeight() * 0.20f, this) {
        };
    }

    private void constructButtons(String buttonsToConstructJSONFile, List<PushButton> buttons) {
        // Attempt to load in the JSON asset details
        String loadedJSON;
        try {
            loadedJSON = mGame.getFileIO().loadJSON(buttonsToConstructJSONFile);
        } catch (IOException e) {
            throw new RuntimeException(
                    "PauseMenuScreen.constructButtons: Cannot load JSON [" + buttonsToConstructJSONFile + "]");
        }

        // Attempt to extract the JSON information
        try {
            JSONObject settings = new JSONObject(loadedJSON);
            JSONArray buttonDetails = settings.getJSONArray("pushButtons");

            // Store the game layer width and height
            float layerWidth = mDefaultLayerViewport.getWidth();
            float layerHeight = mDefaultLayerViewport.getHeight();

            // Construct each button
            for (int idx = 0; idx < buttonDetails.length(); idx++) {
                float x = (float) buttonDetails.getJSONObject(idx).getDouble("x");
                float y = (float) buttonDetails.getJSONObject(idx).getDouble("y");
                float width = (float) buttonDetails.getJSONObject(idx).getDouble("width");
                float height = (float) buttonDetails.getJSONObject(idx).getDouble("height");

                String defaultButton = buttonDetails.getJSONObject(idx).getString("button");
                String buttonSelected = buttonDetails.getJSONObject(idx).getString("buttonSelected");
                String triggeredGameScreen = buttonDetails.getJSONObject(idx).getString("triggeredGameScreen");

                PushButton button = new PushButton(x * layerWidth, y * layerHeight,
                        width * layerWidth, height * layerHeight,
                        defaultButton, buttonSelected, this);
                buttons.add(button);
                mScreenChanges.put(button, triggeredGameScreen);
            }

        } catch (JSONException | IllegalArgumentException e) {
            throw new RuntimeException(
                    "PauseMenuScreen.constructButtons: JSON parsing error [" + e.getMessage() + "]");
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() > 0) {
            for (PushButton button : mButtons) {
                button.update(elapsedTime);
                /*This is the if statement which allows the statistics screen to be updated
                 * using Scott Barham's code
                 */
                if (mConcede.isPushTriggered()) {
                    EndGameScreen.setMostRecentResult("loss");
                    EndGameScreen.setConcedeResult(true);
                    colosseumDemoScreen.setWasPaused(false); //Set this back to false for a new game
                    mGame.getScreenManager().changeScreenButton(new EndGameScreen(mGame));
                } else if (mResume.isPushTriggered()) {
                    mGame.getScreenManager().changeScreenButton(new CoinTossScreen(mGame,"Meridia","Hircine"));
                } else if (button.isPushTriggered()) {
                    addScreen(mScreenChanges.get(button));
                }
            }
        }
    }

    private void addScreen(String gameScreenToAdd) {
        try {
            GameScreen gameScreen =
                    (GameScreen) Class.forName("uk.ac.qub.eeecs.game." + gameScreenToAdd)
                            .getConstructor(Game.class).newInstance(mGame);
            mGame.getScreenManager().changeScreenButton(gameScreen);

        } catch (ClassNotFoundException | NoSuchMethodException
                | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(
                    "PauseMenuScreen.addScreen: Error creating [" + gameScreenToAdd + " " + e.getMessage() + "]");
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mMenuScreen.draw(elapsedTime, graphics2D, mMenuViewport,
                mDefaultScreenViewport);

        for (PushButton button : mButtons) //Draw all the buttons stored in "mButtons"
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        if (mGetPreference.getBoolean("FPS", true)) {
            fpsCounter.draw(elapsedTime, graphics2D);
        }
    }
}