package legends.game;

import legends.characters.heroes.Hero;
import legends.characters.heroes.Warrior;
import legends.characters.heroes.Sorcerer;
import legends.characters.monsters.*;
import legends.items.weapons.Weapon;
import legends.items.WeaponCategory; // 确保导入了这个枚举
import legends.items.spells.Spell;
import legends.io.GameData;
import legends.characters.monsters.BossConfig; 

import java.util.List;
public class BossBattle {
    public static void checkRoundEvents(int round, List<Monster> monsters, List<Hero> heroes) {
        
        //  Lich King (Floor 3)
        LichKingBoss lich = null;
        for (Monster m : monsters) {
            if (m instanceof LichKingBoss && !m.isDefeated()) {
                lich = (LichKingBoss) m;
                break;
            }
        }

        if (lich != null) {
            // Summon Minions
            if (round == 1 || (round > 1 && (round - 1) % lich.getSummonInterval() == 0)) {
                System.out.println(UI.c(UI.BRIGHT_PURPLE, "\n>>> The Lich King raises Frostmourne! The undead army obeys! <<<"));
                LichKingBoss finalLich = lich;
                monsters.removeIf(m -> m != finalLich);
                monsters.add(new Spirit("Summoned Spirit", lich.getLevel(), 500, 50, 20));
                monsters.add(new Dragon("Summoned Dragon", lich.getLevel(), 800, 100, 10));
                System.out.println(UI.c(UI.BRIGHT_RED, "Old minions are sacrificed! New nightmares have arrived!"));
            }
            // Reward
            if (round == lich.getRewardAfterRounds()) {
                triggerRound5Event(monsters, heroes);
            }
        }

        // Dragon King (Floor 2) 
        DragonKingBoss dragon = null;
        for (Monster m : monsters) {
            if (m instanceof DragonKingBoss && !m.isDefeated()) {
                dragon = (DragonKingBoss) m;
                break;
            }
        }

        if (dragon != null) {
            // Regeneration (Heals 1% Max HP)
            int healAmount = (int) (dragon.getMaxHp() * 0.01);
            if (dragon.getHp() < dragon.getMaxHp()) {
                dragon.setHp(Math.min(dragon.getMaxHp(), dragon.getHp() + healAmount));
                System.out.println(UI.c(UI.BRIGHT_RED, "\n>>> The Dragon King's blood boils! Heals " + healAmount + " HP! <<<"));
            }
        }
    }

    private static void triggerRound5Event(List<Monster> monsters, List<Hero> heroes) {
        System.out.println(UI.c(UI.BRIGHT_YELLOW, "\n>>> ⚡ LEGENDARY MOMENT: BREAKING THE PLANES ⚡ <<<"));
        System.out.println("You have survived the Lich King's onslaught!");

        // Args: Name, Cost, Level, Damage, Hands
        Weapon frostmourne = new Weapon("Frostmourne", 1000, 1, 1500, 2);
        System.out.println("The legendary sword " + UI.c(UI.BRIGHT_CYAN, "[Frostmourne]") + " falls to the ground!");

        for (Hero h : heroes) {
            if (!h.isFainted()) {
                h.getInventory().add(frostmourne);
                h.equipWeaponMain(frostmourne); 
                System.out.println(h.getName() + " picks up Frostmourne and equips it immediately!");
                break;
            }
        }

        System.out.println(UI.c(UI.BRIGHT_GREEN, "The bosses from previous floors are moved by your courage and join the team!"));
        try {
            // Update path if needed
            String baseDir = "src/legends/data"; 
            BossConfig leoricCfg = GameData.loadBossConfig(baseDir, "Leoric.txt");
            BossConfig dragonCfg = GameData.loadBossConfig(baseDir, "Dragon_King.txt");

            Hero allyLeoric = new Warrior(leoricCfg.getName() + "(Ally)", 2000, 
                leoricCfg.getDamage(), leoricCfg.getDodge() * 10, 200, 0, 0);
            allyLeoric.setHp(leoricCfg.getHp());

            Hero allyDragon = new Sorcerer(dragonCfg.getName() + "(Ally)", 5000, 
                200, dragonCfg.getDodge() * 10, dragonCfg.getDamage(), 0, 0);
            allyDragon.setHp(dragonCfg.getHp());

            heroes.add(allyLeoric);
            heroes.add(allyDragon);
            System.out.println("Leoric and Dragon King have joined the battle!");
        } catch (Exception e) {
            System.out.println("[System] Spawning default allies.");
            Hero backupWar = new Warrior("Skeleton King(Ally)", 2000, 1200, 100, 100, 0, 0);
            backupWar.setHp(5000);
            heroes.add(backupWar);
            Hero backupSorc = new Sorcerer("Dragon King(Ally)", 5000, 200, 100, 1500, 0, 0);
            backupSorc.setHp(4000);
            heroes.add(backupSorc);
        }
    }

    // damage
    public static double applySpecialDamageEffects(Hero h, Monster target, double originalDamage) {
        double finalDamage = originalDamage;
        
        Weapon w = h.getEquippedWeapon();
        // no weapon --- OTHER
        WeaponCategory cat = (w != null) ? w.getCategory() : WeaponCategory.OTHER;
        String wName = (w != null) ? w.getName() : "Fists";

        // Frostmourne 
        if (w != null && wName.equalsIgnoreCase("Frostmourne")) {
            if (target instanceof LichKingBoss) {
                System.out.println(UI.c(UI.BRIGHT_CYAN, "❄️ Frostmourne resonates with the Lich King! TRUE DAMAGE! ❄️"));
                double trueDmg = target.getMaxHp() / 3.0;
                finalDamage = Math.max(finalDamage, trueDmg);
            }
        }

        // 2. Skeleton King Resistance 
        if (target instanceof SkeletonKingBoss) {
            if (cat == WeaponCategory.SWORD) {
                //  sword, scythe, axe, dagger, tswords, frostmourne
                System.out.println("Skeleton King's bones deflect the blade! (Damage Reduced)");
                finalDamage *= 0.1;
            } else if (cat == WeaponCategory.BLUNT) {
                //  mace, hammer, staff
                System.out.println("The blunt weapon crushes the skeleton! (Damage Boosted)");
                finalDamage *= 1.5;
            }
        }

        // 3. Dragon King Immunity 
        if (target instanceof DragonKingBoss) {
            DragonKingBoss dragon = (DragonKingBoss) target;
            String allowed = dragon.getAllowedDamage().toLowerCase(); // e.g. "bow,spell"
            
            boolean isAllowed = false;
            if (allowed.contains("bow") && cat == WeaponCategory.BOW) {
                isAllowed = true;
            }

            if (isAllowed) {
                System.out.println(UI.c(UI.BRIGHT_GREEN, "The projectile pierces through the Dragon's scales!"));
                // Normal damage
            } else {
                System.out.println(UI.c(UI.BRIGHT_RED, "Melee attacks cannot reach the flying Dragon! (Immune)"));
                System.out.println("Tip: Use Bows or Spells!");
                finalDamage = 0;
            }
        }

        return finalDamage;
    }

    //Spell Damage Logic
    public static double applySpellDamageEffects(Hero h, Monster target, Spell s, double originalDamage) {
        // Dragon King takes full damage from spells
        if (target instanceof DragonKingBoss) {
             System.out.println(UI.c(UI.BRIGHT_CYAN, "The spell breaks through the Dragon's defense!"));
        }
        return originalDamage;
    }
}