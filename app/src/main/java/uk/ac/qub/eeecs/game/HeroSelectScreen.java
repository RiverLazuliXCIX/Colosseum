package uk.ac.qub.eeecs.game;

import android.graphics.Paint;

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
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;

/**
 *  Screen to allow players to select the hero they will play as, as well as the opponent they will
 *  play against.
 */

public class HeroSelectScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private LayerViewport mHeroSelectViewport; // The screen's layer viewport

    private ArrayList <PushButton> mHeroButtons; // ArrayList of buttons used to select a hero

    private Map <PushButton,String> mHeroButtonTriggers; // Hash map of hero names each button corresponds to

    private String mSelectedPlayerHero, mSelectedOpponentHero;

    private Boolean playerHeroChosen = false;
    private Boolean opponentHeroChosen = false;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

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

    private void setUpViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mHeroSelectViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    public void setupHeroSelectScreenObjects(){

        mGame.getAssetManager().loadAssets("txt/assets/CoinTossAssets.JSON"); // For the background image
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");

        createButtons("txt/assets/HeroSelectScreenAssets.JSON", mHeroButtons);

    }

    // Adapted from JSON refactoring lecture code
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

    @Override
    public void update(ElapsedTime elapsedTime){

        // Process any touch events occurring on update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() >0){

            // Update each button
            for (int i = 0; i< mHeroButtons.size(); i++){
                mHeroButtons.get(i).update(elapsedTime);

                if(mHeroButtons.get(i).isPushTriggered() && !playerHeroChosen){
                    // set player hero
                    mSelectedPlayerHero = mHeroButtonTriggers.get(mHeroButtons.get(i));
                    playerHeroChosen = true;
                }

                if(mHeroButtons.get(i).isPushTriggered() && playerHeroChosen && !opponentHeroChosen){
                    // set opponent hero
                    mSelectedOpponentHero = mHeroButtonTriggers.get(mHeroButtons.get(i));
                    opponentHeroChosen = true;
                }

                // If player and opponent hero has been chosen
                if(playerHeroChosen && opponentHeroChosen){
                    // Load colosseum game screen
                }
            }

        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

        // Draw each of the buttons onscreen
        for (int i = 0; i< mHeroButtons.size(); i++){
            mHeroButtons.get(i).draw(elapsedTime, graphics2D, mDefaultLayerViewport,mDefaultScreenViewport);
        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and setter methods
    // /////////////////////////////////////////////////////////////////////////



}
