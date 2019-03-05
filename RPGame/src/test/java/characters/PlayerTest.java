package characters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c1644043 on 19/04/2017.
 */
public class PlayerTest {
    private Player player;

    @Before
    public void setUp() throws Exception {
        player = new Player();

    }

    @Test
    public void negateEnergySubtractsOneFromPlayerEnergy() throws Exception {
        player.negateEnergy();
        assertEquals(9, player.getEnergy());
    }

    @Test
    public void dealDamageCalcsTheNewHealthValue() throws Exception {
        player.setHealth(player.dealDamage(10));
        assertEquals(40, player.getHealth());
    }

    @Test
    public void playerStartsWithCorrectHealth() throws Exception {
        assertEquals(50, player.getHealth());
    }

    @Test
    public void playerStartsWithCorrectEnergy() throws Exception {
        assertEquals(10, player.getEnergy());
    }

    @Test
    public void playerStartsWithCorrectStrength() throws Exception {
        assertEquals(5, player.getStrength());
    }

    @Test
    public void playerStartsWithCorrectSpeed() throws Exception {
        assertEquals(5, player.getSpeed());
    }

    @Test
    public void playerStartsWithNothingEquippedInLeftHand() throws Exception {
        assertEquals("Fist", player.getEquippedItems()[0].getName());
    }

    @Test
    public void playerStartsWithNothingEquippedInRightHand() throws Exception {
        assertEquals("Fist", player.getEquippedItems()[1].getName());
    }

    @Test
    public void playerStartsWithEmptyInventory() throws Exception {
        assertEquals(0, player.getInventory().size());
    }

    @Test
    public void calcTotalValueAdditionWorks() throws Exception {
        player.setSpeed(player.calcTotalAttributeValue("speed", 50, true));
        assertEquals(55, player.getSpeed());
    }

    @Test
    public void calcTotalValueSubtractionWorks() throws Exception {
        player.setStrength(player.calcTotalAttributeValue("strength", 2, false));
        assertEquals(3, player.getStrength());
    }

    @Test
    public void playerStatusAppearsCorrectly() throws Exception {
        assertEquals("<<<<<< Player Status >>>>>>" + "\r\n" +
                "HEALTH: " + 50 + "\r\n" +
                "ENERGY: " + 10 + "\r\n" +
                "EQUIPPED: " + "Fist" + ", " + "Fist" + "\r\n" +
                "<<<<<<<<<<<<<>>>>>>>>>>>>>>", player.toString());
    }
}