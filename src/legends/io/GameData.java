package legends.io;

import legends.items.weapons.Weapon;
import legends.items.armor.Armor;
import legends.items.potions.Potion;
import legends.items.spells.Spell;
import legends.items.spells.SpellElement;
import legends.characters.heroes.*;
import legends.characters.monsters.*;
import java.io.*;
import java.util.*;

public class GameData {

    public static List<Weapon> loadWeapons(String baseDir) throws IOException {
        List<Weapon> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Weaponry.txt")) {
            String name = r[0];
            int cost = Integer.parseInt(r[1]);
            int level = Integer.parseInt(r[2]);
            int damage = Integer.parseInt(r[3]);
            int hands = Integer.parseInt(r[4]);
            list.add(new Weapon(name, cost, level, damage, hands));
        }
        return list;
    }

    public static List<Armor> loadArmor(String baseDir) throws IOException {
        List<Armor> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Armory.txt")) {
            String name = r[0];
            int cost = Integer.parseInt(r[1]);
            int level = Integer.parseInt(r[2]);
            int reduction = Integer.parseInt(r[3]);
            list.add(new Armor(name, cost, level, reduction));
        }
        return list;
    }

    public static List<Potion> loadPotions(String baseDir) throws IOException {
        List<Potion> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Potions.txt")) {
            String name = r[0];
            int cost = Integer.parseInt(r[1]);
            int level = Integer.parseInt(r[2]);
            int inc = Integer.parseInt(r[3]);
            String affected = r[4];
            list.add(Potion.fromRow(name, cost, level, inc, affected));
        }
        return list;
    }

    private static List<Spell> loadSpellsFrom(String baseDir, String file, SpellElement element) throws IOException {
        List<Spell> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/" + file)) {
            String name = r[0];
            int cost = Integer.parseInt(r[1]);
            int level = Integer.parseInt(r[2]);
            int damage = Integer.parseInt(r[3]);
            int mana = Integer.parseInt(r[4]);
            list.add(new Spell(name, cost, level, damage, mana, element));
        }
        return list;
    }

    public static List<Spell> loadAllSpells(String baseDir) throws IOException {
        List<Spell> all = new ArrayList<>();
        all.addAll(loadSpellsFrom(baseDir, "Firespells.txt", SpellElement.FIRE));
        all.addAll(loadSpellsFrom(baseDir, "Icespells.txt", SpellElement.ICE));
        all.addAll(loadSpellsFrom(baseDir, "Lightningspells.txt", SpellElement.LIGHTNING));
        return all;
    }

    public static List<Warrior> loadWarriors(String baseDir) throws IOException {
        List<Warrior> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Warriors.txt")) {
            String name = r[0];
            int mana = Integer.parseInt(r[1]);
            int strength = Integer.parseInt(r[2]);
            int agility = Integer.parseInt(r[3]);
            int dexterity = Integer.parseInt(r[4]);
            int money = Integer.parseInt(r[5]);
            int exp = Integer.parseInt(r[6]);
            list.add(new Warrior(name, mana, strength, agility, dexterity, money, exp));
        }
        return list;
    }

    public static List<Paladin> loadPaladins(String baseDir) throws IOException {
        List<Paladin> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Paladins.txt")) {
            String name = r[0];
            int mana = Integer.parseInt(r[1]);
            int strength = Integer.parseInt(r[2]);
            int agility = Integer.parseInt(r[3]);
            int dexterity = Integer.parseInt(r[4]);
            int money = Integer.parseInt(r[5]);
            int exp = Integer.parseInt(r[6]);
            list.add(new Paladin(name, mana, strength, agility, dexterity, money, exp));
        }
        return list;
    }

    public static List<Sorcerer> loadSorcerers(String baseDir) throws IOException {
        List<Sorcerer> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Sorcerers.txt")) {
            String name = r[0];
            int mana = Integer.parseInt(r[1]);
            int strength = Integer.parseInt(r[2]);
            int agility = Integer.parseInt(r[3]);
            int dexterity = Integer.parseInt(r[4]);
            int money = Integer.parseInt(r[5]);
            int exp = Integer.parseInt(r[6]);
            list.add(new Sorcerer(name, mana, strength, agility, dexterity, money, exp));
        }
        return list;
    }

    public static List<Dragon> loadDragons(String baseDir) throws IOException {
        List<Dragon> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Dragons.txt")) {
            String name = r[0];
            int level = Integer.parseInt(r[1]);
            int damage = Integer.parseInt(r[2]);
            int defense = Integer.parseInt(r[3]);
            int dodge = Integer.parseInt(r[4]);
            list.add(new Dragon(name, level, damage, defense, dodge));
        }
        return list;
    }

    public static List<Spirit> loadSpirits(String baseDir) throws IOException {
        List<Spirit> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Spirits.txt")) {
            String name = r[0];
            int level = Integer.parseInt(r[1]);
            int damage = Integer.parseInt(r[2]);
            int defense = Integer.parseInt(r[3]);
            int dodge = Integer.parseInt(r[4]);
            list.add(new Spirit(name, level, damage, defense, dodge));
        }
        return list;
    }

    public static List<Exoskeleton> loadExoskeletons(String baseDir) throws IOException {
        List<Exoskeleton> list = new ArrayList<>();
        for (String[] r : TableFileReader.readTable(baseDir + "/Exoskeletons.txt")) {
            String name = r[0];
            int level = Integer.parseInt(r[1]);
            int damage = Integer.parseInt(r[2]);
            int defense = Integer.parseInt(r[3]);
            int dodge = Integer.parseInt(r[4]);
            list.add(new Exoskeleton(name, level, damage, defense, dodge));
        }
        return list;
    }
    public static BossConfig loadBossConfig(String baseDir, String fileName) {
        String path = baseDir + File.separator + "boss" + File.separator + fileName;
        Map<String, String> map = readKeyValueFile(path);

        String name   = map.get("name");
        int level     = Integer.parseInt(map.get("level"));
        int hp        = Integer.parseInt(map.get("hp"));
        int damage    = Integer.parseInt(map.get("damage"));
        int defense   = Integer.parseInt(map.get("defense"));
        int dodge     = Integer.parseInt(map.get("dodge"));

        BossConfig cfg = new BossConfig(name, level, hp, damage, defense, dodge);

        // rest key throw in extras
        for (Map.Entry<String, String> e : map.entrySet()) {
            String k = e.getKey();
            if (k.equals("name") || k.equals("level") || k.equals("hp")
                    || k.equals("damage") || k.equals("defense") || k.equals("dodge")) {
                continue;
            }
            cfg.putExtra(k, e.getValue());
        }

        return cfg;
    }

    // read key:value 
    private static Map<String, String> readKeyValueFile(String path) {
        Map<String, String> result = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    result.put(key, value);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read boss file: " + path);
            e.printStackTrace();
        }
        return result;
    }
}
