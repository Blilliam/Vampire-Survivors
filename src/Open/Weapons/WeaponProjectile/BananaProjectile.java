package Open.Weapons.WeaponProjectile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import Open.Weapons.Weapon;
import main.AppPanel;
import main.GameObject;
import main.Vec2;

public class BananaProjectile extends WeaponEntity {
    
    private Vec2 startPos;
    private double speed = 8.0; // Initial launch speed
    private double returnStrength = 0.15; // How hard it pulls back
    private boolean returning = false;

    public BananaProjectile(GameObject gameObj, Weapon weapon, Vec2 direction, int x, int y) {
        super(gameObj, weapon, direction, x, y);
        this.width = 50;
        this.height = 50;
        this.angle = 0;
        
        // Initialize velocity based on direction
        this.velocity = direction.normalize().scale(speed);
        this.startPos = new Vec2(x, y); 
    }

    @Override
public void updatePhysics() {
    // 1. Get the vector from Banana to Player
    Vec2 playerPos = new Vec2(gameObj.getPlayer().getX(), gameObj.getPlayer().getY());
    Vec2 toPlayer = playerPos.sub(this.position).normalize();

//curve
    Vec2 sideForce = velocity.perpendicular().normalize();
    
    double returnStrength = 0.08; 
    double sideStrength = 0.25;  

    velocity = velocity.add(toPlayer.scale(returnStrength));
    velocity = velocity.add(sideForce.scale(sideStrength));

    //Cap the speed
    double maxSpeed = 10.0;
    if (velocity.length() > maxSpeed) {
        velocity = velocity.normalize().scale(maxSpeed);
    }

    //Apply Movement
    position = position.add(velocity);
    angle += 0.2; // The spin

    this.x = (int)position.getX();
    this.y = (int)position.getY();
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