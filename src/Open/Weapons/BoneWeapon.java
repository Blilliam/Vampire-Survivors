package Open.Weapons;

import Open.Entities.Enemies.Enemy;
import Open.Weapons.WeaponProjectile.BouncingProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;

public class BoneWeapon extends Weapon {
	public BoneWeapon(GameObject gameObj) {
		super(gameObj);
		this.atk = 25; // High damage
		this.speed = 6; // Slow movement
		this.atkDelay = 100; // Very slow fire rate
		this.projectileBounces = 1;
		this.sprite = Assets.bone;
	}

	@Override
	public void update() {
		if (delayCounter > 0) {
			delayCounter--;
		} else {
			Enemy target = gameObj.getPlayer().closestEnemy(400);
			if (target != null) {
				Vec2 dir = new Vec2(target.getX() - gameObj.getPlayer().getX(),
						target.getY() - gameObj.getPlayer().getY());
				gameObj.addProjectiles(new BouncingProjectile(gameObj, this, dir, gameObj.getPlayer().getX(),
						gameObj.getPlayer().getY()));
				delayCounter = atkDelay;
			}
		}
	}
}