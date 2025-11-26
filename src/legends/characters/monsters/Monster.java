package legends.characters.monsters;

public abstract class Monster {
    protected final String name;
    protected final int level;
    protected int hp;
    protected int maxHp;
    protected int damage;
    protected int defense;
    protected int dodgeChance; // percentage 0-100

    public Monster(String name, int level, int damage, int defense, int dodgeChance) {
        this.name = name;
        this.level = level;
        this.damage = damage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
        this.hp = level * 100;
        this.maxHp = hp;
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getDamage() { return damage; }
    public int getDefense() { return defense; }
    public int getDodgeChanceRaw() { return dodgeChance; }
    public boolean isDefeated() { return hp <= 0; }
    public int getMaxHp() { return maxHp; }
    public double dodgeChance() { return dodgeChance / 100.0; }

    public void receiveDamage(double dmg) {
        int finalDmg = (int) Math.max(0, dmg - defense);
        hp = Math.max(0, hp - finalDmg);
    }

    public int attackDamage() {
        return damage;
    }

    public void reduceDamageByPercent(double pct) {
        damage = (int) (damage * (1 - pct));
    }

    public void reduceDefenseByPercent(double pct) {
        defense = (int) (defense * (1 - pct));
    }

    public void reduceDodgeByPercent(double pct) {
        dodgeChance = (int) (dodgeChance * (1 - pct));
    }
    public void setHp(int hp) {
        this.hp = hp;
        this.maxHp = hp;
    }
    public boolean isBoss() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s, Level %d HP=%d dmg=%d def=%d dodge=%d%%",
                name, level, hp, damage, defense, dodgeChance);
    }
}
