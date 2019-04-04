package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.world.LayerViewport;


/*Class was based and built further upon
* the splash screen from the ragnarok past game example
* */
public class SplashScreen extends GameScreen {

    // Properties
    final private long SPLASH_TIMEOUT = 5000;
    private long timeOnCreate, currentTime;
    private PushButton mSplashBackground;
    private LayerViewport mSplashLayerViewport;

    private List<TitleImage> mImages = new ArrayList<>();

    //Shared preferences for music:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    private SharedPreferences.Editor mPrefEditor = mGetPreferences.edit();

    /**
     * Creating the 'Splash Screen' screen
     *
     * @param game Game to which this screen belongs
     */

    public SplashScreen(Game game) {
        super("SplashScreen", game);

        timeOnCreate = System.currentTimeMillis();
        setupViewports();
        setUpSplashScreeneObjects();

        //Set up Music Preferences:
        mPrefEditor.putBoolean("Music", true);
        mPrefEditor.putBoolean("SFX", true);
        mPrefEditor.putBoolean("FPS", false);
        mPrefEditor.commit();

        constructImages("txt/layout/SplashScreenImageLayout.JSON", mImages);
    }

    //Methods
    private void setUpSplashScreeneObjects() {
        // Load in the assets used by the steering demo
        mGame.getAssetManager().loadAssets("txt/assets/SplashScreen.JSON");

        // Create the background
        mSplashBackground = new PushButton(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), "SplashScreenBackground", this);
    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mSplashLayerViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    public void nextScreenCountadown() {
        //Get current time and check for timeout
        //Based upon the splash screen from Ragnarok past example
        currentTime = System.currentTimeMillis();
        if (currentTime - timeOnCreate >= SPLASH_TIMEOUT) {
            goToMenuScreen();
        }
    }

    //Code is based upon Dr Philip Hanna's code which is adapted from the JSON refactoring lecture
    private void constructImages(String imagesToConstructJSONFile, List<TitleImage> images) {

        // Attempt to load in the JSON asset details
        String loadedJSON;
        try {
            loadedJSON = mGame.getFileIO().loadJSON(imagesToConstructJSONFile);
        } catch (IOException e) {
            throw new RuntimeException(
                    "SplashScreen.constructImages: Cannot load JSON [" + imagesToConstructJSONFile + "]");
        }

        // Attempt to extract the JSON information
        try {
            JSONObject settings = new JSONObject(loadedJSON);
            JSONArray imageDetails = settings.getJSONArray("titleImage");

            // Store the game layer width and height
            float layerWidth = mDefaultLayerViewport.getWidth();
            float layerHeight = mDefaultLayerViewport.getHeight();

            // Construct each button
            for (int idx = 0; idx < imageDetails.length(); idx++) {
                float x = (float) imageDetails.getJSONObject(idx).getDouble("x");
                float y = (float) imageDetails.getJSONObject(idx).getDouble("y");
                float width = (float) imageDetails.getJSONObject(idx).getDouble("width");
                float height = (float) imageDetails.getJSONObject(idx).getDouble("height");

                String defaultImage = imageDetails.getJSONObject(idx).getString("image");

                TitleImage image = new TitleImage(x * layerWidth, y * layerHeight,
                        width * layerWidth, height * layerHeight,
                        defaultImage, this);
                images.add(image);
            }

        } catch (JSONException | IllegalArgumentException e) {
            throw new RuntimeException(
                    "SplashScreen.constructImages: JSON parsing error [" + e.getMessage() + "]");
        }
    }

    /**
     * Update the splash screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override

    public void update(ElapsedTime elapsedTime) {
        nextScreenCountadown();

        // Process any touch events occurring since the update
        Input touchInput = mGame.getInput();
        List<TouchEvent> touchEvents = touchInput.getTouchEvents();
        if (touchEvents.size() > 0) {
            mSplashBackground.update(elapsedTime);

            if (mSplashBackground.isPushTriggered())
                goToMenuScreen();
        }
    }

    public void goToMenuScreen() {
        mGame.getScreenManager().changeScreenButton(new MenuScreen(mGame));
    }

    /**
     * Draw the 'Splash Screen' screen
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance
     */

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mSplashBackground.draw(elapsedTime, graphics2D, mSplashLayerViewport,
                mDefaultScreenViewport);

        for (TitleImage image : mImages)
            image.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);
    }

    public long getTimeOnCreate() {
        return timeOnCreate;
    }

    public void setTimeOnCreate(long timeOnCreate) {
        this.timeOnCreate = timeOnCreate;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
