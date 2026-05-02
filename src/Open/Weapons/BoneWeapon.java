package Open.Weapons;

import java.awt.Taskbar.State;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.BouncingProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponTypes;
import main.enums.WeaponUpgrades;

public class BoneWeapon extends Weapon {
	public BoneWeapon(GameObject gameObj) {
		super(gameObj, WeaponTypes.Bone);
		this.setSprite(Assets.bone);
		stats.put(WeaponUpgrades.AttackDamage, (double) 10);
		stats.put(WeaponUpgrades.ProjectileCount, (double) 1);
		stats.put(WeaponUpgrades.AttackSize, (double) 1);
		stats.put(WeaponUpgrades.AttackSpeed, (double) 100);
		stats.put(WeaponUpgrades.ProjectileSpeed, (double) 6);
		stats.put(WeaponUpgrades.Range, (double) 500);
		stats.put(WeaponUpgrades.ProjectileBounce, (double) 1);
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
				Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(),
						target.getY() - gameObj.getPlayer().getY());
				gameObj.addProjectiles(new BouncingProjectile(gameObj, this, dir, gameObj.getPlayer().getX(),
						gameObj.getPlayer().getY()));
				delayCounter = stats.get(WeaponUpgrades.AttackSpeed);
			}
		}
	}
}