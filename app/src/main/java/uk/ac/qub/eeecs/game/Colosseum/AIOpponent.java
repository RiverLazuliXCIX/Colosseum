package uk.ac.qub.eeecs.game.Colosseum;

import android.graphics.Bitmap;

import java.util.ArrayList;

import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.game.Colosseum.Regions.ActiveRegion;
import uk.ac.qub.eeecs.game.Colosseum.Regions.HandRegion;

/*
 * Authored by Scott Barham & Kyle Corrigan
 * An extension of the player class, with the addition of AI behaviors
 */

public class AIOpponent extends Player {

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
    private ArrayList<Integer> playerTauntMinionsHealthValues;

    public AIOpponent(GameScreen gameScreen, String hero){
        super(gameScreen, hero); //-Kyle

        setPortraitYPos(opponentPortraitYPos); //-Kyle
        setAbilityFrameYPos(abilityFrameYPos); //-Kyle
        createHeroAbilityButton(); //-Kyle

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

    /**
     *
     * @param playerHandRegion
     * @param opponentHandRegion
     * @param playerActiveRegion
     * @param opponentActiveRegion
     * @param player
     */
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

        //Create an ArrayList, that at maximum can have the amount of all cards in play (always <=11) for minions that only have taunt. Also ArrayList for the different health values.
        playerTauntMinionsList = new ArrayList<>();
        aiTauntMinionsList  = new ArrayList<>();
        playerTauntMinionsHealthValues  = new ArrayList<>();

        playerHealth = humanPlayer.getCurrentHealth() + humanPlayer.getCurrentArmor(); //get the players total "health" by combining health and armour
        aiHealth = getCurrentHealth() + getCurrentArmor(); //get the ai total "health" by combining health and armour
        boardspaceRemaining = opponentActiveRegion.getMaxNumCardsInRegion()-cardsInAIBoardRegion.size(); //calculate how many cards the ai can play
        handspaceRemaining = opponentHandRegion.getMaxNumCardsInRegion()-cardsInAIHandRegion.size(); //calculate how many cards you can draw before they burn
        manaRemaining = getCurrentMana(); //get the current amount of mana the ai has

        cardLists(cardsInAIBoardRegion, aiBoardCardValues, false, true, false); //Create a list of cards values for ai board
        cardLists(cardsInPlayerBoardRegion, playerCardValues, false, true, true); //Create a list of cards values for player board
        cardLists(cardsInAIHandRegion, cardInHandCategory, true, false, false); //Create a list of cards values for ai hand

    }

