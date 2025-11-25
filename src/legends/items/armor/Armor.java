package legends.items.armor;

import legends.items.Item;

public class Armor extends Item {
    private final int damageReduction;

    public Armor(String name, int cost, int requiredLevel, int damageReduction) {
        super(name, cost, requiredLevel);
        this.damageReduction = damageReduction;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    @Override
    public String toString() {
        return String.format("%s reduce=%d L%d cost=%d",
                name, damageReduction, requiredLevel, cost);
    }
}
