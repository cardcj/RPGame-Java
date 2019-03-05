package interactables;

/**
 * Class used for items that give active effects without the need to be equipped
 */
public class PassiveItem extends Items {
    public PassiveItem(String name, String description, String attrName, int attrValue, boolean positive) {
        super(name, description, attrName, attrValue, positive);
    }
}
