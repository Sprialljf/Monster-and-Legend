package legends.characters.monsters;

public class Exoskeleton extends Monster {
    public Exoskeleton(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level, damage, (int)(defense * 1.1), dodgeChance);
    }
}
