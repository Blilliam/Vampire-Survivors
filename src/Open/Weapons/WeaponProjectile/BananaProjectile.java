package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class BananaProjectile extends WeaponEntity{
	
	
	
	public BananaProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj, weapon, direction, x, y);
		this.width = 50;
		this.height = 50;
		this.angle = 0;
	}
	
	@Override
	public void updatePhysics() {
		position = position.add(velocity);
		angle += 0.1;
	}
	@Override
	public void draw(Graphics2D g) {
		int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2;
		int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2;

		AffineTransform old = g.getTransform();
		
		g.rotate(angle, screenX + width/2.0, screenY + height/2.0);
		g.drawImage(sprite, screenX, screenY, width, height, null);
		g.setTransform(old);

	}

}
