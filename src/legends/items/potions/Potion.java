package legends.items.potions;

import legends.items.Item;
import legends.characters.heroes.Hero;

import java.util.EnumSet;
import java.util.Set;

public class Potion extends Item {

    public enum Attribute {
        HEALTH, MANA, STRENGTH, DEXTERITY, AGILITY, DEFENSE
    }

    private final int increase;
    private final Set<Attribute> affected;

    public Potion(String name, int cost, int requiredLevel,
                  int increase, Set<Attribute> affected) {
        super(name, cost, requiredLevel);
        this.increase = increase;
        this.affected = affected;
    }

    public void applyTo(Hero h) {
        for (Attribute a : affected) {
            switch (a) {
                case HEALTH:   h.setHp(h.getHp() + increase); break;
                case MANA:     h.setMana(h.getMana() + increase); break;
                case STRENGTH: h.setStrength(h.getStrength() + increase); break;
                case DEXTERITY:h.setDexterity(h.getDexterity() + increase); break;
                case AGILITY:  h.setAgility(h.getAgility() + increase); break;
                case DEFENSE:  h.setDefense(h.getDefense() + increase); break;
            }
        }
    }

    public static Potion fromRow(String name, int cost, int level, int inc, String affectedStr) {
        String cleaned = affectedStr.replace("All", "").trim();
        String[] parts = cleaned.split("/");
        Set<Attribute> attrs = EnumSet.noneOf(Attribute.class);
        for (String p : parts) {
            p = p.trim();
            if (p.equalsIgnoreCase("Health")) attrs.add(Attribute.HEALTH);
            else if (p.equalsIgnoreCase("Mana")) attrs.add(Attribute.MANA);
            else if (p.equalsIgnoreCase("Strength")) attrs.add(Attribute.STRENGTH);
            else if (p.equalsIgnoreCase("Dexterity")) attrs.add(Attribute.DEXTERITY);
            else if (p.equalsIgnoreCase("Agility")) attrs.add(Attribute.AGILITY);
            else if (p.equalsIgnoreCase("Defense")) attrs.add(Attribute.DEFENSE);
        }
        return new Potion(name, cost, level, inc, attrs);
    }

    @Override
    public String toString() {
        return String.format("%s +%d %s L%d cost=%d",
                name, increase, affected, requiredLevel, cost);
    }
}
