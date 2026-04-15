package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.util.HashMap;

import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class AuraProjectile extends WeaponEntity {

	public AuraProjectile(GameObject gameObj, Weapon weapon) {
		super(gameObj, weapon, new Vec2(0, 0), 0, 0);
		
		this.hitCooldowns = new HashMap<>();

		this.weapon = weapon;
		this.sprite = weapon.getSprite();
		this.position = new Vec2(AppPanel.WIDTH/2, AppPanel.HEIGHT/2);
		this.currProjectileBounces = weapon.getProjectileBounces();
		isDead = false;
		this.width = 300;
		this.height = 300;

	}
	
	protected void updatePhysics() {
		position.setX(gameObj.getPlayer().getX() - width/2);
		position.setY(gameObj.getPlayer().getY() - height/2);
		
		this.x = (int) position.getX();
		this.y = (int) position.getY();
	}

	@Override
	public void draw(Graphics2D g) {
		int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2;
		int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2;

		g.drawImage(sprite, screenX, screenY, width, height, null);
	}
}