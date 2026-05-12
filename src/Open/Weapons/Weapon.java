package Open.Weapons;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

import main.GameObject;
import main.DamageResult;
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
    protected final int BURST_DELAY = 10; // Frames between each projectile in a burst

    public Weapon(GameObject gameObj, WeaponTypes type) {
        this.gameObj = gameObj;
        this.stats = new EnumMap<>(WeaponUpgrades.class);
        this.baseStats = new EnumMap<>(WeaponUpgrades.class);
        this.delayCounter = 0.0;

        // Core Defaults: Only initialize what every weapon uses.
        // This prevents the UI from showing empty/zero stats for things like "Bounces" on a Sword.
    }

    /**
     * Calculates damage and determines if the hit is a Critical Strike.
     * Returns a DamageResult containing the final number and a red/white flag.
     */
    public DamageResult getDmg() {
        double baseDmg = stats.getOrDefault(WeaponUpgrades.AttackDamage, 0.0);
        double critChance = stats.getOrDefault(WeaponUpgrades.CriticalChance, 0.0);
        double critBonus = stats.getOrDefault(WeaponUpgrades.CriticalDamage, 0.0);

        double totalMult = 1.0;
        boolean isCrit = false;

        // Handle guaranteed crits (e.g., 200% crit chance adds multiplier twice)
        int guaranteedCrits = (int) critChance;
        if (guaranteedCrits > 0) {
            totalMult += (guaranteedCrits * critBonus);
            isCrit = true;
        }

        // Roll for the remaining partial chance
        double partialChance = critChance % 1;
        if (Math.random() < partialChance) {
            totalMult += critBonus;
            isCrit = true;
        }

        int finalDamage = (int) (baseDmg * totalMult);
        return new DamageResult(finalDamage, isCrit);
    }

    public void update() {
        // 1. Check if the main weapon cooldown is ready
        double attackSpeed = stats.getOrDefault(WeaponUpgrades.AttackSpeed, 100.0);
        double range = stats.getOrDefault(WeaponUpgrades.Range, 500.0);

        if (delayCounter >= attackSpeed) {
            if (gameObj.getPlayer().closestEnemy(range) != null) {
                // "Load" the burst based on ProjectileCount stat
                projectilesToFire = stats.getOrDefault(WeaponUpgrades.ProjectileCount, 1.0).intValue();
                delayCounter = 0.0;
            }
        }

        // 2. Handle the Burst Logic (staggered firing)
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
     * Applies upgrades and updates the current stats map.
     */
    public void applyUpgrade(WeaponUpgrades upgrade, WeaponRarity rarity) {
        double currentVal = stats.getOrDefault(upgrade, 0.0);
        double baseValue = getBaseValueFor(upgrade);
        double rarityMultiplier = getRarityMultiplier(rarity);

        switch (upgrade) {
            case AttackDamage:
                stats.put(upgrade, currentVal + (baseValue * rarityMultiplier));
                break;

            case AttackSpeed:
                double speedReduc = 0.05;
                if (rarity == WeaponRarity.SILVER) speedReduc = 0.06;
                if (rarity == WeaponRarity.GOLD) speedReduc = 0.07;
                if (rarity == WeaponRarity.DIAMOND) speedReduc = 0.10;
                stats.put(upgrade, currentVal * (1.0 - speedReduc));
                break;

            case ProjectileCount:
                stats.put(upgrade, currentVal + rarityMultiplier);
                break;

            case AttackSize:
                double sizeInc = 0.10;
                if (rarity == WeaponRarity.SILVER) sizeInc = 0.12;
                if (rarity == WeaponRarity.GOLD) sizeInc = 0.16;
                if (rarity == WeaponRarity.DIAMOND) sizeInc = 0.20;
                stats.put(upgrade, currentVal + sizeInc);
                break;

            case CriticalChance:
                double critInc = 0.05;
                if (rarity == WeaponRarity.SILVER) critInc = 0.06;
                if (rarity == WeaponRarity.GOLD) critInc = 0.08;
                if (rarity == WeaponRarity.DIAMOND) critInc = 0.10;
                stats.put(upgrade, currentVal + critInc);
                break;

            case CriticalDamage:
                double critDmgInc = 0.25;
                if (rarity == WeaponRarity.SILVER) critDmgInc = 0.30;
                if (rarity == WeaponRarity.GOLD) critDmgInc = 0.40;
                if (rarity == WeaponRarity.DIAMOND) critDmgInc = 0.50;
                stats.put(upgrade, currentVal + critDmgInc);
                break;

            case ProjectileSpeed:
                stats.put(upgrade, currentVal + (baseValue * rarityMultiplier));
                break;

            case ProjectileBounce:
                stats.put(upgrade, currentVal + rarityMultiplier);
                break;
        }
        onUpgrade();
    }

    private double getRarityMultiplier(WeaponRarity rarity) {
        return switch (rarity) {
            case SILVER -> 1.2;
            case GOLD -> 1.4;
            case DIAMOND -> 2.0;
            default -> 1.0; // BRONZE
        };
    }
    
    public BufferedImage getIcon() {
        return icon;
    }
    
    protected void onUpgrade() {
        // Optional hook for subclasses
    }

    public BufferedImage getSprite() { return sprite; }
    public void setSprite(BufferedImage sprite) { this.sprite = sprite; }

    public EnumMap<WeaponUpgrades, Double> getBaseStats() {
        return baseStats;
    }
}