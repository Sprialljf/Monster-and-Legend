package legends.world;

public class CommonCell extends Cell {
    public CommonCell(int r, int c) { super(r, c); }

    @Override public boolean isAccessible() { return true; }
    @Override public char displayChar() { return ' '; }
}
