package characters;

/**
 * Interface that determines the common traits that should be possessed by characters
 */
public interface Character {
    int dealDamage(int damage);

    int calcTotalAttributeValue(String attr, int modifier, boolean addition);
}
