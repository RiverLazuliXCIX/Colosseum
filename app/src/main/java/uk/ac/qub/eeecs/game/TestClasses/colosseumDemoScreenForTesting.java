package uk.ac.qub.eeecs.game.TestClasses;

import java.util.List;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.FPSCounter;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.CardDeck;
import uk.ac.qub.eeecs.game.EndGameScreen;
import uk.ac.qub.eeecs.game.Colosseum.FatigueCounter;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.Turn;
import uk.ac.qub.eeecs.game.Colosseum.UserWhoStarts;

public class colosseumDemoScreenForTesting extends GameScreen {

    //////////////////
    //  PROPERTIES  //
    //////////////////

    private LayerViewport mGameViewport;
    private Input mInput;

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

    //UserWhoStarts variable to hold data about who started in this match:
    private UserWhoStarts mUserWhoStarts;

    //Variables required for the Game Timer:
    private long startTime = 0, pauseTime = 0, pauseTimeTotal = 0; //Setting up variables to hold times of the game
    private static boolean wasPaused = false;

    //Variables required for the Enemy Turn Timer:
    private final long ENEMY_TURN_TIME = 2000;
    private long mEnemyTurnBegins, mCurrentTime;

    private boolean startTimeRecorded = false;

    //Set up FPS counter:
    private FPSCounter fpsCounter;

    // Hand and Active regions for opponent and player
    // Hand region for colosseum is setup and initialised within coin toss screen and as such, must be passed
    // via the constructor
    ActiveRegion playerActiveRegion, opponentActiveRegion;
    HandRegion playerHandRegion, opponentHandRegion;

    //////////////////
    // CONSTRUCTOR  //
    //////////////////

    public colosseumDemoScreenForTesting(Player player, AIOpponent opponent, Turn currentTurn, UserWhoStarts starter,
                                         long EnemyTurnBegins, CardDeck playerDeck, CardDeck enemyDeck,
                                         HandRegion playerHandRegion, HandRegion opponentHandRegion, Game game) {
        super("CardScreen", game);

        //FOR TESTING PURPOSES -
        boolean testing = true;

        setUpViewports();
        if (!testing) {
            setUpGameObjects();
            setUpActiveRegions();
        }

        //Get data from the CoinTossScreen:
        this.mPlayer = player;
        this.mOpponent = opponent;
        this.mCurrentTurn = currentTurn;
        this.mUserWhoStarts = starter;
        this.mEnemyTurnBegins = EnemyTurnBegins;
        this.mPlayerDeck = playerDeck;
        this.mEnemyDeck = enemyDeck;
        this.playerHandRegion = playerHandRegion;
        this.opponentHandRegion = opponentHandRegion;

        //Shuffle two decks:
        playerDeck.shuffleCards();
        enemyDeck.shuffleCards();
    }

    ///////////////
    //  METHODS  //
    ///////////////

    public void setUpGameObjects() {

        //Setting up FPS counter:
        fpsCounter = new FPSCounter(mGameViewport.getWidth() * 0.50f, mGameViewport.getHeight() * 0.20f, this) {};

        //Setting up demo player:
        mPlayer = new Player(this, "Meridia");
        mOpponent = new AIOpponent(this, "EmperorCommodus");

    }

