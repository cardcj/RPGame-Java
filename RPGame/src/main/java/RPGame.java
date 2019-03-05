import characters.Enemy;
import characters.Player;
import interactables.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Class where the Game is run
 */
public class RPGame {

    private Room currentRoom;

    private void pickupCommand(RPGame game, Scanner usrIn, Player player) {
        String response;
        System.out.println("What do you want to pickup?: ");
        response = usrIn.nextLine();
        Item currentRoomItem = game.currentRoom.getItemInRoom();
        PassiveItem currentRoomPassItem = game.currentRoom.getPassItemInRoom();
        if (currentRoomItem != null && response.equalsIgnoreCase(currentRoomItem.getName())) {
            player.getInventory().add(currentRoomItem);
            game.currentRoom.setItemInRoom(null);
        } else if (currentRoomPassItem != null && response.equalsIgnoreCase(currentRoomPassItem.getName())) {
            player.getInventory().add(currentRoomPassItem);
            if (currentRoomPassItem.getAttrName().equalsIgnoreCase("strength")) {
                player.setStrength(player.calcTotalAttributeValue("strength", currentRoomPassItem.getAttrValue(),
                        currentRoomPassItem.isPositive()));
            } else {
                player.setSpeed(player.calcTotalAttributeValue("speed", currentRoomPassItem.getAttrValue(),
                        currentRoomPassItem.isPositive()));
            }
            game.currentRoom.setPassItemInRoom(null);
        } else {
            System.out.println("No such item exists here");
        }
    }

    private void equipCommand(Scanner usrIn, Player player) {
        String response;
        System.out.println("<<<<<< Inventory >>>>>>");
        for (Object item : player.getInventory()) {
            Items itemOut = (Items) player.getInventory().get(player.getInventory().indexOf(item));
            System.out.println(itemOut.getName());
        }
        System.out.println("<<<<<<<<<<<<>>>>>>>>>>>>");
        System.out.println("What do you want to equip and to what hand(L/R)?: ");
        response = usrIn.nextLine();
        String[] responses = response.split(" ");
        if (responses.length > 1 && (responses[1].equalsIgnoreCase("l") ^ responses[1].equalsIgnoreCase("r"))) {
            for (Object item : player.getInventory()) {

                // Correct syntax found from:
                // Author: Singh, C
                // Date of Retrieval: 29 March 2017
                // Available from: http://beginnersbook.com/2013/04/try-catch-in-java/
                // try catch implemented to handle passive items
                try {
                    Item itemToCheck = (Item) item;
                    // Check whether the item exists in the players inventory
                    if (itemToCheck.getName().equalsIgnoreCase(responses[0])) {
                        // Equip item to the hand that the player has chosen
                        itemToCheck.equipItem(player, responses[1]);
                        // Remove the item that has been equipped from the inventory to avoid duplication of items
                        player.getInventory().remove(itemToCheck);
                        break;
                    }
                } catch (ClassCastException e) {
                    System.out.println("INVALID ACTION: Passive Items cannot be equipped");
                }
            }
        } else {
            System.out.println("INVALID INPUT: Please enter L or R after item you want to equip");
        }
    }

    private void unequipCommand(Scanner usrIn, Player player) {
        String response;
        System.out.println("What do you want to unequip(L/R)?: ");
        response = usrIn.nextLine();
        if (response.equalsIgnoreCase("l")) {
            player.getInventory().add(player.getEquippedItems()[0]);
            player.getEquippedItems()[0] = new Item("Fist", "Unarmed", "strength", 0, true);
        } else if (response.equalsIgnoreCase("r")) {
            player.getInventory().add(player.getEquippedItems()[1]);
            player.getEquippedItems()[1] = new Item("Fist", "Unarmed", "strength", 0, true);
        } else {
            System.out.println("INVALID INPUT: Please enter L or R after item you want to equip");
        }
    }

