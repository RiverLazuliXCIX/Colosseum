
package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.ui.PushButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.AIOpponent;
import uk.ac.qub.eeecs.game.Colosseum.Effect;
import uk.ac.qub.eeecs.game.Colosseum.MinionCard;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Kyle Corrigan
 */

@RunWith(MockitoJUnitRunner.class)
public class PlayerClassTest {

    // /////////////////////////////////////////////////////////////////////////
    // Mocking setup
    // /////////////////////////////////////////////////////////////////////////
    @Mock
    private Game game;
    @Mock
    private GameScreen gameScreen;
    @Mock
    private AssetManager assetManager;
    @Mock
    private Bitmap bitmap;
    @Mock
    private Input input;
    @Mock
    private LayerViewport layerViewport;

    @Before
    public void setUp() {
        when(game.getAssetManager()).thenReturn(assetManager);
        when(assetManager.getBitmap(any(String.class))).thenReturn(bitmap);
        when(game.getInput()).thenReturn(input);
        when(gameScreen.getGame()).thenReturn(game);
        when(gameScreen.getName()).thenReturn("colosseumDemoScreen");
        when(gameScreen.getDefaultLayerViewport()).thenReturn(layerViewport);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing player constructor
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Test to ensure that the player constructor creates a player object
     */
    @Test
    public void player_ConstructorTest() {

        String hero = "c";
        Player player = new Player(gameScreen,hero);

        // Test to ensure that a player object is created
        assertNotNull(player);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing getters and setters
    // /////////////////////////////////////////////////////////////////////////

    @Test
    public void player_GettersAndSettersTest() {

        // Setting up player and pushbutton for testing getters and setters
        PushButton testHeroAbility = new PushButton(1,1,1,1,"TestBitmap",gameScreen);
        Player player = new Player(gameScreen,"TestHero");

        int expectedCurrentHealth = 1;
        int expectedCurrentManaCap = 2;
        int expectedCurrentMana = 3;
        int expectedCurrentArmor = 4;
        int expectedCurrentAttack = 5;
        int expectedCurrentWeaponDurability = 6;
        boolean expectedYourTurn = true;
        float expectedPortraitXPos = 7.0f;
        float expectedPortraitYPos = 8.0f;
        float expectedPortraitHeight = 35.0f;
        float expectedPortraitWidth = 35.0f;
        float expectedAbilityXPos = 9.0f;
        float expectedAbilityYPos = 10.0f;
        float expectedAbilityHeight = expectedPortraitHeight-15;
        float expectedAbilityWidth = expectedPortraitWidth-15;
        boolean expectedWeaponEquipped = true;
        boolean expectedAbilityUsed = true;
        Effect expectedPEffect = Effect.TAUNT;
        int expectedEDurtion = 11;
        PushButton expectedHeroAbility = testHeroAbility;
        String expectedHero = "Hircine";
        int expectedMaxHealth = 30;
        int expectedMaxMana = 10;
        int expectedAbilityCost = 2;

        // Setting values using the setters from the player class
        player.setCurrentHealth(1);
        player.setCurrentManaCap(2);
        player.setCurrentMana(3);
        player.setCurrentArmor(4);
        player.setCurrentAttack(5);
        player.setCurrentWeaponDurability(6);
        player.setYourTurn(true);
        player.setPortraitXPos(7.0f);
        player.setPortraitYPos(8.0f);
        player.setAbilityFrameXPos(9.0f);
        player.setAbilityFrameYPos(10.0f);
        player.setWeaponEquipped(true);
        player.setAbilityUsedThisTurn(true);
        player.setPEffect(Effect.TAUNT);
        player.setEDuration(11);
        player.setHeroAbility(testHeroAbility);
        player.setHero("Hircine");

        // retrieving actual values using the getters
        assertEquals(expectedCurrentHealth, player.getCurrentHealth());
        assertEquals(expectedCurrentManaCap, player.getCurrentManaCap());
        assertEquals(expectedCurrentArmor, player.getCurrentArmor());
        assertEquals(expectedCurrentMana, player.getCurrentMana());
        assertEquals(expectedCurrentAttack, player.getCurrentAttack());
        assertEquals(expectedCurrentWeaponDurability, player.getCurrentWeaponDurability());
        assertEquals(expectedYourTurn, player.getYourTurn());
        assertEquals(expectedPortraitXPos, player.getPortraitXPos(), 0.0f);
        assertEquals(expectedPortraitYPos, player.getPortraitYPos(),0.0f);
        assertEquals(expectedAbilityXPos, player.getAbilityFrameXPos(),0.0f);
        assertEquals(expectedAbilityYPos, player.getAbilityFrameYPos(),0.0f);
        assertEquals(expectedWeaponEquipped, player.isWeaponEquipped());
        assertEquals(expectedAbilityUsed, player.isAbilityUsedThisTurn());
        assertEquals(expectedPEffect, player.getPEffect());
        assertEquals(expectedEDurtion, player.getEDuration());
        assertEquals(expectedHero, player.getHero());
        assertEquals(expectedMaxHealth, Player.getMaxHealth());
        assertEquals(expectedMaxMana, Player.getMaxMana());
        assertEquals(expectedAbilityCost, Player.getHeroAbilityCost());
        assertEquals(expectedPortraitHeight, expectedPortraitHeight,0.0f);
        assertEquals(expectedPortraitWidth, expectedPortraitWidth,0.0f);
        assertEquals(expectedAbilityHeight, expectedAbilityHeight,0.0f);
        assertEquals(expectedAbilityWidth, expectedAbilityWidth,0.0f);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing receiveDamage();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Damages the player by 6 from the default health value of 30 with 0 armor
     * Expecting 24 health and 0 Armor to remain.
     */
    @Test
    public void player_damageCurrentHealthNoArmor() {
        // Define expected properties
        int expectedHealth = 24;
        int expectedArmor = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentHealth(30);

        // Damage the player character, from default health of 30
        player.receiveDamage(6);

        // Test that after taking damage health is decreased to the correct amount
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    /*
     * Damages the player by 6 from the default health value of 30 and added 3 armor
     * Expecting 27 health and 0 Armor to remain.
     */
    @Test
    public void player_damageCurrentHealthWithArmor() {
        // Define expected properties
        int expectedHealth = 27;
        int expectedArmor = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);

        // Increases player armor by 3 from it's default value of 0
        player.increaseArmor(3);
        // Damage the player character, from default health of 30
        player.receiveDamage(6);

        // Test that after taking damage health is decreased to the correct amount and armor is zero
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }


    /*
     * Damages the player by 3 from the default health value of 30 with added 3 armor
     * Expecting 30 health and 0 Armor to remain.
     */
    @Test
    public void player_damageReducesArmorToZero() {
        // Define expected properties
        int expectedHealth = 30;
        int expectedArmor = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentHealth(30);
        // Increases player armor by 3 from it's default value of 0
        player.increaseArmor(3);
        // Damage the player character, from default health of 30
        player.receiveDamage(3);

        // Test that after taking damage health is not decreased, and armor is reduced to zero
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    /*
     * Damages the player by 1 from the default health value of 30 with 3 armor
     * Expecting 30 health and 2 Armor to remain.
     */
    @Test
    public void player_damageReducesArmorToAboveZero() {
        // Define expected properties
        int expectedHealth = 30;
        int expectedArmor = 2;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);

        // Increases player armor by 3 from it's default value of 0
        player.increaseArmor(3);
        // Damage the player character, from default health of 30
        player.receiveDamage(1);

        // Test that after taking damage health is not decreased, and armor is reduced to zero
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing heal();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Heal the player 2 points from 28 to the health cap, expecting the health to reach the cap of
     * 30
     */
    @Test
    public void player_healToCap() {
        // Define expected properties
        int expectedHealth = 30;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentHealth(28);

        // Heals the player 2 points of damage back to max of 30
        player.heal(2);

        // Test that player heals to the expected health cap of 30
        assertEquals(expectedHealth,player.getCurrentHealth());
    }

    /*
     * Heal the player 1 points from 28 to 29.
     */
    @Test
    public void player_healToBelowCap() {
        // Define expected properties
        int expectedHealth = 29;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentHealth(28);

        // Heals the player  points of damage back to 29
        player.heal(1);

        // Test that player heals to the expected health of 29
        assertEquals(expectedHealth,player.getCurrentHealth());
    }

    /*
     * Heal the player 10 points from 30 (Expected result is 30, as it is the health cap)
     */
    @Test
    public void player_healOverCap() {
        // Define expected properties
        int expectedHealth = 30;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentHealth(30);

        // Heals the player by 10
        player.heal(10);

        // Test to ensure that the player's health cannot rise above 30
        assertEquals(expectedHealth,player.getCurrentHealth());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing reduceCurrentMana();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Try to reduce mana while at 0 (Should fail, mana should stay at 0)
     */
    @Test
    public void player_reduceManaWhileAtZero() {
        // Define expected properties
        int expectedMana = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentMana(0);

        // Reduces player mana by 1
        player.reduceCurrentMana(1);

        // Test that player mana is not reduced while at 0
        assertEquals(expectedMana,player.getCurrentMana());
    }

    /*
     * Reduce mana to value above 0
     */
    @Test
    public void player_reduceManaToAboveZero() {
        // Define expected properties
        int expectedMana = 1;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentMana(2);

        // Reduces player mana by 1
        player.reduceCurrentMana(1);

        // Test that player mana is reduced to the expected amount
        assertEquals(expectedMana,player.getCurrentMana());
    }

    /*
     * Testing mana being reduced to 0
     */
    @Test
    public void player_reduceManaToZero() {
        // Define expected properties
        int expectedMana = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentMana(1);

        // Reduces player mana by 1
        player.reduceCurrentMana(1);

        // Test that player mana is reduced to the expected amount
        assertEquals(expectedMana,player.getCurrentMana());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing increaseCurrentMana();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Increasing mana from 0 to 1
     */
    @Test
    public void player_increaseManaFromZero() {
        // Define expected properties
        int expectedMana = 1;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentMana(0);

        // Increase player mana by 1
        player.increaseCurrentMana();

        // Test that player mana is increased to the expected amount
        assertEquals(expectedMana,player.getCurrentMana());
    }


    /*
     * Increasing current mana by 3 while player has a mana level of 1
     */
    @Test
    public void player_increaseManaWhileManaPresent() {
        // Define expected properties
        int expectedMana = 4;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentMana(1);

        // increase player mana by 3
        player.increaseCurrentMana();

        // Test that player mana is increased to the expected amount
        assertEquals(expectedMana,player.getCurrentMana());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing increaseCurrentManaCap();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Increasing mana cap from 9 to 10, acceptable amount
     */
    @Test
    public void player_increaseManaCapAcceptable() {
        // Define expected properties
        int expectedManaCap = 10;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentManaCap(9);

        // increase the player mana cap by 1
        player.increaseCurrentManaCap();

        // Test that player mana cap is increased to the expected amount
        assertEquals(expectedManaCap,player.getCurrentManaCap());
    }

    /*
     * Attempting to increase the mana cap while at the maximum value of 10, should stay at 10
     * boundary case
     */
    @Test
    public void player_increaseManaCapAboveMaximum() {
        // Define expected properties
        int expectedManaCap = 10;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentManaCap(10);

        // increase the player mana cap by 1
        player.increaseCurrentManaCap();

        // Test that player mana cap is increased to the expected amount
        assertEquals(expectedManaCap,player.getCurrentManaCap());
    }

    /*
     * Increasing mana cap from 0 to 1, acceptable case
     */
    @Test
    public void player_increaseManaCapfromZero() {
        // Define expected properties
        int expectedManaCap = 1;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentManaCap(0);

        // increase the player mana cap by 1
        player.increaseCurrentManaCap();

        // Test that player mana cap is increased to the expected amount
        assertEquals(expectedManaCap,player.getCurrentManaCap());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing increaseArmor();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Increasing armor from 0 to 3, acceptable amount
     */
    @Test
    public void player_increaseArmorFromZero() {
        // Define expected properties
        int expectedArmor = 3;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentArmor(0);

        // increase the player armor by 3
        player.increaseArmor(3);

        // Test that player armor is increased to the expected amount
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    /*
     * Increasing armor from 3 to 6, acceptable amount
     */
    @Test
    public void player_increaseArmorWhileArmorPresent() {
        // Define expected properties
        int expectedArmor = 6;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentArmor(3);

        // increase the player armor by 3
        player.increaseArmor(3);

        // Test that player armor is increased to the expected amount
        assertEquals(expectedArmor,player.getCurrentArmor());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing reduceAttack();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Reduce attack from 1 to 0, boundary case
     */
    @Test
    public void player_reduceAttackToZero() {
        // Define expected properties
        int expectedAttack = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentAttack(1);

        // Reduce the player attack by 1
        player.reduceAttack(1);

        // Test that player attack is decreased to the expected amount
        assertEquals(expectedAttack,player.getCurrentAttack());
    }

    /*
     * Reduce attack to value below zero, should stop with an attack value of 0
     */
    @Test
    public void player_reduceAttackBelowZero() {
        // Define expected properties
        int expectedAttack = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentAttack(3);

        // Reduce the player attack by 4
        player.reduceAttack(4);

        // Test that player attack is decreased to the expected amount
        assertEquals(expectedAttack,player.getCurrentAttack());
    }

    /*
     * Reduce attack from 2 to 1 acceptable case
     */
    @Test
    public void player_reduceAttackAboveZero() {
        // Define expected properties
        int expectedAttack = 1;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentAttack(2);

        // Reduce the player attack by 1
        player.reduceAttack(1);

        // Test that player attack is decreased to the expected amount
        assertEquals(expectedAttack,player.getCurrentAttack());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing increaseAttack();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Increase attack while hero currently has no attack available
     */
    @Test
    public void player_increaseAttackFromZero() {
        // Define expected properties
        int expectedAttack = 3;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentAttack(0);

        // increase the player attack by 3
        player.increaseAttack(3);

        // Test that player attack is increased to the expected amount
        assertEquals(expectedAttack,player.getCurrentAttack());
    }

    /*
     * Increase attack while hero currently has attack available
     */
    @Test
    public void player_increaseAttackaboveZero() {
        // Define expected properties
        int expectedAttack = 6;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentAttack(3);

        // increase the player attack by 3
        player.increaseAttack(3);

        // Test that player attack is increased to the expected amount
        assertEquals(expectedAttack,player.getCurrentAttack());
    }

    // /////////////////////////////////////////////////////////////////////////
    // Testing dealWeaponDamage();
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Deal 1 weapon damage to opponent, opponent health should reduce by 1 to 29
     * weapon durability should hit 0, weapon is no longer equipped, and player
     * attack reduced to 0.
     */
    @Test
    public void player_weaponAttackOpponentPlayerDurabilityReducedToZero() {
        // Define expected properties
        int expectedDurability = 0;
        boolean expectedPlayerWeaponEquipped = false;
        int expectedPlayerAttack = 0;
        int expectedOpponentHealth = 29;

        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setWeaponEquipped(true);
        player.setCurrentAttack(1);
        player.setCurrentWeaponDurability(1);

        // Create a new opponent instance
        AIOpponent opponent = new AIOpponent(gameScreen, hero);
        opponent.setCurrentHealth(30);

        // Deal weapon damage to opponent reduce durability by 1
        player.dealWeaponDamage(opponent);

        // Test that player weapon durability is reduced to the expected amount
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedPlayerWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedPlayerAttack, player.getCurrentAttack());
        assertEquals(expectedOpponentHealth,opponent.getCurrentHealth());

    }

    /*
     * Deal 1 weapon damage to minion card, reducing minion health from 10 to 9, player should receive
     * 2 damage from minion after attack reducing health from 30 to 28, and the weapon with 2 durability
     * should reduce to 1, and stay equipped.
     */

    @Test
    public void player_weaponAttackOpponentMinionDurabilityReducedAboveZero() {
        // Define expected properties
        int expectedDurability = 1;
        boolean expectedPlayerWeaponEquipped = true;
        int expectedPlayerAttack = 1;
        int expectedPlayerHealth = 28;
        int expectedMinionHealth = 9;

        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentHealth(30);
        player.setWeaponEquipped(true);
        player.setCurrentAttack(1);
        player.setCurrentWeaponDurability(2);

        // Create a new minion instance
        MinionCard minion = new MinionCard(gameScreen);
        minion.setHealth(10);
        minion.setAttack(2);

        // Deal weapon damage to minion reduce durability by 1
        player.dealWeaponDamage(minion);

        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedPlayerWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedPlayerAttack, player.getCurrentAttack());
        assertEquals(expectedPlayerHealth,player.getCurrentHealth());
        assertEquals(expectedMinionHealth,minion.getHealth());

    }

    /*
     * Attempt to deal weapon damage to reduce durability to below 0, should fail, and durability
     * set at 0, weapon equipped is set to false, attack is set to 0, and opponent health remains
     * the same (Triggers in the event a spell reduced durability to 0 or less prior to attacking)
     */
    @Test
    public void player_weaponAttackDurabilityAtZero() {
        // Define expected properties
        int expectedDurability = 0;
        boolean expectedPlayerWeaponEquipped = false;
        int expectedOpponentHealth = 30;

        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setWeaponEquipped(true);
        player.setCurrentAttack(2);
        player.setCurrentWeaponDurability(0);

        // Create a new opponent instance
        AIOpponent opponent = new AIOpponent(gameScreen, hero);
        opponent.setCurrentHealth(30);

        // Attempt to deal weapon damage to opponent
        player.dealWeaponDamage(opponent);

        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedPlayerWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedOpponentHealth,opponent.getCurrentHealth());

    }



    // /////////////////////////////////////////////////////////////////////////
    // Testing Hero Abilities
    // /////////////////////////////////////////////////////////////////////////

    /*
     * Using Emperor Commodus' ability, ability used this turn should be set to true, weapon equipped
     * should be true, durability should be 2, attack should be 1 and current mana should be 2.
     */
    @Test
    public void player_commodusAbilityNoWeaponEquipped() {
        // Define expected properties
        int expectedCurrentMana = 2;
        int expectedAttack = 1;
        int expectedDurability = 2;
        boolean expectedWeaponEquipped = true;
        boolean expectedAbilityUsed = true;

        // Create a new player instance
        String hero = "EmperorCommodus";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(4);
        player.setCurrentManaCap(4);
        player.setCurrentAttack(0);
        player.setCurrentWeaponDurability(0);
        player.setWeaponEquipped(false);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has been reduced by 2, a weapon has been equipped, player attack and weapon
        // durability are set to 1 and 2 respectively and ability used is set to true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedAttack,player.getCurrentAttack());
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Emperor Commodus' ability, ability used this turn should be set to true, weapon equipped
     * should be true, durability should be 2, attack should be 1 and current mana should be 2. Since
     * a weapon is already equipped, durability and attack should be overwritten to that of commodus'
     * weapon.
     */
    @Test
    public void player_commodusAbilityWeaponEquipped() {
        // Define expected properties
        int expectedCurrentMana = 2;
        int expectedAttack = 1;
        int expectedDurability = 2;
        boolean expectedWeaponEquipped = true;
        boolean expectedAbilityUsed = true;

        // Create a new player instance
        String hero = "EmperorCommodus";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(4);
        player.setCurrentManaCap(4);
        player.setCurrentAttack(10);
        player.setCurrentWeaponDurability(10);
        player.setWeaponEquipped(true);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has been reduced by 2, a weapon has been equipped, player attack and weapon
        // durability are set to 1 and 2 respectively and ability used is set to true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedAttack,player.getCurrentAttack());
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Emperor Commodus' ability with insufficient mana and no weapon equipped, ability used
     * this turn should be kept at false, weapon equipped should be false, durability should be 0,
     * attack should be 0 and current mana should stay at 1.
     */
    @Test
    public void player_commodusAbilityInsufficientManaNoWeaponEquipped() {
        // Define expected properties
        int expectedCurrentMana = 1;
        int expectedAttack = 0;
        int expectedDurability = 0;
        boolean expectedWeaponEquipped = false;
        boolean expectedAbilityUsed = false;

        // Create a new player instance
        String hero = "EmperorCommodus";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(1);
        player.setCurrentManaCap(1);
        player.setCurrentAttack(0);
        player.setCurrentWeaponDurability(0);
        player.setWeaponEquipped(false);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has not been deducted, ability has not been used, and weapon has not been equipped
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedAttack,player.getCurrentAttack());
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Emperor Commodus' ability with insufficient mana and a weapon already equipped, ability
     * used this turn should be kept at false, weapon equipped should stay true, durability should
     * stay 10, attack should stay 10 and current mana should stay at 1.
     */
    @Test
    public void player_commodusAbilityInsufficientManaWeaponEquipped() {
        // Define expected properties
        int expectedCurrentMana = 1;
        int expectedAttack = 10;
        int expectedDurability = 10;
        boolean expectedWeaponEquipped = true;
        boolean expectedAbilityUsed = false;

        // Create a new player instance
        String hero = "EmperorCommodus";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(1);
        player.setCurrentManaCap(1);
        player.setCurrentAttack(10);
        player.setCurrentWeaponDurability(10);
        player.setWeaponEquipped(true);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has not been deducted, ability has not been used, and weapon has not been equipped
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedAttack,player.getCurrentAttack());
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
        assertEquals(expectedWeaponEquipped,player.isWeaponEquipped());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Mars' ability should increase player armor by 2 and deduct 2 mana from the player, setting
     * ability used this turn to true. (Mars ability uses increaseArmor() to increase armor which
     * was previously tested)
     */
    @Test
    public void player_marsAbilitySufficientMana() {
        // Define expected properties
        int expectedCurrentMana = 2;
        int expectedArmor = 2;
        boolean expectedAbilityUsed = true;

        // Create a new player instance
        String hero = "Mars";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(4);
        player.setCurrentManaCap(4);
        player.setCurrentArmor(0);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has been reduced by 2, armor has been increased by 2 and ability used this
        // turn is true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedArmor,player.getCurrentArmor());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Mars' ability with insufficient mana should not increase player armor, and the ability
     * should not be used.
     */
    @Test
    public void player_marsAbilityInsufficientMana() {
        // Define expected properties
        int expectedCurrentMana = 1;
        int expectedArmor = 0;
        boolean expectedAbilityUsed = false;

        // Create a new player instance
        String hero = "Mars";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(1);
        player.setCurrentManaCap(1);
        player.setCurrentArmor(0);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has been not reduced by 2, armor has not been increased by 2 and ability used this
        // turn is false
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedArmor,player.getCurrentArmor());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Meridia's ability with insufficient mana should not increase player current health, and the ability
     * should not be used.
     */
    @Test
    public void player_meridiaAbilityInsufficientMana() {
        // Define expected properties
        int expectedCurrentMana = 1;
        int expectedHealth = 30;
        boolean expectedAbilityUsed = false;

        // Create a new player instance
        String hero = "Meridia";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentHealth(30);
        player.setCurrentMana(1);
        player.setCurrentManaCap(1);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has not been reduced by 2, health has not been increased by 2 and ability used this
        // turn is false
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Meridia's ability with sufficient mana should increase player current health to cap, and the ability
     * should be used. Meridia heals by 2, unless health is at cap, or will reach cap after use, at which point
     * it will stay at cap. (Ability doesn't cast at max health)
     */
    @Test
    public void player_meridiaAbilitySufficientManaUndamaged() {
        // Define expected properties
        int expectedCurrentMana = 2;
        int expectedHealth = 30;
        boolean expectedAbilityUsed = false;

        // Create a new player instance
        String hero = "Meridia";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(2);
        player.setCurrentManaCap(2);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has not been reduced by 2, health has not been increased by 2 and ability used this
        // turn is false
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Meridia's ability with sufficient mana should increase player current health to cap, and the ability
     * should be used. Meridia heals by 2, unless health is at cap, or will reach cap after use, at which point
     * it will stay at cap. (Checks to ensure there is no over-heal)
     */
    @Test
    public void player_meridiaAbilitySufficientManaDamagedBy1() {
        // Define expected properties
        int expectedCurrentMana = 0;
        int expectedHealth = 30;
        boolean expectedAbilityUsed = true;

        // Create a new player instance
        String hero = "Meridia";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(2);
        player.setCurrentManaCap(2);
        player.setCurrentHealth(29);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has been reduced by 2, health has been increased by 2 and ability used this
        // turn is true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Meridia's ability with sufficient mana should increase player current health to cap, and the ability
     * should be used. Meridia heals by 2, unless health is at cap, or will reach cap after use, at which point
     * it will stay at cap. (Checks to ensure it heals to cap)
     */
    @Test
    public void player_meridiaAbilitySufficientManaDamagedBy2() {
        // Define expected properties
        int expectedCurrentMana = 0;
        int expectedHealth = 30;
        boolean expectedAbilityUsed = true;

        // Create a new player instance
        String hero = "Meridia";
        Player player = new Player(gameScreen,hero);

        // Initialising test player variables
        player.setCurrentMana(2);
        player.setCurrentManaCap(2);
        player.setCurrentHealth(28);
        player.setAbilityUsedThisTurn(false);

        // Executing the method to be tested
        player.useHeroAbilities();

        // Test that mana has been reduced by 2, health has been increased by 2 and ability used this
        // turn is true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedHealth,player.getCurrentHealth());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

}