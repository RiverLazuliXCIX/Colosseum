package uk.ac.qub.eeecs.game.TestClasses;

import java.util.List;
import java.util.Random;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
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
import uk.ac.qub.eeecs.game.EndGameScreen;
import uk.ac.qub.eeecs.game.colosseumDemoScreen;

//CoinTossScreen, coded by Dearbhaile Walsh
public class CoinTossScreenForTesting extends GameScreen {

    // Properties
    //Different objects required for this screen to function
    private LayerViewport mGameViewport;

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

    //Define the two Decks
    private CardDeck mPlayerDeck, mEnemyDeck;

    //UserWhoStarted variable to hold data about who started in this match:
    private UserWhoStarts mUserWhoStarts;

    //Variable to store time of enemy turn beginning (if applicable):
    private long mEnemyTurnBegins = 0;

    //Variables required for the message (lines 1 and 2) to display properly
    private int mCoinTossResult = 0;
    private String mCoinTossMsg1 = "";
    private String mCoinTossMsg2 = "";

    //Create instance of Coin object:
    private Coin mCoin;

    //For testing purposes!
    Boolean ifTesting;

    // Constructor
    //Create the 'CoinTossScreen' screen
    public CoinTossScreenForTesting(Game game) {
        super("CoinTossScreen", game);
        ifTesting = true;

        mTimeOnCreate = System.currentTimeMillis();
        setupViewports();
        setUpCTSObjects();
        setUpGameObjects();
        if (ifTesting == false) {
            coinFlipStart();
            mCoinTossResult = coinFlipStart();
            coinFlipResult(mCoinTossResult);
            chooseTextToDisplay();
        }
    }

    public void setUpGameObjects() {
        //Setting up demo player:
        mPlayer = new Player(this, "Meridia");
        mOpponent = new AIOpponent(this, "EmperorCommodus");

        mPlayer.setCurrentMana(1);
        mPlayer.setCurrentManaCap(1);

        mOpponent.setCurrentMana(1);
        mOpponent.setCurrentManaCap(1);

        //This method sets up the player and enemy decks, called when screen is loaded. - Dearbhaile
        HandRegion playerHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mPlayer.position.y - (mPlayer.getPortraitHeight() / 2), mDefaultLayerViewport.getBottom());
        HandRegion opponentHandRegion = new HandRegion(mDefaultLayerViewport.getRight() / 2 - (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getRight() / 2 + (4 * (50.0f / 1.5f)), mDefaultLayerViewport.getTop(), mOpponent.position.y + (mOpponent.getPortraitHeight() / 2));

        mPlayerDeck = new CardDeck(1, "Basic Player Deck", this, false, playerHandRegion);
        mEnemyDeck = new CardDeck(2, "Basic Enemy Deck", this, true, opponentHandRegion);

        for (int i = 0; i < mEnemyDeck.getmCardHand().size(); i++) {
            mEnemyDeck.getmCardHand().get(i).flipCard();
        }
    }

    public void setUpCTSObjects() {

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

    public int coinFlipStart() {
        Random RANDOM = new Random();
        int flip = RANDOM.nextInt(6001);
        if (flip == 6000) { //side of coin (1/6000 chance to auto-win)
            return 2;
        } else if (flip >= 3000 && flip < 6000) { //heads (ai starts)
            return 1;
        } else if (flip >= 0 && flip < 3000) { //tails (user starts)
            return 0;
        }
        return -1;
    }

    //For testing purposes only:
    public int setResult() {
        mCoinTossResult = coinFlipStart();
        return mCoinTossResult;
    }

    // Method for setting up stats based on Coin Toss:
    public void coinFlipResult(int result) {
        switch (result) {
            case 0: // ie, player starts
                mCurrentTurn.setUpStats_PlayerStarts(mPlayer, mPlayerDeck, mOpponent, mEnemyDeck);
                mUserWhoStarts = UserWhoStarts.PLAYERSTARTS;
                break;
            case 1: // ie, ai starts
                mCurrentTurn.setUpStats_EnemyStarts(mPlayer, mPlayerDeck, mOpponent, mEnemyDeck);
                mUserWhoStarts = UserWhoStarts.ENEMYSTARTS;
                break;
            case 2: //edge of coin - set opponent health to 0, auto win game.
                EndGameScreen.setCoinFlipResult(true);
                break;
        }
    }

    public void chooseTextToDisplay() {
        if (mCoinTossResult == 0) {
            mCoinTossMsg1 = "The coin landed on heads! You get to play first.";
            mCoinTossMsg2 = "The other player draws 4 cards, and gets 1 additional mana.";
        }
        else if (mCoinTossResult == 1) {
            mCoinTossMsg1 = "The coin landed on tails! The enemy plays first.";
            mCoinTossMsg2 = "You draw an extra card and additional mana for your troubles.";
        }
        else if (mCoinTossResult == 2) {
            mCoinTossMsg1 = "The coin landed on its edge!";
            mCoinTossMsg2 = "You automatically win the game for being lucky!";
        }
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        mCurrentTime = System.currentTimeMillis();
        mTimeRemaining = 10 - ((mCurrentTime - mTimeOnCreate)/1000);

        if (!mCoin.isComplete()) {
            mCoin.coinAnimation();
        }

        if (mCurrentTime - mTimeOnCreate >= mCoinToss_Timeout) {
            if (mOpponent.getYourTurn()) {
                mEnemyTurnBegins = System.currentTimeMillis();
            }

            mGame.getScreenManager().getCurrentScreen().dispose();
            mGame.getScreenManager().changeScreenButton(new colosseumDemoScreen(mPlayer, mOpponent, mCurrentTurn,
                    mUserWhoStarts, mEnemyTurnBegins, mPlayerDeck, mEnemyDeck, mGame));
        }

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            mSkipButton.update(elapsedTime);

            //If the 'skip animation' button is pressed, then go straight to game:
            if (mSkipButton.isPushTriggered()) {
                mCoinToss_Timeout = 0;
            }
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

    }

    //Getters and setters:
    public int getmCoinTossResult() { return this.mCoinTossResult; }
    public String getmCoinTossMsg1() { return this.mCoinTossMsg1; }
    public String getmCoinTossMsg2() { return this.mCoinTossMsg2; }

    public Player getmPlayer() { return this.mPlayer; }
    public AIOpponent getmOpponent() { return this.mOpponent; }
    public CardDeck getmPlayerDeck() { return this.mPlayerDeck; }
    public CardDeck getmEnemyDeck() { return this.mEnemyDeck; }
    public UserWhoStarts getmUserWhoStarts() {return this.mUserWhoStarts; }
}
