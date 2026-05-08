package Open.Weapons;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import main.GameObject;
import main.enums.WeaponRarity;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public abstract class Weapon {
    private BufferedImage sprite;
    protected GameObject gameObj;
    protected BufferedImage icon;
    protected Double delayCounter;
    protected EnumMap<WeaponUpgrades, Double> stats;
    protected EnumMap<WeaponUpgrades, Double> baseStats;
    
    protected int projectilesToFire = 0;
    protected int subDelayCounter = 0;
    protected final int BURST_DELAY = 10; // Frames between each banana in a burst

    public Weapon(GameObject gameObj, WeaponTypes type) {
        this.gameObj = gameObj;
        this.stats = new EnumMap<>(WeaponUpgrades.class);
        this.baseStats = new EnumMap<>(WeaponUpgrades.class);
        this.delayCounter = 0.0;
        
        for (WeaponUpgrades upgrade : WeaponUpgrades.values()) {
            stats.put(upgrade, 0.0);
            baseStats.put(upgrade, 0.0);
        }

        // Core Defaults based on your documentation
        stats.put(WeaponUpgrades.CriticalDamage, 1.0); // Starts at 100% (1.0)
        stats.put(WeaponUpgrades.AttackSize, 1.0);    // Default scale 100%
        stats.put(WeaponUpgrades.ProjectileCount, 1.0);
    }

    /**
     * Calculates damage including Critical hits.
     * Documentation: 100% base Crit Damage means a crit deals 200% total damage.
     * Formula: BaseDmg * (1 + (CritDamageMultiplier))
     */
    public int getDmg() {
        double baseDmg = stats.get(WeaponUpgrades.AttackDamage);
        double critChance = stats.get(WeaponUpgrades.CriticalChance);
        double critBonus = stats.get(WeaponUpgrades.CriticalDamage);

        double totalMult = 1.0;
        int guaranteedCrits = (int) critChance;
        totalMult += (guaranteedCrits * critBonus);

        double partialChance = critChance % 1;
        if (Math.random() < partialChance) {
            totalMult += critBonus;
        }
        return (int) (baseDmg * totalMult);
    }

    public void update() {
        // 1. Check if the main weapon cooldown is ready
        if (delayCounter >= stats.get(WeaponUpgrades.AttackSpeed)) {
            if (gameObj.getPlayer().closestEnemy(stats.get(WeaponUpgrades.Range)) != null) {
                // Instead of a loop, we "load" the burst
                projectilesToFire = stats.get(WeaponUpgrades.ProjectileCount).intValue();
                delayCounter = 0.0;
            }
        }

        // 2. Handle the Burst Logic
        if (projectilesToFire > 0) {
            subDelayCounter++;
            
            if (subDelayCounter >= BURST_DELAY) {
                fireProjectile();
                projectilesToFire--;
                subDelayCounter = 0;
            }
        }

        delayCounter++;
    }

    protected abstract void fireProjectile();

	public EnumMap<WeaponUpgrades, Double> getStats() {
        return stats;
    }

    protected double getBaseValueFor(WeaponUpgrades upgrade) {
        return baseStats.getOrDefault(upgrade, 0.0);
    }

    protected void setBaseStats() {
        baseStats.putAll(stats);
    }

    /**
     * Applies upgrades based on the Stat Overview documentation.
     */
    public void applyUpgrade(WeaponUpgrades upgrade, WeaponRarity rarity) {
    	
    	
        double currentVal = stats.getOrDefault(upgrade, 0.0);
        double baseValue = getBaseValueFor(upgrade);

        // Your documentation uses specific multipliers per rarity:
        // Bronze: 1.0, Silver: 1.2, Gold: 1.4, Diamond: 2.0
        double rarityMultiplier = getRarityMultiplier(rarity);

        switch (upgrade) {
            case AttackDamage:
                // Bronze: +X, Silver: +1.2X, etc.
                stats.put(upgrade, currentVal + (baseValue * rarityMultiplier));
                break;

            case AttackSpeed:
                // Bronze: -5%, Silver: -6%, Gold: -7%, Diamond: -10%
                double speedReduc = 0.05;
                if (rarity == WeaponRarity.SILVER) speedReduc = 0.06;
                if (rarity == WeaponRarity.GOLD) speedReduc = 0.07;
                if (rarity == WeaponRarity.DIAMOND) speedReduc = 0.10;
                stats.put(upgrade, currentVal * (1.0 - speedReduc));
                break;

            case ProjectileCount:
                // Bronze: +1, Silver: +1.2... (Note: Projectile count is usually int, but using double for logic)
                stats.put(upgrade, currentVal + rarityMultiplier);
                break;

            case AttackSize:
                // Bronze: +10%, Silver: +12%, Gold: +16%, Diamond: +20%
                double sizeInc = 0.10;
                if (rarity == WeaponRarity.SILVER) sizeInc = 0.12;
                if (rarity == WeaponRarity.GOLD) sizeInc = 0.16;
                if (rarity == WeaponRarity.DIAMOND) sizeInc = 0.20;
                stats.put(upgrade, currentVal + sizeInc);
                break;

            case CriticalChance:
                // Bronze: +5%, Silver: +6%, Gold: +8%, Diamond: +10%
                double critInc = 0.05;
                if (rarity == WeaponRarity.SILVER) critInc = 0.06;
                if (rarity == WeaponRarity.GOLD) critInc = 0.08;
                if (rarity == WeaponRarity.DIAMOND) critInc = 0.10;
                stats.put(upgrade, currentVal + critInc);
                break;

            case CriticalDamage:
                // Bronze: +25%, Silver: +30%, Gold: +40%, Diamond: +50%
                double critDmgInc = 0.25;
                if (rarity == WeaponRarity.SILVER) critDmgInc = 0.30;
                if (rarity == WeaponRarity.GOLD) critDmgInc = 0.40;
                if (rarity == WeaponRarity.DIAMOND) critDmgInc = 0.50;
                stats.put(upgrade, currentVal + critDmgInc);
                break;

            case ProjectileSpeed:
                // Bronze: +X, Silver: +1.2X, etc.
                stats.put(upgrade, currentVal + (baseValue * rarityMultiplier));
                break;

            case ProjectileBounce:
                // Bronze: +1, Silver: +1.2, etc.
                stats.put(upgrade, currentVal + rarityMultiplier);
                break;
        }
        onUpgrade();
    }

    private double getRarityMultiplier(WeaponRarity rarity) {
        switch (rarity) {
            case SILVER: return 1.2;
            case GOLD: return 1.4;
            case DIAMOND: return 2.0;
            case BRONZE: 
            default: return 1.0;
        }
    }
    
    protected void onUpgrade() {
    	return;
    }

    public BufferedImage getSprite() { return sprite; }
    public void setSprite(BufferedImage sprite) { this.sprite = sprite; }

	public EnumMap<WeaponUpgrades, Double> getBaseStats() {
		// TODO Auto-generated method stub
		return baseStats;
	}
}