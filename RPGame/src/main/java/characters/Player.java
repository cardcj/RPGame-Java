package characters;

import interactables.Item;
import interactables.Items;

import java.util.ArrayList;

/**
 * Class to represent the Player
 */
public class Player implements Character {
    private int health;
    private int energy;
    private int strength;
    private int speed;
    private Item[] equippedItems;
    private ArrayList<Items> inventory;

    public Player() {
        this.health = 50;
        this.energy = 10;
        this.strength = 5;
        this.speed = 5;
        this.equippedItems = new Item[]{new Item("Fist", "Unarmed", "strength", 0, true),
                                        new Item("Fist", "Unarmed", "strength", 0, true)};
        this.inventory = new ArrayList<Items>();
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        if (strength >= 0) {
            this.strength = strength;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed >= 0) {
            this.speed = speed;
        }
    }

    public Item[] getEquippedItems() {
        return equippedItems;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void negateEnergy() {
        this.energy -= 1;
    }

    public ArrayList getInventory() {
        return inventory;
    }

    public void equippedItemCheck(int itemToCheck) {
        // Check whether the player is unarmed
        if (!this.getEquippedItems()[itemToCheck].getName().equalsIgnoreCase("Fist")) {
            this.getEquippedItems()[itemToCheck].itemUsed();
            boolean shouldDestroy = this.getEquippedItems()[itemToCheck].reduceModifier();
            if (shouldDestroy) {
                System.out.println("Your " + this.getEquippedItems()[itemToCheck].getName() + " broke!");
                this.getEquippedItems()[itemToCheck] = new Item("Fist", "Unarmed", "strength", 0, true);
            }
        }
    }

    @Override
    public int dealDamage(int damage) {
        return (this.health - damage);
    }

    @Override
    public int calcTotalAttributeValue(String attr, int modifier, boolean addition) {
        int newValue;
        if (addition) {
            if (attr.equalsIgnoreCase("strength")) {
                newValue = this.getStrength() + modifier;
            } else {
                newValue = this.getSpeed() + modifier;
            }
        } else {
            if (attr.equalsIgnoreCase("strength")) {
                newValue = this.getStrength() - modifier;
            } else {
                newValue = this.getSpeed() - modifier;
            }
        }
        return newValue;
    }

    @Override
    public String toString() {
        String lEquipped = equippedItems[0].getName();
        String rEquipped = equippedItems[1].getName();
        return "<<<<<< Player Status >>>>>>" + "\r\n" +
                "HEALTH: " + health + "\r\n" +
                "ENERGY: " + energy + "\r\n" +
                "EQUIPPED: " + lEquipped + ", " + rEquipped + "\r\n" +
                "<<<<<<<<<<<<<>>>>>>>>>>>>>>";
    }
}
