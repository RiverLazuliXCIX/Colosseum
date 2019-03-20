package uk.ac.qub.eeecs.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.Colosseum.FatigueCounter;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.Turn;
import uk.ac.qub.eeecs.game.Colosseum.UserWhoStarts;


public class colosseumDemoScreen extends GameScreen {

    //////////////////
    //  PROPERTIES  //
    //////////////////
    private LayerViewport mGameViewport;
    private Input mInput;

    //Define the background board
    private GameObject mGameBackground;

    //Array List to hold the Push Buttons
    private List<PushButton> mButtons = new ArrayList<>();

    //Push buttons for ending player's turn, for pausing the game, and discarding a card:
    private PushButton mEndTurnButton, mEndTurnButtonOff, mPauseButton, mDiscardButton;

    //Turn object that stores all data about the current turn:
    private Turn mCurrentTurn = new Turn();

    //FatigueCounter objects store all data about what fatigue the player/enemy should take:
    private FatigueCounter mPlayerFatigue = new FatigueCounter(), mEnemyFatigue = new FatigueCounter();

    //Define the Player
    private Player mPlayer;

    // Define the Opponent
    private AIOpponent mOpponent;

    //Define the two Decks
    private CardDeck mPlayerDeck, mEnemyDeck;

    //UserWhoStarted variable to hold data about who started in this match:
    private UserWhoStarts mUserWhoStarts;

    //Variables required for the Game Timer:
    private long startTime = 0, pauseTime = 0, pauseTimeTotal = 0; //Setting up variables to hold times of the game
    private static boolean wasPaused = false;

    //Variables required for the Enemy Turn Timer:
    private final long ENEMY_TURN_TIME = 5000;
    private long mEnemyTurnBegins, mCurrentTime;

    private boolean startTimeRecorded = false;

    //Paint items that will be used to draw text
    private Paint mText;
    private Paint textPaint = new Paint();

    //Information needed to set Music/SFX/FPS Preferences:
    private Context mContext = mGame.getActivity();
    private SharedPreferences mGetPreference = PreferenceManager.getDefaultSharedPreferences(mContext);

    //Set up FPS counter:
    private FPSCounter fpsCounter;

    //Array List to hold the GameObjects:
    private List<GameObject> mGameObjs = new ArrayList<>();

    //Game objects being set up:
    private GameObject pDenarius, eDenarius, mPlayerDeckImg, mEnemyDeckImg;

    //Test regions being set up:
    ActiveRegion playerActiveRegion, opponentActiveRegion;
    HandRegion playerHandRegion, opponentHandRegion;

    //////////////////
    // CONSTRUCTOR  //
    //////////////////

    public colosseumDemoScreen(Player player, AIOpponent opponent, Turn currentTurn, UserWhoStarts starter, long EnemyTurnBegins, CardDeck playerDeck, CardDeck enemyDeck, Game game) {
        super("CardScreen", game);
        setUpViewports();
        setUpGameObjects();
        setUpButtons();
        setUpRegions();

        //Get data from the CoinTossScreen:
        this.mPlayer = player;
        this.mOpponent = opponent;
        this.mCurrentTurn = currentTurn;
        this.mUserWhoStarts = starter;
        this.mEnemyTurnBegins = EnemyTurnBegins;
        this.mPlayerDeck = playerDeck;
        this.mEnemyDeck = enemyDeck;

        //Shuffle two decks:
        playerDeck.shuffleCards();
        enemyDeck.shuffleCards();
    }

    ///////////////
    //  METHODS  //
    ///////////////

