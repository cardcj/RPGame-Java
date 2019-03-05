package interactables;

import characters.Enemy;

import java.util.HashMap;
import java.util.Random;

/**
 * Class used to represent the rooms in the game
 */
public class Room {
    private String name;
    private HashMap<String, Room> directions;
    private Item itemInRoom;
    private PassiveItem passItemInRoom;
    private Enemy enemyInRoom;
    private Food foodInRoom;

    public Room(String name) {
        this.name = name;
        this.directions = new HashMap<>();
        this.itemInRoom = null;
        this.passItemInRoom = null;
        this.enemyInRoom = null;
        this.foodInRoom = null;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Room> getDirections() {
        return directions;
    }

    public Item getItemInRoom() {
        return itemInRoom;
    }

    public void setItemInRoom(Item itemInRoom) {
        this.itemInRoom = itemInRoom;
    }

    public PassiveItem getPassItemInRoom() {
        return passItemInRoom;
    }

    public void setPassItemInRoom(PassiveItem passItemInRoom) {
        this.passItemInRoom = passItemInRoom;
    }

    public Enemy getEnemyInRoom() {
        return enemyInRoom;
    }

    public void setEnemyInRoom(Enemy enemyInRoom) {
        this.enemyInRoom = enemyInRoom;
    }

    public Food getFoodInRoom() {
        return foodInRoom;
    }

    public void setFoodInRoom(Food foodInRoom) {
        this.foodInRoom = foodInRoom;
    }

    public static void setupRooms(Room... rooms) {
        // Room in which the item or enemy is placed is random based on a random int to select a room at random
        // Random implementation retrieved from:
        // Author: Oracle
        // Date: 29 March 2017
        // Available from: https://docs.oracle.com/javase/7/docs/api/java/util/Random.html
        rooms[new Random().nextInt(rooms.length - 2)].setItemInRoom(new Item("katana", "sword", "strength", 10, true));
        rooms[new Random().nextInt(rooms.length - 2)].setPassItemInRoom(new PassiveItem("amulet", "magic amulet", "speed", 7, true));
        // -3 and +1 used to ensure that the Bandit can't be set in the first room, boss room or finish room
        rooms[new Random().nextInt(rooms.length - 3) + 1].setEnemyInRoom(new Enemy("Bandit", 20, 3, 3));
        // Setting food in Great Hall and entrance
        rooms[0].setFoodInRoom(new Food("Water", "Cup of water", "energy", 2));
        rooms[1].setFoodInRoom(new Food("Bread", "Loaf of bread", "health", 15));
        rooms[3].setEnemyInRoom(new Enemy("Boss", 70, 10, 6));
        for (Room room : rooms) {
            switch (room.getName()) {
                case "Corridor":
                    room.getDirections().put("North", rooms[1]);
                    room.getDirections().put("East", rooms[2]);
                    break;
                case "Great Hall":
                    room.getDirections().put("North", rooms[3]);
                    room.getDirections().put("South", rooms[0]);
                    break;
                case "Cellar":
                    room.getDirections().put("West", rooms[0]);
                    break;
                case "Boss Room":
                    room.getDirections().put("South", rooms[1]);
                    room.getDirections().put("North", rooms[4]);
            }
        }
    }

    @Override
    public String toString() {
        String state = "You are in the " + name + '\n';;

        for (String key : directions.keySet()) {
            state = state.concat("To the " + key + " there is a " + directions.get(key).getName() + '\n');
        }

        // If statements for the different states that a Room can hold
        if (itemInRoom != null) {
            state = state.concat("There is a " + itemInRoom.getName() + " in the room" + '\n');
        }

        if (passItemInRoom != null) {
            state = state.concat("There is a " + passItemInRoom.getName() + " in the room" + '\n');
        }

        if (enemyInRoom != null) {
            state = state.concat("There is a " + enemyInRoom.getName() + " in the room" + '\n');
        }

        if (foodInRoom != null) {
            state = state.concat("There is some " + foodInRoom.getName() + " in the room");
        }

        return state;
    }
}
