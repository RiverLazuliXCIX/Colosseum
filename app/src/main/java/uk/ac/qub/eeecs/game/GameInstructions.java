package uk.ac.qub.eeecs.game;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.GameScreen;

public class GameInstructions extends GameScreen {

    /*
    This Class was made by @Diarmuid Toal

     * Unfortunately this was not how I wanted this to be but due to time constrains
     * this was the only viable way I could do this. This class is for solely writing
     * the instructions which will be read from the HTPScreen.
     *
     * The code is in this Class so that the HTPScreen will not become cluttered with
     * too much strings. This also allows for text to be read easily.
     */
    public GameInstructions(Game game) {
        super("PauseScreen", game);
        //Setting up  viewports method
    }

    public static String line1() {
        String[] firstline = new String[2];
        firstline[0] = "•How to Play Colosseum: ";
        firstline[1] = "At the beginning of the game decks are shuffled, and a coin is ";
        return firstline[0] + firstline[1];
    }

    public static String line2() {
        String[] secondLine = new String[2];
        secondLine[0] = "tossed to determine who plays their turn first. ";
        secondLine[1] = "The player who lost the coin toss receives ";
        return secondLine[0] + secondLine[1];
    }

    public static String line3() {
        String[] thirdLine = new String[2];
        thirdLine[0] = "an extra coin, which acts as a special card or token ";
        thirdLine[1] = "that can be used by the owner to ";
        return thirdLine[0] + thirdLine[1];
    }

    public static String line4() {
        String[] fourthLine = new String[2];
        fourthLine[0] = "temporarily increase their denarius by one for that turn only. ";
        fourthLine[1] = "The player who won the coin";
        return fourthLine[0] + fourthLine[1];
    }

    public static String line5() {
        String fifthLine = "toss will have their turn first. ";
        return fifthLine;
    }

    public static String line6() {
        String[] sixthLine = new String[2];
        sixthLine[0] = "•In order to win the game, the objective is to reduce the ";
        sixthLine[1] = "opposing player’s health to 0 by";
        return sixthLine[0] + sixthLine[1];
    }

    public static String line7() {
        String[] seventhLine = new String[2];
        seventhLine[0] = "any means. This can be done by attacking with your minions, ";
        seventhLine[1] = "abilities, and other cards.";
        return seventhLine[0] + seventhLine[1];
    }

    public static String line8() {
        String[] eighthLine= new String[2];
        eighthLine[0] = "•At the beginning of each new turn, you’ll gain a new coin – a denarius. ";
        eighthLine[1] = "With a maximum of ";

        return eighthLine[0] + eighthLine[1];
    }

    public static String line9()
    {
        String ninthLine = "10 denarius to be used in a turn.";
        return  ninthLine;
    }

    public  static  String line10()
    {
        String[] tenthLine= new String[2];
        tenthLine[0] = "•The player that wins the coin toss will start first, ";
        tenthLine[1] = "but the opponent will gain an extra denarius";
        return tenthLine[0] + tenthLine[1];
    }

    /**
     * Update method allowing for events on the screen to be handled
     *
     * @param elapsedTime Elapsed time information for the frame
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
    }

    /**
     * Draw method to allow for objects to be drawn onto the screen
     *
     * @param elapsedTime Elapsed time information for the frame
     * @param graphics2D  Graphics instance used to draw the screen
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
    }
}
