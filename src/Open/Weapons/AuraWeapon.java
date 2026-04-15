package Open.Weapons;

import java.awt.Graphics2D;

import Open.Weapons.WeaponProjectile.AuraProjectile;
import Open.Weapons.WeaponProjectile.BananaProjectile;
import Open.Weapons.WeaponProjectile.GunProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;

public class AuraWeapon extends Weapon{
	boolean created;

	public AuraWeapon(GameObject gameObj) {
		super(gameObj);
		setAtk(10);
		projectileCount = 1;
		setSprite(Assets.aura);
		atkDelay = 20;
		setSpeed(0);
		setProjectileBounces(-1);
		setAtk(3);
		range = 500;
		setMaxDuration(Double.MAX_VALUE);
		
		created = false;
	}
	
	public void update() {
		if (created == false) {
			gameObj.addProjectiles(new AuraProjectile(gameObj, this));
			created = true;
		}
	}
	
	public void draw(Graphics2D g2) {
		
	}
	
}