package uk.ac.qub.eeecs.game.TestClasses;

import android.graphics.Bitmap;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Card;
import uk.ac.qub.eeecs.game.Colosseum.Effect;
import uk.ac.qub.eeecs.game.Colosseum.MinionCard;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;
import uk.ac.qub.eeecs.game.Colosseum.SpellCard;
import uk.ac.qub.eeecs.game.Colosseum.WeaponCard;

/*
 * Authored by Scott Barham & Kyle Corrigan
 * An extension of the player class, with the addition of AI behaviors
 */

public class AIOpponentForTesting extends PlayerForTesting {

    private float opponentPortraitXPos = getPortraitXPos(); // Centers the player portrait x coordinate to the center of the screen, same as the player portrait - Kyle
    private float opponentPortraitYPos = getGameScreen().getDefaultLayerViewport().getTop()-(getPortraitHeight()/2)-(70.0f/1.5f); // Displays the portrait at the top of the screen - Kyle
    private float abilityFrameXPos = getAbilityFrameXPos(); //-Kyle
    private float abilityFrameYPos = opponentPortraitYPos - (getPortraitHeight()/2) + (getAbilityFrameHeight()/2); //-Kyle

    //Pass in the regions & player via the method call, so the data can be grabbed from each, and then used in methods - Scott
    private HandRegion playerHandRegion,opponentHandRegion; //-Scott
    private ActiveRegion playerActiveRegion,opponentActiveRegion; //-Scott
    private Player humanPlayer;

    //Create a list of cards in the AI hand, board and player board region -Scott
    private ArrayList<Card> cardsInAIHandRegion, cardsInAIBoardRegion, cardsInPlayerBoardRegion, cardsPlayableInAIHand;
    private int playerHealth, aiHealth, boardspaceRemaining, handspaceRemaining, manaRemaining; //Values of current player and ai attributes. -Scott
    private int[] playerCardValues = {0, 0}, aiBoardCardValues = {0, 0}; //List of the values of played card values with notation of {attack,health}. -Scott
    private int[] cardInHandCategory = {0,0,0}; //different card types, in order of "minion", "spell", "weapon" for cards in hand/board. -Scott
    private ArrayList<Card> playerTauntMinionsList, aiTauntMinionsList; //Used to keep a track of taunt minions cards - Scott

    public AIOpponentForTesting(GameScreen gameScreen, String hero){
        super(gameScreen, hero); //-Kyle

        setPortraitYPos(opponentPortraitYPos); //-Kyle
        setAbilityFrameYPos(abilityFrameYPos); //-Kyle

        //Rest of code after this by Scott
        /*this.playerHandRegion = playerHandRegion;
        this.opponentHandRegion = opponentHandRegion;
        this.playerActiveRegion = playerActiveRegion;
        this.opponentActiveRegion = opponentActiveRegion;*/
    }

    public void playRandom(HandRegion playerHandRegion, HandRegion opponentHandRegion, ActiveRegion playerActiveRegion, ActiveRegion opponentActiveRegion) { //Temporary code to play a random minion to test attacking functionality - Scott
        this.playerHandRegion = playerHandRegion;
        this.opponentHandRegion = opponentHandRegion;
        this.playerActiveRegion = playerActiveRegion;
        this.opponentActiveRegion = opponentActiveRegion;

        opponentActiveRegion.addCard(opponentHandRegion.getCardsInRegion().get(0));
        opponentHandRegion.removeCard(opponentHandRegion.getCardsInRegion().get(0));
    }


