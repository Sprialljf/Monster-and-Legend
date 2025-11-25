package legends.characters.monsters;

import java.util.HashMap;
import java.util.Map;

public class BossConfig {
    private final String name;
    private final int level;
    private final int hp;
    private final int damage;
    private final int defense;
    private final int dodge;
    private final Map<String, String> extras = new HashMap<>();

    public BossConfig(String name, int level, int hp, int damage, int defense, int dodge) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.dodge = dodge;
    }

    public String getName()   { return name; }
    public int getLevel()     { return level; }
    public int getHp()        { return hp; }
    public int getDamage()    { return damage; }
    public int getDefense()   { return defense; }
    public int getDodge()     { return dodge; }

    public void putExtra(String key, String value) {
        extras.put(key, value);
    }

    public String getExtra(String key) {
        return extras.get(key);
    }

    public double getExtraDouble(String key, double def) {
        String v = extras.get(key);
        if (v == null) return def;
        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public int getExtraInt(String key, int def) {
        String v = extras.get(key);
        if (v == null) return def;
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public boolean getExtraBoolean(String key, boolean def) {
        String v = extras.get(key);
        if (v == null) return def;
        return Boolean.parseBoolean(v);
    }
}
