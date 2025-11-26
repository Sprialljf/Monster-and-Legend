package legends.game;

import legends.characters.heroes.*;
import legends.characters.monsters.*;
import legends.io.GameData;
import legends.items.weapons.Weapon;
import legends.items.armor.Armor;
import legends.items.potions.Potion;
import legends.items.spells.Spell;
import legends.market.Market;
import legends.world.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameController {

    private final Scanner in = new Scanner(System.in);

    private final String baseDir = detectBaseDir();

    private final MainMenu mainMenu = new MainMenu(in);

    private Party party;
    private GameMap map;
    private Market market;

    private int floor = 1;                 
    private boolean hasActiveGame = false; 
    private boolean gameOver = false;      
    private boolean exitRequested = false;

    private List<Weapon> weapons;
    private List<Armor> armors;
    private List<Potion> potions;
    private List<Spell> spells;

    private List<Monster> dragonPool;
    private List<Monster> spiritPool;
    private List<Monster> exoPool;

    private BossConfig leoricCfg;
    private BossConfig dragonKingCfg;
    private BossConfig lichKingCfg;

    private boolean leoricDefeated = false;
    private boolean dragonKingDefeated = false;
    private boolean lichKingDefeated = false;

    private final Random rng = new Random();

 
    public void start() throws IOException {
        while (!exitRequested) {
            MainMenu.Choice choice = mainMenu.prompt(hasActiveGame, floor);
            switch (choice) {
                case NEW_GAME:
                    startNewGame();
                    break;
                case CONTINUE:
                    continueGame();
                    break;
                case EXIT:
                    exitRequested = true;
                    break;
            }
        }
        System.out.println("Goodbye!");
    }

    // New/Continue Game

    private void startNewGame() throws IOException {
        System.out.println("Loading game data...");
        loadGameData();
        resetGameStateForNewGame();
        createParty();

        int mapSize = MapSizeSelector.chooseMapSize(in); 
        map = new GameMap(mapSize, market);

        hasActiveGame = true;
        gameOver = false;

        gameLoop();
    }

    private void continueGame() {
        if (!hasActiveGame || party == null || map == null || gameOver) {
            System.out.println("No active game to continue. Please start a new game.");
            return;
        }
        System.out.println("Continuing your adventure on floor " + floor + "...");
        gameLoop();
    }
    
    private void loadGameData() throws IOException {
        // items
        weapons = GameData.loadWeapons(baseDir);
        armors  = GameData.loadArmor(baseDir);
        potions = GameData.loadPotions(baseDir);
        spells  = GameData.loadAllSpells(baseDir);

        // monster
        dragonPool = new ArrayList<>(GameData.loadDragons(baseDir));
        spiritPool = new ArrayList<>(GameData.loadSpirits(baseDir));
        exoPool    = new ArrayList<>(GameData.loadExoskeletons(baseDir));

        // Boss config
        leoricCfg      = GameData.loadBossConfig(baseDir, "Leoric.txt");
        dragonKingCfg  = GameData.loadBossConfig(baseDir, "Dragon_King.txt");
        lichKingCfg    = GameData.loadBossConfig(baseDir, "Arthas.txt");

        // market
        market = new Market();
        market.addWeapons(weapons);
        market.addArmors(armors);
        market.addPotions(potions);
        market.addSpells(spells);
    }
    private String detectBaseDir() {
        String cwd = System.getProperty("user.dir");

        // case1: from project root 
        File direct = new File(cwd, "src/legends/data/Weaponry.txt");
        if (direct.exists()) {
            return "src/legends/data";
        }

        // case2: from out dir
        File parent = new File(cwd, "../src/legends/data/Weaponry.txt");
        if (parent.exists()) {
            return "../src/legends/data";
        }

        System.out.println("[WARN] Could not auto-detect data dir, fallback to src/legends/data");
        return "src/legends/data";
    }
    private void resetGameStateForNewGame() {
        floor = 1;
        leoricDefeated = false;
        dragonKingDefeated = false;
        lichKingDefeated = false;
    }

    // Party Creation

    private void createParty() throws IOException {
        List<Warrior>  warriors  = GameData.loadWarriors(baseDir);
        List<Paladin>  paladins  = GameData.loadPaladins(baseDir);
        List<Sorcerer> sorcerers = GameData.loadSorcerers(baseDir);

        party = new Party();

        System.out.println("Choose your heroes (up to 3).");
        boolean adding = true;
        while (adding && party.getHeroes().size() < 3) {
            System.out.println("1) Warrior");
            System.out.println("2) Paladin");
            System.out.println("3) Sorcerer");
            System.out.println("q) Done");
            String choice = in.nextLine().trim().toLowerCase();
            switch (choice) {
                case "1":
                    pickHeroFromList(warriors);
                    break;
                case "2":
                    pickHeroFromList(paladins);
                    break;
                case "3":
                    pickHeroFromList(sorcerers);
                    break;
                case "q":
                    adding = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
        if (party.getHeroes().isEmpty()) {
            System.out.println("You must choose at least one hero. Picking first warrior for you.");
            party.addHero(warriors.get(0));
        }
    }

    private void pickHeroFromList(List<? extends Hero> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, list.get(i).getName());
        }
        System.out.println("Choose hero number:");
        try {
            int idx = Integer.parseInt(in.nextLine().trim());
            if (idx < 1 || idx > list.size()) return;
            party.addHero(list.get(idx - 1));
        } catch (Exception ignored) {}
    }

    private void gameLoop() {
        boolean running = true;
        gameOver = false;

        while (running && !gameOver) {
            System.out.println();
            FloorVision.printFloorHeader(floor);
            map.print(party.getRow(), party.getCol());

            System.out.println("Select an option:");
            System.out.println("Move up:    w");
            System.out.println("Move down:  s");
            System.out.println("Move left:  a");
            System.out.println("Move right: d");
            System.out.println("Interact:   e");
            System.out.println("Check inventory / stats: i");
            System.out.println("Fight floor boss:        b");
            System.out.println("Quit to main menu:       q");

            String cmd = in.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "w": move(-1, 0); break;
                case "s": move(1, 0);  break;
                case "a": move(0, -1); break;
                case "d": move(0, 1);  break;
                case "e": interact();  break;
                case "i": showStats(); break;
                case "b": fightBossForCurrentFloor(); break;
                case "q":
                    running = false;   // return mainmenu
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        if (gameOver) {
            hasActiveGame = false;
            System.out.println("Game over. Returning to main menu...");
        }
    }

    private void move(int dr, int dc) {
        int nr = party.getRow() + dr;
        int nc = party.getCol() + dc;
        if (nr < 0 || nr >= map.getSize() || nc < 0 || nc >= map.getSize()) {
            System.out.println("Cannot move outside the map.");
            return;
        }
        Cell dest = map.getCell(nr, nc);
        if (!dest.isAccessible()) {
            System.out.println("Inaccessible cell.");
            return;
        }
        party.move(dr, dc);

        if (dest instanceof CommonCell) {
            if (rng.nextDouble() < 0.3) {
                startBattle();
            }
        }
    }

    private void interact() {
        Cell cell = map.getCell(party.getRow(), party.getCol());
        if (cell instanceof MarketCell) {
            for (Hero h : party.getHeroes()) {
                System.out.println("Entering market with " + h.getName());
                market.open(h, in);
            }
        } else {
            System.out.println("Nothing to interact with here.");
        }
    }

    private void showStats() {
        System.out.println(UI.title("Party Status"));
        for (Hero h : party.getHeroes()) {
            UI.printHeroStatus(h);
        }
    }


    private void startBattle() {
        System.out.println("A battle is about to start!");

        int n = party.getHeroes().size();
        List<Monster> monsters = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int type = rng.nextInt(3);
            Monster m;
            if (type == 0) {
                m = dragonPool.get(rng.nextInt(dragonPool.size()));
            } else if (type == 1) {
                m = spiritPool.get(rng.nextInt(spiritPool.size()));
            } else {
                m = exoPool.get(rng.nextInt(exoPool.size()));
            }
            m = cloneMonster(m);
            monsters.add(m);
        }

        Battle battle = new Battle(party.getHeroes(), monsters, in);
        boolean win = battle.fight();
        if (!win) {
            System.out.println("Your party has fallen...");
            gameOver = true;
        }
    }

    private Monster cloneMonster(Monster m) {
        if (m instanceof Dragon d) {
            return new Dragon(d.getName(), d.getLevel(), d.attackDamage(),
                    0, (int) (d.dodgeChance() * 100));
        } else if (m instanceof Spirit s) {
            return new Spirit(s.getName(), s.getLevel(), s.attackDamage(),
                    0, (int) (s.dodgeChance() * 100));
        } else if (m instanceof Exoskeleton e) {
            return new Exoskeleton(e.getName(), e.getLevel(), e.attackDamage(),
                    0, (int) (e.dodgeChance() * 100));
        }
        return m;
    }

   //Boss Battle

    private boolean runBossBattle(Monster boss) {
        List<Monster> monsters = new ArrayList<>();
        monsters.add(boss);
        Battle battle = new Battle(party.getHeroes(), monsters, in);
        return battle.fight();
    }

    // Boss floor
    private void fightBossForCurrentFloor() {
        Monster boss = null;

        if (floor == 1) {
            if (leoricDefeated) {
                System.out.println("You have already defeated Leoric on this floor.");
                return;
            }
            boss = new SkeletonKingBoss(leoricCfg);
        } else if (floor == 2) {
            if (dragonKingDefeated) {
                System.out.println("You have already defeated Dragon King on this floor.");
                return;
            }
            boss = new DragonKingBoss(dragonKingCfg);
        } else if (floor == 3) {
            if (lichKingDefeated) {
                System.out.println("You have already defeated Arthas, the Lich King.");
                return;
            }
            boss = new LichKingBoss(lichKingCfg);
        } else {
            System.out.println("There is no boss on floor " + floor + ".");
            return;
        }

        System.out.println("\n=== Boss Battle ===");
        System.out.println(boss.toString());
        System.out.println("Prepare yourself...\n");

        boolean win = runBossBattle(boss);

        if (win) {
            if (boss instanceof SkeletonKingBoss) {
                leoricDefeated = true;
                System.out.println("You defeated Skeleton King Leoric!");
                advanceFloor();
            } else if (boss instanceof DragonKingBoss) {
                dragonKingDefeated = true;
                System.out.println("You defeated the Dragon King!");
                advanceFloor();
            } else if (boss instanceof LichKingBoss) {
                lichKingDefeated = true;
                System.out.println("You defeated Arthas, the Lich King!");
                System.out.println("Congratulations, you have cleared the game!");
                gameOver = true;
                hasActiveGame = false;
            }
        } else {
            System.out.println("The party was defeated by the boss...");
            gameOver = true;
        }
    }


    private void advanceFloor() {
        floor++;

        int oldSize = map.getSize();
        int newSize = oldSize + 2;

        System.out.println("\n== Floor " + floor + " unlocked! ==");
        System.out.println("Map expands from " + oldSize + "x" + oldSize +
                           " to " + newSize + "x" + newSize + "!");

        market = new Market();
        market.addWeapons(weapons);
        market.addArmors(armors);
        market.addPotions(potions);
        market.addSpells(spells);

        map = new GameMap(newSize, market);

        // reset position
        party.setPosition(0, 0); 
    }
}
