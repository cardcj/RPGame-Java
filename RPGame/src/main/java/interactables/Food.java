package interactables;

/**
 * Class for food items in the game, will either replenish energy or health
 */
public class Food {
    private String name;
    private String description;
    private String attrName;
    private int value;

    public Food (String name, String description, String attrName, int value) {
        this.name = name;
        this.description = description;
        this.attrName = attrName;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAttrName() {
        return attrName;
    }

    public int getValue() {
        return value;
    }
}
