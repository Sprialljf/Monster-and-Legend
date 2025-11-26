package legends.characters.heroes;

import legends.items.weapons.Weapon;
import legends.items.weapons.HandUsage;
import legends.items.armor.Armor;
import legends.items.potions.Potion;
import legends.items.spells.Spell;
import legends.characters.monsters.Monster;

import java.util.ArrayList;
import java.util.List;

public abstract class Hero {
    protected final String name;
    protected int level = 1;
    protected int exp;
    protected int maxHp;
    protected int hp;
    protected int mana;
    protected int maxMana;
    protected int strength;
    protected int agility;
    protected int dexterity;
    protected int defense;
    protected int gold;

    protected Weapon mainHand;
    protected Weapon offHand;
    protected Armor armor;

    protected final List<Object> inventory = new ArrayList<>();

    public Hero(String name, int mana, int strength, int agility,
                int dexterity, int gold, int exp) {
        this.name = name;
        this.mana = mana;
        this.maxMana = mana; 
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.defense = 0;
        this.gold = gold;
        this.exp = exp;
        this.maxHp = 150 * level;
        this.hp = maxHp;
    }
    public Weapon getEquippedWeapon() {
            return this.mainHand;
        }
    // region getters/setters
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getStrength() { return strength; }
    public int getAgility() { return agility; }
    public int getDexterity() { return dexterity; }
    public int getDefense() { return defense; }
    public int getGold() { return gold; }
    public List<Object> getInventory() { return inventory; }
    public int getExp() {return exp;}
    public int getExpForNextLevel() {return level * 10;}
    public void setHp(int hp) { this.hp = Math.min(hp, maxHp); }
    public void setMana(int mana) {this.mana = Math.min(mana, maxMana);}
    public void setStrength(int strength) { this.strength = strength; }
    public void setAgility(int agility) { this.agility = agility; }
    public void setDexterity(int dexterity) { this.dexterity = dexterity; }
    public void setDefense(int defense) { this.defense = defense; }
    // endregion

    public boolean isFainted() { return hp <= 0; }

    public void receivePhysicalDamage(int dmg) {
        int reduction = defense + (armor == null ? 0 : armor.getDamageReduction());
        int finalDmg = Math.max(0, dmg - reduction);
        hp = Math.max(0, hp - finalDmg);
    }

    public double dodgeChance() {
        double chance = agility / 10000.0;   // 800 -> 0.08 = 8%
        if (chance > 0.3) chance = 0.3;      // max 30%
        return chance;
    }


    public void gainGold(int g) { gold += g; }
    public boolean spendGold(int g) {
        if (gold < g) return false;
        gold -= g;
        return true;
    }

    public void gainExp(int e) {
        exp += e;
        while (exp >= level * 10) {
            exp -= level * 10;
            levelUp();
        }
    }
    
    protected abstract void levelUp();

    public double getExpProgress() {
        int need = getExpForNextLevel();
        if (need <= 0) return 0;
        double r = (double) exp / need;
        return Math.min(1.0, r);
    }
    // Weapon handling & attacks 

    public void equipWeaponMain(Weapon w) {
        if (w.getHandsRequired() == 2) offHand = null;
        mainHand = w;
    }

    public void equipWeaponOff(Weapon w) {
        if (w.getHandsRequired() != 1)
            throw new IllegalArgumentException("Off-hand must be 1-handed");
        if (mainHand != null && mainHand.getHandsRequired() == 2)
            throw new IllegalStateException("Can't off-hand with 2-hand main weapon");
        offHand = w;
    }

    public void equipArmor(Armor a) { this.armor = a; }

    public double physicalAttackDamage(HandUsage usage) {
        if (mainHand == null || mainHand.isBroken()) {
            return strength * 0.1;
        }
        switch (usage) {
            case ONE_HAND:
                mainHand.consumeDurability(1);
                return mainHand.getBaseDamage() * (0.8 + strength / 1500.0);
            case TWO_HAND:
                mainHand.consumeDurability(1);
                double mult = mainHand.getHandsRequired() == 1 ? 1.5 : 1.2;
                return mainHand.getBaseDamage() * mult * (1 + strength / 1000.0);
            case DUAL_WIELD:
                if (offHand == null ||
                    mainHand.getHandsRequired() != 1 ||
                    offHand.getHandsRequired() != 1) {
                    return physicalAttackDamage(HandUsage.ONE_HAND);
                }
                mainHand.consumeDurability(1);
                offHand.consumeDurability(1);
                double d1 = mainHand.getBaseDamage() * 0.9 * (1 + strength / 1500.0);
                double d2 = offHand.getBaseDamage() * 0.9 * (1 + strength / 1500.0);
                return d1 + d2;
            default:
                return strength * 0.1;
        }
    }
    public boolean canCast(Spell s) {
        return level >= s.getRequiredLevel() && mana >= s.getManaCost();
    }

    public double castSpell(Spell s, Monster target) {
        if (!canCast(s)) return 0;
        mana -= s.getManaCost();

        double dmg = s.getDamage() * (1 + dexterity / 10000.0);
        switch (s.getElement()) {
            case ICE:
                target.reduceDamageByPercent(0.1);
                break;
            case FIRE:
                target.reduceDefenseByPercent(0.1);
                break;
            case LIGHTNING:
                target.reduceDodgeByPercent(0.1);
                break;
        }
        return dmg;
    }

    public void usePotion(Potion p) {
        p.applyTo(this);
    }

    public void endOfRoundRegen() {
        if (isFainted()) return;
       hp = Math.min(maxHp, hp + maxHp / 10);
       mana += mana / 10;  
    }

    public void reviveAfterBattle() {
        if (isFainted()) {
            hp = maxHp / 2;
            mana /= 2;
        }
    }

    
    @Override
    public String toString() {
        return String.format("%s, Level %d (exp: %d/%d)%n" +
                        "Health: %d/%d  Mana: %d%n" +
                        "Strength: %d  Dexterity: %d  Agility: %d%n" +
                        "Gold: %d%n" +
                        "Equipped Weapon: %s%n" +
                        "Equipped Armor: %s%n",
                name, level, exp, level * 10,
                hp, maxHp, mana,
                strength, dexterity, agility,
                gold,
                mainHand == null ? "Fists" : mainHand.toString(),
                armor == null ? "Unarmored" : armor.toString());
    }
}
