package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.FireStaffProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class FireStaffWeapon extends Weapon {
    public FireStaffWeapon(GameObject gameObj) {
        super(gameObj, WeaponTypes.FireStaff);
        stats.put(WeaponUpgrades.AttackDamage, (double) 8);
		stats.put(WeaponUpgrades.ProjectileCount, (double) 1);
		stats.put(WeaponUpgrades.AttackSize, (double) 1);
		stats.put(WeaponUpgrades.AttackSpeed, (double) 60);
		stats.put(WeaponUpgrades.ProjectileSpeed, (double) 4);
		stats.put(WeaponUpgrades.Range, (double) 700);
		stats.put(WeaponUpgrades.CriticalDamage, (double) 2);
		stats.put(WeaponUpgrades.CriticalChance, (double) 0.1);
    }

    @Override
    public void update() {
        if (delayCounter > 0) {
            delayCounter--;
        } else {
            Enemy target = gameObj.getPlayer().closestEnemy(stats.get(WeaponUpgrades.Range));
            if (target != null) {
                spawnProjectile(target);
                delayCounter = stats.get(WeaponUpgrades.AttackSpeed) * (1 - gameObj.getPlayer().getArtifactManager().getPercentAttackSpeed());
            }
        }
    }

    private void spawnProjectile(Enemy target) {
        Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(), 
                            target.getY() - gameObj.getPlayer().getY());
        
        gameObj.addProjectiles(new FireStaffProjectile(gameObj, this, dir, 
                               gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
    }
}