package legends.characters.heroes;

public class Paladin extends Hero {

    public Paladin(String name, int mana, int strength, int agility,
                   int dexterity, int gold, int exp) {
        super(name, mana, strength, agility, dexterity, gold, exp);
    }

    @Override
    protected void levelUp() {
        level++;
        maxHp = level * 80;
        hp = maxHp;
        maxMana = level * 15;
        mana = maxMana;

        strength = (int)(strength * 1.2);
        agility  = (int)(agility  * 1.1);
        dexterity= (int)(dexterity* 1.05);
        System.out.println(name + " leveled up to " + level + "!");
    }
}
