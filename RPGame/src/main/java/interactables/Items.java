package interactables;

/**
 * Abstract for functionality that can be applied to both normal and passive items
 */
public abstract class Items {

    private String name;
    private String description;
    private String attrName;
    private int attrValue;
    private boolean positive;

    public Items(String name, String description, String attrName, int attrValue, boolean positive) {
        this.name = name;
        this.description = description;
        this.attrName = attrName;
        this.attrValue = attrValue;
        this.positive = positive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttrName() {
        return attrName;
    }

    public int getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(int attrValue) {
        this.attrValue = attrValue;
    }

    public String inspectItem() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPositive() {
        return positive;
    }
}
