package legends.market;

import legends.items.weapons.Weapon;
import legends.items.armor.Armor;
import legends.items.potions.Potion;
import legends.items.spells.Spell;
import legends.characters.heroes.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Market {
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Armor> armors = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Spell> spells = new ArrayList<>();

    public void addWeapons(List<Weapon> list) { weapons.addAll(list); }
    public void addArmors(List<Armor> list) { armors.addAll(list); }
    public void addPotions(List<Potion> list) { potions.addAll(list); }
    public void addSpells(List<Spell> list) { spells.addAll(list); }

    public void open(Hero hero, Scanner in) {
        boolean stay = true;
        while (stay) {
            System.out.println("What are you looking to do?");
            System.out.println("1) Buy");
            System.out.println("2) Sell");
            System.out.println("3) Repair weapons");
            System.out.println("b) Back");
            String choice = in.nextLine().trim().toLowerCase();
            switch (choice) {
                case "1": buyMenu(hero, in); break;
                case "2": sellMenu(hero, in); break;
                case "3": repairMenu(hero, in); break;
                case "b": stay = false; break;
            }
        }
    }

    private void buyMenu(Hero hero, Scanner in) {
        System.out.printf("Gold: %d%n", hero.getGold());
        System.out.println("What are you looking to buy?");
        System.out.println("1) Weapons");
        System.out.println("2) Armor");
        System.out.println("3) Potions");
        System.out.println("4) Spells");
        System.out.println("b) back");
        String choice = in.nextLine().trim().toLowerCase();
        switch (choice) {
            case "1":
                buyFromList(hero, in, weapons);
                break;
            case "2":
                buyFromList(hero, in, armors);
                break;
            case "3":
                buyFromList(hero, in, potions);
                break;
            case "4":
                buyFromList(hero, in, spells);
                break;
            default:
                break;
        }
    }

    private void sellMenu(Hero hero, Scanner in) {
        List<Object> inv = hero.getInventory();
        if (inv.isEmpty()) {
            System.out.println("Your inventory is empty.");
            return;
        }
        for (int i = 0; i < inv.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, inv.get(i));
        }
        System.out.println("Enter item number to sell, or 0 to cancel:");
        int idx = parseIntSafe(in);
        if (idx <= 0 || idx > inv.size()) return;
        Object obj = inv.remove(idx - 1);
        int price;
        if (obj instanceof Weapon) price = ((Weapon)obj).getCost() / 2;
        else if (obj instanceof Armor) price = ((Armor)obj).getCost() / 2;
        else if (obj instanceof Potion) price = ((Potion)obj).getCost() / 2;
        else if (obj instanceof Spell)  price = ((Spell)obj).getCost() / 2;
        else price = 0;
        hero.gainGold(price);
        System.out.printf("Sold for %d gold.%n", price);
    }

    private void repairMenu(Hero hero, Scanner in) {
        // 简化：修理 mainHand & offHand
        // 现实中你可以做得更细；这里只是示例
        System.out.println("Repairing all equipped weapons costs 100 gold.");
        if (hero.getGold() < 100) {
            System.out.println("Not enough gold.");
            return;
        }
        hero.spendGold(100);
        // 需要让 Hero 提供 repair 方法，这里省略；可以手动修 main/off
        System.out.println("Weapons repaired (if any).");
    }

    private <T> void buyFromList(Hero hero, Scanner in, List<T> list) {
        if (list.isEmpty()) {
            System.out.println("No items available.");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, list.get(i));
        }
        System.out.println("Enter the number of the item you want, or 0 to cancel.");
        int idx = parseIntSafe(in);
        if (idx <= 0 || idx > list.size()) return;
        T item = list.get(idx - 1);
        int cost = 0;
        int reqLevel = 0;
        if (item instanceof Weapon) {
            cost = ((Weapon) item).getCost();
            reqLevel = ((Weapon) item).getRequiredLevel();
        } else if (item instanceof Armor) {
            cost = ((Armor) item).getCost();
            reqLevel = ((Armor) item).getRequiredLevel();
        } else if (item instanceof Potion) {
            cost = ((Potion) item).getCost();
            reqLevel = ((Potion) item).getRequiredLevel();
        } else if (item instanceof Spell) {
            cost = ((Spell) item).getCost();
            reqLevel = ((Spell) item).getRequiredLevel();
        }
        if (hero.getLevel() < reqLevel) {
            System.out.println("Level too low.");
            return;
        }
        if (!hero.spendGold(cost)) {
            System.out.println("Not enough gold.");
            return;
        }
        hero.getInventory().add(item);
        System.out.println("Bought: " + item);
    }

    private int parseIntSafe(Scanner in) {
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
