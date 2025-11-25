package legends.items.weapons;

import legends.items.Item;
import legends.items.WeaponCategory;

public class Weapon extends Item {
    private final int baseDamage;
    private final int handsRequired;
    private int durability;
    private final int maxDurability;

    public Weapon(String name, int cost, int requiredLevel,
                  int baseDamage, int handsRequired) {
        super(name, cost, requiredLevel);
        this.baseDamage = baseDamage;
        this.handsRequired = handsRequired;
        this.maxDurability = 20;
        this.durability = maxDurability;
    }

    public int getBaseDamage() { return baseDamage; }
    public int getHandsRequired() { return handsRequired; }
    public int getDurability() { return durability; }
    public int getMaxDurability() { return maxDurability; }
    public boolean isBroken() { return durability <= 0; }

    public void repair() {
        this.durability = maxDurability;
    }

    public void consumeDurability(int amount) {
        durability = Math.max(0, durability - amount);
    }

    @Override
    public String toString() {
        return String.format("%s dmg=%d L%d cost=%d hands=%d dur=%d/%d",
                name, baseDamage, requiredLevel, cost, handsRequired,
                durability, maxDurability);
    }
    public WeaponCategory getCategory() {
        String n = getName().toLowerCase();
        if (n.contains("bow")) {
            return WeaponCategory.BOW;
        }
        // sword/scythe/axe/dagger/tswords/frostmourne Sword and Dagger Category
        if (n.contains("sword") || n.contains("scythe")
                || n.contains("axe") || n.contains("dagger")
                || n.contains("tswords") || n.contains("frostmourne")) {
            return WeaponCategory.SWORD;
        }
        //  BLUNT
        if (n.contains("mace") || n.contains("hammer") || n.contains("staff")) {
            return WeaponCategory.BLUNT;
        }
        return WeaponCategory.OTHER;
    }
}
