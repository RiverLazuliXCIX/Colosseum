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

public class StatisticsScreen extends GameScreen {

    private PushButton backButton; // Back button to return from this screen
    private List<PushButton> pushButtons = new ArrayList<>(); // List of all push buttons
    private GameObject sBackground; // Add in the background for the statistics screen
    private LayerViewport mGameViewport;

    private static double totalWins = 0; //create a count for total number of wins
    private static double totalLosses = 0; //create a count for total number of losses
    private static String mostRecentResult = "No games played yet"; //holder for the most recent result
    private double winLossRatio = 0.0; //holder for the ratio of wins/losses
    private double winPercent = 0.0; //holder for win percentage
    private double totalGamesPlayed = 0; //create a count for total number of games played.
    private static double recentGameTime = 0.0; //most recent game time
    private static double totalGameTime = 0.0; //total game time for this session
    private boolean winStreak = false, lossStreak = false; //used to determine if the user is on a win or loss streak
    private static int ownMinionsKilled, enemyMinionsKilled; //for how many minions on their side, or enemy side have died (total).


    /**
     * Constructor for the How To Play screen
     *
     * @param game The game object

     */
    public StatisticsScreen(Game game) {
        // Set up the screen and load the assets
        super("StatsScreen", game);

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
        mGame.getAssetManager().loadAssets("txt/assets/OptionScreenAssets.JSON"); //Using similar assets in the options screen
        // Create the back button
        backButton = new PushButton(
                mGameViewport.getWidth() * 0.88f, mGameViewport.getHeight() * 0.10f,
                mGameViewport.getWidth() * 0.075f, mGameViewport.getHeight() * 0.10f,
                "BackArrow", "BackArrowSelected", this);
        pushButtons.add(backButton);

        // Load the background for the screen
        sBackground = new GameObject(mGameViewport.getWidth()/ 2.0f,
                mGameViewport.getHeight()/ 2.0f, mGameViewport.getWidth(),
                mGameViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("OptionsBackground"), this);

    }

    //Getters and setters for total wins
    public static double getTotalWins() {
        return totalWins;
    }
    public static void setTotalWins(double winInput) {
        totalWins = winInput;
    }

    //Setter for most recent time
    public static void setRecentGameTime(double recentTime) {recentGameTime = recentTime; }
    //Getter and setter for total game time
    public static double getTotalGameTime() { return totalGameTime; }
    public static void setTotalGameTime(double totalTime) { totalGameTime = totalTime; }

    //Getters and setters for minions killed
    public static int getOwnMinionsKilled() { return ownMinionsKilled; }
    public static void setOwnMinionsKilled(int ownMinionsKilled) { StatisticsScreen.ownMinionsKilled = ownMinionsKilled; }
    public static int getEnemyMinionsKilled() { return enemyMinionsKilled; }
    public static void setEnemyMinionsKilled(int enemyMinionsKilled) { StatisticsScreen.enemyMinionsKilled = enemyMinionsKilled; }

    //Getters and setters for total losses
    public static double getTotalLosses() {
        return totalLosses;
    }
    public static void setTotalLosses(double lossInput) {
        totalLosses = lossInput;
    }

    //Getters and setters for most recent results
    public static String getMostRecentResult() {
        return mostRecentResult;
    }
    public static void setMostRecentResult(String resultInput) { mostRecentResult = resultInput; }

    /**
     * Update method allowing for events on the screen to be handled
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        totalGamesPlayed = totalWins + totalLosses; //calculate the total games played

        if(totalGamesPlayed!=0) {
            winLossRatio = (double)totalWins/(double)totalGamesPlayed; //create a win/loss ratio for output later
            winPercent = winLossRatio*100.0; //create a win percentage for output later
        }


        // Process any touch events occurring since the last update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        // If there are events
        if (touchEvents.size() > 0) {
            // Update each of the push buttons
            for (PushButton button : pushButtons)
                button.update(elapsedTime);

            // If the back button is pressed, return to the previous screen
            if (backButton.isPushTriggered()) {
                mGame.getScreenManager().previousScreen(); //Calls the "previousScreen" method to return to the screen listed below this in the stack (as this screen can be reached from different screens).
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
        sBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        // Draw the back button (and any further buttons which might be added)
        for (PushButton button : pushButtons)
            button.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Draw Statistics text onto the screen
        int screenHeight = graphics2D.getSurfaceHeight();
        float textHeight = screenHeight / 30.0f;
        textPaint.setTextSize(textHeight); //create a appropriate sizing of text

        graphics2D.drawText("Statistics for games played in the current session", mGameViewport.getWidth()*1.2f, mGameViewport.getHeight()*0.6f, textPaint);
        graphics2D.drawText("Most Recent Result: ", mGameViewport.getWidth()*0.9f, mGameViewport.getHeight(), textPaint);
        graphics2D.drawText(String.valueOf(mostRecentResult), mGameViewport.getWidth()*2.3f, mGameViewport.getHeight(), textPaint); //draw the text of the most recent result
        graphics2D.drawText("Current Wins: ", mGameViewport.getWidth()*0.9f, mGameViewport.getHeight()*1.5f, textPaint);
        graphics2D.drawText(String.valueOf(totalWins), mGameViewport.getWidth()*2.3f, mGameViewport.getHeight()*1.5f, textPaint); //draw the text of the current wins count
        graphics2D.drawText("Current Losses: ", mGameViewport.getWidth()*0.9f, mGameViewport.getHeight()*2f, textPaint);
        graphics2D.drawText(String.valueOf(totalLosses), mGameViewport.getWidth()*2.3f, mGameViewport.getHeight()*2f, textPaint); //draw the text of the current losses count
        graphics2D.drawText("Win/Loss ratio: ", mGameViewport.getWidth()*0.9f, mGameViewport.getHeight()*2.5f, textPaint);
        graphics2D.drawText(String.valueOf(winLossRatio), mGameViewport.getWidth()*2.3f, mGameViewport.getHeight()*2.5f, textPaint); //draw the text of the wins-loss ratio
        graphics2D.drawText("Win Percentage: ", mGameViewport.getWidth()*0.9f, mGameViewport.getHeight()*3f, textPaint);
        graphics2D.drawText(String.valueOf(winPercent)+" %", mGameViewport.getWidth()*2.3f, mGameViewport.getHeight()*3f, textPaint); //draw the text of the win percentage
        graphics2D.drawText("Total games played: ", mGameViewport.getWidth()*0.9f, mGameViewport.getHeight()*3.5f, textPaint);
        graphics2D.drawText(String.valueOf((int)totalGamesPlayed), mGameViewport.getWidth()*2.3f, mGameViewport.getHeight()*3.5f, textPaint); //draw the text of the total games played

    }
}
