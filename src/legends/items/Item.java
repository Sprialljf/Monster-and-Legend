package legends.items;

public abstract class Item {
    protected final String name;
    protected final int cost;
    protected final int requiredLevel;

    public Item(String name, int cost, int requiredLevel) {
        this.name = name;
        this.cost = cost;
        this.requiredLevel = requiredLevel;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getRequiredLevel() { return requiredLevel; }

    @Override
    public String toString() {
        return String.format("%s (L%d, %d gold)", name, requiredLevel, cost);
    }
}
