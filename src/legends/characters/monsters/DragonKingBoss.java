package legends.characters.monsters;

public class DragonKingBoss extends Monster {

    private final String immune;         // "melee"
    private final String allowedDamage;  // "bow,spell"

    public DragonKingBoss(BossConfig cfg) {
        super(
            cfg.getName(),
            cfg.getLevel(),
            cfg.getDamage(),
            cfg.getDefense(),
            cfg.getDodge()
        );
        setHp(cfg.getHp());

        this.immune        = cfg.getExtra("immune");
        this.allowedDamage = cfg.getExtra("allowed_damage");
    }

    public String getImmune() {
        return immune;
    }

    public String getAllowedDamage() {
        return allowedDamage;
    }

    @Override
    public boolean isBoss() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("Dragon King %s (Boss) L%d HP=%d dmg=%d def=%d dodge=%d%%",
                name, level, hp, damage, defense, dodgeChance);
    }
}
