package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Open.Entities.Entity;
import Open.Entities.Enemies.Enemy;
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
	double currProjectileBounces;
	double angle;

	/**
	 * creates new projectile entity
	 * 
	 * @param gameObj
	 * @param weapon
	 * @param direction vector
	 * @param x
	 * @param y
	 */
	public WeaponEntity(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj);
		this.weapon = weapon;
		this.sprite = weapon.sprite;
		this.position = new Vec2(x, y);
		this.acceleration = new Vec2(0, 0);
		this.velocity = direction.normalize().scale(weapon.speed);
		this.currProjectileBounces = weapon.projectileBounces;
		isDead = false;
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
		for (Enemy e : gameObj.getEnemies()) {
			if (Entity.rectCollision(this, e)) {
				e.damage(weapon.atk);
				this.currProjectileBounces--;
				if (currProjectileBounces <= 0) {
					isDead = true;
				}
			}
		}

	}

	@Override
	public void draw(Graphics2D g) {
		int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2;
		int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2;

		g.drawImage(sprite, screenX, screenY, width, height, null);

	}

}
