package Open.Weapons;

import java.awt.image.BufferedImage;
import java.util.EnumMap;

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
    // Store original values so upgrades scale off the base, not current
    protected EnumMap<WeaponUpgrades, Double> baseStats;

    public Weapon(GameObject gameObj, WeaponTypes type) {
        this.gameObj = gameObj;
        this.stats = new EnumMap<>(WeaponUpgrades.class);
        this.baseStats = new EnumMap<>(WeaponUpgrades.class);
        this.delayCounter = 0.0;
        
        // Initialize defaults to prevent NullPointerErrors in math
        for (WeaponUpgrades upgrade : WeaponUpgrades.values()) {
            stats.put(upgrade, 0.0);
            baseStats.put(upgrade, 0.0);
        }
        // Set core defaults
        stats.put(WeaponUpgrades.CriticalDamage, 1.0); 
        baseStats.put(WeaponUpgrades.CriticalDamage, 1.0);
    }

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

    public abstract void update();

    public EnumMap<WeaponUpgrades, Double> getStats() {
        return stats;
    }

    // This replaces the missing method error
    protected double getBaseValueFor(WeaponUpgrades upgrade) {
        return baseStats.getOrDefault(upgrade, 0.0);
    }

    // Called in child constructors (Aura, Sword, etc.) after setting initial stats
    protected void setBaseStats() {
        baseStats.putAll(stats);
    }

    public void applyUpgrade(WeaponUpgrades upgrade, WeaponRarity rarity) {
        double currentVal = stats.getOrDefault(upgrade, 0.0);
        double baseValue = getBaseValueFor(upgrade);

        switch (upgrade) {
            case AttackDamage:
                stats.put(upgrade, currentVal + (baseValue * rarity.multiplier));
                break;
            case AttackSpeed:
                stats.put(upgrade, currentVal * (1.0 - rarity.speedReduction));
                break;
            case ProjectileCount:
                stats.put(upgrade, currentVal + rarity.flatBonus);
                break;
            case AttackSize:
                // Docs: +10% to +20% scaling
                stats.put(upgrade, currentVal + rarity.sizeIncrease);
                break;
            case CriticalChance:
                stats.put(upgrade, currentVal + rarity.critChanceAdd);
                break;
            case CriticalDamage:
                stats.put(upgrade, currentVal + rarity.critDamageAdd);
                break;
        }
    }

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
}