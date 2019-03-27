package uk.ac.qub.eeecs.game.Colosseum;

//CODE HIGHLIGHT 3 - 'TURN' CLASS:

//
// 'Turn' Class is used to hold information about the current turn happening in the game.
//

public class Turn {
    //Coded by Dearbhaile Walsh

    //int required to hold turn number:
    private int mTurnNum;

    //final int required to put a cap on mana:
    private final int MANA_CAP = 10;

    //Constructor for Turn Class:
    public Turn() {
        mTurnNum = 1; // Initially set to 1
    }

    //Called each time there is a new turn:
    public void newTurnFunc(Player player, AIOpponent opponent) {
        incrementTurnNum(); //Turn number is incremented by 1, in order to count the turns
        increasePlayerMana_NewTurn(player); //Increase Player Mana
        increasePlayerMana_NewTurn(opponent); //Increase Opponent Mana
    }

    public void increasePlayerMana_NewTurn(Player player) {
        if (player.getCurrentMana() < MANA_CAP) { //If mana/mana cap are at 10, it cannot increase further
            player.increaseCurrentMana(1); //Otherwise, increase mana by 1
            if (player.getCurrentManaCap() < MANA_CAP) {
                player.increaseCurrentManaCap(); //Again, check that the Mana Cap is below 10 before increasing it.
                //Otherwise, if player mana is increased e.g. from 4 to 5, but mana cap was already at 10, mana cap
                //Should not increase to 11 and so on and so forth. It should *always* be capped at 10.
            }
        }
    }

    //This method will be used to set up stats in the event of a player getting a 'heads' result in the coin flip:
    public void setUpStats_PlayerStarts(Player player, CardDeck playerDeck, AIOpponent opponent, CardDeck enemyDeck) {
        player.setYourTurn(true); //Player's "yourTurn" value is set to true
        opponent.setYourTurn(false); //Enemy's "yourTurn" value is set to false
        opponent.setCurrentMana(2); //Enemy gets 2 mana points, ie one more than the usual 1
        opponent.setCurrentManaCap(2); //Mana cap is increased to accommodate the mana value
        playerDeck.drawSetNumCards(3); //Player draws usual number of starting cards, ie 3
        enemyDeck.drawSetNumCards(4); //Enemy draws an extra card, in compensation
    }

    //This method will be used to set up stats in the event of a player getting a 'tails' result in the coin flip:
    public void setUpStats_EnemyStarts(Player player, CardDeck playerDeck, AIOpponent opponent, CardDeck enemyDeck) {
        opponent.setYourTurn(true); //Enemy's "yourTurn" value is set to true
        player.setYourTurn(false); //Player's "yourTurn" value is set to false
        player.setCurrentMana(2); //Player gets 2 mana points, ie one more than the usual 1
        player.setCurrentManaCap(2); //Mana cap is increased to accommodate the mana value
        enemyDeck.drawSetNumCards(3); //Enemy draws usual number of starting cards, ie 3
        playerDeck.drawSetNumCards(4); //Enemy draws an extra card, in compensation
    }

    public void incrementTurnNum() {
        this.mTurnNum++; // Method to increase current turn number
    };

    //Getters and setters:
    public int getmTurnNum() {return this.mTurnNum;}
}
//END OF CODE HIGHLIGHT 3