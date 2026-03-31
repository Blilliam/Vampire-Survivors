package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Open.Entities.Entity;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class WeaponEntity extends Entity {
	BufferedImage sprite;
	
	Weapon weapon;
	
	Vec2 position;
	Vec2 velocity;
	Vec2 acceleration;
	
	
	
	/**
	 * creates new projectile entity
	 * @param gameObj
	 * @param weapon
	 * @param direction vector
	 * @param x
	 * @param y
	 */
	public WeaponEntity(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj);
		this.sprite = weapon.sprite;
		this.position = new Vec2(x, y);
		this.acceleration = new Vec2(0, 0);
		this.velocity = direction.normalize().scale(weapon.speed);
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	protected void updatePhysics() {
		
		
	}
	
	public void update() {
		updatePhysics();
		x = (int) position.x;
		y = (int) position.y;
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sprite, x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2,
				y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2, width + 50, height + 50,
				null);
		
	}
	
	
	
}
