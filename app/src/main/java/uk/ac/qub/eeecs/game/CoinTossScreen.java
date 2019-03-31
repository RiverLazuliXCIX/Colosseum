package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.ScreenManager;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.ui.TitleImage;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.Coin;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.Turn;
import uk.ac.qub.eeecs.game.Colosseum.UserWhoStarts;

//CoinTossScreen, coded by Dearbhaile Walsh & Scott Barham
public class CoinTossScreen extends GameScreen {

    //Different objects required for this screen to function
    private GameObject mCTSBackground;
    private LayerViewport mGameViewport;
    private TitleImage mCoinTossTitle;
    private FPSCounter fpsCounter;

    //Variables required for the time delay on this screen:
    private long mCoinToss_Timeout = 10000;
    private long mTimeOnCreate, mCurrentTime;
    private long mTimeRemaining;

    //Create PushButton necessary to skip animation
    PushButton mSkipButton;

    //Turn object that stores all data about the current turn:
    private Turn mCurrentTurn = new Turn();

    //Define the Player
    private Player mPlayer;

    // Define the Opponent
    private AIOpponent mOpponent;

    // Used to store the selected heroes from the hero select screen, used in initialising the player/opponent
    private String mPlayerHero, mOpponentHero;

    //Define the two Decks
    private CardDeck mPlayerDeck, mEnemyDeck;

    // Defining hand regions (needs to be passed into colosseum screen now to prevent duplicate hand regions)
    private HandRegion mPlayerHandRegion, mOpponentHandRegion;

    //UserWhoStarted variable to hold data about who started in this match:
    private UserWhoStarts mUserWhoStarts;

    //Variable to store time of enemy turn beginning (if applicable):
    private long mEnemyTurnBegins = 0;

    //Variables required for the message (lines 1 and 2) to display properly
    private String mCoinTossResult = "";
    private String mCoinTossMsg1 = "";
    private String mCoinTossMsg2 = "";

    //Paint items that will be used to draw text
    private Paint mMessageText;
    private Paint mTimerText;

    //Information needed to set Music/SFX/FPS Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);

    //Create instance of Coin object:
    private Coin mCoin;

    //'Edge case' coin toss variables:
    protected static int edgeCounter = 0; //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
    protected static boolean edgeCase = false;

    // Constructor
    //Create the 'CoinTossScreen' screen
    public CoinTossScreen(Game game, String playerHero, String opponentHero) {
        super("CoinTossScreen", game);
        mPlayerHero = playerHero;
        mOpponentHero = opponentHero;
        mTimeOnCreate = System.currentTimeMillis();
        setupViewports();
        setUpCTSObjects();
        setUpGameObjects();
        mCoinTossResult = coinFlipStart();
        coinFlipResult(mCoinTossResult);

        //Set up Coin object for display in animation, has to be initialised after coinFlipStart otherwise it was never changed - Scott
        mCoin = new Coin( mDefaultLayerViewport.getRight() / 2.f, mDefaultLayerViewport.getTop() / 2.f,100.0f,100.0f, this, getmCoinTossResult());

        chooseTextToDisplay();
    }

