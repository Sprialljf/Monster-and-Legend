package legends.world;

public abstract class Cell {
    protected final int row;
    protected final int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isAccessible();
    public abstract char displayChar();
}
