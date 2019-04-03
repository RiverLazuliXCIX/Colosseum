
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
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.util.ViewportHelper;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;

/**
 * Created by Kyle Corrigan
 */

/**
 *  Screen to allow players to select the hero they will play as, as well as the opponent they will
 *  play against.
 *
 * @author Kyle Corrigan
 */

public class HeroSelectScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private LayerViewport heroSelectViewport; // The screen's layer viewport

    private GameObject gameBackground; // Stores the background of the hero select screen

    private String titleText = "Select a hero - Player"; // Stores the screen title text
    private String descriptionText = ""; // Stores the hero description text

    // ArrayList of buttons used to select a hero (heroButtons)
    // Arraylist of buttons to confirm selection or transition between screens (UIButtons)
    private ArrayList <PushButton> heroButtons, uiButtons;

    // heroButtonTriggers: Hash map of hero names each button corresponds to
    // uiButtonTriggers: Hash map of UI button names each button corresponds to (eg. continue and back)
    // heroDescriptionTriggers: Hash map of Hero buttons and the hero descriptions they should display
    private Map <PushButton,String> heroButtonTriggers, uiButtonTriggers, heroDescriptionTriggers;

    private GameObject heroSelectionBox; // Sprite used to represent the hero selection box

    private String selectedPlayerHero = ""; // The hero selected for the player (default empty value to allow for isEmpty check when continue pressed)
    private String selectedOpponentHero = ""; // The hero selected for the opponent (default empty value to allow for isEmpty check when continue pressed)

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
        this.heroButtonTriggers = new HashMap<>();
        this.heroButtons = new ArrayList<>();
        this.uiButtonTriggers = new HashMap<>();
        this.uiButtons = new ArrayList<>();
        this.heroDescriptionTriggers = new HashMap<>();

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
        heroSelectViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    /**
     * Sets up objects associated with the screen, eg. hero buttons and background etc.
     */
    public void setupHeroSelectScreenObjects(){

        // Loads assets relating to heroes, Hero assets contains assets for hero icons and abilities etc.
        // while hero select screen assets store assets relating specifically to this screen, eg. button details and background
        // bitmap
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/HeroSelectScreenAssets.JSON");

        // Setting up the background to fit the screen size
        Bitmap backgroundBitmap = getGame().getAssetManager().getBitmap("HeroSelectBG");
        gameBackground = new GameObject(mDefaultScreenViewport.width/2.0f,
                mDefaultScreenViewport.height/2.0f, mDefaultScreenViewport.width,
                mDefaultScreenViewport.height, backgroundBitmap, this);

        // Creating the on-screen buttons
        setupButtons();

    }

    /**
     * Adapted from JSON refactoring lecture code
     * Reads and adds buttons to the screen based on the content contained in the JSON file
     * Button triggers used to set player and opponent hero, as well as pulling related hero information
     * used in the on-screen hero description.
     *
     * @param buttonJSON JSON file used to store button details
     */
    private void createHeroButtonsFromJSON(String buttonJSON){

        String loadedJSON;
        // Try catch method for handling loading/configuring buttons from a JSON file
        try{
            loadedJSON = mGame.getFileIO().loadJSON(buttonJSON);
        } catch (IOException e){
            throw new RuntimeException("HeroSelectScreen.createHeroButtonsFromJSON: Cannot load JSON (" + buttonJSON +" )");
        }

        // Retrieving the information from the JSON file
        try{
            JSONObject settings = new JSONObject(loadedJSON);
            JSONArray buttonSettings = settings.getJSONArray("heroButtons");

            // Store the layer width and height to scale buttons for their positions on screen
            float layerWidth = mDefaultLayerViewport.getWidth();
            float layerHeight = mDefaultLayerViewport.getHeight();

            // Pulling information to construct each button in the JSON file, along with the associated
            for(int i=0;i<buttonSettings.length(); i++){

                float x = (float)buttonSettings.getJSONObject(i).getDouble("x");
                float y = (float)buttonSettings.getJSONObject(i).getDouble("y");
                float width = (float)buttonSettings.getJSONObject(i).getDouble("width");
                float height = (float)buttonSettings.getJSONObject(i).getDouble("height");

                String defaultBitmap = buttonSettings.getJSONObject(i).getString("defaultBitmap");
                String pushBitmap= buttonSettings.getJSONObject(i).getString("pushBitmap");
                String triggeredHeroName = buttonSettings.getJSONObject(i).getString("triggeredHeroName");
                String manaCost = buttonSettings.getJSONObject(i).getString("manaCost");
                String abilityName = buttonSettings.getJSONObject(i).getString("abilityName");
                String abilityDescription = buttonSettings.getJSONObject(i).getString("abilityDescription");

                // Create the push button from the details parsed in the JSON file
                PushButton heroButton = new PushButton(x*layerWidth,y*layerHeight, width*layerWidth,height*layerHeight, defaultBitmap, pushBitmap, this);
                heroButtons.add(heroButton);
                heroButtonTriggers.put(heroButton, triggeredHeroName);

                // Create the description string for the hero from the details parsed in the JSON file
                String description = triggeredHeroName + "\n"
                        + "Ability cost: "+manaCost+"\n"
                        + "Ability name: "+abilityName+"\n"
                        + "Ability description: "+abilityDescription+"\n";
                heroDescriptionTriggers.put(heroButton, description);

            }

        } catch (JSONException | IllegalArgumentException e){
            throw new RuntimeException("HeroSelectScreen.createHeroButtonsFromJSON: JSON parsing error ("+e.getMessage()+" )");
        }
    }

    /**
     * Creation of UI buttons to allow players to confirm their selection, as well as navigate between screens
     */
    private void createUIButtons(){

        // Sets the UI buttons width and height in relation to the default layer viewport
        float uiButtonWidth = (mDefaultLayerViewport.getWidth() / 5.0f)/2.5f;
        float uiButtonHeight = (mDefaultLayerViewport.getHeight() / 3.0f)/2.5f;

        // Confirmation button: allows users to confirm the hero they have selected
        PushButton confirmationButton = new PushButton(mDefaultLayerViewport.getRight()-uiButtonWidth,
                mDefaultLayerViewport.getBottom()+uiButtonHeight,uiButtonWidth,uiButtonHeight,
                "ConfirmArrow","ConfirmArrowPushed",this);

        // Back button: Allows users to return to the previous hero selection, or, if the player
        //              is selecting their own hero, return to the main menu
        PushButton backButton = new PushButton(mDefaultLayerViewport.getLeft()+uiButtonWidth,
                mDefaultLayerViewport.getBottom()+uiButtonHeight,uiButtonWidth,uiButtonHeight,
                "ReturnArrow","ReturnArrowPushed",this);

        // Adding the created buttons to the uiButton arraylist
        uiButtons.add(confirmationButton);
        uiButtons.add(backButton);

        // Adding the strings associated with the buttons to allow for processing
        uiButtonTriggers.put(confirmationButton,"confirmationButton");
        uiButtonTriggers.put(backButton,"backButton");

    }

    /**
     * Creating and setting up HeroSelectScreen buttons
     */
    private void setupButtons(){

        createHeroButtonsFromJSON("txt/assets/HeroSelectScreenAssets.JSON");
        createUIButtons();

    }

    /**
     * Checks if hero buttons have been pressed, and carries out the related action.
     *
     * @param elapsedTime Elapsed time information
     */
    private void updateHeroButtons(ElapsedTime elapsedTime){

        // Update each hero button to check if push event is triggered
        for (int i = 0; i< heroButtons.size(); i++){
            heroButtons.get(i).update(elapsedTime);

            // Update push events to move selection rectangle to selected hero portrait
            if(heroButtons.get(i).isPushTriggered()){

                // If neither player nor opponent have chosen a hero, first hero button press selects player hero
                if(!playerHeroChosen && !opponentHeroChosen){
                    selectedPlayerHero = heroButtonTriggers.get(heroButtons.get(i));
                    descriptionText = heroDescriptionTriggers.get(heroButtons.get(i));
                }

                // If player has chosen their hero, next hero button press selects the opponent hero
                if(playerHeroChosen && !opponentHeroChosen){
                    selectedOpponentHero = heroButtonTriggers.get(heroButtons.get(i));
                    descriptionText = heroDescriptionTriggers.get(heroButtons.get(i));
                }

            }

        }

    }

    /**
     * Checks if UI buttons have been pressed, such as confirm and back buttons, and carries out the
     * related action.
     *
     * @param elapsedTime Elapsed time information
     */
    private void updateUIButtons(ElapsedTime elapsedTime){

        // Stores whether a hero has been chosen this update, to differentiate between player and opponent selections
        boolean uiButtonPressedThisUpdate = false;

        // Update each UI button to check if push event is triggered
        for(int i = 0; i<uiButtons.size();i++ ){
            uiButtons.get(i).update(elapsedTime);

            if(uiButtons.get(i).isPushTriggered()){

                // Meaning if player has not chosen their hero, and opponent has not, it is the player's hero choice
                if(uiButtonTriggers.get(uiButtons.get(i)).equals("confirmationButton")
                        && !playerHeroChosen && !opponentHeroChosen && !uiButtonPressedThisUpdate
                        && !selectedPlayerHero.isEmpty()){

                    uiButtonPressedThisUpdate = true;
                    playerHeroChosen=true;
                    titleText = "Select a hero - Opponent";
                    descriptionText =""; // Resets description text to prevent text carrying over for next selection

                }

                // Meaning if player has chosen their hero, and opponent has not, it is the opponent's hero choice
                if(uiButtonTriggers.get(uiButtons.get(i)).equals("confirmationButton")
                        && playerHeroChosen && !opponentHeroChosen && !uiButtonPressedThisUpdate
                        && !selectedOpponentHero.isEmpty()){

                    uiButtonPressedThisUpdate = true;
                    opponentHeroChosen=true;

                }

                // If back button pressed and player has already selected their hero, allow player
                // to choose their player again
                if(uiButtonTriggers.get(uiButtons.get(i)).equals("backButton")
                        && playerHeroChosen && !opponentHeroChosen && !uiButtonPressedThisUpdate){
                    uiButtonPressedThisUpdate = true;

                    playerHeroChosen = false;
                    selectedPlayerHero ="";
                    selectedOpponentHero ="";
                    titleText = "Select a hero - Player";
                    descriptionText =""; // Resets description text to prevent text carrying over for next selection

                }

                // If back button pressed and player has not selected their hero, return to main menu
                // and remove the hero select screen
                if(uiButtonTriggers.get(uiButtons.get(i)).equals("backButton")
                        && !playerHeroChosen && !opponentHeroChosen && !uiButtonPressedThisUpdate){
                    uiButtonPressedThisUpdate = true;
                    mGame.getScreenManager().changeScreenButton(new MenuScreen(mGame));
                    mGame.getScreenManager().removeScreen(this);

                }

            }

        }

    }


    /**
     * Updates the screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime){

        // Process any touch events occurring on update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        // if touch event occurs
        if (touchEvents.size() >0){

            // Update Hero and UI buttons
            updateHeroButtons(elapsedTime);
            updateUIButtons(elapsedTime);

        }

        // If player and opponent hero has been chosen
        if(playerHeroChosen && opponentHeroChosen){
            // Load coin toss screen, passing results from hero selection
            mGame.getScreenManager().changeScreenButton(new CoinTossScreen(mGame, selectedPlayerHero, selectedOpponentHero));
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

        float descriptionX = mDefaultScreenViewport.width/2.0f;
        float descriptionY = (mDefaultScreenViewport.height/2.0f)+textBodyPaint.getTextSize();

        // Splits the hero description text into separate lines based on the "\n" new line delimiter
        for (String line: descriptionText.split("\n")) {
            graphics2D.drawText(line, descriptionX, descriptionY, textBodyPaint);
            descriptionY += textBodyPaint.descent() - textBodyPaint.ascent();
        }

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
        gameBackground.draw(elapsedTime,graphics2D);

        // Draw screen text
        drawHeroScreenText(graphics2D);

        // Draw each of the hero buttons onscreen
        for (int i = 0; i< heroButtons.size(); i++){
            heroButtons.get(i).draw(elapsedTime, graphics2D, mDefaultLayerViewport,mDefaultScreenViewport);
        }

        // Draw each of the UI buttons onscreen
        for (int i = 0; i< uiButtons.size(); i++){
            uiButtons.get(i).draw(elapsedTime, graphics2D, mDefaultLayerViewport,mDefaultScreenViewport);
        }

    }

    // /////////////////////////////////////////////////////////////////////////
    // Getter and setter methods
    // /////////////////////////////////////////////////////////////////////////


    public GameObject getGameBackground() { return gameBackground; }
    public void setGameBackground(GameObject gameBackground) { this.gameBackground = gameBackground; }

    public LayerViewport getHeroSelectViewport() { return heroSelectViewport; }
    public void setHeroSelectViewport(LayerViewport heroSelectViewport) { this.heroSelectViewport = heroSelectViewport; }

    public ArrayList<PushButton> getHeroButtons() { return heroButtons; }
    public void setHeroButtons(ArrayList<PushButton> heroButtons) { this.heroButtons = heroButtons; }

    public Map<PushButton, String> getHeroButtonTriggers() { return heroButtonTriggers; }
    public void setHeroButtonTriggers(Map<PushButton, String> heroButtonTriggers) { this.heroButtonTriggers = heroButtonTriggers; }

    public String getSelectedPlayerHero() { return selectedPlayerHero; }
    public void setSelectedPlayerHero(String selectedPlayerHero) { this.selectedPlayerHero = selectedPlayerHero; }

    public String getSelectedOpponentHero() { return selectedOpponentHero; }
    public void setSelectedOpponentHero(String selectedOpponentHero) { this.selectedOpponentHero = selectedOpponentHero; }

    public Boolean getPlayerHeroChosen() { return playerHeroChosen; }
    public void setPlayerHeroChosen(Boolean playerHeroChosen) { this.playerHeroChosen = playerHeroChosen; }

    public Boolean getOpponentHeroChosen() { return opponentHeroChosen; }
    public void setOpponentHeroChosen(Boolean opponentHeroChosen) { this.opponentHeroChosen = opponentHeroChosen; }

    public String getTitleText() { return titleText; }
    public void setTitleText(String titleText) { this.titleText = titleText; }

    public String getDescriptionText() { return descriptionText; }
    public void setDescriptionText(String descriptionText) { this.descriptionText = descriptionText; }

    public ArrayList<PushButton> getUiButtons() { return uiButtons; }
    public void setUiButtons(ArrayList<PushButton> uiButtons) { this.uiButtons = uiButtons; }

    public Map<PushButton, String> getUiButtonTriggers() { return uiButtonTriggers; }
    public void setUiButtonTriggers(Map<PushButton, String> uiButtonTriggers) { this.uiButtonTriggers = uiButtonTriggers; }

    public GameObject getHeroSelectionBox() { return heroSelectionBox; }
    public void setHeroSelectionBox(Sprite heroSelectionBox) { this.heroSelectionBox = heroSelectionBox; }
}