    private void cardLists(ArrayList<Card> cardsInRegion, int[] cardValues, boolean handRegion, boolean gatherTauntData, boolean isPlayer) { //Create lists of cards values, depending on regions chosen  - Scott
        //If it is from the board region, handCategory is false, else if hand region, its true.
        for(int i=0; i<cardsInRegion.size(); i++){ //Go through each card in the region
            if(cardsInRegion.get(i) instanceof MinionCard) { //Checking if the current card looked at is an instance of "MinionCard"
                MinionCard minion = (MinionCard) cardsInRegion.get(i); //Get the card as a minion
                int minionAttack = minion.getAttack();
                int minionHealth = minion.getHealth();
                if(gatherTauntData) { //if we need to gather taunt data
                    if(minion.getEffect() == Effect.TAUNT) { //Check if the minion is a taunt minion
                        if(isPlayer) { //if this is player board list then add to playerTauntMinionList
                            playerTauntMinionsList.add(cardsInRegion.get(i));
                        } else { //else its ai board list, so add to aiTauntMinionList
                            playerTauntMinionsList.add(cardsInRegion.get(i));
                        }
                    }
                }
                if(!handRegion) { //If we are looking at the board region..
                    cardValues[0] += minionAttack; //calculate the total attack values for all minions that player controls
                    cardValues[1] += minionHealth; //calculate the total health values for all minions that player controls
                } else { //Else we are looking at hand region
                    cardValues[0]++; //Increase the amount of minion card count
                }
            } else if(handRegion) { //If we are looking at the hand region..
                if(cardsInRegion.get(i) instanceof SpellCard) { //Checking if the current card looked at is an instance of "SpellCard"
                    cardValues[1]++; //Increase the amount of spell card count
                } else if(cardsInRegion.get(i) instanceof WeaponCard) { //Checking if the current card looked at is an instance of "WeaponCard"
                    cardValues[2]++; //Increase the amount of weapon card count
                }
            }
        }
    }

    private void resetValues(int[] valuesToReset) { //Reset values of integer arrays to prevent carried over values - Scott
        for(int i=0; i < valuesToReset.length; i++) {
            valuesToReset[i] = 0;
        }
    }

    private ArrayList<Card> getCurrentlyPlayableCards(ArrayList<Card> cardsInHand) { //Create a list of cards that can be played from hand according to their mana costs - Scott
        ArrayList<Card> playableCardsList = new ArrayList<>();
        for(int i=0; i<cardsInHand.size(); i++) { //Go through each card in the region
            if(cardsInHand.get(i).getCoinCost() < manaRemaining) { //Only if the ai has the mana to play it, should the card be considered.
                playableCardsList.add(cardsInHand.get(i)); //Add this new card to the new list
            }
        }
        return playableCardsList; //return the list of playable cards
    }

    private void aiTurnSetup(HandRegion playerHandRegion, HandRegion opponentHandRegion, ActiveRegion playerActiveRegion, ActiveRegion opponentActiveRegion, Player player) { //Setup an up to date version of all the information the AI will use to make decisions upon what moves it takes - Scott

        //reset these to default values to prevent carry over values
        resetValues(playerCardValues); resetValues(aiBoardCardValues); resetValues(cardInHandCategory);

        this.playerHandRegion = playerHandRegion; //Get a capture of data from all the current regions
        this.opponentHandRegion = opponentHandRegion;
        this.playerActiveRegion = playerActiveRegion;
        this.opponentActiveRegion = opponentActiveRegion;
        humanPlayer = player;

        cardsInAIHandRegion = opponentHandRegion.getCardsInRegion(); //get a copy of all the cards the AI has in hand
        cardsInAIBoardRegion = opponentActiveRegion.getCardsInRegion(); //get a copy of all the cards the AI has in play
        cardsInPlayerBoardRegion = playerActiveRegion.getCardsInRegion(); //get a copy of all the cards the player has in play
        cardsPlayableInAIHand = getCurrentlyPlayableCards(cardsInAIHandRegion); //get a copy of the actually playable cards from hand

        //Create an ArrayList, that at maximum can have the amount of all cards in play (always <=20) for minions that only have taunt. Reset to 0 here to prevent previous data interfering.
        playerTauntMinionsList.clear();
        aiTauntMinionsList.clear();

        playerHealth = humanPlayer.getCurrentHealth() + humanPlayer.getCurrentArmor(); //get the players total "health" by combining health and armour
        aiHealth = getCurrentHealth() + getCurrentArmor(); //get the ai total "health" by combining health and armour
        boardspaceRemaining = opponentActiveRegion.getMaxNumCardsInRegion()-cardsInAIBoardRegion.size(); //calculate how many cards the ai can play
        handspaceRemaining = opponentHandRegion.getMaxNumCardsInRegion()-cardsInAIHandRegion.size(); //calculate how many cards you can draw before they burn
        manaRemaining = getCurrentMana(); //get the current amount of mana the ai has

        cardLists(cardsInAIBoardRegion, aiBoardCardValues, false, true, false); //Create a list of cards values for ai board
        cardLists(cardsInPlayerBoardRegion, playerCardValues, false, true, true); //Create a list of cards values for player board
        cardLists(cardsInAIHandRegion, cardInHandCategory, true, false, false); //Create a list of cards values for ai hand

    }

