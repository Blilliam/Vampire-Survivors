package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.SwordProjectile;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class SwordWeapon extends Weapon {

    public SwordWeapon(GameObject gameObj) {
        super(gameObj, WeaponTypes.Sword);
        //this.sprite = Assets.katanaSlashSprite; // A crescent moon slash sprite
        stats.put(WeaponUpgrades.AttackDamage, (double) 10);
		stats.put(WeaponUpgrades.ProjectileCount, (double) 1);
		stats.put(WeaponUpgrades.AttackSize, (double) 1);
		stats.put(WeaponUpgrades.AttackSpeed, (double) 35);
		stats.put(WeaponUpgrades.Range, (double) 500);
		stats.put(WeaponUpgrades.CriticalDamage, (double) 2);
		stats.put(WeaponUpgrades.CriticalChance, (double) 0.1);
    }

    @Override
    public void update() {
        if (delayCounter > 0) {
            delayCounter--;
        } else {
            // Find the closest enemy within a short range
            Enemy target = gameObj.getPlayer().closestEnemy(stats.get(WeaponUpgrades.Range));
            
            if (target != null) {
                spawnSlash(target);
                delayCounter = stats.get(WeaponUpgrades.AttackSpeed) * (1 - gameObj.getPlayer().getArtifactManager().getPercentAttackSpeed());
            }
        }
    }

    private void spawnSlash(Enemy target) {
        // Calculate direction toward the enemy
        Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(), 
                            target.getY() - gameObj.getPlayer().getY());
        
        // Spawn the slash at the player's position
        gameObj.addProjectiles(new SwordProjectile(gameObj, this, dir, 
                               gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
    }
}