package legends.characters.monsters;

public class Dragon extends Monster {
    public Dragon(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level, (int)(damage * 1.1), defense, dodgeChance);
    }
}