    private void setUpGameObjects() {
        // Create the background
        Bitmap mBackgroundBitmap = getGame()
                .getAssetManager().getBitmap("ArenaFloor");

        mGameBackground = new GameObject(mDefaultLayerViewport.getWidth() / 2.0f,
                mDefaultLayerViewport.getHeight() / 2.0f, mDefaultLayerViewport.getWidth(),
                mDefaultLayerViewport.getHeight(), mBackgroundBitmap, this);

        //Initialise Paint object for drawing text onscreen:
        mText = new Paint();
        int screenHeight = mDefaultScreenViewport.height;
        float textHeight = screenHeight / 28.0f;
        mText.setTextSize(textHeight);
        mText.setColor(Color.rgb(255, 255, 255));
        mText.setTypeface(Typeface.create("Arial", Typeface.BOLD));

        //Setting up FPS counter:
        fpsCounter = new FPSCounter(mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f, this) {};

        //Setting up demo player:
        mPlayer = new Player(this, "Meridia");
        mOpponent = new AIOpponent(this, "EmperorCommodus");

        //Spacing that will be used to position the objects:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        //Create denarius objects
        Bitmap denarius = getGame().getAssetManager().getBitmap("Denarius");
        pDenarius = new GameObject(spacingX * 3.7f, spacingY * 0.2f, 30, 30, denarius, this);
        mGameObjs.add(pDenarius);
        eDenarius = new GameObject(spacingX * 3.7f, spacingY * 2.79f, 30, 30, denarius, this);
        mGameObjs.add(eDenarius);

        //Set up deck images - Dearbhaile
        Bitmap deckImg = getGame().getAssetManager().getBitmap("CardDeckImg");
        mPlayerDeckImg = new GameObject(spacingX * 0.4f, spacingY * 0.4f, spacingX * 0.6f, spacingY * 0.6f, deckImg, this);
        mGameObjs.add(mPlayerDeckImg);
        mEnemyDeckImg = new GameObject(spacingX * 0.4f, spacingY * 2.2f, spacingX * 0.6f, spacingY * 0.6f, deckImg, this);
        mGameObjs.add(mEnemyDeckImg);
    }

