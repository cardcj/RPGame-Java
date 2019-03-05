package characters;

/**
 * Class to represent enemies that the characters comes across when traversing rooms
 */
public class Enemy implements Character {
    private String name;
    private int health;
    private int strength;
    private int speed;

    public Enemy(String name, int health, int strength, int speed) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.speed = speed;
    }

    public String getName() {
        return name;
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

    public int getSpeed() {
        return speed;
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
}
