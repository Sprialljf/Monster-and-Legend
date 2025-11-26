package legends.characters.monsters;

public class Spirit extends Monster {
    public Spirit(String name, int level, int damage, int defense, int dodgeChance) {
        super(name, level, damage, defense, (int)(dodgeChance * 1.1));
    }
}
