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
import java.util.Random;

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
import uk.ac.qub.eeecs.game.Colosseum.UserWhoStarted;


public class colosseumDemoScreen extends GameScreen {

    ///////////////////////////////////////////////////////////////////////////
    // Properties
    ///////////////////////////////////////////////////////////////////////////
    private LayerViewport mGameViewport;
    private Input mInput;
    private static final Random RANDOM = new Random();

    //Define the background board
    private GameObject mGameBackground;

    //Array List to hold the Push Buttons
    private List<PushButton> mButtons = new ArrayList<>();

    //Push buttons for ending player's turn, for pausing the game, and discarding a card:
    private PushButton mEndTurnButton, mEndTurnButtonOff, mPauseButton, mDiscardButton;

    //Turn object that stores all data about the current turn:
    private Turn mCurrentTurn = new Turn();

    //FatigueCounter objects store all data about what fatigue the player/enemy should take:
    private FatigueCounter mPlayerFatigue = new FatigueCounter();
    private FatigueCounter mEnemyFatigue = new FatigueCounter();

    //Define the Player
    private Player p2;

    // Define the Opponent
    private AIOpponent opponent;

    //Define the two Decks
    private CardDeck playerDeck, enemyDeck;

    //Set up a boolean value for whether or not coin flip is finished
    private boolean coinFlipDone;

    //Set up an int value to hold the outcome of the coin toss
    private int mCoinTossResult;

    //UserWhoStarted variable to hold data about who started in this match:
    private UserWhoStarted userWhoStarted;

    //'Edge case' coin toss variables:
    protected int edgeCounter = 0; //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
    protected static boolean edgeCase = false;

    //Variables required for the Game Timer:
    private long startTime = 0, pauseTime = 0, pauseTimeTotal = 0; //Setting up variables to hold times of the game
    private static boolean wasPaused = false;

    //Variables required for the Enemy Turn Timer:
    private final long ENEMY_TURN_TIME = 5000;
    private long mEnemyTurnBegins, mCurrentTime;

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

    // Constructors
    public colosseumDemoScreen(Game game) {
        super("CardScreen", game);

        coinFlipDone = false;
        setUpViewports();
        setUpGameObjects();
        setUpButtons();
        setUpRegions();
        setUpDecks();
        playerDeck.shuffleCards();
        enemyDeck.shuffleCards();
        coinFlipStart();
        coinFlipResult();
    }