    /**
     *
     */
    private void aiMovesSetup() { // Scott
        if(cardsInAIBoardRegion.size()!=0 || cardsInAIHandRegion.size()!=0) { //If the hand or board isnt empty..
            if (cardsInAIBoardRegion.size() != 0) { //If the board isnt empty
                int usedMinionDamage = 0;

                ArrayList<MinionCard> aiMinionsList = createMinionCardsList(cardsInAIHandRegion);
                if (playerHealth <= aiBoardCardValues[0]) { //If the current minions on board have an attack that would kill the player
                    if (playerTauntMinionsList.isEmpty()) { //If the player has no minions with taunt (blocking an attack)
                        aiCardListAttack(aiMinionsList, null, true);

                    } else {
                        int tauntMinionsHealthTotal = 0; //A total number for the combined taunt minions health.
                        ArrayList<MinionCard> tauntMinions = createMinionCardsList(playerTauntMinionsList); //create a list of minion cards
                        for (int i = 0; i < tauntMinions.size(); i++) {
                            tauntMinionsHealthTotal += tauntMinions.get(i).getHealth(); //Calculate a total number of the combined taunt minions health.
                            playerTauntMinionsHealthValues.add(tauntMinions.get(i).getHealth()); //Create a separated list of the individual health values
                        }

                        if (tauntMinionsHealthTotal + playerHealth <= aiBoardCardValues[0]) { //If there is lethal still after health and taunt minion health
                            //calculate minions to attack taunts, then rest to face. Make sure minions attacking taunts are closest value
                            int position = 0;
                            for (int i = 0; i < playerTauntMinionsHealthValues.size(); i++) { //Go through all possible taunt minions on board
                                position = findClosestMinionValues(aiMinionsList, playerTauntMinionsHealthValues.get(i)); //Find a minion that can kill the taunt in one hit with closest attack value to its health

                                if (position == -1) { //If we couldnt find a minion that could kill the taunt in one hit
                                    ArrayList<Integer> minimumNumberOfMinions = new ArrayList<>(); //Create storage for the minimum number of minions which could kill the taunt off
                                    minimumNumberOfMinions.addAll(findMinimumMinionsToKill(aiMinionsList, playerTauntMinionsHealthValues.get(i))); //Create a list of minions which can kill off that taunt minion effectively

                                    for(int j=0; j<minimumNumberOfMinions.size(); j++) {
                                        aiAttack(aiMinionsList.get(minimumNumberOfMinions.get(j)), tauntMinions.get(i), false); //Attack with the found minions
                                        usedMinionDamage += aiMinionsList.get(minimumNumberOfMinions.get(j)).getAttack();  //Attack the taunt
                                    }
                                } else { //We found a minion which can kill the taunt in one hit
                                    aiAttack(aiMinionsList.get(position), tauntMinions.get(i), false);
                                    usedMinionDamage += aiMinionsList.get(position).getAttack(); //Attack the taunt
                                }

                            }
                            if(usedMinionDamage + playerHealth <= aiBoardCardValues[0]) { //If we still have enough damage to kill (as already attacked minions cant attack again)
                                aiCardListAttack(aiMinionsList, null, true); //Attack the hero
                            } else {
                                //If weapons or spells worked, checks would be added here to see if their damage applied would have caused lethal
                                if(manaRemaining>=2 && getHero()=="Hircine") {
                                    if(usedMinionDamage + playerHealth <= aiBoardCardValues[0]+2) { //Can we kill with the hero power which deals 2 damage to the enemy
                                        aiCardListAttack(aiMinionsList, null, true); //Attack the hero
                                        useHeroAbilities(); //Deals bonus 2 damage if hero is Hircine.
                                    }
                                }
                            }

                        }
                    }
                } else { //Else currently the onboard minions cannot kill the player with just their damage.
                    //If weapons or spells worked, checks would be added here to see if their damage applied would have caused lethal
                    if(manaRemaining>=2 && getHero()=="Hircine") {
                        if(playerHealth <= aiBoardCardValues[0]+2) { //Can we kill with the hero power which deals 2 damage to the enemy
                            aiCardListAttack(aiMinionsList, null, true); //Attack the hero
                            useHeroAbilities(); //Deals bonus 2 damage if hero is Hircine.
                        }
                    }
                }

                //next look at how to defend the most effective way against enemy minions if they have lethal, and without, including placing taunt minions, spells, hero power
            }
            if(cardsInAIHandRegion.size()!=0) //If the hand isnt empty
            {
                //play some minions
            }
        }
        //do hero powers and already equipped weapon stuff here

        //do a check if a card is a weapon
        if (isWeaponEquipped()) {
            //only equip a new weapon if lethal or preventing enemy lethal.
        } else {
            //equip a weapon is a possible move
        }

    }

    private ArrayList<MinionCard> createMinionCardsList(ArrayList<Card> cardsToBeChanged) { //Scott
        ArrayList<MinionCard> minionList = new ArrayList<>();
        for(int i=0; i<cardsToBeChanged.size();i++) {
            if(cardsToBeChanged.get(i) instanceof MinionCard) { //Checking if the current card looked at is an instance of "MinionCard"
                minionList.add((MinionCard) cardsToBeChanged.get(i));
            }
        }
        return minionList;
    }

    private int findClosestMinionValues(ArrayList<MinionCard> minionsList, int valueToFind) { //Find a minion which can kill off the other minion with the most efficient damage. -Scott
        int positionOfCard = -1, closestAttack = Integer.MAX_VALUE;
        for(int i = 0; i<minionsList.size();i++) {
            if (minionsList.get(i).getCanAttack()) { //Check the minion hasnt attacked already this turn
                int minionAttack = minionsList.get(i).getAttack(); //Get the value of the minion attack
                if (minionAttack == valueToFind) { //If the minion attack is equal to value to find (taunt/enemy minion health)
                    return i; //Use this card to kill the minion
                } else if (minionAttack > valueToFind && minionAttack < closestAttack) { //Find a value thats closest to the enemy health, but is also bigger than it so it kills in one attack
                    closestAttack = minionAttack;
                    positionOfCard = i;
                }
            }
        }
        return positionOfCard; //return any cards found
    }

