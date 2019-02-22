package uk.ac.qub.eeecs.gage;

import android.graphics.Bitmap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.ac.qub.eeecs.gage.engine.AssetManager;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.game.Colosseum.Player;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
        player.increaseCurrentMana(1);

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
        player.increaseCurrentMana(3);

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
     * Deal weapon damage to reduce durability to 0 boundary case
     */
    @Test
    public void player_weaponAttackReduceDurabilityToZero() {
        // Define expected properties
        int expectedDurability = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setWeaponEquipped(true);

        player.setCurrentWeaponDurability(1);

        // Deal weapon damage reduce durability by 1
        player.dealWeaponDamage();

        // Test that player weapon durability is reduced to the expected amount
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
    }

    /*
     * Deal weapon damage to reduce durability to above 0, acceptable case
     */
    @Test
    public void player_weaponAttackReduceDurabilityAboveZero() {
        // Define expected properties
        int expectedDurability = 1;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setCurrentWeaponDurability(2);
        player.setWeaponEquipped(true);

        // Deal weapon damage reduce durability by 1
        player.dealWeaponDamage();

        // Test that player weapon durability is reduced to the expected amount
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
    }

    /*
     * Deal weapon damage to reduce durability to below 0, should fail, and durability stays at 0
     */
    @Test
    public void player_weaponAttackReduceDurabilityBelowZero() {
        // Define expected properties
        int expectedDurability = 0;
        String hero = "c";

        // Create a new player instance
        Player player = new Player(gameScreen,hero);
        player.setWeaponEquipped(true);
        player.setCurrentWeaponDurability(0);

        // Deal weapon damage reduce durability by 1
        player.dealWeaponDamage();

        // Test that player weapon durability is reduced to the expected amount
        assertEquals(expectedDurability,player.getCurrentWeaponDurability());
    }

    //TODO Update hero weapon attack tests for attacking minions and other enemy heroes


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
        player.commodusAbilityUsed();

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
        player.commodusAbilityUsed();

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
        player.commodusAbilityUsed();

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
        player.commodusAbilityUsed();

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
        player.marsAbilityUsed();

        // Test that mana has been reduced by 2, armor has been increased by 2 and ability used this
        // turn is true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedArmor,player.getCurrentArmor());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /*
     * Using Mars' ability with insufficient mana should not increase player armor, and the ability
     * should not be expected.
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
        player.marsAbilityUsed();

        // Test that mana has been reduced by 2, armor has been increased by 2 and ability used this
        // turn is true
        assertEquals(expectedCurrentMana,player.getCurrentMana());
        assertEquals(expectedArmor,player.getCurrentArmor());
        assertEquals(expectedAbilityUsed,player.isAbilityUsedThisTurn());

    }

    /**
     * ----REPLACE BRUTALUS ABILITY TESTS HERE WHEN FULLY IMPLEMENTED----
     */

    /**
     * ----REPLACE SAGIRA ABILITY TESTS HERE WHEN FULLY IMPLEMENTED----
     */

    

}
