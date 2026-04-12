package Open.Weapons;

import Open.Weapons.WeaponProjectile.GunProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;

public class GunWeapon extends Weapon{

	public GunWeapon(GameObject gameObj) {
		super(gameObj);
		setAtk(10);
		projectileCount = 1;
		setSprite(Assets.aura);
		atkDelay = 60;
		setSpeed(20);
		setProjectileBounces(1);
		setAtk(3);
		range = 500;
	}
	
	public void update() {
		if (atkDelay < delayCounter) {
			if (gameObj.getPlayer().closestEnemy(range) != null) {
				Vec2 direction = Vec2.between(gameObj.getPlayer(), gameObj.getPlayer().closestEnemy(range));
				gameObj.addProjectiles(new GunProjectile(gameObj, this, direction, gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
				delayCounter = 0;
			}
			
		}
		
		delayCounter++;
	}
	
}
