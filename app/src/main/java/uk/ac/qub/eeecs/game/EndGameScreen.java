package uk.ac.qub.eeecs.game;

import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.colosseumDemoScreen;

public class EndGameScreen extends GameScreen {

    private PushButton menuButton; // Menu button to return from this screen to the menu
    private PushButton newGameButton; // New Game button to go immediately into a new game
    private List<PushButton> pushButtons = new ArrayList<>(); // List of all push buttons
    private GameObject eBackground; // Add in the background for the end game screen
    private LayerViewport mGameViewport;

    private static String mostRecentResult = ""; //holder for the most recent result
    private static boolean coinFlipResult = false; //holder for the coin flip result
    private static boolean concedeResult = false; //holder for if the player concedes


    /**
     * Constructor for the How To Play screen
     *
     * @param game The game object

     */
    public EndGameScreen(Game game) {
        // Set up the screen and load the assets
        super("EndScreen", game);

        setupViewports();
        setUpGameObjects();

    }

    private void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    private void setUpGameObjects() {
        mGame.getAssetManager().loadAssets("txt/assets/EndGameScreenAssets.JSON"); //Using similar assets in the options screen
        // Create the back button
        menuButton = new PushButton(
                mGameViewport.getWidth() * 0.80f, mGameViewport.getHeight() * 0.10f,
                mGameViewport.getWidth() * 0.125f, mGameViewport.getHeight() * 0.15f,
                "MainMenu", "MainMenuSelected", this);
        pushButtons.add(menuButton);

        newGameButton = new PushButton(
                mGameViewport.getWidth() * 0.2f, mGameViewport.getHeight() * 0.10f,
                mGameViewport.getWidth() * 0.125f, mGameViewport.getHeight() * 0.15f,
                "playAnother", "playAnotherSelected", this);
        pushButtons.add(newGameButton);

        // Load the background for the screen
        eBackground = new GameObject(mGameViewport.getWidth()/ 2.0f,
                mGameViewport.getHeight()/ 2.0f, mGameViewport.getWidth(),
                mGameViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("OptionsBackground"), this);

    }

    //Getters and setters for most recent results
    public static String getMostRecentResult() {
        return mostRecentResult;
    }
    public static void setMostRecentResult(String resultInput) { mostRecentResult = resultInput; }

    //Getters and setters for coin flip result
    public static boolean getCoinFlipResult() {
        return coinFlipResult;
    }
    public static void setCoinFlipResult(boolean cResultInput) { coinFlipResult = cResultInput; }

    public static boolean getConcedeResult() {
        return concedeResult;
    }

    public static void setConcedeResult(boolean concedeResult) {
        EndGameScreen.concedeResult = concedeResult;
    }

    private void addStatstics() {
        double i = StatisticsScreen.getTotalLosses();
        double j = StatisticsScreen.getTotalWins();

        if(mostRecentResult=="win") {
            StatisticsScreen.setMostRecentResult("Win");
            StatisticsScreen.setTotalWins(j+1);
        } else if(mostRecentResult=="loss") {
            StatisticsScreen.setMostRecentResult("Loss");
            StatisticsScreen.setTotalLosses(i+1);
        } else if(mostRecentResult=="draw") {
            StatisticsScreen.setMostRecentResult("Draw");
            StatisticsScreen.setTotalWins(j+0.5);
            StatisticsScreen.setTotalLosses(i+0.5);
        }
    }

    /**
     * Update method allowing for events on the screen to be handled
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {


        // Process any touch events occurring since the last update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        // If there are events
        if (touchEvents.size() > 0) {
            // Update each of the push buttons
            for (PushButton button : pushButtons)
                button.update(elapsedTime);

            // If the back button is pressed, return to the previous screen
            if (menuButton.isPushTriggered()) {
                //return to main menu
                addStatstics();
                mGame.getScreenManager().removeScreen("CardScreen");
                mGame.getScreenManager().changeScreenButton(new MenuScreen(mGame));
            }
            if(newGameButton.isPushTriggered()) {
                addStatstics();
                mGame.getScreenManager().removeScreen("CardScreen");
                mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mGame));
            }
        }
    }

    private Paint textPaint = new Paint();

    /**
     * Draw method to allow for objects to be drawn onto the screen
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.WHITE);

        // Draw the background onto the screen
        eBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        // Draw the back button (and any further buttons which might be added)
        for (PushButton button : pushButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Draw Statistics text onto the screen
        int screenHeight = graphics2D.getSurfaceHeight();
        float textHeight = screenHeight / 30.0f;
        textPaint.setTextSize(textHeight); //create a appropriate sizing of text

        graphics2D.drawText("Your game has finished", mGameViewport.getWidth()*1.6f, mGameViewport.getHeight()*0.6f, textPaint);
        graphics2D.drawText("You achieved a " + (String.valueOf(mostRecentResult)), mGameViewport.getWidth()*1.68f, mGameViewport.getHeight()*1.3f, textPaint);
        if(mostRecentResult=="win") {
            graphics2D.drawText("Congratulations on your win!", mGameViewport.getWidth()*1.53f, mGameViewport.getHeight()*2.0f, textPaint);
        } else if(mostRecentResult=="loss") {
            if(concedeResult){
                graphics2D.drawText("You conceded your last game, better luck next time!", mGameViewport.getWidth()*1.2f, mGameViewport.getHeight()*2.0f, textPaint);
            } else {
                graphics2D.drawText("Unfortunate result, good luck next time!", mGameViewport.getWidth() * 1.35f, mGameViewport.getHeight() * 2.0f, textPaint);
            }
        } else if(mostRecentResult=="draw"){
            graphics2D.drawText("At least you didn't lose!", mGameViewport.getWidth()*1.63f, mGameViewport.getHeight()*2.0f, textPaint);
        }
        if(coinFlipResult) {
            graphics2D.drawText("You have got extremely lucky and won with the coin flip landing on it's edge! Congratulations!", mGameViewport.getWidth()*0.5f, mGameViewport.getHeight()*2.5f, textPaint);
        }
        graphics2D.drawText("Your statistics screen has been updated.", mGameViewport.getWidth()*1.34f, mGameViewport.getHeight()*3.0f, textPaint);

    }
}