    private void aiMovesSetup() { // Scott
        if(playerHealth <= aiBoardCardValues[0]){ //If the current minions on board have an attack that would kill the player
            if(playerTauntMinionsList.isEmpty()) { //If the player has no minions with taunt (blocking an attack)
                aiCardListAttack(cardsInAIBoardRegion,null,true);
            } else {
                int tauntMinionsHealthTotal = 0;
                ArrayList<MinionCard> tauntMinion = createMinionCards(playerTauntMinionsList);
                for(int i=0; i<tauntMinion.size();i++) {
                    tauntMinionsHealthTotal += tauntMinion.get(i).getHealth();
                }
                if(tauntMinionsHealthTotal+playerHealth <= aiBoardCardValues[0]) { //If there is lethal still after health and taunt minion health
                    //calculate minions to attack taunts, then rest to face. Make sure minions attacking taunts are closest value
                } else {
                    //try seeing about spell and weapon cards and hircine hero power for lethal
                }
            }
        } else {
            //try seeing about spell and weapon cards and hircine hero power for lethal
        }

        //next look at how to defend the most effective way against enemy minions if they have lethal, and without, including placing taunt minions, spells, hero power

        //then after this play some new minions

        //do a check if a card is a weapon
        if(isWeaponEquipped()) {
            //only equip a new weapon if lethal or preventing enemy lethal.
        } else {
            //equip a weapon is a possible move
        }
    }

    private ArrayList<MinionCard> createMinionCards(ArrayList<Card> cardsToBeChanged) { //Scott
        ArrayList<MinionCard> tauntMinion = new ArrayList<>();
        for(int i=0; i<playerTauntMinionsList.size();i++) {
            tauntMinion.add((MinionCard) playerTauntMinionsList.get(i));
        }
        return tauntMinion;
    }

    private void findClosestMinionValues(ArrayList<Card> minionsList, int valueToFind) { //Scott
        for(int i = 0; i<minionsList.size();i++) {
            //if findingvalue=value, else
            //findingvalue < currentFound && findingvalue >= value
        }
    }

    private void aiCardListAttack(ArrayList<Card> listOfAttackingMinions, Card target, boolean attackingHero){ //Scott
        for(int i=0; i<listOfAttackingMinions.size(); i++){
            aiAttack(listOfAttackingMinions.get(i), target, attackingHero);
        }
    }

    private void aiAttack(Card attacker, Card target, boolean attackingHero) { //Allow you to attack heros or minions - Scott
        if(attacker instanceof MinionCard) { //Checking if the current card looked at is an instance of "MinionCard"
            MinionCard attackingMinion = (MinionCard) attacker; //Get the card as a minion
            if(attackingHero) { //Attack the enemy hero
                attackingMinion.attackEnemy(attackingMinion, humanPlayer); //Attack the hero
            } else if(target instanceof MinionCard) { //Attack the enemy card
                MinionCard targetMinion = (MinionCard) target; //Get the card as a minion
                attackingMinion.attackEnemy(attackingMinion, targetMinion); //Attack the minion
            }
        }
    }



    private void aiAttacks(String attackType) { //Scott
        switch(attackType) {
            //fill out the different attack types
            case "minionLethal":
                //attack minions to face
                break;

            default:
                break;
        }
    }

    public void aiTurn(HandRegion playerHandRegion, HandRegion opponentHandRegion, ActiveRegion playerActiveRegion, ActiveRegion opponentActiveRegion, Player player) { // Scott
        aiTurnSetup(playerHandRegion, opponentHandRegion, playerActiveRegion, opponentActiveRegion, player);
        aiMovesSetup();
        //cards that can be played (weapons if no weapon slot filled, spells always unless mana)
        //cards that can attack
        //any taunt minions in the way
        //if ai can attack with weapon

    }
}

