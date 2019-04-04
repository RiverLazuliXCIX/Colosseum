
package uk.ac.qub.eeecs.game.TestClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.Sprite;
import uk.ac.qub.eeecs.game.CoinTossScreen;

/**
 *  A barebones copy of HeroSelectScreen with graphical elements, and elements processing user input
 *  removed for testing purposes.
 *
 * @author Kyle Corrigan
 */

public class HeroSelectScreenForTesting extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private LayerViewport heroSelectViewport; // The screen's layer viewport

    private GameObject gameBackground, heroSelectionBox;

    private String titleText = "Select a hero - Player"; // Stores the screen title text
    private String descriptionText = ""; // Stores the hero description text

    // ArrayList of buttons used to select a hero (heroButtons)
    // Arraylist of buttons to confirm selection or transition between screens (UIButtons)
    private ArrayList <PushButton> heroButtons, uiButtons;

    // heroButtonTriggers: Hash map of hero names each button corresponds to
    // uiButtonTriggers: Hash map of UI button names each button corresponds to (eg. continue and back)
    // heroDescriptionTriggers: Hash map of Hero buttons and the hero descriptions they should display
    private Map <PushButton,String> heroButtonTriggers, uiButtonTriggers;

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
    public HeroSelectScreenForTesting(Game game){
        super("HeroSelectScreen", game);

        // Initialising hero select screen properties
        this.heroButtonTriggers = new HashMap<>();
        this.heroButtons = new ArrayList<>();
        this.uiButtonTriggers = new HashMap<>();
        this.uiButtons = new ArrayList<>();

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

    }

    /**
     * Updates the screen
     *
     * @param elapsedTime Elapsed time information
     */
    @Override
    public void update(ElapsedTime elapsedTime){

        // If player and opponent hero has been chosen
        if(playerHeroChosen && opponentHeroChosen){
            // Load coin toss screen, passing results from hero selection
            mGame.getScreenManager().changeScreenButton(new CoinTossScreen(mGame, selectedPlayerHero, selectedOpponentHero));
        }

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){

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
