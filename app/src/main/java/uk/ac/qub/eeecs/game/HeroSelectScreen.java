
package uk.ac.qub.eeecs.game;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

/**
 * Created by Kyle Corrigan
 */

/**
 *  Screen to allow players to select the hero they will play as, as well as the opponent they will
 *  play against.
 */

public class HeroSelectScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private LayerViewport mHeroSelectViewport; // The screen's layer viewport

    private GameObject mGameBackground; // Stores the background of the hero select screen

    private String titleText = "Select a hero - Player"; // Stores the screen title text
    private String descriptionText = "Placeholder text, this should update with hero information!"; // Stores the hero description text

    private ArrayList <PushButton> mHeroButtons; // ArrayList of buttons used to select a hero

    private Map <PushButton,String> mHeroButtonTriggers; // Hash map of hero names each button corresponds to

    private String mSelectedPlayerHero, mSelectedOpponentHero; // The heroes selected for both the player and the opponent

    private Boolean playerHeroChosen = false; // Stores whether the player hero has been chosen
    private Boolean opponentHeroChosen = false; // Stores whether the opponent hero has been chosen

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Constructor used to initialise the game screen
     *
     * @param game game the screen belongs to
     */
    public HeroSelectScreen (Game game){
        super("HeroSelectScreen", game);

        // Initialising hero select screen properties
        this.mHeroButtonTriggers = new HashMap<>();
        this.mHeroButtons = new ArrayList<>();

        setUpViewports();
        setupHeroSelectScreenObjects();

    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Sets up viewports for the screen
     */
    private void setUpViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mHeroSelectViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    /**
     * Sets up objects associated with the screen, eg. hero buttons and background et.
     */
    public void setupHeroSelectScreenObjects(){

        // Loads assets relating to heroes, Hero assets contains information for hero icons and abilities etc
        // while hero select screen assets store assets relating specifically to this screen, eg. button details and background
        // bitmap
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/HeroSelectScreenAssets.JSON");

        // Setting up the background to fit the screen size
        Bitmap mBackgroundBitmap = getGame().getAssetManager().getBitmap("HeroSelectBG");
        mGameBackground = new GameObject(mDefaultScreenViewport.width/2.0f,
                mDefaultScreenViewport.height/2.0f, mDefaultScreenViewport.width,
                mDefaultScreenViewport.height, mBackgroundBitmap, this);

        createButtons("txt/assets/HeroSelectScreenAssets.JSON", mHeroButtons);

    }

    /**
     * Adapted from JSON refactoring lecture code
     * Reads and adds buttons to the screen based on the content contained in the JSON file
     * Button triggers used to set player and opponent hero
     *
     * @param buttonJSON JSON file used to store button details
     * @param heroButtons List of buttons to select a hero
     */
    private void createButtons(String buttonJSON, List<PushButton>heroButtons){

        String loadedJSON;
       // Try catch method for handling loading/configuring buttons from a JSON file
       try{
           loadedJSON = mGame.getFileIO().loadJSON(buttonJSON);
       } catch (IOException e){
           throw new RuntimeException("HeroSelectScreen.createButtons: Cannot load JSON (" + buttonJSON +" )");
       }

       // Retrieving the information from the JSON file
        try{
            JSONObject settings = new JSONObject(loadedJSON);
            JSONArray buttonSettings = settings.getJSONArray("heroButtons");

            // Store the layer width and height to scale buttons for their positions on screen
            float layerWidth = mDefaultLayerViewport.getWidth();
            float layerHeight = mDefaultLayerViewport.getHeight();

            // Construct each button in the JSON file
            for(int i=0;i<buttonSettings.length(); i++){
                float x = (float)buttonSettings.getJSONObject(i).getDouble("x");
                float y = (float)buttonSettings.getJSONObject(i).getDouble("y");
                float width = (float)buttonSettings.getJSONObject(i).getDouble("width");
                float height = (float)buttonSettings.getJSONObject(i).getDouble("height");

                String defaultBitmap = buttonSettings.getJSONObject(i).getString("defaultBitmap");
                String pushBitmap= buttonSettings.getJSONObject(i).getString("pushBitmap");
                String triggeredHeroName = buttonSettings.getJSONObject(i).getString("triggeredHeroName");

                // Create the push button from the details parsed in the JSON file
                PushButton heroButton = new PushButton(x*layerWidth,y*layerHeight, width*layerWidth,height*layerHeight, defaultBitmap, pushBitmap, this);
                heroButtons.add(heroButton);
                mHeroButtonTriggers.put(heroButton, triggeredHeroName);

            }

        } catch (JSONException | IllegalArgumentException e){
            throw new RuntimeException("HeroSelectScreen.createButtons: JSON parsing error ("+e.getMessage()+" )");
        }
    }

    /**
     * Updates the screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime){

        boolean selectedThisUpdate = false; // Stores whether a hero has been selected this update

        // Process any touch events occurring on update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        // if touch event occurs
        if (touchEvents.size() >0){

            // Update each button
            for (int i = 0; i< mHeroButtons.size(); i++){
                mHeroButtons.get(i).update(elapsedTime);

            if(mHeroButtons.get(i).isPushTriggered()){

                // If neither player nor opponent have chosen a hero, first hero button press selects player hero
                if(!playerHeroChosen && !opponentHeroChosen && !selectedThisUpdate){
                    mSelectedPlayerHero = mHeroButtonTriggers.get(mHeroButtons.get(i));
                    playerHeroChosen=true;
                    selectedThisUpdate = true;
                    titleText = "Select a hero - Opponent";
                }

                // If player has chosen their hero, next hero button press selects the opponent hero
                if(playerHeroChosen && !opponentHeroChosen && !selectedThisUpdate){
                    mSelectedOpponentHero = mHeroButtonTriggers.get(mHeroButtons.get(i));
                    opponentHeroChosen=true;
                    selectedThisUpdate = true;
                }

            }

            }

        }
        // If player and opponent hero has been chosen
        if(playerHeroChosen && opponentHeroChosen){
            // Load coin toss screen, passing results from hero selection
            mGame.getScreenManager().changeScreenButton(new CoinTossScreen(mGame, mSelectedPlayerHero, mSelectedOpponentHero));
        }

    }

    /**
     * Draws text to be displayed on the hero screen
     *
     * @param graphics2D Graphics instance
     */
    public void drawHeroScreenText( IGraphics2D graphics2D){

        // Initialising properties for the Title text
        Paint textTitlePaint = new Paint();
        textTitlePaint.setTextSize(100);
        textTitlePaint.setColor(Color.WHITE);
        textTitlePaint.setTextAlign(Paint.Align.CENTER);
        textTitlePaint.setStyle(Paint.Style.FILL);
        textTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);

        // Draws title text twice, once with a white fill style, second time with black stroke style to achieve a text outline effect
        graphics2D.drawText(titleText, mDefaultScreenViewport.width/2.0f, mDefaultScreenViewport.top+textTitlePaint.getTextSize(),textTitlePaint);
        textTitlePaint.setColor(Color.BLACK);
        textTitlePaint.setStyle(Paint.Style.STROKE);
        textTitlePaint.setStrokeWidth(4);
        graphics2D.drawText(titleText, mDefaultScreenViewport.width/2.0f, mDefaultScreenViewport.top+textTitlePaint.getTextSize(),textTitlePaint);

        // Initialising properties for the text body (Used in hero description)
        Paint textBodyPaint = new Paint();
        textBodyPaint.setTextSize(50);
        textBodyPaint.setColor(Color.WHITE);
        textBodyPaint.setTextAlign(Paint.Align.CENTER);

        graphics2D.drawText(descriptionText, mDefaultScreenViewport.width/2.0f, (mDefaultScreenViewport.height/2)+textBodyPaint.getTextSize(),textBodyPaint);

    }

    /**
     * Draws screen elements
     *
     * @param elapsedTime Elapsed time information
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        // Draw the background
        mGameBackground.draw(elapsedTime,graphics2D);

        // Draw screen text
        drawHeroScreenText(graphics2D);

        // Draw each of the buttons onscreen
        for (int i = 0; i< mHeroButtons.size(); i++){
            mHeroButtons.get(i).draw(elapsedTime, graphics2D, mDefaultLayerViewport,mDefaultScreenViewport);
        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and setter methods
    // /////////////////////////////////////////////////////////////////////////


    public GameObject getmGameBackground() { return mGameBackground; }
    public void setmGameBackground(GameObject mGameBackground) { this.mGameBackground = mGameBackground; }

    public LayerViewport getmHeroSelectViewport() { return mHeroSelectViewport; }
    public void setmHeroSelectViewport(LayerViewport mHeroSelectViewport) { this.mHeroSelectViewport = mHeroSelectViewport; }

    public ArrayList<PushButton> getmHeroButtons() { return mHeroButtons; }
    public void setmHeroButtons(ArrayList<PushButton> mHeroButtons) { this.mHeroButtons = mHeroButtons; }

    public Map<PushButton, String> getmHeroButtonTriggers() { return mHeroButtonTriggers; }
    public void setmHeroButtonTriggers(Map<PushButton, String> mHeroButtonTriggers) { this.mHeroButtonTriggers = mHeroButtonTriggers; }

    public String getmSelectedPlayerHero() { return mSelectedPlayerHero; }
    public void setmSelectedPlayerHero(String mSelectedPlayerHero) { this.mSelectedPlayerHero = mSelectedPlayerHero; }

    public String getmSelectedOpponentHero() { return mSelectedOpponentHero; }
    public void setmSelectedOpponentHero(String mSelectedOpponentHero) { this.mSelectedOpponentHero = mSelectedOpponentHero; }

    public Boolean getPlayerHeroChosen() { return playerHeroChosen; }
    public void setPlayerHeroChosen(Boolean playerHeroChosen) { this.playerHeroChosen = playerHeroChosen; }

    public Boolean getOpponentHeroChosen() { return opponentHeroChosen; }
    public void setOpponentHeroChosen(Boolean opponentHeroChosen) { this.opponentHeroChosen = opponentHeroChosen; }

}
