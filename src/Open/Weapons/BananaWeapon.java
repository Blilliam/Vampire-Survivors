package Open.Weapons;

import java.awt.Graphics2D;

import Open.Weapons.WeaponProjectile.BananaProjectile;
import main.Assets;
import main.GameObject;
import main.Vec2;

public class BananaWeapon extends Weapon{

	public BananaWeapon(GameObject gameObj) {
		super(gameObj);
		setAtk(10);
		projectileCount = 1;
		setSprite(Assets.ProjectileBanana);
		atkDelay = 60;
		setSpeed(5);
		setProjectileBounces(-1);
		setAtk(3);
		setRange(500);
		setMaxDuration(200);
		this.icon = Assets.BananaIcon;
	}
	
	public void update() {
		if (atkDelay < delayCounter) {
			if (gameObj.getPlayer().closestEnemy(getRange()) != null) {
				Vec2 direction = Vec2.between(gameObj.getPlayer(), gameObj.getPlayer().closestEnemy(getRange()));
				gameObj.addProjectiles(new BananaProjectile(gameObj, this, direction, gameObj.getPlayer().getX(), gameObj.getPlayer().getY()));
				delayCounter = 0;
			}
			
		}
		
		delayCounter++;
	}
	
}