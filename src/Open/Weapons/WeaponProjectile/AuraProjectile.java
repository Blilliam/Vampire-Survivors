package Open.Weapons.WeaponProjectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;

import Open.Entities.Entity;
import Open.Entities.Enemies.Enemy;
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
		this.width = (int) (this.weapon.getRange() * weapon.getSize());
		this.height = (int) (this.weapon.getRange() * weapon.getSize());

	}
	
	protected void updatePhysics() {
		Entity player = gameObj.getPlayer();

		position.setX(player.getX() + player.getWidth()/2 - width/2);
		position.setY(player.getY() + player.getHeight()/2 - height/2);
	}
	@Override
	public void update() {
		updatePhysics();

		x = (int) position.getX();
		y = (int) position.getY();

		for (Enemy e : gameObj.getEnemies()) {

			if (hitCooldowns.containsKey(e)) {
				int time = hitCooldowns.get(e) - 1;

				if (time <= 0) {
					hitCooldowns.remove(e);
				} else {
					hitCooldowns.put(e, time);
				}
			}

			if (Entity.circleCollision(this, e) && !hitCooldowns.containsKey(e)) {
				e.damage(weapon.getAtk());
				
				impactX = x;
			    impactY = y;
			    drawingImpact = true;
				
				// set cooldown
				hitCooldowns.put(e, HIT_COOLDOWN);
				currProjectileBounces--;
				if (currProjectileBounces == 0) {
					isDead = true;
					break;
				}
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		drawImpact(g);
		
	    int centerX = AppPanel.WIDTH / 2;
	    int centerY = AppPanel.HEIGHT / 2;

	    g.setColor(Color.BLUE);

	    for (int i = 0; i < 5; i++) {

	        double scale = Math.pow(0.6, i); // exponential shrink (tweak 0.6)

	        int w = (int)(width * scale);
	        int h = (int)(height * scale);

	        int x = centerX - w / 2;
	        int y = centerY - h / 2;

	        g.drawOval(x, y, w, h);
	    }
	}
}