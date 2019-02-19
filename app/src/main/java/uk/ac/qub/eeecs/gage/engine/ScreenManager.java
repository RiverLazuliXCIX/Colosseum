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
        if (mGameScreens.contains(screen)) {
            //System.out.println("screen already exists"); // Used for debugging new additions to the screenmanager code
            return false;}
        //System.out.println("screen didnt exist"); // Used for debugging new additions to the screenmanager code
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
     * @param name String name reference for the target screen.
     * @return Current game instance instance, or null if no the specified game
     * screen could not be found.
     */
    public GameScreen getScreen(String name) {
        for(GameScreen gameScreen : mGameScreens) {
            if(gameScreen.getName().compareTo(name) == 0) {
                //System.out.println("found"); // Used for debugging new additions to the screenmanager code
                return gameScreen; }
            //System.out.println("not found at current: " + gameScreen.getName()); // Used for debugging new additions to the screenmanager code
        } //System.out.println("not found at all"); // Used for debugging new additions to the screenmanager code
        return null;
    }

    public void changeScreenButton(GameScreen screen) {//Used for changing game screens
        if(mGame.getScreenManager().existingScreen(screen.getName())) {//if the target screen exists already, swap the screen to top of stack (instead of a new instance)
            //System.out.println("screen was already found");
            mGame.getScreenManager().gotoScreen(screen.getName()); //Call method "gotoScreen" to get to target screen
        } else { //Else the screen doesnt exist
            //System.out.println("screen was not already found");
            mGame.getScreenManager().addScreen(screen); //Create a new instance of the screen
        }
    }

    public String getScreenName(GameScreen gameScreen) { //Get the string name from the gamescreen
        return gameScreen.getName();
    }

    public void printAllScreenNames() { //Scott Sprint 5
        System.out.println("NEW LIST");
        for(GameScreen theScreen: mGameScreens) { //Iterate through each gamescreen currently existing
            System.out.println(theScreen.getName()); //Output the list of screens
        }
    }

    public void gotoScreen(String name) { //Changes added by Scott, Sprint 5 - Feb
        int i = 0; //Set iterator to 0
        if (existingScreen(name)) { //If the screen exists, enter loop
            for (GameScreen gameScreen1 : mGameScreens) { //Iterate through each screen currently active
                //System.out.println(i); //Check for the size of the list
                if (gameScreen1.getName().compareTo(name) == 0) { //If the current screens name is equal to the "searched for" screen
                   // System.out.println("Screen popped from original stack"); //For debugging purposes
                    searchedScreen = mGameScreens.elementAt(i); //Store the found screen in temporary variable "searchedScreen"
                    mGameScreens.remove(i); //Remove the gamescreen in the stack at the current point
                    mGameScreens.push(searchedScreen); //Push the found screen onto the top of the stack, thereby making it the "current screen"
                   // System.out.println("Screen pushed onto top of stack"); //For debugging purposes
                    break; //Exit the loop as the screen was found
                }
                i++;//Iterate up the stack looking for the "searched for" screen
              //  System.out.println("Screen pushed onto temp stack"); //For debugging purposes
            }
        }
      //  System.out.println("Screen not already existing, cannot go to screen"); //For debugging purposes
    }

    public boolean existingScreen(String name) { //Scott Sprint 5
        for(GameScreen gameScreen : mGameScreens) { //iterate through each screen looking if the screen input, exists
            if(gameScreen.getName().compareTo(name) == 0) { //if found, return true
                //    System.out.println("found"); //Used for debugging if the screen was already existing
                return true; }
            // System.out.println("not found at current: " + gameScreen.getName()); //outputting each screen it was not found at
        } //System.out.println("not found at all"); //screen not found
        return false;
    }

    public boolean existingScreen(GameScreen screen) { //Scott Sprint 5 - Alternate check for existing screen using the gamescreen instead of the screen name.
        for(GameScreen gameScreen : mGameScreens) { //iterate through each screen looking if the screens' name input, exists
            if(gameScreen.getName().compareTo(screen.getName()) == 0) { //if found, return true
                //    System.out.println("found"); //Used for debugging if the screen was already existing
                return true; }
            // System.out.println("not found at current: " + gameScreen.getName()); //outputting each screen it was not found at
        } //System.out.println("not found at all"); //screen not found
        return false;
    }

    public boolean previousScreen(String name) { //Scott Sprint 5
        int i = 0;
        for(GameScreen gameScreen : mGameScreens) { //Checks for a screen previous (for mini menu to options menu and then back)
            i++;
            if(gameScreen.getName().compareTo(name) == 0) { //if found, return true
                if(i==mGameScreens.size()-1){
                return true; }
                break;
            }
        }
        return false;
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
     * @param name String name reference for the screen to remove.
     * @return Boolean true if the screen was removed, false otherwise (the
     * specified screen could not be found).
     */
    public boolean removeScreen(String name) {
        GameScreen screenToRemove = null;
        for(GameScreen gameScreen : mGameScreens) {
            if(gameScreen.getName().compareTo(name) == 0)
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
