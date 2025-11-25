package legends.characters.heroes;

public class Sorcerer extends Hero {

    public Sorcerer(String name, int mana, int strength, int agility,
                    int dexterity, int gold, int exp) {
        super(name, mana, strength, agility, dexterity, gold, exp);
    }

    @Override
    protected void levelUp() {
        level++;
        maxHp = level * 50;
        hp = maxHp;
        maxMana = level * 30;
        mana = maxMana;
        strength = (int)(strength * 1.1);
        agility  = (int)(agility  * 1.05);
        dexterity= (int)(dexterity* 1.15);
        System.out.println(name + " leveled up to " + level + "!");
    }
}