    private void setUpViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    public void setUpRegions() {
        //Defining playable region width and height ( 50.0f/1.5f is the width of the cards)
        playerActiveRegion = new ActiveRegion(mDefaultLayerViewport.getLeft() + 25.0f, mDefaultLayerViewport.getRight() - 25.0f, mDefaultLayerViewport.getTop() / 2.0f, mDefaultLayerViewport.getBottom() + mPlayer.position.y + (mPlayer.getPortraitHeight() / 2));
        opponentActiveRegion = new ActiveRegion(mDefaultLayerViewport.getLeft() + 25.0f, mDefaultLayerViewport.getRight() - 25.0f, mDefaultLayerViewport.getTop() - (mPlayer.position.y + (mPlayer.getPortraitHeight() / 2)), mDefaultLayerViewport.getTop() / 2.0f);
        playerHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mPlayer.position.y - (mPlayer.getPortraitHeight() / 2), mDefaultLayerViewport.getBottom());
        opponentHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getTop(), mOpponent.position.y + (mOpponent.getPortraitHeight() / 2));
    }

    public void setUpButtons() {
        //Spacing that will be used to position the buttons:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        mEndTurnButton = new PushButton(
                spacingX * 4.5f, spacingY * 1.5f, spacingX * 0.5f, spacingY * 0.5f,
                "EndTurn", this);

        mEndTurnButtonOff = new PushButton(
                spacingX * 4.5f, spacingY * 1.5f, spacingX * 0.5f, spacingY * 0.5f,
                "EndTurn2", this);

        mDiscardButton = new PushButton(
                spacingX * 4.5f, spacingY * 1.0f, spacingX * 0.5f, spacingY * 0.3f,
                "DiscardButton", "DiscardButton_Selected", this);
        mButtons.add(mDiscardButton);

        mPauseButton = new PushButton(
                spacingX * 4.7f, spacingY * 2.7f, spacingX * 0.4f, spacingY * 0.4f,
                "Cog", "CogSelected", this);
        mButtons.add(mPauseButton);
    }

    //Method used to draw each card hand and avoid redundant code. - Dearbhaile
    public void drawCardHand(CardDeck deckRequired, ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (int i = 0; i < deckRequired.getmCardHand().size(); i++) {
            deckRequired.getmCardHand().get(i).draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }
    }

    //Methods relating to stopping and starting turns - Dearbhaile
    //If enemy started, then turns increase every time enemy takes new turn.
    public void endPlayerTurn() {
        mPlayer.setYourTurn(false); // Set Player turn to false
        mOpponent.setYourTurn(true); // Set Opponent turn to true
        mEnemyTurnBegins = System.currentTimeMillis(); // Start timing enemy's turn
        mEnemyDeck.drawCard(mOpponent, mEnemyFatigue, mGame); // Draw card to enemy deck
        if (mUserWhoStarts == UserWhoStarts.ENEMYSTARTS) { //If opponent is starting player,
            mCurrentTurn.newTurnFunc(mPlayer, mOpponent); //Then increment turn number
        }
    }

    //This method checks if 5 seconds have elapsed since enemy turn began
    //If yes, then it triggers player turn to begin again.
    //If player started, then turns increase every time player takes new turn. - Dearbhaile
    public void checkIfEnemysTurn() {
        if (mCurrentTime - mEnemyTurnBegins >= ENEMY_TURN_TIME) { // Current time is constantly being updated in Update method
            mPlayer.setYourTurn(true); // If enemy turn over, set Player turn to true
            mOpponent.setYourTurn(false); // Set Opponent turn to false
            mPlayerDeck.drawCard(mPlayer, mPlayerFatigue, mGame); // Player draws card
            if (mUserWhoStarts == UserWhoStarts.PLAYERSTARTS) { // If player is starting player,
                mCurrentTurn.newTurnFunc(mPlayer, mOpponent); //Then increment turn number.
            }
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (startTimeRecorded == false) {
            startTime = System.currentTimeMillis(); //Start recording the game's start time
            startTimeRecorded = true; //Mark startTimeRecorded as true, so it will not run again.
        }

        //Both decks should constantly be checking for dead cards, ie cards where health <= 0
        //When discovered in either deck, they will be discarded immediately. - Dearbhaile
        mPlayerDeck.checkForDeadCards();
        mEnemyDeck.checkForDeadCards();

        //Current time should constantly be collected, for use when counting enemy's turn time - Dearbhaile
        mCurrentTime = System.currentTimeMillis();

        if (mOpponent.getYourTurn())
            checkIfEnemysTurn(); //If opponent's turn, check when it ends - Dearbhaile

        if (wasPaused) {
            wasPaused = false; //If the game was paused, gather the total time it was paused for - Scott
            pauseTimeTotal += System.currentTimeMillis() - pauseTime;//gather a total paused time, in the case of a user pausing multiple times
        }

        mInput = mGame.getInput(); //Process any touch events occurring since the update

        if (mPlayer.getYourTurn()) { //Player's cards can be dragged when it is their turn, otherwise they cannot - Dearbhaile
            for (Card cards : mPlayerDeck.getmCardHand())
                cards.cardEvents(mPlayerDeck.getmCardHand(), mDefaultScreenViewport, mDefaultLayerViewport, mGame);
        }

        //Temporary: Enemy cards made draggable for testing purposes. TODO: Remove this.
        for (Card cards : mEnemyDeck.getmCardHand())
            cards.cardEvents(mEnemyDeck.getmCardHand(), mDefaultScreenViewport, mDefaultLayerViewport, mGame);

        mPlayer.update(elapsedTime); //Update player stats - Kyle
        mOpponent.update(elapsedTime); //Update opponent stats

        //'EndGameScreen' code - Scott
        if (EndGameScreen.getCoinFlipResult()) { //If the coin flip was on the edge, win the game go to next end game screen
            try {
                Thread.sleep(1000); //Allows player to see when they have won rather than immediately jumping
            } catch (InterruptedException e) { }

            EndGameScreen.setTimePlayed((System.currentTimeMillis() - startTime) - pauseTimeTotal); //Allow for a "time played" statistic
            EndGameScreen.setMostRecentResult("win"); //Record the result
            mGame.getScreenManager().changeScreenButton(new EndGameScreen(mGame));

        } else if (mPlayer.getCurrentHealth() <= 0 || mOpponent.getCurrentHealth() <= 0) { //if either of the health is below 0 enter the if statement
            try {
                Thread.sleep(1000); //Allows player to see when they have won rather than immediately jumping
            } catch (InterruptedException e) { }

            if (mPlayer.getCurrentHealth() <= 0 && mOpponent.getCurrentHealth() <= 0) //if both sides health is 0 or less, the game ends in a draw
                EndGameScreen.setMostRecentResult("draw"); //Record the result
            else if (mPlayer.getCurrentHealth() <= 0) //if the player reaches 0 or less health, they lose
                EndGameScreen.setMostRecentResult("loss"); //Record the result
            else if (mOpponent.getCurrentHealth() <= 0) //if the opponent reaches 0 or less health, the player wins
                EndGameScreen.setMostRecentResult("win"); //Record the result

            EndGameScreen.setTimePlayed((System.currentTimeMillis() - startTime) - pauseTimeTotal); //Allow for a "time played" statistic
            mGame.getScreenManager().changeScreenButton(new EndGameScreen(mGame)); //swap to the end game screen regardless of whatever outcome occurs

        } else {
            List<TouchEvent> touchEvents = mInput.getTouchEvents();
            if (touchEvents.size() > 0) {

                //This next for loop is to prevent the player's cards from slotting into the opponent's card slots - Diarmuid Toal
                for (int i = 0; i < mPlayerDeck.getmCardHand().size(); i++) {
                    // Updates both regions for all cards - Kyle
                    playerActiveRegion.update(mPlayerDeck.getmCardHand().get(i));
                    playerHandRegion.update(mPlayerDeck.getmCardHand().get(i));
                }

                //This next for loop is to prevent the opponent's cards from slotting into the player's card slots - Diarmuid Toal
                for (int i = 0; i < mEnemyDeck.getmCardHand().size(); i++) {
                    //enemyDeck.getmCardHand().get(i).cardEvents(enemyDeck.getmCardHand(), mDefaultScreenViewport, mGameViewport, mGame);

                    // Updates both regions for all cards
                    opponentActiveRegion.update(mEnemyDeck.getmCardHand().get(i));
                    opponentHandRegion.update(mEnemyDeck.getmCardHand().get(i));
                }

                //Update all buttons:
                for (PushButton button : mButtons)
                    button.update(elapsedTime);

                if (mPauseButton.isPushTriggered()) {
                    pauseTime = System.currentTimeMillis(); //gather the current time when the game is being paused
                    wasPaused = true; //allow for a check when the game is next active, to calculate pause time.
                    EndGameScreen.setTimePlayed((System.currentTimeMillis() - startTime) - pauseTimeTotal); //Allow for a "time played" statistic incase of a "concede"
                    mGame.getScreenManager().changeScreenButton(new PauseMenuScreen(mGame));
                }

                mEndTurnButton.update(elapsedTime);
                mEndTurnButtonOff.update(elapsedTime);

                if (mEndTurnButton.isPushTriggered() && mPlayer.getYourTurn()) {
                    mPlayerDeck.discardCards_EndOfTurn();
                    endPlayerTurn();
                }

                if (mDiscardButton.isPushTriggered()) { //Calls discard function if there is a card selected and discard button pressed - Dearbhaile
                    for (int i = 0; i < mPlayerDeck.getmCardHand().size(); i++) {
                        if (mPlayerDeck.getmCardHand().get(i).getmIsSelected()) {
                            Card mCardToDiscard = mPlayerDeck.getmCardHand().get(i);
                            mCardToDiscard.discardCard(mCardToDiscard);
                        }
                    }
                }
            }
        }
    }

    //////////////////////////////
    //       DRAW METHODS       //
    //////////////////////////////
    public void drawPlayers(int spacingX, int spacingY, ElapsedTime elapsedTime,
                            IGraphics2D graphics2D, Player p, CardDeck deck, float ySpacing) {
        //Method for drawing all player and enemy stats/hero images - Sean
        //Draw player portrait
        p.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        //Draw player mana text
        graphics2D.drawText(p.getCurrentMana() + "/" + p.getCurrentManaCap(),
                spacingX * 14.5f, spacingY * (ySpacing + 0.4f), mText);

        //Draw player card stats
        int cardsLeft = deck.getDeck().size();
        int cardsHand = deck.getmCardHand().size();
        int cardsDead = deck.getmDiscardPile().size(); // All stats accurate - Dearbhaile
        graphics2D.drawText("Deck: " + cardsLeft, spacingX * 3.6f,
                spacingY * ySpacing, mText);
        graphics2D.drawText("Hand: " + cardsHand, spacingX * 3.6f,
                spacingY * (ySpacing + 0.4f), mText);
        graphics2D.drawText("Graveyard: " + cardsDead, spacingX * 3.6f,
                spacingY * (ySpacing + 0.8f), mText);

        //Draw player hand  - Dearbhaile
        drawCardHand(deck, elapsedTime, graphics2D);
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        // Clear the screen
        graphics2D.clear(Color.WHITE);
        graphics2D.clipRect(mDefaultScreenViewport.toRect());

        // Draw the background first of all
        mGameBackground.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        // Draws region boundaries for player
        playerActiveRegion.drawRegion(graphics2D, this);
        playerHandRegion.drawRegion(graphics2D, this);

        // Draws region boundaries for opponent
        opponentActiveRegion.drawRegion(graphics2D, this);
        opponentHandRegion.drawRegion(graphics2D, this);

        //Spacing that will be used to position everything:
        int spacingX = (int) mDefaultLayerViewport.getWidth() / 5;
        int spacingY = (int) mDefaultLayerViewport.getHeight() / 3;

        //Draw turn number - Dearbhaile
        graphics2D.drawText("Turn #" + mCurrentTurn.getmTurnNum(), spacingX * 1.0f, spacingY * 0.6f, mText);

        //Draw 'End Turn' button onscreen, which toggles between pressable and not pressable image - Dearbhaile
        if (mPlayer.getYourTurn())
            mEndTurnButton.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        else
            mEndTurnButtonOff.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        //Draw the remainder of the buttons:
        for (PushButton buttons : mButtons) {
            buttons.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        if (mGetPreference.getBoolean("FPS", true)) { //If player has switched FPS counter on
            fpsCounter.draw(elapsedTime, graphics2D); //Draw FPS counter onscreen
        }

        for (GameObject gObject : mGameObjs) { //Draw all game objects
            gObject.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        float statPlayerYSpacing = 11.0f; //PLAYER STATS BEING DRAWN:
        drawPlayers(spacingX, spacingY, elapsedTime, graphics2D, mPlayer, mPlayerDeck, statPlayerYSpacing);

        float statOpponentYSpacing = 0.6f; //OPPONENT STATS BEING DRAWN:
        drawPlayers(spacingX, spacingY, elapsedTime, graphics2D, mOpponent, mEnemyDeck, statOpponentYSpacing);

        if (CoinTossScreen.getEdgeCase()) {
            int screenHeight = graphics2D.getSurfaceHeight();
            float textHeight = screenHeight / 30.0f;
            textPaint.setTextSize(textHeight); //create a appropriate sizing of text
            graphics2D.drawText("Iterations to reach Edge Case:", 100.0f, 50.0f, textPaint); //draw the text "Iterations to reach Edge Case:"
            graphics2D.drawText(String.valueOf(CoinTossScreen.getEdgeCounter()), 100.0f, 100.0f, textPaint); //Output the number of iterations.
        }
    }

    ///////////////////////////
    //  GETTERS AND SETTERS  //
    ///////////////////////////
    public static void setWasPaused(boolean pauseInput) { wasPaused = pauseInput; }
    public UserWhoStarts getUserWhoStarts() { return this.mUserWhoStarts; }
    public ActiveRegion getPlayerActiveRegion() { return this.playerActiveRegion; }
    public ActiveRegion getOpponentActiveRegion() { return this.opponentActiveRegion; }
}