    // Methods
    private void setUpGameObjects() {
        // Load in the assets used by the Colosseum Game Screen:
        mGame.getAssetManager().loadAssets("txt/assets/ColosseumAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/HeroAssets.JSON");
        mGame.getAssetManager().loadAssets("txt/assets/CardAssets.JSON");

        if (edgeCase) { //Used for edge case scenario of coin flip, User Story 18.1, Sprint 4 - Scott
            edgeCaseTest();
        }

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
        fpsCounter = new FPSCounter(mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f, this) {
        };

        //Setting up demo player:
        p2 = new Player(this, "Meridia");
        opponent = new AIOpponent(this, "EmperorCommodus");

        p2.setCurrentMana(4);
        p2.setCurrentManaCap(4);

        opponent.setCurrentMana(4);
        opponent.setCurrentManaCap(4);

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
        playerActiveRegion = new ActiveRegion(mDefaultLayerViewport.getLeft() + 25.0f, mDefaultLayerViewport.getRight() - 25.0f, mDefaultLayerViewport.getTop() / 2.0f, mDefaultLayerViewport.getBottom() + p2.position.y + (p2.getPortraitHeight() / 2));
        opponentActiveRegion = new ActiveRegion(mDefaultLayerViewport.getLeft() + 25.0f, mDefaultLayerViewport.getRight() - 25.0f, mDefaultLayerViewport.getTop() - (p2.position.y + (p2.getPortraitHeight() / 2)), mDefaultLayerViewport.getTop() / 2.0f);
        playerHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), p2.position.y - (p2.getPortraitHeight() / 2), mDefaultLayerViewport.getBottom());
        opponentHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getTop(), opponent.position.y + (opponent.getPortraitHeight() / 2));
    }

    public void setUpDecks() {
        //This method sets up the player and enemy decks, called when screen is loaded. - Dearbhaile
        playerDeck = new CardDeck(1, "Basic Player Deck", this, false, playerHandRegion);
        enemyDeck = new CardDeck(2, "Basic Enemy Deck", this, true, opponentHandRegion);

        for (int i = 0; i < enemyDeck.getmCardHand().size(); i++) {
            enemyDeck.getmCardHand().get(i).flipCard();
        }
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

    //Methods relating to stopping and starting turns - Dearbhaile
    //If enemy started, then turns increase every time enemy takes new turn.
    public void endPlayerTurn() {
        p2.setYourTurn(false); // Set Player turn to false
        opponent.setYourTurn(true); // Set Opponent turn to true
        mEnemyTurnBegins = System.currentTimeMillis(); // Start timing enemy's turn
        enemyDeck.drawCard(opponent, mEnemyFatigue, mGame); // Draw card to enemy deck
        if (userWhoStarted == UserWhoStarted.ENEMYSTARTED) { //If opponent is starting player,
            mCurrentTurn.newTurnFunc(p2, opponent); //Then increment turn number
        }
    }

    //This method checks if 5 seconds have elapsed since enemy turn began
    //If yes, then it triggers player turn to begin again.
    //If player started, then turns increase every time player takes new turn. - Dearbhaile
    public void checkIfEnemysTurn() {
        if (mCurrentTime - mEnemyTurnBegins >= ENEMY_TURN_TIME) { // Current time is constantly being updated in Update method
            p2.setYourTurn(true); // If enemy turn over, set Player turn to true
            opponent.setYourTurn(false); // Set Opponent turn to false
            playerDeck.drawCard(p2, mPlayerFatigue, mGame); // Player draws card
            if (userWhoStarted == UserWhoStarted.PLAYERSTARTED) { // If player is starting player,
                mCurrentTurn.newTurnFunc(p2, opponent); //Then increment turn number.
            }
        }
    }

    //Coin Flip - Scott
    private int coinFlipStart() { //Scott, User Story 16, Sprint 4
        int flip = RANDOM.nextInt(6001);
        if (flip == 6000) { //side of coin (1/6000 chance to auto-win)
            return 2;
        } else if (flip >= 3000 && flip < 6000) { //heads (ai starts)
            return 1;
        } else if (flip >= 0 && flip < 3000) { //tails (user starts)
            return 0;
        }
        return -1; //for error testing only
    }

    private void coinFlipResult() {  // Method for building hand based on coin flip. - Dearbhaile
        mCoinTossResult = coinFlipStart(); //Uses result of CoinFlipStart, i.e. 0 or 1, to set up game
        switch (mCoinTossResult) {
            case 0: // Ie, player starts
                mCurrentTurn.setUpStats_PlayerStarts(p2, playerDeck, opponent, enemyDeck);
                userWhoStarted = UserWhoStarted.PLAYERSTARTED;
                break;
            case 1: // Ie, opponent starts
                mEnemyTurnBegins = System.currentTimeMillis();
                mCurrentTurn.setUpStats_EnemyStarts(p2, playerDeck, opponent, enemyDeck);
                userWhoStarted = UserWhoStarted.ENEMYSTARTED;
                break;
            case 2: // Ie, auto win
                EndGameScreen.setCoinFlipResult(true);
                break;
            default:
                break;
        }
    }

    private void edgeCaseTest() { //Testing for the edge case scenario of the coin flip, User Story 18.1, Sprint 4 - Scott
        boolean i = true;
        while (i) {
            edgeCounter++;
            switch (coinFlipStart()) {
                case 0://tails - player starts
                    break;
                case 1: //heads - ai starts
                    break;
                case 2: //edge of coin - set opponent health to 0, auto win game.
                    i = false;
                    break;
                default: //output an error
                    break;
            }
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        while (!coinFlipDone) { //Before coin flip is done
            startTime = System.currentTimeMillis(); //Start recording the game's start time
            mGame.getScreenManager().addScreen(new CoinTossScreen(mGame, getmCoinTossOutcome())); //Run coin toss, mCoinTossOutcome
            coinFlipDone = true; //Mark coinFlipDone as true, so it will not run again.
        }

        if (coinFlipDone) {//Current time should constantly be collected, for use when counting enemy's turn time - Dearbhaile
            mCurrentTime = System.currentTimeMillis(); //This data starts collecting as soon as coin flip finishes
        }

        if (opponent.getYourTurn()) {
            checkIfEnemysTurn(); //If opponent's turn, check when it ends - Dearbhaile
        }

        if (wasPaused) {
            wasPaused = false; //If the game was paused, gather the total time it was paused for - Scott
            pauseTimeTotal += System.currentTimeMillis() - pauseTime;//gather a total paused time, in the case of a user pausing multiple times
        }

        mInput = mGame.getInput(); //Process any touch events occurring since the update

        //if (p2.getYourTurn()) { //Player's cards can be dragged when it is their turn, otherwise they cannot - Dearbhaile
            for (Card cards : playerDeck.getmCardHand()) {
                cards.cardEvents(playerDeck.getmCardHand(), mDefaultScreenViewport, mDefaultLayerViewport, mGame);
            }
        for (Card cards : enemyDeck.getmCardHand()) {
            cards.cardEvents(enemyDeck.getmCardHand(), mDefaultScreenViewport, mDefaultLayerViewport, mGame);
        }
        //}

        p2.update(elapsedTime); //Update player stats - Kyle
        opponent.update(elapsedTime); //Update opponent stats

        //'EndGameScreen' code - Scott
        if (EndGameScreen.getCoinFlipResult()) { //If the coin flip was on the edge, win the game go to next end game screen
            try {
                Thread.sleep(1000); //Allows player to see when they have won rather than immediately jumping
            } catch (InterruptedException e) {
            }
            EndGameScreen.setTimePlayed((System.currentTimeMillis() - startTime) - pauseTimeTotal); //Allow for a "time played" statistic
            EndGameScreen.setMostRecentResult("win"); //Record the result
            mGame.getScreenManager().changeScreenButton(new EndGameScreen(mGame));
        } else if (p2.getCurrentHealth() <= 0 || opponent.getCurrentHealth() <= 0) { //if either of the health is below 0 enter the if statement
            try {
                Thread.sleep(1000); //Allows player to see when they have won rather than immediately jumping
            } catch (InterruptedException e) {
            }
            if (p2.getCurrentHealth() <= 0 && opponent.getCurrentHealth() <= 0) { //if both sides health is 0 or less, the game ends in a draw
                EndGameScreen.setMostRecentResult("draw"); //Record the result
            } else if (p2.getCurrentHealth() <= 0) { //if the player reaches 0 or less health, they lose
                EndGameScreen.setMostRecentResult("loss"); //Record the result
            } else if (opponent.getCurrentHealth() <= 0) { //if the opponent reaches 0 or less health, the player wins
                EndGameScreen.setMostRecentResult("win"); //Record the result
            }
            EndGameScreen.setTimePlayed((System.currentTimeMillis() - startTime) - pauseTimeTotal); //Allow for a "time played" statistic
            mGame.getScreenManager().changeScreenButton(new EndGameScreen(mGame)); //swap to the end game screen regardless of whatever outcome occurs
        } else {
            List<TouchEvent> touchEvents = mInput.getTouchEvents();
            if (touchEvents.size() > 0) {

                //This next for loop is to prevent the player's cards from slotting into the opponent's card slots - Diarmuid Toal
                for (int i = 0; i < playerDeck.getmCardHand().size(); i++) {

                    // Updates both regions for all cards - Kyle
                    playerActiveRegion.update(playerDeck.getmCardHand().get(i));
                    playerHandRegion.update(playerDeck.getmCardHand().get(i));
                }

                //This next for loop is to prevent the opponent's cards from slotting into the player's card slots - Diarmuid Toal
                for (int i = 0; i < enemyDeck.getmCardHand().size(); i++) {
                    //enemyDeck.getmCardHand().get(i).cardEvents(enemyDeck.getmCardHand(), mDefaultScreenViewport, mGameViewport, mGame);

                    // Updates both regions for all cards
                    opponentActiveRegion.update(enemyDeck.getmCardHand().get(i));
                    opponentHandRegion.update(enemyDeck.getmCardHand().get(i));
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

                if (mEndTurnButton.isPushTriggered()) {
                    playerDeck.discardCards_EndOfTurn();
                    endPlayerTurn();
                }

                if (mDiscardButton.isPushTriggered()) { //Calls discard function if there is a card selected and discard button pressed - Dearbhaile
                    for (int i = 0; i < playerDeck.getmCardHand().size(); i++) {
                        if (playerDeck.getmCardHand().get(i).getmIsSelected()) {
                            Card mCardToDiscard = playerDeck.getmCardHand().get(i);
                            mCardToDiscard.discardCard(mCardToDiscard);
                        }
                    }
                }
            }
        }
    }

    //Draw the card demo screen
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

        if (p2.getYourTurn()) //Draw 'End Turn' button onscreen, which toggles between pressable and not pressable image - Dearbhaile
            mEndTurnButton.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        else
            mEndTurnButtonOff.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);

        for (PushButton buttons : mButtons) { //Draw the remainder of the buttons:
            buttons.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        if (edgeCase) { //To test for the edge case of the coin flip, User Story 18.1, Sprint 4 - Scott
            int screenHeight = graphics2D.getSurfaceHeight();
            float textHeight = screenHeight / 30.0f;
            textPaint.setTextSize(textHeight); //create a appropriate sizing of text
            graphics2D.drawText("Iterations to reach Edge Case:", 100.0f, 50.0f, textPaint); //draw the text "Iterations to reach Edge Case:"
            graphics2D.drawText(String.valueOf(edgeCounter), 100.0f, 100.0f, textPaint);
        }

        if (mGetPreference.getBoolean("FPS", true)) { //If player has switched FPS counter on
            fpsCounter.draw(elapsedTime, graphics2D); //Draw FPS counter onscreen
        }

        for (GameObject gObject : mGameObjs) { //Draw all game objects
            gObject.draw(elapsedTime, graphics2D, mGameViewport, mDefaultScreenViewport);
        }

        float statPlayerYSpacing = 11.0f; //PLAYER STATS BEING DRAWN:
        drawPlayers(spacingX, spacingY, elapsedTime, graphics2D, p2, playerDeck, statPlayerYSpacing);

        float statOpponentYSpacing = 0.6f; //OPPONENT STATS BEING DRAWN:
        drawPlayers(spacingX, spacingY, elapsedTime, graphics2D, opponent, enemyDeck, statOpponentYSpacing);
    }

    //Getters and setters:
    public int getmCoinTossOutcome() {
        return this.mCoinTossResult;
    }
    public boolean getEdgeCase() {
        return edgeCase;
    }
    public static void setEdgeCase(boolean edgeCaseInput) {
        edgeCase = edgeCaseInput;
    }
    public static void setWasPaused(boolean pauseInput) { wasPaused = pauseInput; }
    public UserWhoStarted getUserWhoStarted() { return this.userWhoStarted; }
    public ActiveRegion getPlayerActiveRegion() { return this.playerActiveRegion; }
    public ActiveRegion getOpponentActiveRegion() { return this.opponentActiveRegion; }

}