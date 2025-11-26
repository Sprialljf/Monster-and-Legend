package legends.game;

import legends.characters.heroes.Hero;
import legends.characters.monsters.Monster;

public class UI {
    public static final String RESET       = "\u001B[0m";
    public static final String RED         = "\u001B[31m";
    public static final String GREEN       = "\u001B[32m";
    public static final String YELLOW      = "\u001B[33m";
    public static final String BLUE        = "\u001B[34m";
    public static final String MAGENTA     = "\u001B[35m";
    public static final String CYAN        = "\u001B[36m";
    public static final String WHITE       = "\u001B[37m";
    public static final String BRIGHT_PURPLE = "\033[0;95m";
    public static final String BRIGHT_RED      = "\u001B[91m";
    public static final String BRIGHT_GREEN    = "\u001B[92m";
    public static final String BRIGHT_YELLOW   = "\u001B[93m";
    public static final String BRIGHT_BLUE     = "\u001B[94m";
    public static final String BRIGHT_MAGENTA  = "\u001B[95m";
    public static final String BRIGHT_CYAN     = "\u001B[96m";
    public static final String BRIGHT_WHITE    = "\u001B[97m";

    public static final String BOLD            = "\u001B[1m";

    public static String c(String color, String text) {
        return color + text + RESET;
    }

    public static String title(String text) {
        return "\n" + c(BOLD + BRIGHT_MAGENTA, "== " + text + " ==") + "\n";
    }

    //General bar
    public static String bar(int current, int max, int width, String color) {
        if (max <= 0) max = 1;
        double ratio = Math.max(0, Math.min(1.0, current / (double) max));
        int filled = (int) Math.round(ratio * width);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < filled ? "█" : "-");
        }
        sb.append("]");
        return c(color, sb.toString());
    }
    //EXP bar(narrow)
    public static String expBar(Hero h) {
        int exp = h.getExp();
        int need = h.getLevel() * 10;
        double ratio = need > 0 ? (double) exp / need : 0.0;
        ratio = Math.max(0, Math.min(1, ratio));

        int width = 10;
        int filled = (int) Math.round(ratio * width);

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            sb.append(i < filled ? "█" : "-");
        }
        sb.append("]");

        return c(BRIGHT_YELLOW, sb.toString());
    }

    //Hero status
    public static void printHeroStatus(Hero h) {
        System.out.println(c(BOLD + BRIGHT_CYAN, h.getName())
                + ", Level " + h.getLevel());

        // HP bar
        String hpBar = bar(h.getHp(), h.getMaxHp(), 20, BRIGHT_RED);
        System.out.printf("Health: %s %s%d/%d%s%n",
                hpBar, BRIGHT_WHITE, h.getHp(), h.getMaxHp(), RESET);

        // MP bar
        String mpBar = bar(h.getMana(), h.getMaxMana(), 20, BRIGHT_BLUE);
        System.out.printf("Mana:   %s %s%d/%d%s%n",
                mpBar, BRIGHT_WHITE, h.getMana(), h.getMaxMana(), RESET);

        // EXP bar
        String exp = expBar(h);
        System.out.printf("EXP:    %s %s%d/%d%s%n",
                exp, BRIGHT_WHITE, h.getExp(), h.getLevel() * 10, RESET);

        System.out.printf("STR: %d  DEX: %d  AGI: %d  Gold: %d%n",
                h.getStrength(), h.getDexterity(),
                h.getAgility(), h.getGold());

        System.out.println("------------------------------");
    }

    // Monster status
    public static void printMonsterStatus(Monster m) {
        System.out.println(c(BOLD + BRIGHT_YELLOW, m.getName())
                + ", Level " + m.getLevel());
        String hpBar = bar(m.getHp(), m.getLevel() * 100, 20, BRIGHT_RED);
        System.out.printf("Health: %s %s%d/%d%s%n",
                hpBar, BRIGHT_WHITE, m.getHp(), m.getLevel() * 100, RESET);
        System.out.println("------------------------------");
        System.out.println("Damage:  " + m.getDamage());
        System.out.println("Defense: " + m.getDefense());
        System.out.println("Dodge:   " + m.getDodgeChanceRaw());
    }
    public static void printYouDied() {
        String line = c(BRIGHT_RED + BOLD, "===== YOU DIED =====");
        System.out.println("\n" + line + "\n");
    }
}
