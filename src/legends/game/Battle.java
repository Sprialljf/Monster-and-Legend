package legends.game;

import legends.characters.heroes.Hero;
import legends.characters.monsters.Monster;
import legends.items.weapons.HandUsage;
import legends.items.potions.Potion;
import legends.items.spells.Spell;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private final List<Hero> heroes;
    private final List<Monster> monsters;
    private final Scanner in;
    private final Random rng = new Random();

    public Battle(List<Hero> heroes, List<Monster> monsters, Scanner in) {
        this.heroes = heroes;
        this.monsters = monsters;
        this.in = in;
    }

    // Debug: print detailed monster info
    private void printMonstersDebug() {
        System.out.println("== Monsters ==");
        for (Monster m : monsters) {
            System.out.println(m);
        }
    }

    public boolean fight() {
        System.out.println("Combat begins!");
        System.out.println("Enemies:");
        for (Monster m : monsters) {
            UI.printMonsterStatus(m);
        }

        while (true) {
            // each round start: check win/lose
            if (monsters.stream().allMatch(Monster::isDefeated)) {
                System.out.println("Heroes are victorious!");
                reward();
                return true;
            }
            if (heroes.stream().allMatch(Hero::isFainted)) {
                UI.printYouDied();
                System.out.println("All heroes have fallen...");
                return false;
            }

            printMonstersDebug();

            // hero's turn
            heroesTurn();

            if (heroes.stream().allMatch(Hero::isFainted)) {
                UI.printYouDied();
                System.out.println("All heroes have fallen...");
                return false;
            }
            if (monsters.stream().allMatch(Monster::isDefeated)) {
                System.out.println("Heroes are victorious!");
                reward();
                return true;
            }

            // monsters' turn
            monstersTurn();
            heroes.forEach(Hero::endOfRoundRegen);
        }
    }

    // heroes' turn

    private void heroesTurn() {
        for (Hero h : heroes) {
            if (h.isFainted()) continue;

            System.out.printf("%s takes their turn!%n", h.getName());

            boolean acted = false;
            while (!acted) {
                System.out.println("1) Attack");
                System.out.println("2) Use a potion");
                System.out.println("3) Cast a spell");
                System.out.println("4) View party stats");
                System.out.println("q) Quit game");

                if (!in.hasNextLine()) {
                    System.out.println("No more input. Ending battle.");
                    return;
                }

                String choice = in.nextLine().trim().toLowerCase();
                switch (choice) {
                    case "1": {
                        HandUsage usage = chooseUsage(h);
                        if (usage == null) {  
                            return;
                        }
                        Monster target = chooseMonster();
                        if (target == null) { 
                            acted = true;
                            break;
                        }
                        double dmg = h.physicalAttackDamage(usage);
                        if (rng.nextDouble() < target.dodgeChance()) {
                            System.out.println(target.getName() + " dodged the attack!");
                        } else {
                            target.receiveDamage(dmg);
                            System.out.printf(
                                    "%s hits %s for %.1f damage. (HP=%d)%n",
                                    h.getName(), target.getName(), dmg, target.getHp()
                            );
                        }
                        acted = true;
                        break;
                    }
                    case "2":
                        usePotion(h);
                        acted = true;
                        break;
                    case "3":
                        castSpell(h);
                        acted = true;
                        break;
                    case "4":
                        showPartyStats();
                        break;
                    case "q":
                        System.out.println("Quitting game...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        }
    }

    private HandUsage chooseUsage(Hero h) {
        System.out.println("Choose weapon usage:");
        System.out.println("1) One hand");
        System.out.println("2) Two hands");
        System.out.println("3) Dual wield (if possible)");

        if (!in.hasNextLine()) {
            System.out.println("No more input when choosing weapon usage.");
            return null;
        }

        String c = in.nextLine().trim();
        if (c.equals("2")) return HandUsage.TWO_HAND;
        if (c.equals("3")) return HandUsage.DUAL_WIELD;
        return HandUsage.ONE_HAND;
    }

    private Monster chooseMonster() {
        int idx = 1;
        for (Monster m : monsters) {
            if (m.isDefeated()) continue;
            System.out.printf(
                    "%d) %s (HP=%d)%n",
                    idx++, UI.c(UI.BRIGHT_YELLOW, m.getName()), m.getHp()
            );
        }
        if (idx == 1) {
            System.out.println("No alive monsters to target.");
            return null;
        }

        System.out.println("Choose monster:");

        if (!in.hasNextLine()) {
            System.out.println("No input for target selection.");
            return null;
        }

        try {
            int choice = Integer.parseInt(in.nextLine().trim());
            if (choice < 1 || choice > monsters.size()) return null;
            Monster m = monsters.get(choice - 1);
            if (m.isDefeated()) {
                System.out.println("That monster is already defeated.");
                return null;
            }
            return m;
        } catch (Exception e) {
            return null;
        }
    }

    private void usePotion(Hero h) {
        List<Object> inv = h.getInventory();
        int idx = 1;
        for (Object o : inv) {
            if (o instanceof Potion) {
                System.out.printf("%d) %s%n", idx, o);
            }
            idx++;
        }
        System.out.println("Choose potion number (order in inventory), or 0 to cancel:");

        if (!in.hasNextLine()) {
            System.out.println("No input for potion selection.");
            return;
        }

        try {
            int choice = Integer.parseInt(in.nextLine().trim());
            if (choice <= 0 || choice > inv.size()) return;
            Object obj = inv.get(choice - 1);
            if (obj instanceof Potion) {
                ((Potion) obj).applyTo(h);
                inv.remove(choice - 1);
            }
        } catch (Exception ignored) {}
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
        System.out.println("Choose spell number (order in inventory), or 0 to cancel:");

        if (!in.hasNextLine()) {
            System.out.println("No input for spell selection.");
            return;
        }

        try {
            int choice = Integer.parseInt(in.nextLine().trim());
            if (choice <= 0 || choice > inv.size()) return;
            Object obj = inv.get(choice - 1);
            if (!(obj instanceof Spell)) return;
            Spell s = (Spell) obj;

            Monster target = chooseMonster();
            if (target == null) return;

            double dmg = h.castSpell(s, target);
            if (dmg <= 0) {
                System.out.println("Cannot cast this spell.");
                return;
            }
            if (rng.nextDouble() < target.dodgeChance()) {
                System.out.println(target.getName() + " dodged the spell!");
            } else {
                target.receiveDamage(dmg);
                System.out.printf(
                        "%s hits %s with %s for %.1f damage. (HP=%d)%n",
                        h.getName(), target.getName(), s.getName(), dmg, target.getHp()
                );
            }
        } catch (Exception ignored) {}
    }

    private void showPartyStats() {
        System.out.println(UI.title("Party Status"));
        for (Hero h : heroes) {
            UI.printHeroStatus(h);
        }
    }

    // monsters' turn

    private void monstersTurn() {
        System.out.println("Enemies take their turn...");
        for (Monster m : monsters) {
            if (m.isDefeated()) continue;
            Hero target = pickRandomHero();
            if (target == null) return;
            if (target.isFainted()) continue;

            if (rng.nextDouble() < target.dodgeChance()) {
                System.out.printf("%s dodged %s's attack!%n",
                        target.getName(), m.getName());
                continue;
            }
            int dmg = m.attackDamage();
            target.receivePhysicalDamage(dmg);
            System.out.printf("%s hits %s for %d damage. (HP=%d)%n",
                    m.getName(), target.getName(), dmg, target.getHp());
        }
    }

    private Hero pickRandomHero() {
        List<Hero> alive = heroes.stream()
                .filter(h -> !h.isFainted())
                .toList();
        if (alive.isEmpty()) return null;
        return alive.get(rng.nextInt(alive.size()));
    }

    // reward

    private void reward() {
        int gold = monsters.size() * 100;
        int exp  = monsters.size() * 2;

        for (Hero h : heroes) {
            if (!h.isFainted()) {
                h.gainGold(gold);
                h.gainExp(exp);
            }
            h.reviveAfterBattle();
        }
    }
}
