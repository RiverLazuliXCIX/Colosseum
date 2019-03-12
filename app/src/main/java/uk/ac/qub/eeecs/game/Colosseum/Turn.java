package uk.ac.qub.eeecs.game.Colosseum;

public class Turn {

    //int required to hold turn number:
    private int mTurnNum;

    //final int required to put a cap on mana:
    private final int MANA_CAP = 10;

    //Constructor for Turn:
    public Turn() {
        mTurnNum = 1; // Initially set to 1
    }

    public void newTurnFunc(Player player) {
        incrementTurnNum();
        if (player.getCurrentMana() < MANA_CAP) {
            player.increaseCurrentMana(1);
            player.increaseCurrentManaCap();
        }
    }

    public void setUpStats_PlayerStarts(Player player, CardDeck playerDeck, AIOpponent opponent, CardDeck enemyDeck) {
        player.setYourTurn(true);
        opponent.setYourTurn(false);
        opponent.setCurrentMana(5);
        opponent.setCurrentManaCap(5);
        playerDeck.drawSetNumCards(3);
        enemyDeck.drawSetNumCards(4);
    }

    public void setUpStats_EnemyStarts(Player player, CardDeck playerDeck, AIOpponent opponent, CardDeck enemyDeck) {
        opponent.setYourTurn(true);
        player.setYourTurn(false);
        player.setCurrentMana(5);
        player.setCurrentManaCap(5);
        enemyDeck.drawSetNumCards(3);
        playerDeck.drawSetNumCards(4);
    }

    public void incrementTurnNum() {
        this.mTurnNum++;
    };

    //Getters and setters:
    public int getmTurnNum() {return this.mTurnNum;}
}