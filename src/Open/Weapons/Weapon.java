package Open.Weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GameObject;


public abstract class Weapon {
	private double atk;
	protected double projectileCount;
	protected double rotationSpeed;
	private int speed;
	private BufferedImage sprite;
	protected GameObject gameObj;
	
	protected int atkDelay;
	protected int delayCounter;
	private double projectileBounces;
	protected int range;

	
	
	public Weapon(GameObject gameObj) {
		this.gameObj = gameObj;
	}

	public void draw(Graphics2D g2) {
		
	}
	
	public void update() {
		
	}

	public double getProjectileBounces() {
		return projectileBounces;
	}

	public void setProjectileBounces(double projectileBounces) {
		this.projectileBounces = projectileBounces;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}

	public double getAtk() {
		return atk;
	}

	public void setAtk(double atk) {
		this.atk = atk;
	}
	
}