    private void playerFightCheck(Scanner usrIn, Player player, Enemy currentEnemy) {
        String playerAttackMessageL = "You attack with " + player.getEquippedItems()[0].getName();
        String playerAttackMessageR = "You attack with " + player.getEquippedItems()[1].getName();
        String response;
        Boolean validChoice = false;
        while (!validChoice) {
            System.out.println("What do you attack with?(L/R): ");
            response = usrIn.nextLine();
            if (response.equalsIgnoreCase("l")) {
                validChoice = true;
                System.out.println(playerAttackMessageL);
                currentEnemy.setHealth(currentEnemy.dealDamage(player.calcTotalAttributeValue(
                        player.getEquippedItems()[0].getAttrName(),
                        player.getEquippedItems()[0].getAttrValue(),
                        player.getEquippedItems()[0].isPositive())));
                player.equippedItemCheck(0);
            } else if (response.equalsIgnoreCase("r")) {
                validChoice = true;
                System.out.println(playerAttackMessageR);
                currentEnemy.setHealth(currentEnemy.dealDamage(player.calcTotalAttributeValue(
                        player.getEquippedItems()[1].getAttrName(),
                        player.getEquippedItems()[1].getAttrValue(),
                        player.getEquippedItems()[1].isPositive())));
                player.equippedItemCheck(1);
            }
            else {
                System.out.println("INVALID INPUT: Enter either L or R");
            }
        }
    }

    private void fightSim(Scanner usrIn, Player player, Enemy currentEnemy) {
        String enemyAttackMessage = "The " + currentEnemy.getName() + " attacks you!";
        // Initial status of the fight
        System.out.println("YOU: " + player.getHealth() + " || " + currentEnemy.getName() + ": " + currentEnemy.getHealth());

        while (currentEnemy.getHealth() > 0 && player.getHealth() > 0) {
            // Integer to hold 0 for the players turn and 1 for the enemy's turn
            if (currentEnemy.getSpeed() > player.getSpeed()) {
                System.out.println(enemyAttackMessage);
                player.setHealth(player.dealDamage(currentEnemy.getStrength()));
                System.out.println("YOU: " + player.getHealth() + " || " + currentEnemy.getName() + ": " + currentEnemy.getHealth());
                if (player.getHealth() > 0) {
                    playerFightCheck(usrIn, player, currentEnemy);
                }
            } else {
                playerFightCheck(usrIn, player, currentEnemy);
                System.out.println("YOU: " + player.getHealth() + " || " + currentEnemy.getName() + ": " + currentEnemy.getHealth());
                if (currentEnemy.getHealth() > 0) {
                    player.setHealth(player.dealDamage(currentEnemy.getStrength()));
                    System.out.println(enemyAttackMessage);
                    System.out.println("YOU: " + player.getHealth() + " || " + currentEnemy.getName() + ": " + currentEnemy.getHealth());
                }
            }
        }
        player.negateEnergy();
    }

    private void checkFightResult(Player player, Enemy currentEnemy) {
        if (player.getHealth() <= 0 && currentEnemy.getHealth() > 0) {
            System.out.println("\nYOU LOST!\n");
            System.out.println("\nGAME OVER\n");
        } else if (player.getHealth() > 0 && currentEnemy.getHealth() <= 0) {
            System.out.println("\nYOU WON!\n");
            currentRoom.setEnemyInRoom(null);
        } else {
            System.out.println("\nIT WAS A DRAW!\n");
            System.out.println("\nGAME OVER\n");
        }
    }

    private void consumeCommand(RPGame game, Scanner usrIn, Player player) {
        String response;
        System.out.println("What do you want to consume?: ");
        response = usrIn.nextLine();
        Food currentRoomFood = game.currentRoom.getFoodInRoom();
        if (currentRoomFood != null && response.equalsIgnoreCase(currentRoomFood.getName())) {
            if (currentRoomFood.getAttrName().equalsIgnoreCase("health")) {
                player.setHealth(player.getHealth() + currentRoomFood.getValue());
            } else {
                player.setEnergy(player.getEnergy() + currentRoomFood.getValue());
            }
            game.currentRoom.setFoodInRoom(null);
        } else {
            System.out.println("No such item exists here");
        }
    }

