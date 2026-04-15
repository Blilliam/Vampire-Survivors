package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;

import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class GunProjectile extends WeaponEntity{

	public GunProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj, weapon, direction, x, y);
		this.width = 50;
		this.height = 50;
	}
	
	@Override
	public void updatePhysics() {
		position = position.add(velocity);
	}
	
	@Override
	public void draw(Graphics2D g) {

	    int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2;
	    int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2;

	    Graphics2D g2 = (Graphics2D) g.create();

	    // rotate around center of projectile
	    g2.translate(screenX, screenY);
	    g2.rotate(angle);

	    g2.drawImage(sprite, -width / 2, -height / 2, width, height, null);

	    g2.dispose();
	}

}
