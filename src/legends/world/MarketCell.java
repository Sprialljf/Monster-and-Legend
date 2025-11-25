package legends.world;

import legends.market.Market;

public class MarketCell extends Cell {
    private final Market market;

    public MarketCell(int r, int c, Market market) {
        super(r, c);
        this.market = market;
    }

    @Override public boolean isAccessible() { return true; }
    @Override public char displayChar() { return 'M'; }

    public Market getMarket() { return market; }
}