    private void inspectCommand(RPGame game, Scanner usrIn) {
        String response;
        System.out.println("What do you want to inspect?: ");
        response = usrIn.nextLine();
        Food currentRoomFood = game.currentRoom.getFoodInRoom();
        Item currentRoomItem = game.currentRoom.getItemInRoom();
        PassiveItem currentRoomPassItem = game.currentRoom.getPassItemInRoom();
        if (currentRoomFood != null && response.equalsIgnoreCase(currentRoomFood.getName())) {
            System.out.println("<<<<<< Description >>>>>>");
            System.out.println("<<<<<<<<<<<<<>>>>>>>>>>>>>");
            System.out.println(currentRoomFood.getDescription());
        } else if (currentRoomItem != null && response.equalsIgnoreCase(currentRoomItem.getName())) {
            System.out.println("<<<<<< Description >>>>>>");
            System.out.println(currentRoomItem.inspectItem());
            System.out.println("<<<<<<<<<<<<<>>>>>>>>>>>>>");
        } else if (currentRoomPassItem != null && response.equalsIgnoreCase(currentRoomPassItem.getName())) {
            System.out.println("<<<<<< Description >>>>>>");
            System.out.println(currentRoomPassItem.inspectItem());
            System.out.println("<<<<<<<<<<<<<>>>>>>>>>>>>>");
        } else {
            System.out.println("INVALID INPUT: Cannot inspect that or it doesn't exist");
        }
    }

    private void commandCheck(RPGame game, Scanner usrIn, Player player) {
        String action = usrIn.nextLine();
        // Switch statement with all of the valid commands that a player can enter
        switch (action) {
            case "North":
            case "north":
                if (game.currentRoom.getEnemyInRoom() != null) {
                    System.out.println("\n The " + game.currentRoom.getEnemyInRoom().getName() + " blocked your way \n");
                } else {
                    Room roomToGo = game.currentRoom.getDirections().get("North");
                    if (roomToGo != null) {
                        player.negateEnergy();
                        game.currentRoom = roomToGo;
                    } else {
                        System.out.println("INVALID INPUT: There is no room to go to there");
                    }
                }
                break;
            case "East":
            case "east":
                if (game.currentRoom.getEnemyInRoom() != null) {
                    System.out.println("\nThe " + game.currentRoom.getEnemyInRoom().getName() + " blocked your way\n");
                } else {
                    Room roomToGo = game.currentRoom.getDirections().get("East");
                    if (roomToGo != null) {
                        player.negateEnergy();
                        game.currentRoom = roomToGo;
                    } else {
                        System.out.println("INVALID INPUT: There is no room to go to there");
                    }
                }
                break;
            case "South":
            case "south":
                if (game.currentRoom.getEnemyInRoom() != null) {
                    System.out.println("\n The " + game.currentRoom.getEnemyInRoom().getName() + " blocked your way \n");
                } else {
                    Room roomToGo = game.currentRoom.getDirections().get("South");
                    if (roomToGo != null) {
                        player.negateEnergy();
                        game.currentRoom = roomToGo;
                    } else {
                        System.out.println("INVALID INPUT: There is no room to go to there");
                    }
                }
                break;
            case "West":
            case "west":
                if (game.currentRoom.getEnemyInRoom() != null) {
                    System.out.println("\n The " + game.currentRoom.getEnemyInRoom().getName() + " blocked your way \n");
                } else {
                    Room roomToGo = game.currentRoom.getDirections().get("West");
                    if (roomToGo != null) {
                        player.negateEnergy();
                        game.currentRoom = roomToGo;
                    } else {
                        System.out.println("INVALID INPUT: There is no room to go to there");
                    }
                }
                break;
            case "Pickup":
            case "pickup":
                game.pickupCommand(game, usrIn, player);
                break;
            case "equip":
            case "Equip":
                game.equipCommand(usrIn, player);
                break;
            case "unequip":
            case "Unequip":
                game.unequipCommand(usrIn, player);
                break;
            case "consume":
            case "Consume":
                game.consumeCommand(game, usrIn, player);
                break;
            case "attack":
            case "Attack":
                Enemy currentEnemy = currentRoom.getEnemyInRoom();
                fightSim(usrIn, player, currentEnemy);
                checkFightResult(player, currentEnemy);
                break;
            case "inv":
            case "Inv":
                System.out.println("<<<<<< Inventory >>>>>>");
                for (Object item : player.getInventory()) {
                    Items itemOut = (Items) player.getInventory().get(player.getInventory().indexOf(item));
                    System.out.println(itemOut.getName());
                }
                System.out.println("<<<<<<<<<<<<>>>>>>>>>>>>");
                System.out.println("Press ENTER to stop viewing inventory");
                usrIn.nextLine();
                break;
            case "attr":
            case "Attr":
                System.out.println("<<<<<< Player Attr >>>>>>");
                System.out.println("STRENGTH_R: " + player.calcTotalAttributeValue(
                        player.getEquippedItems()[1].getAttrName(),
                        player.getEquippedItems()[1].getAttrValue(),
                        player.getEquippedItems()[1].isPositive()));
                System.out.println("STRENGTH_L: " + player.calcTotalAttributeValue(
                        player.getEquippedItems()[0].getAttrName(),
                        player.getEquippedItems()[0].getAttrValue(),
                        player.getEquippedItems()[0].isPositive()));
                System.out.println("SPEED:      " + player.getSpeed());
                System.out.println("<<<<<<<<<<<<<>>>>>>>>>>>>>");
                System.out.println("Press ENTER to stop viewing attributes");
                usrIn.nextLine();
                break;
            case "help":
            case "Help":
                System.out.println("<<<<<< Commands >>>>>>");
                System.out.println("north - go north");
                System.out.println("east - go east");
                System.out.println("south - go south");
                System.out.println("west - go west");
                System.out.println("pickup - pickup items dialog");
                System.out.println("equip - equip items from inventory");
                System.out.println("unequip - unequip item");
                System.out.println("attack - attack dialog");
                System.out.println("attr - show player attributes");
                System.out.println("inv - show player inventory");
                System.out.println("consume - consume food dialog");
                System.out.println("insp - inspect dialog");
                System.out.println("<<<<<<<<<<<>>>>>>>>>>>");
                System.out.println("Press ENTER to stop viewing commands");
                usrIn.nextLine();
                break;
            case "insp":
            case "Insp":
                game.inspectCommand(game, usrIn);
                System.out.println("Press ENTER to stop viewing inspection dialog");
                usrIn.nextLine();
                break;
            default:
                System.out.println("INVALID INPUT: Please enter a valid command");
                break;
        }
    }

