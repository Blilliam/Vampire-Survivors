package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import Open.Entities.Entity;
import Open.Entities.Enemies.Enemy;
import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class WeaponEntity extends Entity {

	protected BufferedImage sprite;

	protected HashMap<Enemy, Integer> hitCooldowns;

	protected Weapon weapon;

	protected Vec2 position;
	protected Vec2 velocity;
	protected Vec2 acceleration;
	protected double currProjectileBounces;
	protected double angle;
	protected double duration;

	// tweak this for balance
	private final int HIT_COOLDOWN = 15; // frames

	public WeaponEntity(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
		super(gameObj);

		this.hitCooldowns = new HashMap<>();

		this.weapon = weapon;
		this.sprite = weapon.getSprite();
		this.position = new Vec2(x, y);
		this.acceleration = new Vec2(0, 0);
		this.velocity = direction.normalize().scale(weapon.getSpeed());
		this.currProjectileBounces = weapon.getProjectileBounces();
		isDead = false;
		
		this.angle = Math.atan2(velocity.getY(), velocity.getX()) - Math.PI / 2;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	protected void updatePhysics() {
		// (your movement code if any)
	}

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

			if (Entity.rectCollision(this, e) && !hitCooldowns.containsKey(e)) {

				e.damage(weapon.getAtk());
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
		int screenX = x - gameObj.getPlayer().getX() + AppPanel.WIDTH / 2 - width / 2;
		int screenY = y - gameObj.getPlayer().getY() + AppPanel.HEIGHT / 2 - height / 2;

		g.drawImage(sprite, screenX, screenY, width, height, null);
	}
}