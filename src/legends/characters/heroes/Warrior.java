package legends.characters.heroes;

public class Warrior extends Hero {

    public Warrior(String name, int mana, int strength, int agility,
                   int dexterity, int gold, int exp) {
        super(name, mana, strength, agility, dexterity, gold, exp);
    }

    @Override
    protected void levelUp() {
        level++;
        maxHp = level * 100;
        hp = maxHp;
        maxMana = level * 10;
        mana = maxMana;
        strength = (int)(strength * 1.3);
        agility  = (int)(agility  * 1.05);
        dexterity= (int)(dexterity* 1.05);
        System.out.println(name + " leveled up to " + level + "!");
    }
}
