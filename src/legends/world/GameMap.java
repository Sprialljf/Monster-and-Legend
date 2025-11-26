package legends.world;

import legends.market.Market;

import java.util.Random;
import legends.game.UI;

public class GameMap {
    private final int size;
    private final Cell[][] grid;
    private final Random rng = new Random();

    public GameMap(int size, Market sharedMarket) {
        this.size = size;
        this.grid = new Cell[size][size];
        generate(sharedMarket);
    }

    private void generate(Market market) {
        // 0.2 X, 0.3 M, 0.5 common
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                double p = rng.nextDouble();
                if (p < 0.2) {
                    grid[r][c] = new InaccessibleCell(r, c);
                } else if (p < 0.5) {
                    grid[r][c] = new MarketCell(r, c, market);
                } else {
                    grid[r][c] = new CommonCell(r, c);
                }
            }
        }
        grid[0][0] = new CommonCell(0, 0);
    }

    public int getSize() { return size; }
    public Cell getCell(int r, int c) { return grid[r][c]; }

    public void print(int heroRow, int heroCol) {
        String hSymbol = "I";
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                System.out.print("+---");
            }
            System.out.println("+");
            for (int c = 0; c < size; c++) {
                System.out.print("| ");
                if (r == heroRow && c == heroCol) {
                    System.out.print(UI.c(UI.BRIGHT_CYAN + UI.BOLD, hSymbol));
                } else {
                    char ch = grid[r][c].displayChar();
                    switch (ch) {
                        case 'X':
                            System.out.print(UI.c(UI.BRIGHT_RED, "X"));
                            break;
                        case 'M':
                            System.out.print(UI.c(UI.BRIGHT_GREEN, "M"));
                            break;
                    
                        default:
                            System.out.print(" ");
                    }
                }
                System.out.print(" ");
            }
            System.out.println("|");
        }
        for (int c = 0; c < size; c++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }
}
