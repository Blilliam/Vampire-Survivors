package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.BouncingProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class PewPewWeapon extends Weapon {
    public PewPewWeapon(GameObject gameObj) {
        super(gameObj, WeaponTypes.PewPew);
        this.setSprite(Assets.ProjectileBullet);
        stats.put(WeaponUpgrades.AttackDamage, (double) 8);
		stats.put(WeaponUpgrades.ProjectileCount, (double) 1);
		stats.put(WeaponUpgrades.AttackSize, (double) 1);
		stats.put(WeaponUpgrades.AttackSpeed, (double) 40);
		stats.put(WeaponUpgrades.ProjectileSpeed, (double) 12);
		stats.put(WeaponUpgrades.Range, (double) 700);
		stats.put(WeaponUpgrades.ProjectileBounce, (double) 2);
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
                spawn(target);
                delayCounter = stats.get(WeaponUpgrades.AttackSpeed);
            }
        }
    }

    private void spawn(Enemy target) {
        Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(), 
                            target.getY() - gameObj.getPlayer().getY());
        gameObj.addProjectiles(new BouncingProjectile(gameObj, this, dir, 
                                gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
    }
}