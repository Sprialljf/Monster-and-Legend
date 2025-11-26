package legends.game;

import legends.characters.heroes.Hero;
import legends.characters.monsters.Monster;
import legends.items.weapons.HandUsage;
import legends.items.potions.Potion;
import legends.items.spells.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private final List<Hero> heroes;
    private final List<Monster> monsters;
    private final Scanner in;
    private final Random rng = new Random();

    private int roundCounter = 0; 

    public Battle(List<Hero> heroes, List<Monster> monsters, Scanner in) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.in = in;
    }

    private void printMonstersDebug() {
        System.out.println("== Monsters ==");
        for (Monster m : monsters) {
            System.out.println(m);
        }
    }

    public boolean fight() {
        System.out.println(UI.title("Combat Begins!"));
        System.out.println("Enemies:");
        for (Monster m : monsters) {
            UI.printMonsterStatus(m);
        }

        while (true) {
            //round count
            roundCounter++; 
            System.out.println(UI.c(UI.BRIGHT_WHITE, "\n--- Round " + roundCounter + " ---"));

            //  Call external logic for Boss Battle)
            BossBattle.checkRoundEvents(roundCounter, monsters, heroes);

            // Check win (monsters all defeated)
            if (monsters.stream().allMatch(Monster::isDefeated)) {
                System.out.println(UI.c(UI.BRIGHT_GREEN, "Heroes are victorious!"));
                reward();
                return true;
            }
            //  Check win (heroes all fainted)
            if (heroes.stream().allMatch(Hero::isFainted)) {
                UI.printYouDied();
                return false;
            }

            //  Print battlefield status
            printMonstersDebug();

            heroesTurn();

            if (monsters.stream().allMatch(Monster::isDefeated)) {
                System.out.println(UI.c(UI.BRIGHT_GREEN, "Heroes are victorious!"));
                reward();
                return true;
            }

            monstersTurn();
            
            // regenerate health and mana in end of round
            heroes.forEach(Hero::endOfRoundRegen);
        }
    }

    private void heroesTurn() {
        for (int i = 0; i < heroes.size(); i++) {
            Hero h = heroes.get(i);
            if (h.isFainted()) continue;

            System.out.printf("%s takes their turn!%n", h.getName());
            boolean acted = false;
            while (!acted) {
                System.out.println("1) Attack  2) Potion  3) Spell  4) Stats  q) Quit");
                if (!in.hasNextLine()) return;
                String choice = in.nextLine().trim().toLowerCase();

                switch (choice) {
                    case "1": // Attack
                        HandUsage usage = chooseUsage(h);
                        Monster target = chooseMonster();
                        if (target == null) break;

                        // Calculate damage
                        double baseDmg = h.physicalAttackDamage(usage);

                        //  Call external logic 
                        double finalDmg = BossBattle.applySpecialDamageEffects(h, target, baseDmg);

                        // Dodge check
                        if (rng.nextDouble() < target.dodgeChance()) {
                            System.out.println(target.getName() + " dodged!");
                        } else {
                            target.receiveDamage(finalDmg);
                            System.out.printf("%s hits %s for %.0f damage. (HP=%d/%d)%n", 
                                h.getName(), target.getName(), finalDmg, target.getHp(), target.getMaxHp());
                        }
                        acted = true;
                        break;

                    case "2": usePotion(h); acted = true; break;
                    case "3": castSpell(h); acted = true; break;
                    case "4": showPartyStats(); break;
                    case "q": System.exit(0); break;
                    default: System.out.println("Invalid.");
                }
            }
        }
    }

    // support function

    private HandUsage chooseUsage(Hero h) {
        System.out.println("1) One-hand 2) Two-hand 3) Dual");
        if(!in.hasNextLine()) return HandUsage.ONE_HAND;
        String c = in.nextLine().trim();
        if (c.equals("2")) return HandUsage.TWO_HAND;
        if (c.equals("3")) return HandUsage.DUAL_WIELD;
        return HandUsage.ONE_HAND;
    }

    private Monster chooseMonster() {
        int idx = 1;
        List<Monster> alive = new ArrayList<>();
        for (Monster m : monsters) {
            if (!m.isDefeated()) {
                System.out.printf("%d) %s (HP=%d)%n", idx++, m.getName(), m.getHp());
                alive.add(m);
            }
        }
        if (alive.isEmpty()) return null;
        System.out.print("Choose target: ");
        try {
            int c = Integer.parseInt(in.nextLine().trim());
            if (c < 1 || c > alive.size()) return null;
            return alive.get(c-1);
        } catch(Exception e) { return null; }
    }
    
    private void usePotion(Hero h) {
        List<Object> inv = h.getInventory();
        if (inv.isEmpty()) { System.out.println("Inventory empty."); return; }
        
        int idx = 1;
        for (Object o : inv) {
            if (o instanceof Potion) {
                System.out.printf("%d) %s%n", idx, o);
            }
            idx++;
        }
        System.out.println("Select potion index (1-" + inv.size() + ") or 0 to cancel:");
        try {
            int i = Integer.parseInt(in.nextLine().trim());
            if (i > 0 && i <= inv.size() && inv.get(i-1) instanceof Potion) {
                 ((Potion) inv.get(i-1)).applyTo(h);
                 inv.remove(i-1);
            }
        } catch(Exception ignored){}
    }
    
    private void castSpell(Hero h) {
        List<Object> inv = h.getInventory();
        int idx = 1;
        for (Object o : inv) {
            if (o instanceof Spell) {
                System.out.printf("%d) %s%n", idx, o);
            }
            idx++;
        }
        System.out.println("Select spell index or 0 to cancel:");
        try {
            int i = Integer.parseInt(in.nextLine().trim());
            if (i <= 0 || i > inv.size()) return;
            Object obj = inv.get(i-1);
            if(obj instanceof Spell) {
                Spell s = (Spell) obj;
                Monster target = chooseMonster();
                if(target == null) return;
                double dmg = h.castSpell(s, target);
                if (dmg <= 0) {
                   System.out.println("Cannot cast/Not enough mana.");
                   return;
                }
                if(rng.nextDouble() < target.dodgeChance()){
                    System.out.println("Spell dodged!");
                } else {
                    target.receiveDamage(dmg);
                    System.out.printf("Spell hits %s for %.1f.%n", target.getName(), dmg);
                }
            }
        } catch(Exception ignored){}
    }

    private void showPartyStats() {
        for(Hero h : heroes) UI.printHeroStatus(h);
    }

    private void monstersTurn() {
        System.out.println("Enemies turn...");
        for (Monster m : monsters) {
            if (m.isDefeated()) continue;
            Hero t = pickRandomHero();
            if (t == null) return;
            
            int dmg = m.attackDamage();
            t.receivePhysicalDamage(dmg);
            System.out.printf("%s hits %s for %d damage. (HP=%d)%n", 
                m.getName(), t.getName(), dmg, t.getHp());
        }
    }

    private Hero pickRandomHero() {
        List<Hero> alive = heroes.stream().filter(h -> !h.isFainted()).toList();
        if (alive.isEmpty()) return null;
        return alive.get(rng.nextInt(alive.size()));
    }

    private void reward() {
        int gold = monsters.size() * 500;
        int exp = monsters.size() * 10;
        System.out.printf("Battle won! Gold +%d, Exp +%d%n", gold, exp);
        for (Hero h : heroes) {
            if (!h.isFainted()) {
                h.gainGold(gold);
                h.gainExp(exp);
            }
            h.reviveAfterBattle();
        }
    }
}