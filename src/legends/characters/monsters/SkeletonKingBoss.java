package legends.characters.monsters;

public class SkeletonKingBoss extends Monster {

    private final double swordReduction;
    private final double bluntMultiplier;

    public SkeletonKingBoss(BossConfig cfg) {
        super(
            cfg.getName(),
            cfg.getLevel(),
            cfg.getDamage(),
            cfg.getDefense(),
            cfg.getDodge()
        );
        setHp(cfg.getHp());

        this.swordReduction  = cfg.getExtraDouble("reduction_sword", 0.75);
        this.bluntMultiplier = cfg.getExtraDouble("multiplier_blunt", 2.0);
    }

    public double getSwordReduction() {
        return swordReduction;
    }

    public double getBluntMultiplier() {
        return bluntMultiplier;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("Skeleton King %s (Boss) L%d HP=%d dmg=%d def=%d dodge=%d%%",
                name, level, hp, damage, defense, dodgeChance);
    }
}

