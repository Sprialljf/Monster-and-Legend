package legends.characters.monsters;

public class LichKingBoss extends Monster {

    private final int summonInterval;
    private final boolean summonOnRound1;
    private final int rewardAfterRounds;
    private final String rewardItem;
    private final double percentDamageFromRewardItem;
    private final String summonSpiritName;
    private final String summonDragonName;
    private final String summonExoName;

    private int roundsElapsed = 0;

    public LichKingBoss(BossConfig cfg) {
        super(
            cfg.getName(),
            cfg.getLevel(),
            cfg.getDamage(),
            cfg.getDefense(),
            cfg.getDodge()
        );
        setHp(cfg.getHp());

        this.summonInterval  = cfg.getExtraInt("summon_interval", 2);
        this.summonOnRound1  = cfg.getExtraBoolean("summon_on_round1", true);
        this.rewardAfterRounds = cfg.getExtraInt("reward_after_rounds", 5);
        this.rewardItem      = cfg.getExtra("reward_item");
        this.percentDamageFromRewardItem =
                cfg.getExtraDouble("percent_damage_from_reward_item", 0.33);

        this.summonSpiritName = cfg.getExtra("summon_spirit");
        this.summonDragonName = cfg.getExtra("summon_dragon");
        this.summonExoName    = cfg.getExtra("summon_exoskeleton");
    }

    public int  getSummonInterval() { return summonInterval; }
    public boolean isSummonOnRound1() { return summonOnRound1; }
    public int  getRewardAfterRounds() { return rewardAfterRounds; }
    public String getRewardItem() { return rewardItem; }
    public double getPercentDamageFromRewardItem() { return percentDamageFromRewardItem; }

    public String getSummonSpiritName() { return summonSpiritName; }
    public String getSummonDragonName() { return summonDragonName; }
    public String getSummonExoName()    { return summonExoName; }

    public int getRoundsElapsed() { return roundsElapsed; }
    public void incRoundsElapsed() { roundsElapsed++; }

    @Override
    public boolean isBoss() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("Lich King %s (Boss) L%d HP=%d dmg=%d def=%d dodge=%d%%",
                name, level, hp, damage, defense, dodgeChance);
    }
}
