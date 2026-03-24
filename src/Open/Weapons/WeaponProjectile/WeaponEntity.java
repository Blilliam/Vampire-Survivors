package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Open.Entities.Entity;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;

public class WeaponEntity extends Entity {
	BufferedImage sprite;
	
	Weapon weapon;
	
	/**
	 * creates new projectile entity
	 * @param gameObj
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param rotationSpeed
	 * @param projectileSpeed
	 * @param sprite
	 */
	public WeaponEntity(GameObject gameObj, Weapon weapon, int x, int y, int width, int height, double rotationSpeed, int speed, BufferedImage sprite) {
		super(gameObj);
		this.x =x;
		this.y = y;
		
		this.width = width;
		this.height = height;
		
		this.sprite = sprite;
		
		this.speed = speed;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	@Override
	public void update() {
		
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sprite, x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2,
				y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2, width + 50, height + 50,
				null);
		
	}
	
	
	
}
