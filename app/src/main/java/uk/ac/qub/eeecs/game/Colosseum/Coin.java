package uk.ac.qub.eeecs.game.Colosseum;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

public class Coin extends Sprite  {

    //Used for coin flip animation images
    private final int[] COIN_ANIMATION_IMAGES = {1,2,3,4,5,6,7,8,9};
    private final String COIN_HEADS = "headsFace";
    private final String COIN_TAILS = "tailsFace";
    private final String COIN_EDGE = "coinEdge";
    private int currentCoinFrame = 0;
    private int framesLeft = 0;
    private boolean secondAnimationLoop = false;
    private boolean coinEdge = false;
    private boolean coinFaceHeads = true;


    public Coin(float startX, float startY, float width, float height, GameScreen gameScreen, int coinToss) {
        super(startX, startY, width, height, null, gameScreen);
        mGameScreen.getGame().getAssetManager().loadAssets("txt/assets/CoinAssets.JSON");
        setCoinBitmap(COIN_HEADS + 1);

        setupCoinAnimation(coinToss);
    }

    private void setCoinBitmap(String bmpName) { //Setup the bitmap and make sure its loading something that exists.
        if (mGameScreen.getGame().getAssetManager().getBitmap(bmpName) != null) {
            mBitmap = mGameScreen.getGame().getAssetManager().getBitmap(bmpName);
        }
    }

    private void setupCoinAnimation(int coinToss) { //Setup how many frames the animation will go through depending on what side it must end on.
        switch(coinToss){
            case 0: //Player gets heads
                framesLeft = 76;
                break;
            case 1: //Player gets tails
                framesLeft = 95;
                break;
            case 2:
                framesLeft = 86; //Player gets edge
                break;
            default:
                break;
        }
    }

    public void coinAnimation() {
        if(!secondAnimationLoop) {
            currentCoinFrame+=1;
            coinEdge = false;
            if(currentCoinFrame>9) {
                secondAnimationLoop = true;
                coinEdge = true;
                coinFaceHeads = !coinFaceHeads;
            }
        } else if(secondAnimationLoop) { //might be always true
            currentCoinFrame-=1;
            coinEdge = false;
            if(currentCoinFrame<1) {
                secondAnimationLoop = false;
                coinFaceHeads = !coinFaceHeads;
            }
        }
        if(coinEdge) {
        setCoinBitmap(COIN_EDGE);
        } else {
            if(coinFaceHeads) {
                setCoinBitmap(COIN_HEADS + COIN_ANIMATION_IMAGES[currentCoinFrame]);
            } else if (!coinFaceHeads) {  //might be always true
                setCoinBitmap(COIN_TAILS + COIN_ANIMATION_IMAGES[currentCoinFrame]);
            }
        }
        framesLeft--;

        // wait duration is determined based on current speed of coin spinning
        int timeToWait;
        if (framesLeft > 50) { timeToWait = 5; }
        else if (framesLeft > 30) { timeToWait = 15; }
        else if (framesLeft > 10) { timeToWait = 45; }
        else if (framesLeft > 5)  { timeToWait = 60; }
        else { timeToWait = 75; }

        // wait period executed
        try { Thread.sleep(timeToWait); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }



    }

    public boolean isComplete() { return (framesLeft <= 0); } //If the flip has been completed, return true, otherwise false
}
