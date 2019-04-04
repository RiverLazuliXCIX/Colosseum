package uk.ac.qub.eeecs.gage.engine;

import java.util.Stack;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * The screen manager stores the available screens defined within the game.
 * Screens can be added or remove to reflect the evolution of the game. Within
 * the central game loop, the current game screen will be retrieved and
 * updated/rendered.
 *
 * A stack structure is used, with the screen at the top of the stack considered
 * to be the current screen that should be updated and rendered.
 *
 * @version 1.0
 */
public class ScreenManager {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Collection of available game screens
     */
    private Stack<GameScreen> mGameScreens;
    private GameScreen searchedScreen; //Used to temporarily store the "searched for" screen, to be moved to the top of the stack.

    /**
     * Game instance
     */
    private Game mGame;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new screen manager
     *
     * @param game Game instance
     */
    public ScreenManager(Game game) {
        mGame = game;
        mGameScreens = new Stack<>();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Add the specified game screen to the manager.
     * <p>
     * Note: When added to the screen manager a screen will automatically become
     * the current game screen (to be updated and rendered).
     *
     * @param screen GameScreen instance to be added
     * @return Boolean true if the screen was added, false if the screen could
     * not be added (a screen with the specified name already exists).
     */
    public boolean addScreen(GameScreen screen) {
        // Add the game screen if the specified name isn't already added
        if (mGameScreens.contains(screen)) { //If screen already exists, dont add it
            return false;}
        //Otherwise push the screen to top of stack
        mGameScreens.push(screen);
        return true;
    }

    /**
     * Return the current game screen.
     *
     * @return Current game instance instance, or null if no current game screen
     * has been defined.
     */
    public GameScreen getCurrentScreen() {
        return mGameScreens.peek();
    }

    /**
     * Return the named game screen.
     *
     * @param screenName String screenName reference for the target screen.
     * @return Current game instance instance, or null if no the specified game
     * screen could not be found.
     */
    public GameScreen getScreen(String screenName) {
        for(GameScreen gameScreen : mGameScreens) { //Iterate through the list of all current gamescreens
            if(gameScreen.getName().compareTo(screenName) == 0) {
                //If the current name (from iterating through the list) is equal to the searched for name then return it
                return gameScreen; }
            //It was not currently found, the rest of the stack can be checked
        } //The gamescreen was not found at all
        return null;
    }

    public void changeScreenButton(GameScreen screen) {//Used for changing game screens order (allowing for screens to be resumed rather than removed and created anew) - Scott
        int findScreenPosition = findScreenIterator(screen.getName()); //Find and set the position the screen is at
        if(findScreenPosition > -1) { //If the screen was found, enter statement
            //If the target screen exists already, swap the screen to top of stack (instead of a new instance)
            mGame.getScreenManager().gotoScreen(findScreenPosition); //Call method "gotoScreen" to get to target screen
        } else { //Else the screen doesnt exist
            mGame.getScreenManager().addScreen(screen); //Create a new instance of the screen
        }

    }

    public String getScreenName(GameScreen gameScreenName) { //Get the string name from the gamescreen
        return gameScreenName.getName();
    }

    public void printAllScreenNames() { //Scott Sprint 5 - Used for debugging current screens
        System.out.println("NEW LIST");
        System.out.println("Screen count: " + mGameScreens.size());
        for(GameScreen theScreen: mGameScreens) { //Iterate through each gamescreen currently existing
            System.out.println(theScreen.getName()); //Output the list of screens
        }
    }

    public GameScreen findScreen(GameScreen screen) { //Scott Sprint 5 - Used for finding already created screens (for future usage).
        for(GameScreen gameScreen : mGameScreens) { //iterate through each screen looking if the screens' name input, exists
            if(gameScreen.getName().compareTo(screen.getName()) == 0) { //if found, return true (if current screen name and input screen name is equal)
                return screen; }
            //It was not currently found, the rest of the stack can be checked
        } //The gamescreen was not found at all, return false
        return null;
    }

    public int findScreenIterator(String screenName) { //Scott Sprint 5 - Used for finding already created screens based off their name and providing the location of the stack they are at.
        int iterator = 0; //Initialise to 0.
        for(GameScreen gameScreen : mGameScreens) { //iterate through each screen looking if the screens' name input, exists
            if(gameScreen.getName().compareTo(screenName) == 0) { //if found, return iterator of current position (if current screen name and input screen name is equal)
                return iterator; }
            iterator++; //Increment by one after we check each screen
            //It was not currently found, the rest of the stack can be checked
        } //The gamescreen was not found at all, return -1 for error catching
        return -1;
    }

    //Changes added by Scott, Sprint 5 - Feb
    public void gotoScreen(int screenLocation) { //This method is used to go to a screen that is already existing, alternative for deleting screens and creating new ones.
        //This is very useful to allow for "resuming" screens, such as resuming a current game, or making sure an options screen retains its previous changes made by the user.
        if(screenLocation > -1) { //If the screen was found, enter statement
            searchedScreen = mGameScreens.elementAt(screenLocation); //Store the found screen in temporary variable "searchedScreen"
            mGameScreens.remove(screenLocation); //Remove the gamescreen in the stack at the current point
            mGameScreens.push(searchedScreen); //Push the found screen onto the top of the stack, thereby making it the "current screen"
        }
    }

    public void previousScreen() { //Scott Sprint 5 - This is used to check if a screen previous to the current screen is there.
        //It is used in the case that a screen has different ways of getting to the screen, and therefore making sure that when the player "goes back" they get to the correct previous screen
        // (e.g. options screen can be accessed from the main menu, and from inside a currently played game, going back to the main menu every time would not be intuitive).
        gotoScreen(mGameScreens.size()-2); //-1 for size as it uses 1 based indexing, -1 again as the gotoScreen (and stacks) use 0 based indexing (so -2 total).
    }

    /**
     * Remove the specified game screen from the manager.
     * <p>
     * Note: Remove a screen from the manager will not result in dispose being
     * automatically called on the removed screen.
     *
     * @param gameScreen Reference to the screen to remove.
     * @return Boolean true if the screen was removed, false otherwise (the
     * specified screen could not be found).
     */
    public boolean removeScreen(GameScreen gameScreen) {
        return mGameScreens.remove(gameScreen);
    }

    /**
     * Remove the specified game screen from the manager.
     * <p>
     * Note: Remove a screen from the manager will not result in dispose being
     * automatically called on the removed screen.
     *
     * @param screenName String name reference for the screen to remove.
     * @return Boolean true if the screen was removed, false otherwise (the
     * specified screen could not be found).
     */
    public boolean removeScreen(String screenName) {
        GameScreen screenToRemove = null;
        for(GameScreen gameScreen : mGameScreens) {
            if(gameScreen.getName().compareTo(screenName) == 0)
                screenToRemove = gameScreen;
        }

        if(screenToRemove == null)
            return false;
        else
            return mGameScreens.remove(screenToRemove);
    }

    /**
     * Remove all screens held by this screen manager.
     */
    public void removeAllScreens() {
        mGameScreens.clear();
    }

    /**
     * Dispose of the manager and all game screens stored within the manager.
     */
    public void dispose() {
        for (GameScreen gameScreen : mGameScreens )
            gameScreen.dispose();
    }
}