    private void resultOut(String toWrite) {
        // Code needed to get the path of the jar that is being executed derived from:
        // Author: Neugebauer, B
        // Date of Retrieval: 25 April 2017
        // Available from: http://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
        // IMPORTANT: When the program is run using source files, the jar is located in RPGame/build/classes
        File jarFile = null;
        try {
            jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String fullPathToDir = jarFile.getParentFile().getAbsolutePath();
        // Method of writing the game results to a file derived from:
        // Author: Pankaj
        // Date of Retrieval: 25 April 2017
        // Available from: http://www.journaldev.com/878/java-write-to-file
        try {
            Files.write(Paths.get(fullPathToDir + "/GameResult.txt"), toWrite.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RPGame game = new RPGame();
        String gameResult = null;
        Scanner usrIn = new Scanner(System.in);
        Player player = new Player();
        Room entrance = new Room("Corridor");
        Room greatHall = new Room("Great Hall");
        Room cellar = new Room("Cellar");
        Room bossRoom = new Room("Boss Room");
        Room finish = new Room("Exit");

        Room.setupRooms(entrance, greatHall, cellar, bossRoom, finish);
        game.currentRoom = entrance;

        while (game.currentRoom != finish && player.getEnergy() > 0 && player.getHealth() > 0) {
            System.out.println(player);
            System.out.println(game.currentRoom);
            System.out.println("What do you want to do? (Type help to get a list of commands): ");
            game.commandCheck(game, usrIn, player);
        }

        if (player.getHealth() > 0) {
            // String that is determined by whether or not the player has reached the finish
            gameResult = game.currentRoom == finish ? "CONGRATULATIONS, YOU BEAT THE GAME!\r\n" :
                    "\r\nYOU RAN OUT OF ENERGY!\r\n\r\nGAME OVER\r\n";
            System.out.println(gameResult);
        } else {
            gameResult = "YOU LOST!\r\nGAME OVER\r\n";
        }


        // Output the game result with the player's status to a file
        game.resultOut(gameResult.concat("\r\n" + player.toString()));
    }
}
