package legends.world;

public class InaccessibleCell extends Cell {
    public InaccessibleCell(int r, int c) { super(r, c); }

    @Override public boolean isAccessible() { return false; }
    @Override public char displayChar() { return 'X'; }
}
