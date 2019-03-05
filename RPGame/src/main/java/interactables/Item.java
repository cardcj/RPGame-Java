package interactables;

import characters.Player;

/**
 * Class used to represent standard items the characters can interact with
 */
public class Item extends Items {
    private int durability;

    public Item(String name, String description, String attrName, int attrValue, boolean positive) {
        super(name, description, attrName, attrValue, positive);
        this.durability = 6;
    }

    public void equipItem(Player player, String handToEquipTo) {
        if (handToEquipTo.equalsIgnoreCase("l")) {
            player.getEquippedItems()[0] = this;
        } else if (handToEquipTo.equalsIgnoreCase("r")) {
            player.getEquippedItems()[1] = this;
        } else {
            System.out.println("Invalid input, please enter L to equip item to left or R to equip item to right");
        }
    }

    public void itemUsed() {
        this.durability -= 1;
    }

    public boolean reduceModifier() {
        boolean destroyItem = false;
        if (this.durability <= 3 && this.durability > 0) {
            this.setAttrValue(this.getAttrValue() - 2);
            String[] nameToCheck = this.getName().split(" ");
            if (!nameToCheck[0].equalsIgnoreCase("Worn")) {
                String newName = "Worn " + this.getName();
                String newDesc = "Damaged " + this.inspectItem();
                this.setName(newName);
                this.setDescription(newDesc);
            }
        } else if (this.durability == 0) {
            destroyItem = true;
        }
        return destroyItem;
    }
}
