package legends.game;

import legends.characters.heroes.Hero;

import java.util.ArrayList;
import java.util.List;

public class Party {
    private final List<Hero> heroes = new ArrayList<>();
    private int row = 0;
    private int col = 0;

    public void addHero(Hero h) {
        if (heroes.size() >= 3) throw new IllegalStateException("Party full");
        heroes.add(h);
    }

    public List<Hero> getHeroes() { return heroes; }

    public int getRow() { return row; }
    public int getCol() { return col; }

    public void move(int dr, int dc) {
        row += dr;
        col += dc;
    }

    public boolean allFainted() {
        return heroes.stream().allMatch(Hero::isFainted);
    }
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