    public void setUpViewports() {
        // Setup the screen viewport to use the full screen.
        mDefaultScreenViewport.set(0, 0, mGame.getScreenWidth(), mGame.getScreenHeight());

        // Calculate the layer height that will preserved the screen aspect ratio
        // given an assume 480 layer width.
        float layerHeight = mGame.getScreenHeight() * (480.0f / mGame.getScreenWidth());

        mDefaultLayerViewport.set(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
        mGameViewport = new LayerViewport(240.0f, layerHeight / 2.0f, 240.0f, layerHeight / 2.0f);
    }

    public void setUpActiveRegions() {
        //Defining playable region width and height ( 50.0f/1.5f is the width of the cards)
        playerActiveRegion = new ActiveRegion(mDefaultLayerViewport.getLeft() + 25.0f, mDefaultLayerViewport.getRight() - 25.0f, mDefaultLayerViewport.getTop() / 2.0f, mDefaultLayerViewport.getBottom() + mPlayer.position.y + (mPlayer.getPortraitHeight() / 2));
        opponentActiveRegion = new ActiveRegion(mDefaultLayerViewport.getLeft() + 25.0f, mDefaultLayerViewport.getRight() - 25.0f, mDefaultLayerViewport.getTop() - (mPlayer.position.y + (mPlayer.getPortraitHeight() / 2)), mDefaultLayerViewport.getTop() / 2.0f);
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

    boolean goOnce = true;

    public void endOfGame(String gameResult) {
        try {
            Thread.sleep(1000); //Allows player to see when they have won rather than immediately jumping to the next screen
        } catch (InterruptedException e) { }

        EndGameScreen.setTimePlayed((System.currentTimeMillis() - startTime) - pauseTimeTotal); //Allow for a "time played" statistic
        EndGameScreen.setMostRecentResult(gameResult); //Record the result
        mGame.getScreenManager().changeScreenButton(new EndGameScreen(mGame)); //Change the screen to the new EndGameScreen
    }

    public void removeDeadCards() {
        mPlayerDeck.checkForDeadCards();
        mEnemyDeck.checkForDeadCards();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (startTimeRecorded == false) {
            startTime = System.currentTimeMillis(); //Start recording the game's start time
            startTimeRecorded = true; //Mark startTimeRecorded as true, so it will not run again.
        }

        //Both decks should constantly be checking for dead cards, ie cards where health <= 0
        //When discovered in either deck, they will be discarded immediately. - Dearbhaile
        removeDeadCards();

        //Current time should constantly be collected, for use when counting enemy's turn time - Dearbhaile
        mCurrentTime = System.currentTimeMillis();

        if (mOpponent.getYourTurn()) {
            if (goOnce) {
                //opponentActiveRegion.addCard(opponentHandRegion.getCardsInRegion().get(0));
                //opponentHandRegion.removeCard(opponentHandRegion.getCardsInRegion().get(0));
                mOpponent.playRandom(playerHandRegion, opponentHandRegion, playerActiveRegion, opponentActiveRegion);
                goOnce = false;
            }
            checkIfEnemysTurn(); //If opponent's turn, check when it ends - Dearbhaile
        }

        if (wasPaused) {
            wasPaused = false; //If the game was paused, gather the total time it was paused for - Scott
            pauseTimeTotal += System.currentTimeMillis() - pauseTime;//gather a total paused time, in the case of a user pausing multiple times
        }

        mInput = mGame.getInput(); //Process any touch events occurring since the update

        if (mPlayer.getYourTurn()) { //Player's cards can be dragged when it is their turn, otherwise they cannot - Dearbhaile
            for (Card cards : mPlayerDeck.getmCardHand())
                cards.cardEvents(mPlayerDeck.getmCardHand(), mOpponent, mDefaultScreenViewport, mDefaultLayerViewport, mGame, false);
        }

        //Temporary: Enemy cards made draggable for testing purposes.
        for (Card cards : opponentActiveRegion.getCardsInRegion())
            cards.cardEvents(opponentActiveRegion.getCardsInRegion(), mOpponent, mDefaultScreenViewport, mDefaultLayerViewport, mGame, true);

        mPlayer.update(elapsedTime); //Update player stats - Kyle
        mOpponent.update(elapsedTime); //Update opponent stats

        //'EndGameScreen' code - Scott
        if (EndGameScreen.getCoinFlipResult()) { //If the coin flip was on the edge, win the game go to next end game screen
            endOfGame("win");

        } else if (mPlayer.getCurrentHealth() <= 0 || mOpponent.getCurrentHealth() <= 0) { //if either of the health is below 0 enter the if statement
            if (mPlayer.getCurrentHealth() <= 0 && mOpponent.getCurrentHealth() <= 0) //if both sides health is 0 or less, the game ends in a draw
                endOfGame("draw");
            else if (mPlayer.getCurrentHealth() <= 0) //if the player reaches 0 or less health, they lose
                endOfGame("loss");//Record the result
            else if (mOpponent.getCurrentHealth() <= 0) //if the opponent reaches 0 or less health, the player wins
                endOfGame("win");

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
            }
        }
    }

    //////////////////////////////
    //       DRAW METHODS       //
    //////////////////////////////
    public void drawPlayers(int spacingX, int spacingY, ElapsedTime elapsedTime,
                            IGraphics2D graphics2D, Player p, CardDeck deck, float ySpacing) {

    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }

    ///////////////////////////
    //  GETTERS AND SETTERS  //
    ///////////////////////////
    public static void setWasPaused(boolean pauseInput) { wasPaused = pauseInput; }
    public void setmCurrentTime(long newTime) { this.mCurrentTime = newTime; }
    public void setmEnemyTurnBegins(long newEnemyTime) { this.mEnemyTurnBegins = newEnemyTime; }

    public UserWhoStarts getUserWhoStarts() { return this.mUserWhoStarts; }
    public ActiveRegion getPlayerActiveRegion() { return this.playerActiveRegion; }
    public ActiveRegion getOpponentActiveRegion() { return this.opponentActiveRegion; }
    public static boolean wasPaused() { return wasPaused; } //Used to resume the game from the main menu without having to see a coinflip again
    public Player getmPlayer() { return this.mPlayer; }
    public AIOpponent getmOpponent() { return this.mOpponent; }
    public Turn getmCurrentTurn() { return this.mCurrentTurn; }
    public long getmEnemyTurnBegins() { return this.mEnemyTurnBegins; }
    public CardDeck getmPlayerDeck() { return this.mPlayerDeck; }
    public CardDeck getmEnemyDeck() { return this.mEnemyDeck; }
    public HandRegion getOpponentHandRegion() { return opponentHandRegion; }
    public HandRegion getPlayerHandRegion() { return playerHandRegion; }
    public FPSCounter getFpsCounter() { return fpsCounter; }

}