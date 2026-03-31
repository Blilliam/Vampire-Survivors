package Open.Weapons;

import Open.Weapons.WeaponProjectile.GunProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;

public class GunWeapon extends Weapon{

	public GunWeapon(GameObject gameObj) {
		super(gameObj);
		atk = 10;
		projectileCount = 1;
		sprite = Assets.aura;
		atkDelay = 60;
	}
	
	public void update() {
		if (atkDelay < delayCounter) {
			Vec2 direction = Vec2.between(gameObj.getPlayer(), gameObj.getPlayer().closestEnemy());
			gameObj.addProjectiles(new GunProjectile(gameObj, this, direction, gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
			delayCounter = 0;
			
		}
		
		delayCounter++;
	}
	
}
