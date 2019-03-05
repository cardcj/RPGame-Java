package interactables;

import characters.Enemy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c1644043 on 25/04/2017.
 */
public class RoomTest {
    private Room testRoom;

    @Before
    public void setupRoom() throws Exception {
        testRoom = new Room("Test Room");
    }

    @Test
    public void getNameCorrect() throws Exception {
        assertEquals("Test Room", testRoom.getName());
    }

    @Test
    public void defaultRoomIsEmptyAndNotLinkedToOthers() throws Exception {
        assertEquals(0, testRoom.getDirections().size());
        assertEquals(null, testRoom.getEnemyInRoom());
        assertEquals(null, testRoom.getFoodInRoom());
        assertEquals(null, testRoom.getItemInRoom());
        assertEquals(null, testRoom.getPassItemInRoom());
    }

    @Test
    public void setAndGetDirectionsCorrect() throws Exception {
        Room testRoomTwo = new Room("Test Room 2");
        testRoom.getDirections().put("North", testRoomTwo);
        assertEquals("Test Room 2", testRoom.getDirections().get("North").getName());
    }

    @Test
    public void setAndGetEnemyInRoomCorrect() throws Exception {
        Enemy enemyOne = new Enemy("Enemy1", 10, 10, 10);
        testRoom.setEnemyInRoom(enemyOne);
        assertEquals("Enemy1", testRoom.getEnemyInRoom().getName());
    }

    @Test
    public void setAndGetFoodInRoomCorrect() throws Exception {
        Food foodOne = new Food("Food1", "Food", "health", 10);
        testRoom.setFoodInRoom(foodOne);
        assertEquals("Food1", testRoom.getFoodInRoom().getName());
    }

    @Test
    public void setAndGetItemInRoomCorrect() throws Exception {
        Item itemOne = new Item("Item1", "Item", "Strength", 10, true);
        testRoom.setItemInRoom(itemOne);
        assertEquals("Item1", testRoom.getItemInRoom().getName());
    }

    @Test
    public void setAndGetPassItemInRoomCorrect() throws Exception {
        PassiveItem pItemOne = new PassiveItem("PItem1", "PItem", "Speed", 10, false);
        testRoom.setPassItemInRoom(pItemOne);
        assertEquals("PItem1", testRoom.getPassItemInRoom().getName());
    }

    @Test
    public void gameRoomsSetupLinksRoomsCorrectly() throws Exception {
        Room entrance = new Room("Corridor");
        Room greatHall = new Room("Great Hall");
        Room cellar = new Room("Cellar");
        Room bossRoom = new Room("Boss Room");
        Room finish = new Room("Exit");

        Room.setupRooms(entrance, greatHall, cellar, bossRoom, finish);

        assertEquals("Cellar", entrance.getDirections().get("East").getName());
        assertEquals("Great Hall", entrance.getDirections().get("North").getName());
        assertEquals("Corridor", cellar.getDirections().get("West").getName());
        assertEquals("Corridor", greatHall.getDirections().get("South").getName());
        assertEquals("Boss Room", greatHall.getDirections().get("North").getName());
        assertEquals("Exit", bossRoom.getDirections().get("North").getName());
        assertEquals("Great Hall", bossRoom.getDirections().get("South").getName());
    }

}