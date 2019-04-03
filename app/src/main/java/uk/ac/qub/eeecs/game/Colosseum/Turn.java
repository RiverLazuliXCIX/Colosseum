package uk.ac.qub.eeecs.game.Colosseum;

/**
 *  'Turn' Class is used to hold information about the current turn happening in the game.
 *  @author Dearbhaile Walsh
 */

public class Turn {

    //int required to hold turn number:
    private int mTurnNum;

    //final int required to put a cap on mana:
    private final int MANA_CAP = 10;

    //final int required to set initial value
    private final int STARTING_VALUE = 1;

    //final ints required to set the normal/extra card values to different players
    //depending on who is starting (ie, player or AI starting the game)
    private final int EXTRA_MANA = 2;
    private final int EXTRA_CARD = 4;
    private final int NORMAL_CARD_NUM = 3;

    //Constructor for Turn Class:
    public Turn() {
        mTurnNum = STARTING_VALUE; // Initially set to 1
    }

    //Called each time there is a new turn:
    public void newTurnFunc(Player player, AIOpponent opponent) {
        incrementTurnNum(); //Turn number is incremented by 1, in order to count the turns
        increasePlayerMana_NewTurn(player); //Increase Player Mana
        increasePlayerMana_NewTurn(opponent); //Increase Opponent Mana
    }

    public void increasePlayerMana_NewTurn(Player player) {
        if (player.getCurrentMana() < MANA_CAP) {
            player.increaseCurrentMana(1);
            if (player.getCurrentManaCap() < MANA_CAP) {
                player.increaseCurrentManaCap(); //Check that the Mana Cap is below 10 before increasing it.
                //Otherwise, if player mana is increased e.g. from 4 to 5, but mana cap was already at 10, mana cap
                //Should not increase to 11 and so on and so forth. It should *always* be capped at 10.
                player.increaseCurrentManaToCap(); //Otherwise, increase mana by 1
            } else if (player.getCurrentManaCap() == MANA_CAP) {
                player.increaseCurrentManaToCap();
            }
        }
    }

    //This method will be used to set up stats in the event of a player getting a 'heads' result in the coin flip:
    public void setUpStats_PlayerStarts(Player player, CardDeck playerDeck, AIOpponent opponent, CardDeck enemyDeck) {
        player.setYourTurn(true); //Player's "yourTurn" value is set to true
        opponent.setYourTurn(false); //Enemy's "yourTurn" value is set to false
        opponent.setCurrentMana(EXTRA_MANA); //Enemy gets 2 mana points, ie one more than the usual 1
        opponent.setCurrentManaCap(EXTRA_MANA); //Mana cap is increased to accommodate the mana value
        playerDeck.drawSetNumCards(NORMAL_CARD_NUM); //Player draws usual number of starting cards, ie 3
        enemyDeck.drawSetNumCards(EXTRA_CARD); //Enemy draws an extra card, ie 2, in compensation
    }

    //This method will be used to set up stats in the event of a player getting a 'tails' result in the coin flip:
    public void setUpStats_EnemyStarts(Player player, CardDeck playerDeck, AIOpponent opponent, CardDeck enemyDeck) {
        opponent.setYourTurn(true); //Enemy's "yourTurn" value is set to true
        player.setYourTurn(false); //Player's "yourTurn" value is set to false
        player.setCurrentMana(EXTRA_MANA); //Player gets 2 mana points, ie one more than the usual 1
        player.setCurrentManaCap(EXTRA_CARD); //Mana cap is increased to accommodate the mana value
        enemyDeck.drawSetNumCards(NORMAL_CARD_NUM); //Enemy draws usual number of starting cards, ie 3
        playerDeck.drawSetNumCards(EXTRA_CARD); //Enemy draws an extra card, in compensation
    }

    public void incrementTurnNum() {
        this.mTurnNum++; // Method to increase current turn number
    };

    //Accessor and Mutator Methods:
    public int getmTurnNum() {return this.mTurnNum;}
}