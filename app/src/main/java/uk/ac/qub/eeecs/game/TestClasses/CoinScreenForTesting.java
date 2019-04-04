package uk.ac.qub.eeecs.game.TestClasses;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.Sprite;

public class CoinScreenForTesting extends Sprite  { //Authored by Scott

    //Used for coin flip animation images amount
    private final int[] COIN_ANIMATION_IMAGES = {1,2,3,4,5,6,7,8,9};
    //Calculate the length and first position of the images.
    private final int COIN_ANIM_IMAGE_LENGTH = COIN_ANIMATION_IMAGES.length;
    private final int COIN_ANIM_IMAGE_FIRST = COIN_ANIMATION_IMAGES[0];
    //Have final variables for the filenames of the images
    private final String COIN_HEADS = "headsFace";
    private final String COIN_TAILS = "tailsFace";
    private final String COIN_EDGE = "coinEdge";

    private int currentCoinFrame = 1; //Used for getting the correct image file, will always be a number within COIN_ANIMATION_IMAGES
    private int framesLeft = 0; //Used as a counter for how many frames are left of the animation
    private boolean[] reverseAnimToggle = {false,false}; //Used for a holder if the order is being reversed or not animation image wise.

    private boolean secondAnimationLoop = false; //If the animation is showing the coin flipping
    private boolean coinEdge = false;
    private boolean coinFaceHeads = true;


    public CoinScreenForTesting(float startX, float startY, float width, float height, GameScreen gameScreen, String coinToss) {
        super(startX, startY, width, height, null, gameScreen);
      //  mGameScreen.getGame().getAssetManager().loadAssets("txt/assets/CoinAssets.JSON");
      //  setCoinBitmap(COIN_HEADS + 1);

        setupCoinAnimation(coinToss);
    }

    private void setCoinBitmap(String bmpName) { //Setup the bitmap and make sure its loading something that exists. - Method from 'QUBBattle', 'Coin,java', 'setBitmap'
   //     if (mGameScreen.getGame().getAssetManager().getBitmap(bmpName) != null) {
      //      mBitmap = mGameScreen.getGame().getAssetManager().getBitmap(bmpName);
   //     }
    }

    public void setupCoinAnimation(String coinToss) { //Setup how many frames the animation will go through depending on what side it must end on.
        switch(coinToss){
            case "Heads": //Player gets tails
                //Calculate the amount of heads frames
                framesLeft = calculateCoinFrames(COIN_ANIM_IMAGE_LENGTH+2)-1;
                break;
            case "Tails": //Player gets tails
                //Calculate the amount of tails frames
                framesLeft =calculateCoinFrames(COIN_ANIM_IMAGE_LENGTH)+1;
                break;
            case "Edge": //Player gets edge
                //Calculate the amount of edge frames (adding one as it ends on the edgeImage)
                framesLeft = calculateCoinFrames(COIN_ANIM_IMAGE_LENGTH+1);
                break;
            default: //framesLeft is default setup to 0, so animation would not play as this state should not be reached.
                break;
        }
    }

    private int calculateCoinFrames(int iterationMultiplier) { //Calculate the frames it needs to use in the animation
        //Structure: Amount of times for each heads/tails side until its on the correct heads/tails face, times by enough flips for 3 seconds.
        //Then add on the amount of "edge of coin" images that were needed.
        return (iterationMultiplier * COIN_ANIM_IMAGE_LENGTH-(iterationMultiplier/2)) + (iterationMultiplier/2)-2;
    }

    private void forwardAnimLoop() { //Forward loop, going incrementally from images 1-9 (coin rotates towards edge)
        currentCoinFrame += 1; //increase the counter
        if (currentCoinFrame >= COIN_ANIM_IMAGE_LENGTH){ //if the counter is 9 or more, change it to the coin edge image, and move it to second animation loop (backward)
            secondAnimationLoop = true; //Enable the second animation loop line
            coinEdge = true; //When the end of these images are reached, display the coin edge image before moving onto the next loop
            animUpdate(1,0); //Update the animation values
        }
    }

    private void backwardAnimLoop() { //Backward loop, going decrementally from images 9-1 (coin flips toward (coin rotates towards face)
        currentCoinFrame -= 1; //decrease the counter
        if (currentCoinFrame <= COIN_ANIM_IMAGE_FIRST) { //if the counter is 1 or less,  move it to first animation loop (forward)
            secondAnimationLoop = false; //Disable the second animation loop line
            animUpdate(0,1); //Update the animation values
        }
    }

    private void animUpdate(int toggleFirst, int toggleSecond) { //Used to ensure that coin flips correctly
        //Coin flips follows the correct side (if on tails face, and the coin flip reverses, it should still be tails face downwards to coin edge
        if(!reverseAnimToggle[toggleFirst]) {
            //Enable the toggle for if the animation is reversing for the current (e.g. tail edge to tail face, then tail face to tail edge after)
            reverseAnimToggle[toggleFirst] = true;
            //Disable previous toggle for other animation reversing (so it doesnt jump from heads edge to heads face, into tails edge to tails face).
            reverseAnimToggle[toggleSecond] = false;
        }
        if(!reverseAnimToggle[0]) { //If the toggle is false (switching from heads to tails or vice versa)
            coinFaceHeads = !coinFaceHeads; //Then swap which face should currently be showing.
        }
    }

    public void coinAnimation() {
        //Check that the range will always be a number which can be actually output image wise (between 10 and 0)
        if (currentCoinFrame < COIN_ANIM_IMAGE_LENGTH+1 && currentCoinFrame > COIN_ANIM_IMAGE_FIRST-1) {
            coinEdge = false; //Reset the coin edge image to false at the start.
            if (!secondAnimationLoop) { //If the coin is on first animation loop of that side of coin.
                forwardAnimLoop();
            } else { //else the coin is on second animation loop of that side of coin.
                backwardAnimLoop();
            }

            if (coinEdge) { //If the current frame is the coin edge, display that image
                setCoinBitmap(COIN_EDGE);
            } else {
                if (coinFaceHeads) { //If the current frame is on the heads side, display that relevant image section
                    setCoinBitmap(COIN_HEADS + COIN_ANIMATION_IMAGES[currentCoinFrame]);
                } else if(!coinFaceHeads) { //If the current frame is on the tails side, display that relevant image section
                    setCoinBitmap(COIN_TAILS + COIN_ANIMATION_IMAGES[currentCoinFrame]);
                }
            }
        }
        framesLeft--; //Decrease the total amount of frames left after this frame is done.

        //Coin spins slower as it reaches the final coin decision. - This small section below was inspired from from 'QUBBAttle' past project in "Coin.java", method "progressAnimation()"
        int timeToWait;
        if (framesLeft > 15) { timeToWait = 30; }
        else if (framesLeft > 5)  { timeToWait = 40; }
        else { timeToWait = 50; }
        //Using the timeToWait to delay the animation.
        try { Thread.sleep(timeToWait); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }

    }

    public boolean isComplete() { return (framesLeft <= 0); } //If the flip has been completed, return true, otherwise false

    public int getCurrentCoinFrame() {
        return currentCoinFrame;
    }

    public void setCurrentCoinFrame(int currentCoinFrame) {
        this.currentCoinFrame = currentCoinFrame;
    }

    public int getFramesLeft() {
        return framesLeft;
    }

    public void setFramesLeft(int framesLeft) {
        this.framesLeft = framesLeft;
    }
}