    public void setUpGameObjects() {
        //This class acts as a loader class for the colosseumDemoScreen:
        mGame.getAssetManager().loadAssets("txt/assets/ColosseumAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/CardAssets.JSON");

        if (edgeCase) { //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
            edgeCaseTest();
        }

        //Setting up demo player:
        mPlayer = new Player(this, mPlayerHero);
        mOpponent = new AIOpponent(this, mOpponentHero);

        //Set up initial PLAYER and ENEMY stats:
        mPlayer.setCurrentMana(1);
        mPlayer.setCurrentManaCap(1);
        mOpponent.setCurrentMana(1);
        mOpponent.setCurrentManaCap(1);

        //Set up Hand Regions to be passed in to new screen:
        mPlayerHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mPlayer.position.y - (mPlayer.getPortraitHeight() / 2), mDefaultLayerViewport.getBottom());
        mOpponentHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getTop(), mOpponent.position.y + (mOpponent.getPortraitHeight() / 2));

        //This method sets up the player and enemy decks, called when screen is loaded
        mPlayerDeck = new CardDeck(1, "Basic Player Deck", this, false, mPlayerHandRegion);
        mEnemyDeck = new CardDeck(2, "Basic Enemy Deck", this, true, mOpponentHandRegion);

        for (int i = 0; i < mEnemyDeck.getmCardHand().size(); i++) {
            mEnemyDeck.getmCardHand().get(i).flipCard(); //Enemy cards are initially flipped
        }
    }

    public void setUpCTSObjects() {
        mGame.getAssetManager().loadAssets("txt/assets/CoinTossAssets.JSON");

        //Background game object is created:
        mCTSBackground = new GameObject(mGameViewport.getWidth()/ 2.0f,
                mGameViewport.getHeight()/ 2.0f, mGameViewport.getWidth(),
                mGameViewport.getHeight(), getGame()
                .getAssetManager().getBitmap("CTSBackground"), this);

        //Set up the FPS counter:
        fpsCounter = new FPSCounter( mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f , this) {};

        // Spacing that will be used to position the Coin Toss Screen Objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        // Create the title image
        mCoinTossTitle = new TitleImage(mDefaultLayerViewport.getWidth() / 2.0f, spacingY * 2.5f, spacingX*1.5f, spacingY/2.2f, "CTSTitle",this);

        //Create the Skip button
        mSkipButton = new PushButton(spacingX * 3.9f, spacingY * 2.5f, spacingX*0.8f, spacingY*0.8f,
                "SkipArrow", this);

        //PAINT OBJECTS:
        //Initialise Paint Objects I will use to draw text
        mMessageText = new Paint();
        int screenHeight = mDefaultScreenViewport.height;
        float textHeight = screenHeight / 20.0f;
        mMessageText.setTextSize(textHeight);
        mMessageText.setColor(Color.BLACK);
        mMessageText.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        mTimerText = new Paint();
        float smallTextHeight = screenHeight / 24.0f;
        mTimerText.setTextSize(smallTextHeight);
        mTimerText.setColor(Color.BLACK);
        mTimerText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
    }

    public void setupViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    private void edgeCaseTest() { //Testing for the edge case scenario of the coin flip, User Story 18.1, Sprint 4 - Scott
        boolean i = true;
        while (i) {
            edgeCounter++;
            switch (coinFlipStart()) {
                case "Tails"://Tails - player starts
                case "Heads": //Heads - ai starts
                default: //Shouldn't be reached
                    break;
                case "Edge": //Edge of coin - set opponent health to 0, auto win game.
                    i = false;
                    break;
            }
        }
    }

    private String coinFlipStart() { //Initialise the coin flip for a random result - Scott
        Random RANDOM = new Random();
        int flip = RANDOM.nextInt(6001);
        if (flip == 6000) { //side of coin (1/6000 chance to auto-win)
            return "Edge";
        } else if (flip >= 3000 && flip < 6000) { //tails (ai starts)
            return "Tails";
        } else if (flip >= 0 && flip < 3000) { //heads (user starts)
            return "Heads";
        }
        return "Fail";
    }

    //CODE HIGHLIGHT 4 - 'COINTOSS SCREEN' - Dearbhaile
    // Method for setting up stats based on Coin Toss:
    private void coinFlipResult(String result) {
        switch (result) {
            case "Heads": // ie, player starts - Dearbhaile
                mCurrentTurn.setUpStats_PlayerStarts(mPlayer, mPlayerDeck, mOpponent, mEnemyDeck);
                mUserWhoStarts = UserWhoStarts.PLAYERSTARTS;
                break;
            case "Tails": // ie, AI starts - Dearbhaile
                mCurrentTurn.setUpStats_EnemyStarts(mPlayer, mPlayerDeck, mOpponent, mEnemyDeck);
                mUserWhoStarts = UserWhoStarts.ENEMYSTARTS;
                break;
            case "Edge": //Edge of coin: opponent health to 0, auto win game - Scott
                EndGameScreen.setCoinFlipResult(true);
                break;
        }
    }

    //NOT THIS
    private void changeScreens() { //Method to remove the current screen and move to the main game screen
        mGame.getScreenManager().removeScreen(CoinTossScreen.this);
        mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mPlayer, mOpponent, mCurrentTurn,
                mUserWhoStarts, mEnemyTurnBegins, mPlayerDeck, mEnemyDeck,mPlayerHandRegion,mOpponentHandRegion, mGame));
    }

    //CODE HIGHLIGHT 4 CONTINUES - Dearbhaile
    public void chooseTextToDisplay() { //- Dearbhaile
        if (mCoinTossResult == "Heads") {
            mCoinTossMsg1 = "The coin landed on heads! You get to play first.";
            mCoinTossMsg2 = "The other player draws 4 cards, and gets 1 additional mana.";
        }
        else if (mCoinTossResult == "Tails") {
            mCoinTossMsg1 = "The coin landed on tails! The enemy plays first.";
            mCoinTossMsg2 = "You draw an extra card and additional mana for your troubles.";
        }
        else if (mCoinTossResult == "Edge") {
            mCoinTossMsg1 = "The coin landed on its edge!";
            mCoinTossMsg2 = "You automatically win the game for being lucky!";
        }
    }
    //END OF CODE HIGHLIGHT 4

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        mCurrentTime = System.currentTimeMillis();
        mTimeRemaining = 10 - ((mCurrentTime - mTimeOnCreate)/1000);

        if(colosseumDemoScreen.wasPaused()) { //If the player used the "main menu" button from inside a game, allow them to resume instantly. Scott
            changeScreens();
        }

        if (!mCoin.isComplete()) { //If the coin animation isnt complete, continue updating the animation until it completes. - Scott
            mCoin.coinAnimation();
        }

        //Timer checks how long screen has been running, and changes screen after 10 seconds - Dearbhaile
        if (mCurrentTime - mTimeOnCreate >= mCoinToss_Timeout) {
            if (mOpponent.getYourTurn()) //If opponent starts, then start counting their turn time, so that it ends after 5secs. - Dearbhaile
                mEnemyTurnBegins = System.currentTimeMillis();
            changeScreens(); //Call the change screens method to change to the main game screen.
        }

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            mSkipButton.update(elapsedTime);

            //If the 'skip animation' button is pressed, then go straight to game:
            if (mSkipButton.isPushTriggered()) { //Dearbhaile
                mCoinToss_Timeout = 0;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen
        graphics2D.clear(Color.WHITE);

        //Draw the background
        mCTSBackground.draw(elapsedTime, graphics2D, mDefaultLayerViewport,
                mDefaultScreenViewport);

        //Draw the title image
        mCoinTossTitle.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        //Draw the skip button
        mSkipButton.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        // Spacing that will be used to position the Paint object:
        float SCREEN_WIDTH = mGame.getScreenWidth();
        float SCREEN_HEIGHT = mGame.getScreenWidth();

        //Draw the coin sprite, used for the coin animation - Scott
        mCoin.draw(elapsedTime, graphics2D, mDefaultLayerViewport, mDefaultScreenViewport);

        if (mCurrentTime - mTimeOnCreate >= 4000) { //Display message according to which outcome user received. - Dearbhaile
            graphics2D.drawText(mCoinTossMsg1, SCREEN_WIDTH * 0.24f, SCREEN_HEIGHT * 0.42f, mMessageText);
            graphics2D.drawText(mCoinTossMsg2, SCREEN_WIDTH * 0.18f, SCREEN_HEIGHT * 0.48f, mMessageText);

            graphics2D.drawText("Game will begin in " + mTimeRemaining + " seconds...", SCREEN_WIDTH * 0.46f, SCREEN_HEIGHT * 0.52f, mTimerText);
        }

        if(mGetPreference.getBoolean("FPS", true)) {
            fpsCounter.draw(elapsedTime, graphics2D);
        }
    }

    //Getters and setters:
    public static boolean getEdgeCase() { return edgeCase; }
    public static void setEdgeCase(boolean edgeCaseInput) { edgeCase = edgeCaseInput; }
    public static int getEdgeCounter() { return edgeCounter; }
    public String getmCoinTossResult() { return this.mCoinTossResult; }
    public String getmCoinTossMsg1() { return this.mCoinTossMsg1; }
    public String getmCoinTossMsg2() { return this.mCoinTossMsg2; }
    public Player getmPlayer() { return this.mPlayer; }
    public AIOpponent getmOpponent() { return this.mOpponent; }
    public CardDeck getmPlayerDeck() { return this.mPlayerDeck; }
    public CardDeck getmEnemyDeck() { return this.mEnemyDeck; }
    public UserWhoStarts getmUserWhoStarts() {return this.mUserWhoStarts; }

}