    private ArrayList<Integer> findMinimumMinionsToKill(ArrayList<MinionCard> minionsList, int valueToFind) { //Find a combination of minions to kill a minion efficiently, if a single minion was previously not able.
        ArrayList<Integer> minimumNumberOfMinionsPositions = new ArrayList<>(); //Create a list for minion positions
        ArrayList<Integer> cardAttackValues = new ArrayList<>(); //Create a list of all cards attack values
        int closestAttack = 0, closestAttack2 = 0; //Create the closest attacks for trying to create valueToFind
        for (int i = 0; i < minionsList.size(); i++) {
            cardAttackValues.add(minionsList.get(i).getAttack()); //Create a list of minions attack values
        }
        for (int i = 0; i < cardAttackValues.size(); i++) { //Iterate through all the values available
            for (int j = 0; j < cardAttackValues.size(); j++) {
                if (valueToFind != closestAttack + closestAttack2) { //If the value to find isnt already made up on closestAttacks
                    if (j != i) { //If the searches isnt looking at same card
                        if (minionsList.get(j).getCanAttack() && minionsList.get(i).getCanAttack()) { //Check either of the minions haven't attacked already this turn
                            if (closestAttack + closestAttack2 < valueToFind) { //If the closest attacks are less than value to find (first iteration will go here)
                                if (cardAttackValues.get(i) + cardAttackValues.get(i) == valueToFind) { //See if currently looked at cards are the ones we want
                                    closestAttack = cardAttackValues.get(i); //Set the new closest attack
                                    closestAttack2 = cardAttackValues.get(j); //Set the new closest attack2
                                    minimumNumberOfMinionsPositions.clear(); //Clear any leftover values
                                    minimumNumberOfMinionsPositions.add(i, j); //Add in the two new positions of values found
                                } else if ((cardAttackValues.get(i) + cardAttackValues.get(j) > valueToFind) || (cardAttackValues.get(i) + cardAttackValues.get(j) > closestAttack + closestAttack2)) {
                                    //If the added values are greater than the value we want to find, or are just bigger than previously held values
                                    closestAttack = cardAttackValues.get(i); //Set the new closest attack
                                    closestAttack2 = cardAttackValues.get(j); //Set the new closest attack2
                                    minimumNumberOfMinionsPositions.clear(); //Clear any leftover values
                                    minimumNumberOfMinionsPositions.add(i, j); //Add in the two new positions of values found
                                }
                            } else if ((cardAttackValues.get(i) + cardAttackValues.get(j) >= valueToFind) && (cardAttackValues.get(i) + cardAttackValues.get(j) < closestAttack + closestAttack2)) {
                                //If the added values are what we are looking for, or bigger than the value, as well as being smaller than previously stored added values, then store these as they are closer
                                closestAttack = cardAttackValues.get(i); //Set the new closest attack
                                closestAttack2 = cardAttackValues.get(j); //Set the new closest attack2
                                minimumNumberOfMinionsPositions.clear(); //Clear any leftover values
                                minimumNumberOfMinionsPositions.add(i, j); //Add in the two new positions of values found
                            }
                        }
                    }
                }
            }
        }

        if (closestAttack + closestAttack2 < valueToFind) { //If we found values that were closer, but didnt kill off the minion, we need to find another minion to make the value equal or greater than
            int newValueToFind = valueToFind - closestAttack + closestAttack2; //Create a new value to found
            int position = findClosestMinionValues(minionsList, newValueToFind); //Use the single minion finding method, see if we can kill the minion with just one more minion.
            if (position == -1) { //If we couldnt find a single minion addition to kill off the target
                minimumNumberOfMinionsPositions.addAll(findMinimumMinionsToKill(minionsList, newValueToFind)); //Recursively call this method to find more minions until we hit the target value and kill off the minion
            }
        }
        return minimumNumberOfMinionsPositions; //When we have the list of minions positions needed, return these
    }

    private void aiCardListAttack(ArrayList<MinionCard> listOfAttackingMinions, MinionCard target, boolean attackingHero){ //Scott
        for(int i=0; i<listOfAttackingMinions.size(); i++){
            aiAttack(listOfAttackingMinions.get(i), target, attackingHero);
        }
    }

    private void aiAttack(MinionCard attacker, MinionCard target, boolean attackingHero) { //Allow you to attack heros or minions - Scott
        if(attackingHero) { //Attack the enemy hero
            attacker.attackEnemy(attacker, humanPlayer); //Attack the hero
        } else { //Attack the enemy card
            attacker.attackEnemy(attacker, target); //Attack the minion
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
