package legends.items.spells;

import legends.items.Item;

public class Spell extends Item {
    private final int damage;
    private final int manaCost;
    private final SpellElement element;

    public Spell(String name, int cost, int requiredLevel,
                 int damage, int manaCost, SpellElement element) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.manaCost = manaCost;
        this.element = element;
    }

    public int getDamage() { return damage; }
    public int getManaCost() { return manaCost; }
    public SpellElement getElement() { return element; }

    @Override
    public String toString() {
        return String.format("%s [%s] dmg=%d mana=%d L%d cost=%d",
                name, element, damage, manaCost, requiredLevel, cost);
    }
}
