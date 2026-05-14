package Open.Weapons.WeaponProjectile;

import Open.Entities.Entity;
import Open.Entities.Enemies.Enemy;
import Open.Weapons.PewPewWeapon;
import Open.Weapons.Weapon;
import main.GameObject;
import main.Vec2;
import main.enums.WeaponUpgrades;

public class BouncingProjectile extends WeaponEntity {

	public BouncingProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj, weapon, direction, x, y);
		this.width = 25;
		this.height = 25;
		if (weapon.getClass().equals(PewPewWeapon.class)) {
			height/=2;
		}
		// velocity is already set in the super() constructor!
	}

	@Override
	protected void updatePhysics() {
		position = position.add(velocity);

		duration++;
		if (duration > 300) {
			isDead = true;
		}
	}

	@Override
	protected void onHitEffect(Enemy hitEnemy) {
		if (currProjectileBounces > 0) {
			bounce(hitEnemy);
		}
	}

	private void bounce(Enemy currentTarget) {
		Enemy nextTarget = null;
		double closestDist = 400;

		for (Enemy e : gameObj.getEnemies()) {
			if (e == currentTarget || e.isDying())
				continue;

			double dist = Entity.getDistance(this, e);

			if (dist < closestDist) {
				closestDist = dist;
				nextTarget = e;
			}
		}

		if (nextTarget != null) {
			Vec2 newDir = new Vec2(nextTarget.getX() - position.getX(), nextTarget.getY() - position.getY());
			this.velocity = newDir.normalize().scale(weapon.getStats().get(WeaponUpgrades.ProjectileSpeed));
		}
	}
}