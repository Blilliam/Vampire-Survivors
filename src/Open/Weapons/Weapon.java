package Open.Weapons;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GameObject;


public abstract class Weapon {
	protected double atk;
	protected double projectileCount;
	protected double rotationSpeed;
	protected int speed;
	protected BufferedImage sprite;
	protected GameObject gameObj;
	protected BufferedImage icon;
	
	protected int atkDelay;
	protected int delayCounter;
	protected double projectileBounces;
	private int range;
	private double maxDuration;
	protected double size;

	
	
	public Weapon(GameObject gameObj) {
		this.gameObj = gameObj;
		size = 1;
	}
	
	public abstract void update();

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

	public double getMaxDuration() {
		return maxDuration;
	}

	public void setMaxDuration(double maxDuration) {
		this.maxDuration = maxDuration;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	
	public void setSize(double newsize) {
		size = newsize;
	}
	
}
