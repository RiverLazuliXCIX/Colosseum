package uk.ac.qub.eeecs.game.prototypeClasses;

import java.util.ArrayList;

import uk.ac.qub.eeecs.game.Colosseum.MinionCard;

/**
 * Created by Matthew, 06/12/2018
 */
public class Deck {

    private ArrayList<Card> deck;
    private ArrayList<Card> hand;
    private ArrayList<MinionCard> graveyard;

    private int minionsLeft;
    private int specialsLeft;

    private int cardsInHand;

    public Deck() {
        deck = new ArrayList<>(30);
        hand = new ArrayList<>(10);
        graveyard = new ArrayList<>(0);
        minionsLeft = 0;
        specialsLeft = 0;
        cardsInHand = 0;

        populateDeck();
        drawXCards(10);
    }

    public void populateDeck() {
        // once variations of cards can be created, a deck can then be created
    }

    public void drawCard() {
        if (deck.size() > 0) {
            Card c = deck.remove(deck.size() - 1);

            if (cardsInHand < 10) {
                hand.add(c);
                //updateCardsLeft(c);
                return;
            }

            // otherwise delete card/add to graveyard as hand is full
            // if (c instanceof MinionCard) addToGraveyard((MinionCard) c);
        } else {
            // player/enemy takes damage
        }
    }

    public void drawXCards(int x) {
        for (int i = 0; i < x; i++) drawCard();
    }

    public void addToGraveyard(MinionCard m) {
        graveyard.add(m);
    }

    /* commented out to stop compilation errors
    public void resurrect(MinionCard m) {
        for (int i = 0; i < graveyard.size(); i++) {
            if (m.equals(graveyard.get(i))) {
                hand.add(graveyard.remove(i));
                return;
            }
        }

        // an error should be shown if a minion not in the graveyard tries to be resurrected, i.e. if
        // the loop completes without the return statement being called or there are no minions
        // in the graveyard
    }

    private void updateCardsLeft(Card c) {
        if (c instanceof MinionCard) minionsLeft--;
        else specialsLeft--;
    }
    */

    public ArrayList<Card> getDeck() { return this.deck; }
    public void setDeck(ArrayList<Card> deck) { this.deck = deck; }

}